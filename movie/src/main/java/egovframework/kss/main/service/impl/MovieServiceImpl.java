package egovframework.kss.main.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import egovframework.kss.main.dto.SingleMovieDTO;
import egovframework.kss.main.model.Movie;
import egovframework.kss.main.model.Person;
import egovframework.kss.main.service.MovieService;
import egovframework.rte.fdl.property.EgovPropertyService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Service("MovieService")
public class MovieServiceImpl implements MovieService {

	@Autowired
	private EgovPropertyService propertiesService;

	private static Logger Logger = LoggerFactory.getLogger(MovieServiceImpl.class);
	private static final OkHttpClient client = new OkHttpClient();

	@Override
	@Cacheable(value = "genreCache")
	public Map<Integer, String> fetchGenres() {
		Map<Integer, String> genreMap = new HashMap<>();
		Request request2 = new Request.Builder().url("https://api.themoviedb.org/3/genre/movie/list?language=ko").get().addHeader("accept", "application/json").addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1Njk4OWZmMjMxOGQ4MWRlZTM5YTZhN2E1NjEzNjNhYyIsIm5iZiI6MTczMTkwMTMxNy4yMjA4NDYyLCJzdWIiOiI2NzMyYTRhNjYwN2U4YWEyMGVmNjdiMWEiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.N6xEzYqx7A6JQbWpjIIFRKnXUS8HEL_krVOW-by98d0").build();
		Response response2 = null;
		try {
			response2 = client.newCall(request2).execute();
			if (response2.isSuccessful() && response2.body() != null) {
				String responseBody = response2.body().string();
				System.out.println(responseBody);
				JSONObject jsonResponse = new JSONObject(responseBody);
				JSONArray genres = jsonResponse.getJSONArray("genres");

				for (int i = 0; i < genres.length(); i++) {
					JSONObject genre = genres.getJSONObject(i);
					int id = genre.getInt("id");
					String name = genre.getString("name");
					genreMap.put(id, name);
				}
			}
		} catch (IOException e1) {
			Logger.error(e1.getMessage());
		} finally {
			if (response2 != null) {
				response2.close();
			}
		}
		return genreMap;
	}

	@Override
	@Cacheable(value = "popularMoviesCache")
	public List<Movie> fetchPopularMovies(Map<Integer, String> genreMap) {
		List<Movie> movies = new ArrayList<>();
		Request request = new Request.Builder().url("https://api.themoviedb.org/3/movie/popular?language=ko-KR&page=1&region=KR").get().addHeader("accept", "application/json").addHeader("Authorization", propertiesService.getString("TMDB.Access.Token")).build();

		Response response = null;
		try {
			response = client.newCall(request).execute();
			if (response.isSuccessful() && response.body() != null) {
				String responseBody = response.body().string();
				System.out.println(responseBody);
				JSONObject jsonResponse = new JSONObject(responseBody);
				JSONArray results = jsonResponse.getJSONArray("results");

				for (int i = 0; i < results.length(); i++) { //JSONArray는 Iterable를 구현하는 클래스가 아니라서 for(a:b) 형식 못쓴다...
					try {
						JSONObject result = results.getJSONObject(i);
						Movie movie = new Movie();
						movie.setId(result.getInt("id"));
						movie.setTitle(result.getString("title"));
						movie.setOverview(result.getString("overview"));
						movie.setImg_url("https://image.tmdb.org/t/p/w342" + result.getString("poster_path")); //이미지 호출 url과 사이즈까지 한번에 더한다
						movie.setPopularity(result.getLong("popularity"));
						movie.setVote_average(roundToFirstDecimal(result.getDouble("vote_average")));
						movie.setVote_count(result.getInt("vote_count"));
						movie.setRelease_date(Date.valueOf(result.getString("release_date")));

						JSONArray genreIdsJsonArray = result.getJSONArray("genre_ids");
						int[] genreIds = new int[genreIdsJsonArray.length()]; // JSONArray의 길이로 int[] 생성
						String[] genreNames = new String[genreIdsJsonArray.length()];

						for (int j = 0; j < genreIdsJsonArray.length(); j++) {
							int genreId = genreIdsJsonArray.getInt(j);
							genreIds[j] = genreId; // 각 요소를 int로 변환하여 배열에 저장
							genreNames[j] = genreMap.get(genreId);
						}
						movie.setGenre_ids(genreIds);
						movie.setGenre_names(genreNames);

						movies.add(movie);
					} catch (Exception e) {
						// 예외가 발생하면 해당 영화만 빼놓고 진행
						System.err.println(i + "번째 인덱스에서 에러 발생. 에러 메시지: " + e.getMessage());
					}
				}

			} else {
				System.out.println("응답 에러남");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logger.error(e.getMessage());

		} finally {
			if (response != null) {
				response.close();  // 이걸 해줘야지 쓰레드 풀이 닫힌다. 안하면 경고 뜬다.
			}
		}

		return movies;
	}

	public double roundToFirstDecimal(double value) {
		BigDecimal bd = new BigDecimal(Double.toString(value));
		bd = bd.setScale(1, RoundingMode.HALF_UP); // 소수점 첫째 자리에서 반올림
		return bd.doubleValue();
	}

	@Override
	@Cacheable(value = "upcomingMoviesCache")
	public List<Movie> upComingMovies(Map<Integer, String> genreMap) {
		List<Movie> movies = new ArrayList<>();
		Request request = new Request.Builder().url("https://api.themoviedb.org/3/movie/upcoming?language=ko&page=1&region=KR").get().addHeader("accept", "application/json").addHeader("Authorization", propertiesService.getString("TMDB.Access.Token")).build();

		Response response = null;
		try {
			response = client.newCall(request).execute();
			if (response.isSuccessful() && response.body() != null) {
				String responseBody = response.body().string();
				System.out.println(responseBody);
				JSONObject jsonResponse = new JSONObject(responseBody);
				JSONArray results = jsonResponse.getJSONArray("results");

				for (int i = 0; i < results.length(); i++) {
					try {
						JSONObject result = results.getJSONObject(i);
						Movie movie = new Movie();
						movie.setId(result.getInt("id"));
						movie.setTitle(result.getString("title"));
						movie.setOverview(result.getString("overview"));
						movie.setImg_url("https://image.tmdb.org/t/p/w342" + result.getString("poster_path"));
						movie.setPopularity(result.getLong("popularity"));
						movie.setVote_average(roundToFirstDecimal(result.getDouble("vote_average")));
						movie.setVote_count(result.getInt("vote_count"));
						movie.setRelease_date(Date.valueOf(result.getString("release_date")));

						JSONArray genreIdsJsonArray = result.getJSONArray("genre_ids");
						int[] genreIds = new int[genreIdsJsonArray.length()];
						String[] genreNames = new String[genreIdsJsonArray.length()];

						for (int j = 0; j < genreIdsJsonArray.length(); j++) {
							int genreId = genreIdsJsonArray.getInt(j);
							genreIds[j] = genreId;
							genreNames[j] = genreMap.get(genreId);
						}
						movie.setGenre_ids(genreIds);
						movie.setGenre_names(genreNames);

						movies.add(movie);
					} catch (Exception e) {

						System.err.println(i + "번째 인덱스에서 에러 발생. 에러 메시지: " + e.getMessage());
					}
				}

			} else {
				System.out.println("응답 에러남");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logger.error(e.getMessage());

		} finally {
			if (response != null) {
				response.close();
			}
		}

		return movies;
	}

	@Override
	@Cacheable(value = "topRatedMoviesCache")
	public List<Movie> topRatedMovies(Map<Integer, String> genreMap) {
		List<Movie> movies = new ArrayList<>();
		Request request = new Request.Builder().url("https://api.themoviedb.org/3/movie/top_rated?language=ko-KR&page=1&region=KR").get().addHeader("accept", "application/json").addHeader("Authorization", propertiesService.getString("TMDB.Access.Token")).build();

		Response response = null;
		try {
			response = client.newCall(request).execute();
			if (response.isSuccessful() && response.body() != null) {
				String responseBody = response.body().string();
				System.out.println(responseBody);
				JSONObject jsonResponse = new JSONObject(responseBody);
				JSONArray results = jsonResponse.getJSONArray("results");

				for (int i = 0; i < results.length(); i++) {
					try {
						JSONObject result = results.getJSONObject(i);
						Movie movie = new Movie();
						movie.setId(result.getInt("id"));
						movie.setTitle(result.getString("title"));
						movie.setOverview(result.getString("overview"));
						movie.setImg_url("https://image.tmdb.org/t/p/w342" + result.getString("poster_path"));
						movie.setPopularity(result.getLong("popularity"));
						movie.setVote_average(roundToFirstDecimal(result.getDouble("vote_average")));
						movie.setVote_count(result.getInt("vote_count"));
						movie.setRelease_date(Date.valueOf(result.getString("release_date")));

						JSONArray genreIdsJsonArray = result.getJSONArray("genre_ids");
						int[] genreIds = new int[genreIdsJsonArray.length()];
						String[] genreNames = new String[genreIdsJsonArray.length()];

						for (int j = 0; j < genreIdsJsonArray.length(); j++) {
							int genreId = genreIdsJsonArray.getInt(j);
							genreIds[j] = genreId;
							genreNames[j] = genreMap.get(genreId);
						}
						movie.setGenre_ids(genreIds);
						movie.setGenre_names(genreNames);

						movies.add(movie);
					} catch (Exception e) {

						System.err.println(i + "번째 인덱스에서 에러 발생. 에러 메시지: " + e.getMessage());
					}
				}

			} else {
				System.out.println("응답 에러남");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logger.error(e.getMessage());

		} finally {
			if (response != null) {
				response.close();
			}
		}

		return movies;
	}

	@Override
	@Cacheable(value = "nowPlayingMoviesCache")
	public List<Movie> nowPlayingMovies(Map<Integer, String> genreMap) {
		List<Movie> movies = new ArrayList<>();
		Request request = new Request.Builder().url("https://api.themoviedb.org/3/movie/now_playing?language=ko-KR&page=1&region=KR").get().addHeader("accept", "application/json").addHeader("Authorization", propertiesService.getString("TMDB.Access.Token")).build();

		Response response = null;
		try {
			response = client.newCall(request).execute();
			if (response.isSuccessful() && response.body() != null) {
				String responseBody = response.body().string();
				System.out.println(responseBody);
				JSONObject jsonResponse = new JSONObject(responseBody);
				JSONArray results = jsonResponse.getJSONArray("results");

				for (int i = 0; i < results.length(); i++) {
					try {
						JSONObject result = results.getJSONObject(i);
						Movie movie = new Movie();
						movie.setId(result.getInt("id"));
						movie.setTitle(result.getString("title"));
						movie.setOverview(result.getString("overview"));
						movie.setImg_url("https://image.tmdb.org/t/p/w342" + result.getString("poster_path"));
						movie.setPopularity(result.getLong("popularity"));
						movie.setVote_average(roundToFirstDecimal(result.getDouble("vote_average")));
						movie.setVote_count(result.getInt("vote_count"));
						movie.setRelease_date(Date.valueOf(result.getString("release_date")));

						JSONArray genreIdsJsonArray = result.getJSONArray("genre_ids");
						int[] genreIds = new int[genreIdsJsonArray.length()];
						String[] genreNames = new String[genreIdsJsonArray.length()];

						for (int j = 0; j < genreIdsJsonArray.length(); j++) {
							int genreId = genreIdsJsonArray.getInt(j);
							genreIds[j] = genreId;
							genreNames[j] = genreMap.get(genreId);
						}
						movie.setGenre_ids(genreIds);
						movie.setGenre_names(genreNames);

						movies.add(movie);
					} catch (Exception e) {

						System.err.println(i + "번째 인덱스에서 에러 발생. 에러 메시지: " + e.getMessage());
					}
				}

			} else {
				System.out.println("응답 에러남");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logger.error(e.getMessage());

		} finally {
			if (response != null) {
				response.close();
			}
		}

		return movies;
	}

	@Override
	@Cacheable(value = "popularPeopleCache")
	public List<Person> popularPeople() {
		List<Person> persons = new ArrayList<>();
		Request request = new Request.Builder().url("https://api.themoviedb.org/3/person/popular?language=ko-KR&page=1").get().addHeader("accept", "application/json").addHeader("Authorization", propertiesService.getString("TMDB.Access.Token")).build();
		Response response = null;

		try {
			response = client.newCall(request).execute();
			if (response.isSuccessful() && response.body() != null) {
				String responseBody = response.body().string();
				System.out.println(responseBody);
				JSONObject jsonResponse = new JSONObject(responseBody);
				JSONArray results = jsonResponse.getJSONArray("results");
				int max = (results.length() < 4) ? results.length() : 4; //최대 4개까지. 단, 불러온 개수가 4보다 적으면 불러온 만큼만 출력
				for (int j = 0; j < max; j++) {
					JSONObject result = results.getJSONObject(j);
					Person person = new Person();
					person.setDepartment(result.getString("known_for_department"));
					person.setId(result.getInt("id"));
					person.setImg_url("https://image.tmdb.org/t/p/w185" + result.getString("profile_path"));
					person.setName(result.getString("name"));
					persons.add(person);

				}
			} else {
				System.out.println("응답 에러남");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logger.error(e.getMessage());
		} finally {
			if (response != null) {
				response.close();
			}
		}

		return persons;
	}

	@Override
	public SingleMovieDTO singleMovie(int id, Map<Integer, String> genreMap) {
		Request request = new Request.Builder().url("https://api.themoviedb.org/3/movie/1034541?append_to_response=videos&language=ko-KR").get().addHeader("accept", "application/json").addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1Njk4OWZmMjMxOGQ4MWRlZTM5YTZhN2E1NjEzNjNhYyIsIm5iZiI6MTczMTkwMTMxNy4yMjA4NDYyLCJzdWIiOiI2NzMyYTRhNjYwN2U4YWEyMGVmNjdiMWEiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.N6xEzYqx7A6JQbWpjIIFRKnXUS8HEL_krVOW-by98d0").build();
		Response response = null;
		SingleMovieDTO singleMovie = new SingleMovieDTO();

		try {
			response = client.newCall(request).execute();
			if (response.isSuccessful() && response.body() != null) {
				String responseBody = response.body().string();
				System.out.println(responseBody);
				JSONObject jsonResponse = new JSONObject(responseBody);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logger.error(e.getMessage());
		} finally {
			if (response != null) {
				response.close();
			}
		}

		return null;
	}
}

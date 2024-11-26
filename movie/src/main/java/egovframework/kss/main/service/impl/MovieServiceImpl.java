package egovframework.kss.main.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.annotation.PreDestroy;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.kss.main.dao.MovieDAO;
import egovframework.kss.main.dto.MovieSearchResultDTO;
import egovframework.kss.main.dto.PersonSearchResultDTO;
import egovframework.kss.main.dto.SingleMovieDTO;
import egovframework.kss.main.model.Cast;
import egovframework.kss.main.model.Crew;
import egovframework.kss.main.model.Genre;
import egovframework.kss.main.model.Movie;
import egovframework.kss.main.model.Person;
import egovframework.kss.main.model.Video;
import egovframework.kss.main.service.MovieService;
import egovframework.kss.main.service.UserService;
import egovframework.kss.main.vo.UserVO;
import egovframework.rte.fdl.property.EgovPropertyService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Service("MovieService")
public class MovieServiceImpl implements MovieService {

	@Autowired
	private EgovPropertyService propertiesService;

	@Autowired
	UserService userService;

	@Autowired
	MovieDAO movieDAO;

	private static Logger Logger = LoggerFactory.getLogger(MovieServiceImpl.class);
	private static final OkHttpClient client = new OkHttpClient();

	@PreDestroy
	public void cleanup() {
		client.connectionPool().evictAll(); // 연결 풀 비우기
	}

	@Override
	@Cacheable(value = "genreCache")
	public Map<Integer, String> fetchGenres() {
		Map<Integer, String> genreMap = new HashMap<>();
		Request request2 = new Request.Builder().url("https://api.themoviedb.org/3/genre/movie/list?language=ko").get().addHeader("accept", "application/json").addHeader("Authorization", propertiesService.getString("TMDB.Access.Token")).build();
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
					person.setId(result.getInt("id"));
					person.setImg_url("https://image.tmdb.org/t/p/w185" + result.getString("profile_path"));
					person.setName(result.getString("name"));
					person.setDepartment(result.getString("known_for_department"));
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
	public SingleMovieDTO movieDetail(int id, Map<Integer, String> genreMap) {
		Request request = new Request.Builder().url("https://api.themoviedb.org/3/movie/" + id + "?append_to_response=videos,credits&language=ko-KR").get().addHeader("accept", "application/json").addHeader("Authorization", propertiesService.getString("TMDB.Access.Token")).build();
		Response response = null;
		SingleMovieDTO singleMovie = new SingleMovieDTO();

		try {
			response = client.newCall(request).execute();
			if (response.isSuccessful() && response.body() != null) {
				String responseBody = response.body().string();
				System.out.println(responseBody);
				JSONObject jsonResponse = new JSONObject(responseBody);

				singleMovie.setTitle(jsonResponse.getString("title"));
				singleMovie.setRuntime(jsonResponse.getInt("runtime"));
				singleMovie.setOverview(jsonResponse.getString("overview"));
				singleMovie.setRelease_date(Date.valueOf(jsonResponse.getString("release_date")));
				singleMovie.setVote_average(roundToFirstDecimal(jsonResponse.getDouble("vote_average")));
				singleMovie.setImg_url("https://image.tmdb.org/t/p/w342" + jsonResponse.getString("poster_path"));
				JSONArray genreArray = jsonResponse.getJSONArray("genres");
				Genre[] genres = new Genre[genreArray.length()];

				for (int i = 0; i < genreArray.length(); i++) {
					JSONObject genre = genreArray.getJSONObject(i);
					genres[i] = new Genre(genre.getInt("id"), id, genre.getString("name"));

				}
				singleMovie.setGenres(genres);

				JSONObject credits = jsonResponse.getJSONObject("credits");

				JSONArray castArray = credits.getJSONArray("cast");
				Cast[] casts = new Cast[castArray.length()];

				for (int i = 0; i < castArray.length(); i++) {
					JSONObject castObject = castArray.getJSONObject(i);
					Cast cast = new Cast();

					// 속성 이름과 메소드 매핑
					Map<String, Consumer<Cast>> propertySetters = new HashMap<>();
					propertySetters.put("cast_id", c -> c.setCast_id(castObject.getInt("cast_id")));
					propertySetters.put("id", c -> c.setPerson_id(castObject.getInt("id")));
					propertySetters.put("order", c -> c.setOrder(castObject.getInt("order")));
					propertySetters.put("profile_path", c -> c.setImg_url("https://image.tmdb.org/t/p/w300" + castObject.getString("profile_path")));
					propertySetters.put("name", c -> c.setName(castObject.getString("name")));
					propertySetters.put("character", c -> c.setCharacter(castObject.getString("character")));

					// 속성 설정
					for (String property : propertySetters.keySet()) {
						if (castObject.has(property) && !castObject.isNull(property)) {
							propertySetters.get(property).accept(cast);
						}
					}

					cast.setMovie_id(id); // movie_id는 항상 설정
					casts[i] = cast;

				}
				singleMovie.setCasts(casts);

				JSONArray crewArray = credits.getJSONArray("crew");
				Crew[] crews = new Crew[crewArray.length()];

				for (int i = 0; i < crewArray.length(); i++) {
					JSONObject crewObject = crewArray.getJSONObject(i);
					Crew crew = new Crew();

					// 속성 이름과 메소드 매핑
					Map<String, Consumer<Crew>> propertySetters = new HashMap<>();
					propertySetters.put("cast_id", c -> c.setCast_id(crewObject.getInt("cast_id")));
					propertySetters.put("id", c -> c.setPerson_id(crewObject.getInt("id")));
					propertySetters.put("order", c -> c.setOrder(crewObject.getInt("order")));
					propertySetters.put("profile_path", c -> c.setImg_url("https://image.tmdb.org/t/p/w300" + crewObject.getString("profile_path")));
					propertySetters.put("name", c -> c.setName(crewObject.getString("name")));
					propertySetters.put("department", c -> c.setDepartment(crewObject.getString("department")));
					propertySetters.put("job", c -> c.setJob(crewObject.getString("job")));

					// 속성 설정
					for (String property : propertySetters.keySet()) {
						if (crewObject.has(property) && !crewObject.isNull(property)) {
							propertySetters.get(property).accept(crew);
						}
					}

					crew.setMovie_id(id); // movie_id는 항상 설정
					crews[i] = crew;
				}
				singleMovie.setCrews(crews);

				JSONObject videoResponse = jsonResponse.getJSONObject("videos");
				JSONArray videoArray = videoResponse.getJSONArray("results");
				Video[] videos = new Video[videoArray.length()];
				boolean selectTrailer = false;
				for (int i = 0; i < videoArray.length(); i++) {
					try {
						JSONObject videoObject = videoArray.getJSONObject(i);
						Video video = new Video();
						video.setMovie_id(id);
						video.setId(videoObject.getString("id"));
						video.setKey(videoObject.getString("key"));
						video.setName(videoObject.getString("name"));
						String publishedAtString = videoObject.getString("published_at");
						if (!selectTrailer && videoObject.getString("type").equals("Trailer")) {
							singleMovie.setTrailer(videoObject.getString("key"));
							selectTrailer = true;
						}
						// T를 공백으로 바꾸고 Z를 제거
						publishedAtString = publishedAtString.replace("T", " ").replace("Z", "");
						video.setPublished_at(Timestamp.valueOf(publishedAtString.substring(0, 19)));
						video.setSite(videoObject.getString("site"));

						videos[i] = video;
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}

				}
				singleMovie.setVideos(videos);

			}
		} catch (IOException e) {

			Logger.error(e.getMessage());
		} finally {
			if (response != null) {
				response.close();
			}
		}

		request = new Request.Builder().url("https://api.themoviedb.org/3/movie/" + id + "/images?include_image_language=ko&language=ko").get().addHeader("accept", "application/json").addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1Njk4OWZmMjMxOGQ4MWRlZTM5YTZhN2E1NjEzNjNhYyIsIm5iZiI6MTczMTk4ODYyMS4wMTczNzE3LCJzdWIiOiI2NzMyYTRhNjYwN2U4YWEyMGVmNjdiMWEiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.rzPvwXmyx-l1lO8pY033zhkWFZlrue83qS0dJtMwXmo").build();
		try {
			response = client.newCall(request).execute();
			if (response.isSuccessful() && response.body() != null) {
				String responseBody = response.body().string();
				System.out.println(responseBody);
				JSONObject jsonResponse = new JSONObject(responseBody);
				JSONArray backdrops = jsonResponse.getJSONArray("backdrops");
				if (backdrops.length() >= 1) {
					String backdrop_url = "https://image.tmdb.org/t/p/original" + backdrops.getJSONObject(0).getString("file_path");
					singleMovie.setBackdrop_url(backdrop_url);
				}

				JSONArray imageArray = jsonResponse.getJSONArray("posters");
				String[] images = new String[imageArray.length()];
				for (int i = 0; i < imageArray.length(); i++) {
					JSONObject imageObject = imageArray.getJSONObject(i);
					String image = "https://image.tmdb.org/t/p/w500" + imageObject.getString("file_path");
					images[i] = image;
				}
				singleMovie.setImage_urls(images);
			}
		} catch (IOException e) {

			Logger.error(e.getMessage());
		} finally {
			if (response != null) {
				response.close();
			}
		}

		return singleMovie;
	}

	@Override
	public MovieSearchResultDTO movieSearch(int page, String query) {
		Request request = new Request.Builder().url("https://api.themoviedb.org/3/search/movie?query=" + query + "&include_adult=true&language=ko-KR&page=" + page + "&region=KR").get().addHeader("accept", "application/json").addHeader("Authorization", propertiesService.getString("TMDB.Access.Token")).build();
		Response response = null;
		MovieSearchResultDTO searchResult = new MovieSearchResultDTO();
		try {
			response = client.newCall(request).execute();
			if (response.isSuccessful() && response.body() != null) {
				String responseBody = response.body().string();
				System.out.println(responseBody);
				JSONObject jsonResponse = new JSONObject(responseBody);
				searchResult.setTotal_pages(jsonResponse.getInt("total_pages"));
				searchResult.setTotal_results(jsonResponse.getInt("total_results"));
				JSONArray results = jsonResponse.getJSONArray("results");
				Movie[] movies = new Movie[results.length()];
				for (int i = 0; i < results.length(); i++) {
					JSONObject result = results.getJSONObject(i);
					Movie movie = new Movie();
					movie.setId(result.getInt("id"));
					movie.setImg_url("https://image.tmdb.org/t/p/w342" + result.getString("poster_path"));
					movie.setTitle(result.getString("title"));
					movie.setVote_average(roundToFirstDecimal(result.getDouble("vote_average")));
					movies[i] = movie;
				}
				searchResult.setMovies(movies);
			} else {
				System.out.println("응답 에러남");
			}
		} catch (Exception e) {
			Logger.error(e.getMessage());
		} finally {
			if (response != null) {
				response.close();
			}
		}
		return searchResult;
	}

	@Override
	public PersonSearchResultDTO personSearch(int page, String query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public void addFavorite(SingleMovieDTO movie) {
		UserVO user = userService.getCurrentUser();
		insertMovie(movie);

	}

	@Override
	public void insertMovie(SingleMovieDTO movie) {

	}

	@Override
	public void insertCast(Cast cast) {
		// TODO Auto-generated method stub

	}

	@Override
	public void insertGenre(Genre genre) {
		// TODO Auto-generated method stub

	}

	@Override
	public void insertVideo(Video video) {
		// TODO Auto-generated method stub

	}

	@Override
	public void insertCrew(Crew crew) {
		// TODO Auto-generated method stub

	}

}

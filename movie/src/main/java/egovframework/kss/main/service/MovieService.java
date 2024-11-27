package egovframework.kss.main.service;

import java.util.List;
import java.util.Map;

import egovframework.kss.main.dto.MovieSearchResultDTO;
import egovframework.kss.main.dto.PersonSearchResultDTO;
import egovframework.kss.main.dto.SingleMovieDTO;
import egovframework.kss.main.model.Cast;
import egovframework.kss.main.model.Crew;
import egovframework.kss.main.model.Genre;
import egovframework.kss.main.model.Movie;
import egovframework.kss.main.model.Person;
import egovframework.kss.main.model.Video;

public interface MovieService {

	public Map<Integer, String> fetchGenres();

	public List<Movie> fetchPopularMovies(Map<Integer, String> genreMap);

	public List<Movie> upComingMovies(Map<Integer, String> genreMap);

	public List<Movie> topRatedMovies(Map<Integer, String> genreMap);

	public List<Movie> nowPlayingMovies(Map<Integer, String> genreMap);

	public List<Person> popularPeople();

	public SingleMovieDTO movieDetail(int id, Map<Integer, String> genreMap);

	public MovieSearchResultDTO movieSearch(int page, String query);

	public PersonSearchResultDTO personSearch(int page, String query);

	public boolean isFavorite(int id);

	public void addFavorite(SingleMovieDTO movie);

	public void insertMovie(SingleMovieDTO movie);

	public void insertCast(Cast cast);

	public void insertGenre(Genre genre);

	public void insertMovieGenre(Map<String, Object> params);

	public void insertVideo(Video video);

	public void insertCrew(Crew crew);

}

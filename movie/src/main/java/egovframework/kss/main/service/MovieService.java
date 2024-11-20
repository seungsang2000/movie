package egovframework.kss.main.service;

import java.util.List;
import java.util.Map;

import egovframework.kss.main.dto.MovieSearchResultDTO;
import egovframework.kss.main.dto.PersonSearchResultDTO;
import egovframework.kss.main.dto.SingleMovieDTO;
import egovframework.kss.main.model.Movie;
import egovframework.kss.main.model.Person;

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
}

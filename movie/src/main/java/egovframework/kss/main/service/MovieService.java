package egovframework.kss.main.service;

import java.util.List;
import java.util.Map;

import egovframework.kss.main.model.Movie;
import egovframework.kss.main.model.Person;

public interface MovieService {

	void fetchGenres(Map<Integer, String> genreMap);

	public List<Movie> fetchPopularMovies(Map<Integer, String> genreMap);

	public List<Movie> upComingMovies(Map<Integer, String> genreMap);

	public List<Movie> topRatedMovies(Map<Integer, String> genreMap);

	public List<Person> popularPeople();

}

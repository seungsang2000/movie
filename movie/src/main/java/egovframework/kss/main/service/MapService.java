package egovframework.kss.main.service;

import java.util.List;

import egovframework.kss.main.model.MovieTheater;

public interface MapService {

	List<MovieTheater> getNearbyMovieTheaters(String lat, String lng);

}

package egovframework.kss.main.mapper;

import java.util.Map;

import egovframework.kss.main.dto.SingleMovieDTO;
import egovframework.kss.main.model.Cast;
import egovframework.kss.main.model.Crew;
import egovframework.kss.main.model.Genre;
import egovframework.kss.main.model.Video;

public interface MovieMapper {
	public void insertMovie(SingleMovieDTO movie);

	public void insertCast(Cast cast);

	public void insertGenre(Genre genre);

	public void insertMovieGenre(Map<String, Object> params);

	public void insertVideo(Video video);

	public void insertCrew(Crew crew);

	public boolean isFavorite(Map<String, Object> params);

	public void insertFavorite(Map<String, Object> params);

	public void insertMovieAndRelated(SingleMovieDTO movie);

}

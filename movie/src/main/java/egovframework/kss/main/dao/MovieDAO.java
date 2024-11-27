package egovframework.kss.main.dao;

import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import egovframework.kss.main.dto.SingleMovieDTO;
import egovframework.kss.main.mapper.MovieMapper;
import egovframework.kss.main.model.Cast;
import egovframework.kss.main.model.Crew;
import egovframework.kss.main.model.Genre;
import egovframework.kss.main.model.Video;

@Repository("MovieDAO")
public class MovieDAO {

	@Autowired
	SqlSession sqlSession;

	public boolean isFavorite(Map<String, Object> params) {
		MovieMapper movieMapper = sqlSession.getMapper(MovieMapper.class);
		return movieMapper.isFavorite(params);
	}

	public void insertFavorite(Map<String, Object> params) {
		MovieMapper movieMapper = sqlSession.getMapper(MovieMapper.class);
		movieMapper.insertFavorite(params);
	}

	public void insertMovie(SingleMovieDTO movie) {
		MovieMapper movieMapper = sqlSession.getMapper(MovieMapper.class);
		movieMapper.insertMovie(movie);
	}

	public void insertCast(Cast cast) {
		MovieMapper movieMapper = sqlSession.getMapper(MovieMapper.class);
		movieMapper.insertCast(cast);
	}

	public void insertGenre(Genre genre) {
		MovieMapper movieMapper = sqlSession.getMapper(MovieMapper.class);
		movieMapper.insertGenre(genre);
	}

	public void insertMovieGenre(Map<String, Object> params) {
		MovieMapper movieMapper = sqlSession.getMapper(MovieMapper.class);
		movieMapper.insertMovieGenre(params);
	}

	public void insertVideo(Video video) {
		MovieMapper movieMapper = sqlSession.getMapper(MovieMapper.class);
		movieMapper.insertVideo(video);
	}

	public void insertCrew(Crew crew) {
		MovieMapper movieMapper = sqlSession.getMapper(MovieMapper.class);
		movieMapper.insertCrew(crew);
	}

	public void insertMovieAndRelated(SingleMovieDTO movie) {
		MovieMapper movieMapper = sqlSession.getMapper(MovieMapper.class);
		movieMapper.insertMovieAndRelated(movie);
	}

}

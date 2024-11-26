package egovframework.kss.main.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import egovframework.kss.main.dto.SingleMovieDTO;
import egovframework.kss.main.mapper.MovieMapper;

@Repository("MovieDAO")
public class MovieDAO {

	@Autowired
	SqlSession sqlSession;

	public void insertMovie(SingleMovieDTO movie) {
		MovieMapper movieMapper = sqlSession.getMapper(MovieMapper.class);
		movieMapper.insertMovie(movie);
	}

}

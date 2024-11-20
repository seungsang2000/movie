package egovframework.kss.main.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import egovframework.kss.main.mapper.UserMapper;
import egovframework.kss.main.vo.UserVO;

@Repository("UserDAO")
public class UserDAO {

	@Autowired
	SqlSession sqlSession;

	public void insertUser(UserVO newUser) {
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
		userMapper.insertUser(newUser);
	}

}

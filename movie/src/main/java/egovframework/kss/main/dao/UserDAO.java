package egovframework.kss.main.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import egovframework.kss.main.dto.PasswordKeyDTO;
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

	public UserVO selectUserByUsername(String username) {
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
		return userMapper.selectUserByUsername(username);
	}

	public void insertPasswordKey(PasswordKeyDTO passwordKeyDTO) {
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
		userMapper.insertPasswordKey(passwordKeyDTO);

	}

	public void deletePasswordKeyByEmail(String tomail) {
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
		userMapper.deletePasswordKeyByEmail(tomail);

	}

	public UserVO selectUserByEmail(String email) {
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
		return userMapper.selectUserByEmail(email);
	}

}

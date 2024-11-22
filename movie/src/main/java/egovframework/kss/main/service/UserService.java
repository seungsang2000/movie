package egovframework.kss.main.service;

import java.util.Map;

import egovframework.kss.main.vo.UserVO;

public interface UserService {
	public void signUp(String email, String password, String username) throws Exception;

	public UserVO selectUserByUsername(String username);

	public UserVO getCurrentUser();

	Map<String, Object> sendMail(String email);

	public UserVO selectUserByEmail(String email);

}

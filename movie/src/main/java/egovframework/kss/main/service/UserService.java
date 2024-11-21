package egovframework.kss.main.service;

import egovframework.kss.main.vo.UserVO;

public interface UserService {
	public void signUp(String email, String password, String username) throws Exception;

	public UserVO selectUserByUsername(String username);

	public UserVO getCurrentUser();

}

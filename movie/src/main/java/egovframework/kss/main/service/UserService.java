package egovframework.kss.main.service;

import java.util.Map;

import egovframework.kss.main.vo.UserVO;

public interface UserService {
	public void signUp(String email, String password, String username) throws Exception;

	public UserVO selectUserByUsername(String username);

	public UserVO getCurrentUser();

	Map<String, Object> sendMail(String email);

	public UserVO selectUserByEmail(String email);

	public Map<String, Object> verifyAuthKey(String authKey);

	public boolean checkExistUserEmailForUpdate(Map<String, Object> params);

	public void updateUser(UserVO user, Map<String, Object> param) throws Exception;

}

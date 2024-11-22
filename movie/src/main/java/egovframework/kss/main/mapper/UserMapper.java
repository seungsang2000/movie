
package egovframework.kss.main.mapper;

import java.util.Map;

import egovframework.kss.main.dto.PasswordKeyDTO;
import egovframework.kss.main.vo.UserVO;

public interface UserMapper {
	public void insertUser(UserVO newUser);

	public UserVO selectUserByUsername(String username);

	public void insertPasswordKey(PasswordKeyDTO passwordKeyDTO);

	public PasswordKeyDTO selectVerifyKey(String key);

	public void deletePasswordKeyByEmail(String tomail);

	public UserVO selectUserByEmail(String email);

	public boolean checkExistUserEmailForUpdate(Map<String, Object> params);

	public void updateUser(UserVO user);

}

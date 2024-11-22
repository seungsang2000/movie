
package egovframework.kss.main.mapper;

import egovframework.kss.main.dto.PasswordKeyDTO;
import egovframework.kss.main.vo.UserVO;

public interface UserMapper {
	public void insertUser(UserVO newUser);

	public UserVO selectUserByUsername(String username);

	public void insertPasswordKey(PasswordKeyDTO passwordKeyDTO);

	public void deletePasswordKeyByEmail(String tomail);

	public UserVO selectUserByEmail(String email);

}

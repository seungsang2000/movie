package egovframework.kss.main.service.impl;

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import egovframework.kss.main.dao.UserDAO;
import egovframework.kss.main.service.UserService;
import egovframework.kss.main.vo.UserVO;

@Service("UserService")
public class UserServiceImpl implements UserService {

	@Autowired
	UserDAO userDAO;

	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Override
	public void signUp(String email, String password, String username) throws Exception {
		// 이메일 유효성 검사
		String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\\\.[a-zA-Z]{2,}$";
		Pattern emailPattern = Pattern.compile(emailRegex);
		if (!emailPattern.matcher(email).matches()) {
			throw new Exception("이메일 형식이 잘못되었습니다.");
		}

		// 사용자 이름 유효성 검사
		String usernameRegex = "^[a-zA-Z][a-zA-Z0-9-_\\.]{8,20}$";
		if (!username.matches(usernameRegex)) {
			throw new Exception("사용자 이름은 첫 글자가 영어여야 하고, 8-20자 사이, 영어, 숫자, -, _, .만 포함할 수 있습니다.");
		}

		// 비밀번호 유효성 검사
		String passwordRegex = "^[a-zA-Z\\d\\W]{8,20}$";
		if (!password.matches(passwordRegex)) {
			throw new Exception("비밀번호는 8~20자의 영어, 숫자, 특수문자만 포함해야 합니다.");
		}

		String hashedPassword = passwordEncoder.encode(password);
		UserVO newUser = new UserVO();
		newUser.setEmail(email);
		newUser.setPassword(hashedPassword);
		newUser.setUsername(username);
		newUser.setRole("user"); //지금은 관리자계정 만들 계획 없음.... 일단 전부 유저권한주고, 관리자 계정 필요시 해당사항 수정
		userDAO.insertUser(newUser);
	}

}

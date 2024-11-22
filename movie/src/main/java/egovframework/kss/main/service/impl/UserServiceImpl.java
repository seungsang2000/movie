package egovframework.kss.main.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import egovframework.kss.main.dao.UserDAO;
import egovframework.kss.main.dto.PasswordKeyDTO;
import egovframework.kss.main.exception.CustomException;
import egovframework.kss.main.mail.TempKey;
import egovframework.kss.main.model.CustomUserDetails;
import egovframework.kss.main.service.UserService;
import egovframework.kss.main.vo.UserVO;

@Service("UserService")
public class UserServiceImpl implements UserService {

	@Autowired
	UserDAO userDAO;

	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Autowired
	private JavaMailSender sender;

	@Override
	public void signUp(String email, String password, String username) throws Exception {
		// 이메일 유효성 검사
		String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
		Pattern emailPattern = Pattern.compile(emailRegex);
		if (!emailPattern.matcher(email).matches()) {
			throw new Exception("이메일 형식이 잘못되었습니다.");
		}

		// 사용자 이름 유효성 검사
		String usernameRegex = "^[a-zA-Z][a-zA-Z0-9-_\\.]{7,19}$";
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

	@Override
	public UserVO selectUserByUsername(String username) {
		return userDAO.selectUserByUsername(username);
	}

	@Override
	public UserVO getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();

		try {
			CustomUserDetails currentUser = (CustomUserDetails) principal;
			UserVO user = selectUserByUsername(currentUser.getUsername());
			if (user == null) {
				throw new CustomException("로그인 후 다시 시도해주세요");
			}
			user.setPassword(null);
			return user;
		} catch (Exception e) {
			throw new CustomException("유저 정보에 이상이 생겼습니다");
		}
	}

	@Override
	public Map<String, Object> sendMail(String email) {
		Map<String, Object> response = new HashMap<>();

		UserVO user = selectUserByEmail(email);

		if (user == null) {
			response.put("success", false);
			response.put("message", "해당 이메일을 찾을 수 없습니다.");
			return response;
		}

		String setfrom = "rovin054@gmail.com";
		String key = new TempKey().getKey(20, false);

		String tomail = user.getEmail();     // 받는 사람 이메일
		String title = "이메일 인증입니다.";
		String content = new StringBuilder().append(user.getUsername()).append("님! 이용해주셔서 감사합니다.\n").append("다음 인증키를 화면에 입력해주십시오.\n").append("인증키: ").append(key).toString();
		try {
			MimeMessage message = sender.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

			messageHelper.setFrom(setfrom, "영화 리뷰 사이트 운영진");  // 보내는사람 생략하거나 하면 정상작동을 안함 두번째 인자값은 보낼때의 이름이다.
			messageHelper.setTo(tomail);     // 받는사람 이메일
			messageHelper.setSubject(title); // 메일제목은 생략이 가능하다
			messageHelper.setText(content);  // 메일 내용

			sender.send(message);

			deletePasswordKeyByEmail(tomail);

			PasswordKeyDTO passwordKeyDTO = new PasswordKeyDTO();
			passwordKeyDTO.setEmail(tomail);
			passwordKeyDTO.setKey(key);

			insertPasswordKey(passwordKeyDTO);

			response.put("success", true);

		} catch (Exception e) {
			e.printStackTrace();
			response.put("success", false);
			response.put("message", "이메일 발송에 실패했습니다.");
		}

		return response;
	}

	private void insertPasswordKey(PasswordKeyDTO passwordKeyDTO) {
		userDAO.insertPasswordKey(passwordKeyDTO);

	}

	private void deletePasswordKeyByEmail(String tomail) {
		userDAO.deletePasswordKeyByEmail(tomail);

	}

	@Override
	public UserVO selectUserByEmail(String email) {
		return userDAO.selectUserByEmail(email);
	}

	@Override
	public Map<String, Object> verifyAuthKey(String authKey) {
		Map<String, Object> response = new HashMap<>();
		try {
			PasswordKeyDTO passwordKey = userDAO.selectVerifyKey(authKey);
			if (passwordKey != null) {
				response.put("success", true);
				userDAO.deletePasswordKeyByEmail(passwordKey.getEmail());
			} else {
				response.put("success", false);
				response.put("message", "인증키가 잘못되었습니다.");
			}
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "에러가 발생했습니다.");
		}

		return response;
	}

	@Override
	public boolean checkExistUserEmailForUpdate(Map<String, Object> params) {
		return userDAO.checkExistUserEmailForUpdate(params);
	}

	@Override
	public void updateUser(UserVO user) {
		userDAO.updateUser(user);
	}

}

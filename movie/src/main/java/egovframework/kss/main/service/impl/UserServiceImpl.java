package egovframework.kss.main.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		String passwordRegex = "^[a-zA-Z\\d\\W]{7,19}$";
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
	public boolean isUserLoggedIn() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		// 로그인된 사용자인지, 익명 사용자가 아닌지 확인
		return authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);
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
			messageHelper.setTo(tomail);
			messageHelper.setSubject(title);
			messageHelper.setText(content);

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
	@Transactional
	public Map<String, Object> verifyAuthKey(String authKey, String email) {
		Map<String, Object> response = new HashMap<>();
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("email", email);
			params.put("key", authKey);
			PasswordKeyDTO passwordKey = userDAO.selectVerifyKey(params);
			if (passwordKey != null) {
				String tempPassword = new TempKey().getKey(10, false);
				String hashedPassword = passwordEncoder.encode(tempPassword);
				params.put("password", hashedPassword);
				userDAO.updateUserPassword(params);
				userDAO.deletePasswordKeyByEmail(passwordKey.getEmail());
				response.put("success", true);
				response.put("tempPassword", tempPassword);
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
	public void updateUser(UserVO user, Map<String, Object> param) throws Exception {
		String email = (String) param.get("email");
		String username = (String) param.get("username");

		// 이메일 중복 검사
		if (checkExistUserEmailForUpdate(param)) {
			throw new Exception("이미 사용 중인 이메일입니다.");
		}

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

		user.setUsername(username);
		user.setEmail(email);
		userDAO.updateUser(user);

		// 변경된 사용자 정보를 기반으로 새로운 CustomUserDetails 생성
		CustomUserDetails updatedUserDetails = new CustomUserDetails(user.getUsername(), user.getPassword(), user.getRole(), user.getImage_path(), user.getId());

		// 새로운 Authentication 객체 생성 (비밀번호는 업데이트된 비밀번호를 사용)
		Authentication authentication = new UsernamePasswordAuthenticationToken(updatedUserDetails, updatedUserDetails.getPassword(), updatedUserDetails.getAuthorities());

		// SecurityContext에 새로운 Authentication 객체 설정
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	@Override
	public void updateUserPassword(String oldPassword, String newPassword) throws Exception {
		UserVO currentUser = getCurrentUser();
		if (passwordEncoder.matches(currentUser.getPassword(), oldPassword)) {
			throw new Exception("기존 비밀번호가 틀렸습니다.");
		}

		String passwordRegex = "^[a-zA-Z\\d\\W]{7,19}$";
		if (!newPassword.matches(passwordRegex)) {
			throw new Exception("비밀번호는 8~20자의 영어, 숫자, 특수문자만 포함해야 합니다.");
		}

		Map<String, Object> params = new HashMap<>();
		params.put("email", currentUser.getEmail());
		params.put("password", newPassword);
		userDAO.updateUserPassword(params);

	}

}

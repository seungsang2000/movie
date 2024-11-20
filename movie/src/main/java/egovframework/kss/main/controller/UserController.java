package egovframework.kss.main.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.kss.main.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService userService;

	@PostMapping("/signup.do")
	public ResponseEntity<Map<String, Object>> signUp(HttpServletRequest request) {
		Map<String, Object> response = new HashMap<>();

		try {
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			String username = request.getParameter("username");

			if (email == null || email.isEmpty()) {
				throw new Exception("이메일은 필수 입력 항목입니다.");
			}
			if (password == null || password.isEmpty()) {
				throw new Exception("비밀번호는 필수 입력 항목입니다.");
			}
			if (username == null || username.isEmpty()) {
				throw new Exception("사용자 이름은 필수 입력 항목입니다.");
			}

			// 서비스 호출
			userService.signUp(email, password, username);

			response.put("success", true);
			response.put("message", "회원가입 성공");
		} catch (DataIntegrityViolationException e) {
			// 중복된 이메일이나 사용자 이름이 있을 때
			response.put("success", false);
			response.put("message", "이메일 또는 사용자 이름이 이미 존재합니다.");
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", e.getMessage());
		}

		return ResponseEntity.ok(response);

	}

	@PostMapping("/login.do")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> login(HttpServletRequest request) {
		Map<String, Object> response = new HashMap<>();

		try {
			// 여기서 회원가입 수행
			response.put("success", true);
		} catch (Exception e) {
			response.put("success", false);
		}

		return ResponseEntity.ok(response);

	}

}

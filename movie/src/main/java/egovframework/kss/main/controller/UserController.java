package egovframework.kss.main.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.kss.main.service.UserService;
import egovframework.kss.main.vo.UserVO;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService userService;

	@PostMapping("/signup.do")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> signUp(@RequestBody Map<String, String> formData) {
		Map<String, Object> response = new HashMap<>();

		try {
			String email = formData.get("email");
			String password = formData.get("password");
			String username = formData.get("username");

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
			response.put("message", e.getMessage().toString());
		}
		System.out.println(response);
		return ResponseEntity.ok(response);

	}

	@PostMapping("/login.do")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> formData) {
		Map<String, Object> response = new HashMap<>();
		String password = formData.get("password");
		String username = formData.get("username");

		try {
			if (password == null || password.isEmpty()) {
				throw new Exception("비밀번호는 필수 입력 항목입니다.");
			}
			if (username == null || username.isEmpty()) {
				throw new Exception("사용자 이름은 필수 입력 항목입니다.");
			}
			UserVO user = userService.selectUserByUsername(username);

			//로그인 로직 만들기~~~~~
			response.put("success", true);
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", e.getMessage().toString());
		}

		return ResponseEntity.ok(response);

	}

	@RequestMapping("/myPage.do")
	public String myPage(Model model) {

		return "user/myPage";
	}

}

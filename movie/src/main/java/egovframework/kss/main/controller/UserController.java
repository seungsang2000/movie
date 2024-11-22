package egovframework.kss.main.controller;

import java.io.File;
import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import egovframework.kss.main.service.UserService;
import egovframework.kss.main.vo.UserVO;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService userService;

	private String saveImage(MultipartFile file) {
		String uploadDir = "C:\\upload\\"; // 실제 경로
		String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename(); // 중복 방지
		File destinationFile = new File(uploadDir + fileName);

		try {
			file.transferTo(destinationFile); // 파일 저장
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "upload/" + fileName; // 저장된 이미지 경로 반환 (웹에서 접근할 수 있도록)
	}

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

	@PostMapping("/sendEmail.do")
	@ResponseBody
	public Map<String, Object> sendEmail(@RequestParam String email) {
		Map<String, Object> response = userService.sendMail(email);
		return response;
	}

	@PostMapping("/verifyAuthKey.do")
	@ResponseBody
	public Map<String, Object> verifyAuthKey(@RequestParam String authKey) {
		Map<String, Object> response = userService.verifyAuthKey(authKey);
		return response;
	}

	@RequestMapping("/myPage.do")
	public String myPage(Model model) {
		UserVO currentUser = userService.getCurrentUser();
		model.addAttribute("currentUser", currentUser);

		return "user/myPage";
	}

	@PostMapping("/updateUserProfile.do")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> updateUserProfile(@RequestParam String name, @RequestParam String email) {
		Map<String, Object> response = new HashMap<>();

		UserVO user = userService.getCurrentUser();

		Map<String, Object> param = new HashMap<>();
		param.put("email", email);
		param.put("id", user.getId());

		// 이메일 중복 검사
		if (userService.checkExistUserEmailForUpdate(param)) {

			response.put("success", false);
			response.put("message", "이미 사용 중인 이메일입니다.");
			return ResponseEntity.ok(response);
		}

		user.setUsername(name);
		user.setEmail(email);

		userService.updateUser(user);

		response.put("success", true);
		return ResponseEntity.ok(response);
	}

}

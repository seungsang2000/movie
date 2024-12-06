package egovframework.kss.main.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
	public Map<String, Object> verifyAuthKey(@RequestParam String authKey, @RequestParam String email) {
		Map<String, Object> response = userService.verifyAuthKey(authKey, email);
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
	public ResponseEntity<Map<String, Object>> updateUserProfile(@RequestParam String username, @RequestParam String email) {
		Map<String, Object> response = new HashMap<>();

		UserVO user = userService.getCurrentUser();
		Map<String, Object> param = new HashMap<>();
		param.put("email", email);
		param.put("id", user.getId());
		param.put("username", username);

		try {
			userService.updateUser(user, param);
			response.put("success", true);
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", e.getMessage().toString());
		}
		return ResponseEntity.ok(response);
	}

	@RequestMapping("/tempPassword.do")
	public String tempPassword(@RequestParam String password, Model model) {
		model.addAttribute("password", password);

		return "user/tempPasswordPage";
	}

	@PostMapping("/changePassword.do")
	public Map<String, Object> changePassword(HttpServletRequest request) {
		Map<String, Object> response = new HashMap<>();
		try {
			//유효성 검사까지는 컨트롤러 단에서 수행
			String oldPassword = request.getParameter("oldPassword");
			String newPassword = request.getParameter("newPassword");
			String confirmPassword = request.getParameter("confirmPassword");

			if (oldPassword == null || oldPassword.isEmpty()) {
				throw new Exception("기존 비밀번호가 입력되지 않았습니다.");
			}
			if (newPassword == null || newPassword.isEmpty()) {
				throw new Exception("새 비밀번호가 입력되지 않았습니다.");
			}
			if (confirmPassword == null || confirmPassword.isEmpty()) {
				throw new Exception("비밀번호 확인이 입력되지 않았습니다.");
			}
			if (!newPassword.equals(confirmPassword)) {
				throw new Exception("입력된 비밀번호와 비밀번호 확인이 다릅니다");
			}

			userService.updateUserPassword(oldPassword, newPassword);
			response.put("success", true);

		} catch (Exception e) {
			response.put("success", false);
			response.put("message", e.getMessage());
			e.printStackTrace();
		}

		return response;
	}

}

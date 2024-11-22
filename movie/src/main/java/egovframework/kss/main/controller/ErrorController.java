package egovframework.kss.main.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorController {
	@RequestMapping("/error/403.do")
	public ModelAndView errorPage() {
		String message = "";
		try {
			message = URLEncoder.encode("로그인이 필요한 페이지입니다", "UTF-8");
		} catch (UnsupportedEncodingException e) {

		}
		return new ModelAndView("redirect:/errorPage.do?error=" + message);
	}
}

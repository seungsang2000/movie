package egovframework.kss.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

	@RequestMapping(value = "/main.do")
	public String mainPage() {
		return "login";
	}

	@RequestMapping(value = "/list.do")
	public String listPage() {
		return "list";
	}

	@RequestMapping(value = "/chkusr.do")
	public String checkUser() {
		return "chkusr";
	}

}

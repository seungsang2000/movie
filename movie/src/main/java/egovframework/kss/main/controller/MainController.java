package egovframework.kss.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.rte.fdl.property.EgovPropertyService;

@Controller
public class MainController {

	@Autowired
	private EgovPropertyService propertiesService;

	@RequestMapping(value = "/main.do")
	public String mainPage() {
		System.out.println(propertiesService.getString("TMDB.Access.Token"));
		return "index";
	}

}

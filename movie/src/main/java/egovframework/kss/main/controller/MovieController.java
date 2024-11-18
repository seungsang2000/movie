package egovframework.kss.main.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import egovframework.kss.main.service.MovieService;
import egovframework.rte.fdl.property.EgovPropertyService;
import okhttp3.OkHttpClient;

@Controller
public class MovieController {
	@Autowired
	private EgovPropertyService propertiesService;

	private static Logger Logger = LoggerFactory.getLogger(MainController.class);
	private static final OkHttpClient client = new OkHttpClient();

	@Autowired
	MovieService movieService;

	@RequestMapping("singleMovie.do")
	public String singleMovie(@RequestParam("id") int id, Model model) {

		Map<Integer, String> genreMap = new HashMap<>();
		movieService.fetchGenres(genreMap); //장르 id와 이름 가져오기

		return "singleMovie";
	}

}

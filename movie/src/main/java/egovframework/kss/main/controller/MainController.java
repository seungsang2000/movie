package egovframework.kss.main.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.kss.main.model.Movie;
import egovframework.kss.main.model.Person;
import egovframework.kss.main.service.MovieService;
import egovframework.rte.fdl.property.EgovPropertyService;
import okhttp3.OkHttpClient;

@Controller
public class MainController {

	@Autowired
	private EgovPropertyService propertiesService;

	private static Logger Logger = LoggerFactory.getLogger(MainController.class);
	private static final OkHttpClient client = new OkHttpClient();

	@Autowired
	MovieService movieService;

	@RequestMapping(value = "/main.do")
	public String mainPage(Model model) {
		System.out.println(propertiesService.getString("TMDB.Access.Token"));

		Map<Integer, String> genreMap = new HashMap<>();
		movieService.fetchGenres(genreMap); //장르 id와 이름 가져오기

		List<Movie> movies = movieService.fetchPopularMovies(genreMap); // 인기 영화 정보 가져오기
		model.addAttribute("popularMovies", movies);

		movies = movieService.upComingMovies(genreMap);
		model.addAttribute("upComingMovies", movies);

		movies = movieService.topRatedMovies(genreMap);
		model.addAttribute("topRatedMovies", movies);

		List<Person> persons = movieService.popularPeople();
		model.addAttribute("popularPeople", persons);

		return "home";
	}

}

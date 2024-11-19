package egovframework.kss.main.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.kss.main.model.Movie;
import egovframework.kss.main.model.Person;
import egovframework.kss.main.service.MovieService;

@Controller
public class MainController {

	@Autowired
	MovieService movieService;

	@RequestMapping(value = "/main.do")
	public String mainPage(Model model) {

		Map<Integer, String> genreMap = movieService.fetchGenres(); //장르 id와 이름 가져오기

		List<Movie> movies = movieService.fetchPopularMovies(genreMap); // 인기 영화 정보 가져오기
		model.addAttribute("popularMovies", movies);

		movies = movieService.upComingMovies(genreMap);
		model.addAttribute("upComingMovies", movies);

		movies = movieService.topRatedMovies(genreMap);
		model.addAttribute("topRatedMovies", movies);

		movies = movieService.nowPlayingMovies(genreMap);
		model.addAttribute("nowPlayingMovies", movies);

		List<Person> persons = movieService.popularPeople();
		model.addAttribute("popularPeople", persons);

		return "home";
	}

}

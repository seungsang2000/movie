package egovframework.kss.main.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import egovframework.kss.main.dto.SingleMovieDTO;
import egovframework.kss.main.service.MovieService;
import egovframework.kss.main.service.UserService;
import egovframework.kss.main.vo.UserVO;

@Controller
public class MovieController {
	@Autowired
	MovieService movieService;

	@Autowired
	UserService userService;

	@RequestMapping("singleMovie.do")
	public String singleMovie(@RequestParam("id") int id, Model model) {

		UserVO currentUser = userService.getCurrentUser();
		System.out.println(currentUser);

		Map<Integer, String> genreMap = movieService.fetchGenres(); //장르 id와 이름 가져오기
		SingleMovieDTO singlemovie = movieService.movieDetail(id, genreMap);
		model.addAttribute("movie", singlemovie);
		return "singleMovie";
	}

}

package egovframework.kss.main.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.kss.main.dto.SingleMovieDTO;
import egovframework.kss.main.service.MovieService;
import egovframework.kss.main.service.UserService;

@Controller
public class MovieController {
	@Autowired
	MovieService movieService;

	@Autowired
	UserService userService;

	@RequestMapping("singleMovie.do")
	public String singleMovie(@RequestParam("id") int id, Model model) {

		Map<Integer, String> genreMap = movieService.fetchGenres(); //장르 id와 이름 가져오기
		SingleMovieDTO singlemovie = movieService.movieDetail(id, genreMap);
		model.addAttribute("movie", singlemovie);
		return "singleMovie";
	}

	@PostMapping("addFavorite.do")
	@ResponseBody
	public Map<String, Object> addFavorite(@RequestParam("movie") SingleMovieDTO movie) {
		Map<String, Object> response = new HashMap<>();
		try {
			movieService.addFavorite(movie);
			response.put("success", true);
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "선호 영화 등록이 실패하였습니다");
		}

		return response;
	}

	@RequestMapping("map.do")
	public String map(@RequestParam(value = "cinema", required = false) String cinema) {
		if (cinema != null) {
			// 후에 시네마 검색하는 로직 쓰기~~
		}
		return "map";
	}

}

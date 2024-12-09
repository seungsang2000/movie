package egovframework.kss.main.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.kss.main.dto.MovieSearchResultDTO;
import egovframework.kss.main.exception.CustomException;
import egovframework.kss.main.model.Movie;
import egovframework.kss.main.model.MovieTheater;
import egovframework.kss.main.model.Person;
import egovframework.kss.main.service.MapService;
import egovframework.kss.main.service.MovieService;

@Controller
public class MainController {

	@Autowired
	MovieService movieService;

	@Autowired
	MapService mapService;

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

	@RequestMapping("/search.do")
	public String search(@RequestParam("type") String type, @RequestParam("query") String query, @RequestParam(value = "page", defaultValue = "1") int page, Model model) {

		model.addAttribute("query", query);
		model.addAttribute("page", page);
		if (type.equals("movie")) {
			MovieSearchResultDTO searchResult = movieService.movieSearch(page, query);
			model.addAttribute("searchResult", searchResult);
			return "movieSearch";
		} else if (type.equals("person")) {
			return "personSearch";
		} else {
			throw new CustomException("검색에 오류가 발생했습니다.");
		}

	}

	@RequestMapping(value = "/errorPage.do")
	public String errorPage(@RequestParam(required = false) String error, Model model) {
		model.addAttribute("error", error);
		return "errorPage";
	}

	@RequestMapping("map.do")
	public String map(@RequestParam(value = "cinema", required = false) String cinema) {
		if (cinema != null) {
			// 후에 시네마 검색하는 로직 쓰기~~
		}
		return "map_openLayers";
	}

	@PostMapping("/theaters.do")
	@ResponseBody
	public ResponseEntity<List<MovieTheater>> getNearbyMovieTheaters(HttpServletRequest request) {
		try {
			// 클라이언트에서 위도(lat)와 경도(lng) 값 가져오기
			String lat = request.getParameter("lat");
			String lng = request.getParameter("lng");

			// Google API에서 영화관 데이터를 가져오는 서비스 호출
			List<MovieTheater> movieTheaters = mapService.getNearbyMovieTheaters(lat, lng);

			// 영화관 목록 반환
			return new ResponseEntity<>(movieTheaters, HttpStatus.OK);

		} catch (Exception e) {
			// 예외 발생 시, 500 오류 반환
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}

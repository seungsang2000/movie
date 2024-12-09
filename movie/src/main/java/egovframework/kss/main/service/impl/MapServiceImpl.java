package egovframework.kss.main.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import egovframework.kss.main.model.MovieTheater;
import egovframework.kss.main.service.MapService;
import egovframework.rte.fdl.property.EgovPropertyService;

@Service("MapService")
public class MapServiceImpl implements MapService {

	@Autowired
	private EgovPropertyService propertiesService;

	@Override
	public List<MovieTheater> getNearbyMovieTheaters(String lat, String lng) {
		String API_KEY = propertiesService.getString("Google.Map.APIKey");

		String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + lat + "," + lng + "&radius=3000&type=movie_theater&key=" + API_KEY;

		RestTemplate restTemplate = new RestTemplate();
		String response = restTemplate.getForObject(url, String.class);

		System.out.println(response);

		return null;

		// 응답을 파싱하여 MovieTheater 객체 리스트로 변환
		//return parseMovieTheatersFromResponse(response);

	}

	private List<MovieTheater> parseMovieTheatersFromResponse(String response) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			JsonNode rootNode = objectMapper.readTree(response);
			JsonNode resultsNode = rootNode.get("results");

			List<MovieTheater> movieTheaters = new ArrayList<>();
			for (JsonNode resultNode : resultsNode) {
				String name = resultNode.get("name").asText();
				String address = resultNode.get("vicinity").asText();

				// MovieTheater 객체 생성 후 리스트에 추가
				MovieTheater movieTheater = new MovieTheater();
				movieTheaters.add(movieTheater);
			}

			return movieTheaters;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>(); // 실패 시 빈 리스트 반환
		}
	}

}

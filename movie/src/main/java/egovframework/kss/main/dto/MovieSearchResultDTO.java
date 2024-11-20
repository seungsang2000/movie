package egovframework.kss.main.dto;

import egovframework.kss.main.model.Movie;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieSearchResultDTO {
	private int total_pages;
	private int total_results;
	private Movie[] movies;
}

package egovframework.kss.main.dto;

import java.sql.Date;

import egovframework.kss.main.model.Cast;
import egovframework.kss.main.model.Genre;
import egovframework.kss.main.model.Video;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SingleMovieDTO {
	private int id;
	private String title;
	private String overview;
	private String img_url;
	private Date release_date;
	private Long popularity;
	private double vote_average;
	private int vote_count;
	private Genre[] genres;
	private Cast[] casts;
	private Video[] videos;
}

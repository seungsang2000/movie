package egovframework.kss.main.dto;

import java.sql.Date;

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
	private int[] genre_ids;
	private String[] genre_names;
}

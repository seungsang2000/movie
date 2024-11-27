package egovframework.kss.main.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cast {
	private int person_id;
	private int cast_id;
	private int movie_id;
	private int order;
	private String img_url;
	private String name;
	private String character;
	private String department;
}

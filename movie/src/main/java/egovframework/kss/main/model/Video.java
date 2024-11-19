package egovframework.kss.main.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Video {
	private String id;
	private String key;
	private String name;
	private String site;
	private Timestamp published_at;
	private int movieId;
}

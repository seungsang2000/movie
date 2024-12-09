package egovframework.kss.main.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieTheater {
	private String name;      // 영화관 이름
	private String vicinity;  // 영화관 위치
	private double lat;       // 위도
	private double lng;       // 경도
}

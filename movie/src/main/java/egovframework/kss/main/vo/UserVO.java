package egovframework.kss.main.vo;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserVO {

	private int id;
	private String role; // 권한
	private String email;
	private String username;
	private String password;
	private String image_path;
	private Timestamp created_at;
}

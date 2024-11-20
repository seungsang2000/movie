package egovframework.kss.main.dto;

import egovframework.kss.main.model.Person;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonSearchResultDTO {
	private int total_pages;
	private int total_results;
	private Person[] movies;
}

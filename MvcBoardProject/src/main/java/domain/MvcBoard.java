package domain;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MvcBoard {
	private int no;
	private String author;
	private String title;
	private String content;
	private int hit;
	private String ip;
	private Date postdate;
}
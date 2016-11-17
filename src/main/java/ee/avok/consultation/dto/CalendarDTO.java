package ee.avok.consultation.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CalendarDTO {
	private int id;
	private String title; // Required
	private Date date; // Required
	private String url;

}

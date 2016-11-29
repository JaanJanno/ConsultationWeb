package ee.avok.consultation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Used to export data to CSV.
 * 
 * @author Tõnis Kasekamp
 *
 */
@Data
@AllArgsConstructor
public class CsvBean {

	private String studentName;
	private String consultantName;
	private String consultationDate;
	private String consultationTime;

	private String language;
	private String programme;
	private String degree;
	private String department;
	private String textType;
	private String comments;
}

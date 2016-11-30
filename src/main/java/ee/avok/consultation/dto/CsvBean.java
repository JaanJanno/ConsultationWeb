package ee.avok.consultation.dto;

import ee.avok.consultation.domain.model.NewConsultationOption;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Used to export data to CSV.
 * 
 * @author Tõnis Kasekamp
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CsvBean {

	private String studentName;
	private String consultantName;
	private String date;
	private String time;

	private String language;
	private String programme;
	private String degree;
	private String department;
	private String textType;
	private String comments;

	// Consultant feedback
	private String discussionElement;
	private String discussedMaterial;
	private String coveredIssues;
	private String summary;
	private NewConsultationOption suggestedNewConsultation;

	// Student feedback
	private Boolean firstConsultation;
	private Boolean useful;
	private Boolean providedSupport;
	private Boolean comingBack;
	private String studentComments;
}

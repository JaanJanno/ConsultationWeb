package ee.avok.consultation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompletedDTO {

	private int id;
	private String name;
	private String consultantName;
	private boolean consultantFeedback;
	private boolean studentFeedback;
}

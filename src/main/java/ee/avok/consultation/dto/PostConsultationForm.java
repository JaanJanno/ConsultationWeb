package ee.avok.consultation.dto;

import lombok.Data;

@Data
public class PostConsultationForm {

	String discussion;
	String disscussedMaterial;
	String coveredIssues;
	
	String summary;
    String suggestedNewConsultation;
		
}

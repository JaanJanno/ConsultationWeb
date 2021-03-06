package ee.avok.consultation.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class ConsultantFeedback {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column
	String discussionElement;
	@Column
	String discussedMaterial;
	@Column
	String coveredIssues;
	@Column
	String summary;
	@Column
    NewConsultationOption suggestedNewConsultation;
	public ConsultantFeedback() {
		super();
	}

	public ConsultantFeedback(int id) {
		super();
		this.id = id;
	}
		
}
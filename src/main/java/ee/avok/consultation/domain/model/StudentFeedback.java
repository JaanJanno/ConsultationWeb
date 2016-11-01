package ee.avok.consultation.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.Setter;

@Entity
@Data
@Setter
public class StudentFeedback {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column
	Boolean firstConsultation;
	@Column
	Boolean useful;
	@Column
	Boolean providedSupport;
	@Column
	Boolean comingBack;
	@Column
	String  comments;
	@Column
	String uid; // SECRET field for authorization.
	
	public StudentFeedback() {
		super();
	}

	public StudentFeedback(int id) {
		super();
		this.id = id;
	}

}
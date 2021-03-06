package ee.avok.consultation.domain.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ee.avok.consultation.auth.domain.model.Account;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(exclude = "previous")
public class ConsultationRequest {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column
	private String name;
	@Column
	private String email;
	@Column
	private String programme;
	@Column
	private String degree;
	@Column
	private String textType;

	@Column
	private String department;

	@Column
	private String language;
	@Column(columnDefinition = "TEXT")
	private String comments;
	@Enumerated(EnumType.STRING)
	private ConsultationStatus status;

	@OneToOne
	private Upload upload;

	@ManyToOne(optional = true)
	private Account consultant;

	// Times are in this order
	@Temporal(TemporalType.TIMESTAMP)
	private Date receivedDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date acceptedDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date scheduledDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date meetingDate;
	@Temporal(TemporalType.TIMESTAMP)
	private Date completedDate;

	@Column
	private String meetingPlace;

	@OneToOne(optional = true)
	private ConsultationRequest previous;
	@OneToOne(mappedBy = "previous")
	private ConsultationRequest next;
	
	@OneToOne
	private StudentFeedback studentFeedback;
	@OneToOne
	private ConsultantFeedback consultantFeedback;

	public String generateUploadUrl() {
		return "/uploads/" + Integer.toString(upload.getId()) + "/" + upload.getFilename();
	}

	public String generateFeedbackUrl(String baseUrl) {
		String url = baseUrl;
		return url + "/requests/" + Integer.toString(id) + "/studentfeedback/" + studentFeedback.getUid();
	}

	public boolean hasFile() {
		return this.upload != null;
	}
	
	public boolean hasStudentFeedback() {
		return studentFeedback.getUid() == null;
	}
	
	public boolean hasConsultantFeedback() {
		return consultantFeedback != null;
	}

	public ConsultationRequest() {
	}

	public ConsultationRequest(int id) {
		this.id = id;
	}

	public ConsultationRequest reConsultation(boolean sameConsultant) {
		ConsultationRequest req = new ConsultationRequest();
		req.name = this.name;
		req.email = this.email;
		req.programme = this.programme;
		req.degree = this.degree;
		req.textType = this.textType;
		req.department = this.department;
		req.language = this.language;
		req.comments = this.comments;
		if(hasFile()) {
			req.upload = this.upload;
		}
		if(sameConsultant) {
			req.consultant = this.consultant;
		}
		return req;
	}
	
				
}

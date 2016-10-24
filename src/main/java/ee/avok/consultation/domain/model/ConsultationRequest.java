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

@Entity
@Data
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
	
	public String generateUploadUrl() {
		return "/uploads/" + Integer.toString(upload.getId()) + "/" + upload.getFilename();
	}
	
	
	public boolean hasFile() {
		return this.upload != null;
	}
/*	public ConsultationRequest() {
	}

	



	public String generateUploadName() {
		return upload.getFilename();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getProgramme() {
		return programme;
	}

	public void setProgramme(String programme) {
		this.programme = programme;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public ConsultationStatus getStatus() {
		return status;
	}

	public void setStatus(ConsultationStatus status) {
		this.status = status;
	}

	public Upload getUpload() {
		return upload;
	}

	public void setUpload(Upload upload) {
		this.upload = upload;
	}

	public String getDepartment() {
		return department;
	}

	public String getDegree() {
		return degree;
	}

	public String setDegree(String degree) {
		return this.degree = degree;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public Account getConsultant() {
		return consultant;
	}

	public void setConsultant(Account consultant) {
		this.consultant = consultant;
	}

	public Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	public Date getAcceptedDate() {
		return acceptedDate;
	}

	public void setAcceptedDate(Date acceptedDate) {
		this.acceptedDate = acceptedDate;
	}

	public Date getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(Date scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	public Date getMeetingDate() {
		return meetingDate;
	}

	public void setMeetingDate(Date meetingDate) {
		this.meetingDate = meetingDate;
	}

	public Date getCompletedDate() {
		return completedDate;
	}

	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}

	public String getMeetingPlace() {
		return meetingPlace;
	}

	public void setMeetingPlace(String meetingPlace) {
		this.meetingPlace = meetingPlace;
	}

	public String getTextType() {
		return textType;
	}

	public void setTextType(String textType) {
		this.textType = textType;
	}
*/
}

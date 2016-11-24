package ee.avok.consultation.service;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.avok.consultation.auth.domain.model.Account;
import ee.avok.consultation.auth.domain.model.UnauthorizedException;
import ee.avok.consultation.domain.model.ConsultantFeedback;
import ee.avok.consultation.domain.model.ConsultationRequest;
import ee.avok.consultation.domain.model.ConsultationStatus;
import ee.avok.consultation.domain.model.NewConsultationOption;
import ee.avok.consultation.domain.model.StudentFeedback;
import ee.avok.consultation.domain.repository.ConsultantFeedbackRepository;
import ee.avok.consultation.domain.repository.ConsultationRequestRepository;
import ee.avok.consultation.domain.repository.StudentFeedbackRepository;

@Service
public class FeedbackService {

	@Autowired
	StudentFeedbackRepository studentFeedbackRepo;
	@Autowired
	ConsultantFeedbackRepository consultantFeedbackRepo;
	@Autowired
	ConsultationRequestRepository consultationRepo;
	@Autowired
	EmailService mailServ;

	public void verifyStudentFeedbackUID(Integer id, String uid) throws UnauthorizedException {
		ConsultationRequest req = consultationRepo.findOne(id);
		if(!req.getStudentFeedback().getUid().equals(uid)) {
			throw new UnauthorizedException("Incorrect feedback UID.");
		}
	}

	public void submitStudentFeedback(int id, StudentFeedback feedbackForm) {
		ConsultationRequest req = consultationRepo.findOne(id);
		StudentFeedback feedback = req.getStudentFeedback();
		feedback.setFirstConsultation(feedbackForm.getFirstConsultation());
		feedback.setUseful(feedbackForm.getUseful());
		feedback.setProvidedSupport(feedbackForm.getProvidedSupport());
		feedback.setComingBack(feedbackForm.getComingBack());
		feedback.setComments(feedbackForm.getComments());
		feedback.setUid(null);
		studentFeedbackRepo.save(feedback);
	}
	
	public void verifyConsultantFeedbackUser(Account user, Integer id) throws UnauthorizedException {
		ConsultationRequest req = consultationRepo.findOne(id);
		if(req.getConsultantFeedback() != null) {
			throw new UnauthorizedException("Already has feedback.");
		}
		if(!req.getConsultant().equals(user)) {
			throw new UnauthorizedException("Incorrect user.");
		}
	}
	
	public void submitConsultantFeedback(int id, ConsultantFeedback feedbackForm) {
		ConsultationRequest req = consultationRepo.findOne(id);
		feedbackForm = consultantFeedbackRepo.save(feedbackForm);
		req.setConsultantFeedback(feedbackForm);
		req.setStatus(ConsultationStatus.COMPLETED);
		req.setCompletedDate(Date.from(Instant.now()));
		consultationRepo.save(req);
		
		if(feedbackForm.getSuggestedNewConsultation().equals(NewConsultationOption.NO)) {
			mailServ.sendFeedbackRequest(req);
		}
	}
	
	public void addStudentFeedback(ConsultationRequest req) {
		StudentFeedback feedback = new StudentFeedback();
		feedback.setUid(UUID.randomUUID().toString());
		feedback = studentFeedbackRepo.save(feedback);
		req.setStudentFeedback(feedback);
	}

}
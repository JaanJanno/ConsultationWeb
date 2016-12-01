package ee.avok.consultation.service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ee.avok.consultation.auth.domain.model.Account;
import ee.avok.consultation.domain.model.ConsultantFeedback;
import ee.avok.consultation.domain.model.ConsultationRequest;
import ee.avok.consultation.domain.model.ConsultationStatus;
import ee.avok.consultation.domain.model.StudentFeedback;
import ee.avok.consultation.domain.model.Upload;
import ee.avok.consultation.domain.repository.ConsultationRequestRepository;
import ee.avok.consultation.domain.repository.UploadRepository;
import ee.avok.consultation.dto.CompletedDTO;
import ee.avok.consultation.dto.SetTimeDTO;

@Service
public class ConsultationServiceImpl implements ConsultationService {

	@Autowired
	ConsultationRequestRepository conReqRepo;

	@Autowired
	UploadRepository upRepo;

	@Autowired
	FeedbackService feedServ;
	
	@Autowired
	EmailService mailServ;

	@Override
	public void createConsultation(ConsultationRequest conReq) {
		feedServ.addStudentFeedback(conReq);
		conReq.setStatus(ConsultationStatus.RECEIVED);
		if (conReq.getUpload() != null)
			upRepo.save(conReq.getUpload());
		conReqRepo.save(conReq);

	}

	@Override
	public List<ConsultationRequest> findByStatus(ConsultationStatus status) {
		if (status == null)
			return findAll();
		else
			return conReqRepo.findByStatus(status);
	}

	@Override
	public List<ConsultationRequest> findByStatus(String status) {
		ConsultationStatus st = ConsultationStatus.valueOf(status.toUpperCase());
		return findByStatus(st);
	}

	@Override
	public List<ConsultationRequest> findByStatusAndConsultant(ConsultationStatus status, Account consultant) {
		if (status == ConsultationStatus.RECEIVED)
			return conReqRepo.findByStatus(status);
		else
			return conReqRepo.findByStatusAndConsultant(status, consultant);
	}

	@Override
	public List<ConsultationRequest> findByStatusAndConsultant(String status, Account consultant) {
		ConsultationStatus st = ConsultationStatus.valueOf(status.toUpperCase());
		return findByStatusAndConsultant(st, consultant);
	}

	@Override
	public List<ConsultationRequest> findAll() {
		return (List<ConsultationRequest>) conReqRepo.findAll();
	}

	@Override
	public ConsultationRequest findOne(int id) {
		return conReqRepo.findOne(id);
	}

	@Override
	public void createConsultation(ConsultationRequest conReq, MultipartFile file) {
		feedServ.addStudentFeedback(conReq);
		conReq.setStatus(ConsultationStatus.RECEIVED);
		conReq.setReceivedDate(new Date());

		try {
			if (file.getBytes().length > 0) {
				Upload upload = new Upload();
				upload.setFilename(file.getOriginalFilename());
				upload.setUpload(file.getBytes());
				conReq.setUpload(upload);
				upRepo.save(upload);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		conReqRepo.save(conReq);

	}

	@Override
	public void setAccepted(int id, Account consultant) {
		ConsultationRequest conReq = findOne(id);
		conReq.setStatus(ConsultationStatus.ACCEPTED);
		conReq.setAcceptedDate(new Date());
		conReq.setConsultant(consultant);
		conReqRepo.save(conReq);

	}

	@Override
	public List<CompletedDTO> findCompleted() {
		List<ConsultationRequest> reqs = findByStatus(ConsultationStatus.COMPLETED);
		return createCompletedDTO(reqs);
	}

	@Override
	public List<CompletedDTO> findCompleted(Account consultant) {
		List<ConsultationRequest> reqs = findByStatusAndConsultant(ConsultationStatus.COMPLETED, consultant);
		return createCompletedDTO(reqs);
	}

	private List<CompletedDTO> createCompletedDTO(List<ConsultationRequest> reqs) {
		List<CompletedDTO> dtos = new ArrayList<>();
		reqs.forEach(r -> dtos.add(new CompletedDTO(r.getId(), r.getName(), r.getConsultant().getName(),
				r.hasConsultantFeedback(), r.hasStudentFeedback())));
		return dtos;
	}

	@Override
	public StudentFeedback getStudentFeedbackFor(int id) {
		return conReqRepo.findOne(id).getStudentFeedback();
	}

	@Override
	public ConsultantFeedback getConsultantFeedbackFor(int id) {
		return conReqRepo.findOne(id).getConsultantFeedback();
	}

	@Override
	public void setConsultationDate(int id, SetTimeDTO setTime) throws ParseException {
		ConsultationRequest r = conReqRepo.findOne(id);
		DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.ENGLISH);
		Date date = format.parse(setTime.getTime());

		r.setScheduledDate(Date.from(Instant.now()));
		r.setMeetingDate(date);
		r.setMeetingPlace(setTime.getPlace());
		r.setStatus(ConsultationStatus.SCHEDULED);
		conReqRepo.save(r);
		
		mailServ.sendReminder(r);
	}

	@Override
	public void reconsultationWithSame(ConsultationRequest req) {
		ConsultationRequest newReq = req.reConsultation(true);
		newReq.setPrevious(req);
		newReq.setStatus(ConsultationStatus.ACCEPTED);
		feedServ.addStudentFeedback(newReq);
		newReq.setReceivedDate(new Date());
		newReq.setAcceptedDate(new Date());
		newReq = conReqRepo.save(newReq);
		req.setNext(newReq);
		conReqRepo.save(req);
	}

	@Override
	public void reconsultationWithNew(ConsultationRequest req) {
		ConsultationRequest newReq = req.reConsultation(false);
		newReq.setPrevious(req);
		newReq.setStatus(ConsultationStatus.RECEIVED);
		feedServ.addStudentFeedback(newReq);
		newReq.setReceivedDate(new Date());
		newReq = conReqRepo.save(newReq);
		req.setNext(newReq);
		conReqRepo.save(req);
	}

}

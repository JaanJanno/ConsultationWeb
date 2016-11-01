package ee.avok.consultation.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ee.avok.consultation.auth.domain.model.Account;
import ee.avok.consultation.domain.model.ConsultationRequest;
import ee.avok.consultation.domain.model.ConsultationStatus;
import ee.avok.consultation.domain.model.StudentFeedback;
import ee.avok.consultation.domain.model.Upload;
import ee.avok.consultation.domain.repository.ConsultationRequestRepository;
import ee.avok.consultation.domain.repository.StudentFeedbackRepository;
import ee.avok.consultation.domain.repository.UploadRepository;
import ee.avok.consultation.dto.CompletedDTO;

@Service
public class ConsultationServiceImpl implements ConsultationService {

	@Autowired
	ConsultationRequestRepository conReqRepo;

	@Autowired
	UploadRepository upRepo;
	
	@Autowired
	StudentFeedbackRepository studentFeedbackRepo;

	@Override
	public void createConsultation(ConsultationRequest conReq) {
		addStudentFeedbackObject(conReq);
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
		addStudentFeedbackObject(conReq);
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
		List<ConsultationRequest> reqs = findByStatusAndConsultant(ConsultationStatus.COMPLETED,consultant);
		return createCompletedDTO(reqs);
	}
	
	private List<CompletedDTO> createCompletedDTO(List<ConsultationRequest> reqs) {
		List<CompletedDTO> dtos = new ArrayList<>();
		// TODO check if feedback exists
		reqs.forEach(
				r -> dtos.add(new CompletedDTO(r.getId(), r.getName(), r.getConsultant().getName(), false, false)));
		return dtos;
	}

	private void addStudentFeedbackObject(ConsultationRequest req) {
		StudentFeedback feedback = new StudentFeedback();
		feedback.setUid(UUID.randomUUID().toString());
		feedback = studentFeedbackRepo.save(feedback);
		req.setStudentFeedback(feedback);
	}

	@Override
	public StudentFeedback getStudentFeedbackFor(int id) {
		return conReqRepo.findOne(id).getStudentFeedback();
	}

}

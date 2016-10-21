package ee.avok.consultation.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import ee.avok.consultation.auth.domain.model.Account;
import ee.avok.consultation.domain.model.ConsultationRequest;
import ee.avok.consultation.domain.model.ConsultationStatus;

public interface ConsultationService {

	void createConsultationREST(ConsultationRequest conReq);

	void createConsultation(ConsultationRequest conReq, MultipartFile file);

	List<ConsultationRequest> findByStatus(ConsultationStatus status);

	List<ConsultationRequest> findByStatusAndConsultant(ConsultationStatus status, Account consultant);

	List<ConsultationRequest> findAll();

	ConsultationRequest findOne(int id);

	void setAccepted(int id, Account consultant);

}
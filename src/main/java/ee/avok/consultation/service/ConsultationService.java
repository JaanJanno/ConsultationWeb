package ee.avok.consultation.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import ee.avok.consultation.auth.domain.model.Account;
import ee.avok.consultation.domain.model.ConsultationRequest;
import ee.avok.consultation.domain.model.ConsultationStatus;
import ee.avok.consultation.dto.CompletedDTO;

public interface ConsultationService {

	void createConsultation(ConsultationRequest conReq);

	void createConsultation(ConsultationRequest conReq, MultipartFile file);

	List<ConsultationRequest> findByStatus(ConsultationStatus status);

	List<ConsultationRequest> findByStatusAndConsultant(ConsultationStatus status, Account consultant);

	/**
	 * Generates a list of {@link CompletedDTO} to show in the admin completed
	 * requests view.
	 */
	List<CompletedDTO> findCompleted();

	/**
	 * Generates a list of {@link CompletedDTO} to show in the consultant
	 * completed requests view.
	 * 
	 * @param consultant
	 *            Consultant to find by.
	 */
	List<CompletedDTO> findCompleted(Account consultant);

	List<ConsultationRequest> findAll();

	ConsultationRequest findOne(int id);

	void setAccepted(int id, Account consultant);

}
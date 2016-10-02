package ee.avok.consultation.service;

import java.util.List;

import ee.avok.consultation.domain.model.ConsultationRequest;
import ee.avok.consultation.domain.model.ConsultationStatus;

public interface ConsultationService {

	void createConsultation(ConsultationRequest conReq);

	List<ConsultationRequest> findByStatus(ConsultationStatus status);

	List<ConsultationRequest> findAll();

	ConsultationRequest findOne(int id);

	void updateStatus(int id, ConsultationStatus status);
}

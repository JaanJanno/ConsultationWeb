package ee.avok.consultation.service;

import ee.avok.consultation.domain.model.ConsultationRequest;

public interface ConsultationService {

	void createConsultation(ConsultationRequest conReq);
}

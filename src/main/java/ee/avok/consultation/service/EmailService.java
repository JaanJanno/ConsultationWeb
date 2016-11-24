package ee.avok.consultation.service;

import ee.avok.consultation.domain.model.ConsultationRequest;

public interface EmailService {
	
	void sendReminder(ConsultationRequest req);

	void sendFeedbackRequest(ConsultationRequest req);

}
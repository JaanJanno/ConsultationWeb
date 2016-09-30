package ee.avok.consultation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.avok.consultation.domain.model.ConsultationRequest;
import ee.avok.consultation.domain.repository.ConsultationRequestRepository;

@Service
public class ConsultationServiceImpl implements ConsultationService {

	@Autowired
	ConsultationRequestRepository conReqRepo;

	@Override
	public void createConsultation(ConsultationRequest conReq) {
		conReqRepo.save(conReq);

	}

}

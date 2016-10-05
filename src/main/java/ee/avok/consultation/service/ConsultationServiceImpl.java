package ee.avok.consultation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.avok.consultation.domain.model.ConsultationRequest;
import ee.avok.consultation.domain.model.ConsultationStatus;
import ee.avok.consultation.domain.repository.ConsultationRequestRepository;
import ee.avok.consultation.domain.repository.UploadRepository;

@Service
public class ConsultationServiceImpl implements ConsultationService {

	@Autowired
	ConsultationRequestRepository conReqRepo;
	
	@Autowired
	UploadRepository upRepo;

	@Override
	public void createConsultation(ConsultationRequest conReq) {
		conReq.setStatus(ConsultationStatus.RECEIVED);
		if(conReq.getUpload() != null)
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
	public List<ConsultationRequest> findAll() {
		return (List<ConsultationRequest>) conReqRepo.findAll();
	}

	@Override
	public ConsultationRequest findOne(int id) {
		return conReqRepo.findOne(id);
	}

	@Override
	public void updateStatus(int id, ConsultationStatus status) {
		ConsultationRequest conReq = findOne(id);
		conReq.setStatus(status);
		conReqRepo.save(conReq);

	}

}
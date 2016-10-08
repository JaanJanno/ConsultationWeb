package ee.avok.consultation.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ee.avok.consultation.domain.model.ConsultationRequest;
import ee.avok.consultation.domain.model.ConsultationStatus;
import ee.avok.consultation.domain.model.Upload;
import ee.avok.consultation.domain.repository.ConsultationRequestRepository;
import ee.avok.consultation.domain.repository.UploadRepository;

@Service
public class ConsultationServiceImpl implements ConsultationService {

	@Autowired
	ConsultationRequestRepository conReqRepo;
	
	@Autowired
	UploadRepository upRepo;

	@Override
	public void createConsultationREST(ConsultationRequest conReq) {
		
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

	@Override
	public void createConsultation(ConsultationRequest conReq, MultipartFile file) {
		
		conReq.setStatus(ConsultationStatus.RECEIVED);
			
		Upload upload = new Upload();
		try {		
			upload.setFilename(file.getOriginalFilename());
			upload.setUpload(file.getBytes());
			conReq.setUpload(upload);
			upRepo.save(upload);			
		} catch (IOException e) {
			e.printStackTrace();
		}	
		conReqRepo.save(conReq);
		
	}

}

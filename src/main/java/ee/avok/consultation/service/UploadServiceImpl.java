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
public class UploadServiceImpl implements UploadService {

	@Autowired
	UploadRepository upRepo;

	@Override
	public Upload retrieveUpload(Integer id) {
		return upRepo.findById(id);
	}

}

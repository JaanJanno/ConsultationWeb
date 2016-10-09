package ee.avok.consultation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.avok.consultation.domain.model.Upload;
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

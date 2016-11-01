package ee.avok.consultation.domain.repository;

import org.springframework.data.repository.CrudRepository;

import ee.avok.consultation.domain.model.Upload;

public interface UploadRepository extends CrudRepository<Upload, Integer> {
	
	Upload findById(int id);

}

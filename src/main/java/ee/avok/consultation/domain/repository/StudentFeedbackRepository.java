package ee.avok.consultation.domain.repository;

import org.springframework.data.repository.CrudRepository;

import ee.avok.consultation.domain.model.StudentFeedback;

public interface StudentFeedbackRepository extends CrudRepository<StudentFeedback, Integer> {
	
	StudentFeedback findById(int id);

}

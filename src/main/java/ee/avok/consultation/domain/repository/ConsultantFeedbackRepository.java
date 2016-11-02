package ee.avok.consultation.domain.repository;

import org.springframework.data.repository.CrudRepository;

import ee.avok.consultation.domain.model.ConsultantFeedback;

public interface ConsultantFeedbackRepository extends CrudRepository<ConsultantFeedback, Integer> {
	
	ConsultantFeedback findById(int id);

}
package ee.avok.consultation.domain.repository;

import org.springframework.data.repository.CrudRepository;

import ee.avok.consultation.domain.model.ConsultationRequest;

public interface ConsultationRequestRepository extends CrudRepository<ConsultationRequest, Integer> {

}

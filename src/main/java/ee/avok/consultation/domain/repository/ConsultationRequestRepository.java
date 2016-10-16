package ee.avok.consultation.domain.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ee.avok.consultation.auth.domain.model.Account;
import ee.avok.consultation.domain.model.ConsultationRequest;
import ee.avok.consultation.domain.model.ConsultationStatus;

public interface ConsultationRequestRepository extends CrudRepository<ConsultationRequest, Integer> {

	List<ConsultationRequest> findByStatus(ConsultationStatus status);
	
	List<ConsultationRequest> findByStatusAndConsultant(ConsultationStatus status, Account consultant);
}

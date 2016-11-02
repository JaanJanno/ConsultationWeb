package ee.avok.consultation.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import ee.avok.consultation.auth.domain.model.Account;
import ee.avok.consultation.domain.model.ConsultationRequest;
import ee.avok.consultation.domain.model.ConsultationStatus;

public interface ConsultationRequestRepository extends CrudRepository<ConsultationRequest, Integer> {

	List<ConsultationRequest> findByStatus(ConsultationStatus status);

	List<ConsultationRequest> findByStatusAndConsultant(ConsultationStatus status, Account consultant);

	@Query("select count(*) from ConsultationRequest where receivedDate != null")
	int countAllReceived();

	@Query("select count(*) from ConsultationRequest where acceptedDate != null")
	int countAllAccepted();

	@Query("select count(*) from ConsultationRequest where scheduledDate != null")
	int countAllScheduled();

	@Query("select count(*) from ConsultationRequest where completedDate != null")
	int countAllCompleted();

	List<ConsultationRequest> findByMeetingDateNotNull();

	List<ConsultationRequest> findByConsultantIdAndMeetingDateNotNull(int consultantId);

}

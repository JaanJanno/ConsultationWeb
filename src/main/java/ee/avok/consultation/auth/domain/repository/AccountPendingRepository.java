package ee.avok.consultation.auth.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ee.avok.consultation.auth.domain.model.AccountPending;

public interface AccountPendingRepository extends JpaRepository<AccountPending, Integer> {
	
}

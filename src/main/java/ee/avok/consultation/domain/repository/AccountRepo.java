package ee.avok.consultation.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ee.avok.consultation.auth.domain.model.Account;
import ee.avok.consultation.auth.domain.model.Role;

@Repository
public interface AccountRepo extends JpaRepository<Account, Integer>{
	
	public List<Account> findByRole(Role role);


}

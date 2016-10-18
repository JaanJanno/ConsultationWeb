package ee.avok.consultation.auth.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import ee.avok.consultation.auth.domain.model.Account;
import ee.avok.consultation.auth.domain.model.Role;

public interface AccountRepository extends JpaRepository<Account, Integer> {
	public List<Account> findByRole(Role role);

	Account findById(int id);
	
	Account findByUsername(String name);

}

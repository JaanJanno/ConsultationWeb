package ee.avok.consultation.auth.domain.repository;

import org.springframework.data.repository.CrudRepository;

import ee.avok.consultation.auth.domain.model.Account;

public interface AccountRepository extends CrudRepository<Account, Integer> {

	Account findById(int id);
	
	Account findByUsername(String name);

}

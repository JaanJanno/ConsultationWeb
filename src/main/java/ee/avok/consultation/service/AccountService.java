package ee.avok.consultation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.avok.consultation.auth.domain.model.Account;
import ee.avok.consultation.auth.domain.model.Role;
import ee.avok.consultation.auth.domain.repository.AccountRepository;

@Service
public class AccountService {

	@Autowired
	AccountRepository accountRepo;

	public List<Account> getAllConsultants() {
		return accountRepo.findByRole(Role.CONSULTANT);
	}

	public void deactivate(int id) {
		Account user = accountRepo.findById(id);
		user.setActive(false);
		accountRepo.save(user);
	}

	public void activate(int id) {
		Account user = accountRepo.findById(id);
		user.setActive(true);
		accountRepo.save(user);
	}

}
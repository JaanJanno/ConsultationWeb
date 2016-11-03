package ee.avok.consultation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.avok.consultation.auth.domain.model.Account;
import ee.avok.consultation.auth.domain.model.InvalidPasswordException;
import ee.avok.consultation.auth.domain.model.Role;
import ee.avok.consultation.auth.domain.repository.AccountRepository;
import ee.avok.consultation.auth.service.AuthService;

@Service
public class AccountService {

	@Autowired
	AccountRepository accountRepo;
	@Autowired
	AuthService authService;

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

	public void setUserEmail(Account user, String email) {
		user.setEmail(email);
		accountRepo.save(user);
	}

	public void setUserPassword(Account user, String oldPassword, String newPassword) throws InvalidPasswordException {
		if(!authService.checkPassword(user.getId(), oldPassword)) {
			throw new InvalidPasswordException("Old password does not match!");
		}
		if(newPassword.equals("")) {
			throw new InvalidPasswordException("New password cannot be empty!");
		}
		
		user.setPassword(newPassword);
		accountRepo.save(user);
	}

}
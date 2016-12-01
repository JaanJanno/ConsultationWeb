package ee.avok.consultation.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.avok.consultation.auth.domain.model.Account;
import ee.avok.consultation.auth.domain.model.AccountPending;
import ee.avok.consultation.auth.domain.model.InvalidPasswordException;
import ee.avok.consultation.auth.domain.model.Role;
import ee.avok.consultation.auth.domain.model.UnauthorizedException;
import ee.avok.consultation.auth.domain.repository.AccountPendingRepository;
import ee.avok.consultation.auth.domain.repository.AccountRepository;
import ee.avok.consultation.auth.service.AuthService;

@Service
public class AccountService {

	@Autowired
	AccountRepository accountRepo;
	@Autowired
	AccountPendingRepository accountpendingRepo;
	@Autowired
	AuthService authService;
	@Autowired
	EmailService mailService;

	public List<Account> getAllConsultants() {
		return accountRepo.findByRole(Role.CONSULTANT);
	}
	
	public List<Account> findAll() {
		return accountRepo.findAll();
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

	public void createNewAccountPending(AccountPending account) {
		account.setUid(UUID.randomUUID().toString());
		account = accountpendingRepo.save(account);
		mailService.sendAccountCreation(account);
	}

	public void validateUid(int id, String uid) throws UnauthorizedException {
		AccountPending newAcc = accountpendingRepo.findOne(id);
		if(!newAcc.getUid().equals(uid)) {
			throw new UnauthorizedException("Wrong UID for account creation.");
		}
	}

	public void createAccount(int id, Account account) {
		AccountPending newAcc = accountpendingRepo.findOne(id);
		account.setEmail(newAcc.getEmail());
		account.setActive(true);
		account.setRole(Role.CONSULTANT);
		account.setId(0);
		accountRepo.save(account);
		accountpendingRepo.delete(id);
	}

	public void setAdmin(int id) {
		Account user = accountRepo.findById(id);
		user.setRole(Role.ADMINISTRATOR);
		accountRepo.save(user);
	}

}
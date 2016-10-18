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
	AccountRepository AccountRepo;
	
	public List<Account> getAllConsultants(){
		return AccountRepo.findByRole(Role.CONSULTANT);
	}

}

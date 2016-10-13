package ee.avok.consultation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import ee.avok.consultation.auth.domain.model.Account;
import ee.avok.consultation.auth.domain.model.Role;
import ee.avok.consultation.auth.domain.model.UnauthorizedException;
import ee.avok.consultation.auth.service.AuthService;
import ee.avok.consultation.dto.AccountDTO;

@Controller
public class AccountController {
	
	@Autowired
	AuthService authServ;
	
	@RequestMapping(path="/accounts/managment")
	public String ManagingAccounts(@CookieValue(value = "session", defaultValue = "none") String session,
			Model model) throws UnauthorizedException{
		Account user = authServ.authenticateRequestForRole(session, Role.CONSULTANT);
		model.addAttribute("user", new AccountDTO(user.getId()));
		return "manage_account"; 
	}
	
	@ExceptionHandler(UnauthorizedException.class)
	public String handleNotFound(Exception exc) {
		return "redirect:" + "/login";
	}
	
}
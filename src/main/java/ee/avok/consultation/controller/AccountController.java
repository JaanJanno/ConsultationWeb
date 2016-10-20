package ee.avok.consultation.controller;

import java.util.List;

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
import ee.avok.consultation.domain.model.ConsultationRequest;
import ee.avok.consultation.domain.model.ConsultationStatus;
import ee.avok.consultation.dto.AccountDTO;
import ee.avok.consultation.service.AccountService;

@Controller
public class AccountController {

	@Autowired
	AuthService authServ;

	@Autowired
	AccountService accountService;

	@RequestMapping(path = "/account/edit")
	public String ManagingAccounts(@CookieValue(value = "session", defaultValue = "none") String session, Model model)
			throws UnauthorizedException {
		Account user = authServ.authenticateRequestForRole(session, Role.CONSULTANT);
		model.addAttribute("username", user.getUsername());
		model.addAttribute("name", user.getName());
		model.addAttribute("user", new AccountDTO(user.getId()));
		return "admin/manage_account";
		
	}

	@RequestMapping(path = "/accounts/manage")
	public String editAccounts(Model model) {
		model.addAttribute("accounts", accountService.getAllConsultants());
		return "admin/deactive_accounts";
	}

	@ExceptionHandler(UnauthorizedException.class)
	public String handleNotFound(Exception exc) {
		return "redirect:" + "/login";
	}
}

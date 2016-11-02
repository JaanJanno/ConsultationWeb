package ee.avok.consultation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ee.avok.consultation.auth.domain.model.Account;
import ee.avok.consultation.auth.domain.model.Role;
import ee.avok.consultation.auth.domain.model.UnauthorizedException;
import ee.avok.consultation.auth.service.AuthService;
import ee.avok.consultation.dto.AccountDTO;
import ee.avok.consultation.service.AccountService;

@Controller
public class AccountController {

	@Autowired
	AuthService authServ;

	@Autowired
	AccountService accountService;

	@RequestMapping(value = "/account/edit", method = RequestMethod.GET)
	public String editAccount(@CookieValue(value = "session", defaultValue = "none") String session, Model model)
			throws UnauthorizedException {
		Account user = authServ.authenticateAndAddToModel(model, session, Role.CONSULTANT);

		model.addAttribute("user", new AccountDTO(user.getId()));
		return "general/manage_account";
		
	}

	@RequestMapping(value = "/accounts/manage", method = RequestMethod.GET)
	public String manageAccounts(Model model, @CookieValue(value = "session", defaultValue = "none") String session) throws UnauthorizedException {
		authServ.authenticateAndAddToModel(model, session, Role.ADMINISTRATOR);
		
		model.addAttribute("accounts", accountService.getAllConsultants());
		return "admin/deactive_accounts";
	}

	@ExceptionHandler(UnauthorizedException.class)
	public String handleNotFound(Exception exc) {
		return "redirect:" + "/login";
	}
}

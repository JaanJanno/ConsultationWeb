package ee.avok.consultation.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ee.avok.consultation.auth.domain.model.Account;
import ee.avok.consultation.auth.domain.model.AccountPending;
import ee.avok.consultation.auth.domain.model.InvalidPasswordException;
import ee.avok.consultation.auth.domain.model.Role;
import ee.avok.consultation.auth.domain.model.UnauthorizedException;
import ee.avok.consultation.auth.service.AuthService;
import ee.avok.consultation.dto.AccountDTO;
import ee.avok.consultation.service.AccountService;

@Controller
public class AccountController {
	
	private static Logger LOG = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	AuthService authServ;

	@Autowired
	AccountService accountService;
	
	@RequestMapping(value = "/account/create/{id}/{uid}", method = RequestMethod.GET)
	public String createAccountView(@PathVariable("uid") String uid, @PathVariable int id, Model model)
			throws UnauthorizedException {
		accountService.validateUid(id, uid);
		model.addAttribute("accountform", new Account());
		return "admin/create_account";
	}
	
	@RequestMapping(value = "/account/create/{id}/{uid}", method = RequestMethod.POST)
	public String createAccount(@ModelAttribute Account account, @PathVariable("uid") String uid, @PathVariable int id, Model model)
			throws UnauthorizedException {
		accountService.validateUid(id, uid);
		accountService.createAccount(id, account);
		return "redirect:" + "/login";
	}
	
	@RequestMapping(value = "/accounts/createpending", method = RequestMethod.POST)
	public String newPendingAccount(@ModelAttribute AccountPending account, @CookieValue(value = "session", defaultValue = "none") String session)
			throws UnauthorizedException {
		authServ.authenticateRequestForRole(session, Role.ADMINISTRATOR);
		accountService.createNewAccountPending(account);
		return "redirect:" + "/accounts/manage";
	}

	@RequestMapping(value = "/account/edit", method = RequestMethod.GET)
	public String editAccount(@CookieValue(value = "session", defaultValue = "none") String session, Model model)
			throws UnauthorizedException {
		Account user = authServ.authenticateAndAddToModel(model, session, Role.CONSULTANT);
		model.addAttribute("user", new AccountDTO(user.getId()));
		return "general/manage_account";
	}
	
	@RequestMapping(value = "/account/edit/email", method = RequestMethod.POST)
	public String editEmail(@ModelAttribute AccountDTO editInfo, @CookieValue(value = "session", defaultValue = "none") String session, Model model)
			throws UnauthorizedException {
		Account user = authServ.authenticateRequestForRole(session, Role.CONSULTANT);
		accountService.setUserEmail(user, editInfo.getEmail());
		LOG.info("Setting new email: {}", editInfo.getEmail());
		return "redirect:" + "/account/edit";
	}
	
	@RequestMapping(value = "/account/edit/password", method = RequestMethod.POST)
	public String editPassword(@ModelAttribute AccountDTO editInfo, @CookieValue(value = "session", defaultValue = "none") String session, Model model)
			throws UnauthorizedException, InvalidPasswordException {
		Account user = authServ.authenticateRequestForRole(session, Role.CONSULTANT);
		accountService.setUserPassword(user, editInfo.getOldPassword(), editInfo.getNewPassword());
		LOG.info("Setting new password.");
		return "redirect:" + "/account/edit";
	}

	@RequestMapping(value = "/accounts/manage", method = RequestMethod.GET)
	public String manageAccounts(Model model, @CookieValue(value = "session", defaultValue = "none") String session)
			throws UnauthorizedException {
		authServ.authenticateAndAddToModel(model, session, Role.ADMINISTRATOR);
		model.addAttribute("accounts", accountService.getAllConsultants());
		return "admin/deactive_accounts";
	}

	@RequestMapping(value = "/accounts/{id}/deactivate", method = RequestMethod.POST)
	public String deactivateAccount(@PathVariable int id,
			@CookieValue(value = "session", defaultValue = "none") String session, Model model)
					throws UnauthorizedException {
		authServ.authenticateRequestForRole(session, Role.ADMINISTRATOR);
		accountService.deactivate(id);
		return "redirect:" + "/accounts/manage";
	}
	
	@RequestMapping(value = "/accounts/{id}/activate", method = RequestMethod.POST)
	public String activateAccount(@PathVariable int id,
			@CookieValue(value = "session", defaultValue = "none") String session, Model model)
					throws UnauthorizedException {
		authServ.authenticateRequestForRole(session, Role.ADMINISTRATOR);
		accountService.activate(id);
		return "redirect:" + "/accounts/manage";
	}
	
	@RequestMapping(value = "/accounts/{id}/setadmin", method = RequestMethod.POST)
	public String setAdminAccount(@PathVariable int id,
			@CookieValue(value = "session", defaultValue = "none") String session, Model model)
					throws UnauthorizedException {
		authServ.authenticateRequestForRole(session, Role.ADMINISTRATOR);
		accountService.setAdmin(id);
		return "redirect:" + "/accounts/manage";
	}

	@ExceptionHandler(UnauthorizedException.class)
	public String handleNotFound(Exception exc) {
		return "redirect:" + "/login";
	}
	
	@ExceptionHandler(InvalidPasswordException.class)
	public String handleWrongOldPassword(Exception exc) {
		return "redirect:" + "/account/edit";
	}
}

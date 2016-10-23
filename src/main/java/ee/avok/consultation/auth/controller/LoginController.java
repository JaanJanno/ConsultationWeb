package ee.avok.consultation.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ee.avok.consultation.auth.domain.model.Account;
import ee.avok.consultation.auth.domain.model.UnauthorizedException;
import ee.avok.consultation.auth.service.AuthService;
import ee.avok.consultation.controller.RequestController;

@Controller
public class LoginController {

	private static Logger LOG = LoggerFactory.getLogger(RequestController.class);

	@Autowired
	AuthService authServ;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String getLoginForm() {
		return "general/login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String postLoginForm(@ModelAttribute Account loginRequest, HttpServletResponse response) throws UnauthorizedException {
		try {
			Account account = authServ.authenticateUser(loginRequest.getUsername(), loginRequest.getPassword(), response);
			LOG.info("User logged in: " + loginRequest.getUsername() + ", " + account.getName());
			return "redirect:" + "/requests";
		} catch (UnauthorizedException e) {
			LOG.info("User login failed: " + loginRequest.getUsername());
			return "redirect:" + "/login";
		}
	}
	
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public String postLogout(@CookieValue(value = "session", defaultValue = "none") String session) {
		authServ.endSession(session);
		return "redirect:" + "/login";
	}
	
	
	
	
	

	@ExceptionHandler(UnauthorizedException.class)
	public String handleNotFound(Exception exc) {
		return "redirect:" + "/login";
	}

}
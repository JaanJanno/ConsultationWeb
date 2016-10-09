package ee.avok.consultation.controller;

import java.util.List;

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
import ee.avok.consultation.auth.domain.model.Role;
import ee.avok.consultation.auth.domain.model.UnauthorizedException;
import ee.avok.consultation.auth.service.AuthService;
import ee.avok.consultation.domain.model.ConsultationRequest;
import ee.avok.consultation.domain.model.ConsultationStatus;
import ee.avok.consultation.service.ConsultationService;

@Controller
public class MainController {
	
	private static Logger LOG = LoggerFactory.getLogger(RequestController.class);
	@Autowired
	ConsultationService conServ;
	
	@Autowired
	AuthService authServ;
	
	@RequestMapping("/")
	public String indexPage(Model model, @CookieValue(value = "session", defaultValue = "none") String session) throws UnauthorizedException {
		Account user = authServ.authenticateRequestForRole(session, Role.CONSULTANT);
		
		List<ConsultationRequest> conReqs = conServ.findByStatus(ConsultationStatus.RECEIVED);
		LOG.info("Request size:"+conReqs.size());
		model.addAttribute("consultations", conReqs);
		model.addAttribute("username", user.getUsername());
		model.addAttribute("name", user.getName());
		return "index";
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/requests/detail/{id}")
	public String ConsultationRequestDetail(@PathVariable int id, @ModelAttribute ConsultationRequest conReq,
			Model model, @CookieValue(value = "session", defaultValue = "none") String session) throws UnauthorizedException {
		Account user = authServ.authenticateRequestForRole(session, Role.CONSULTANT);
		
		LOG.info("Consultation request with id {} is sent to show the detail", id);
		model.addAttribute("consultation", conServ.findOne(id));
		model.addAttribute("username", user.getUsername());
		model.addAttribute("name", user.getName());
		return "detail";
	}

	@RequestMapping("/empty")
	public String emptyPage(Model model) {
		return "empty";
	}
	
	@ExceptionHandler(UnauthorizedException.class)
	public String handleNotFound(Exception exc) {
		return "redirect:" + "/login";
	}

}
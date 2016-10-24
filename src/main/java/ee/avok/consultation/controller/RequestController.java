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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import ee.avok.consultation.auth.domain.model.Account;
import ee.avok.consultation.auth.domain.model.Role;
import ee.avok.consultation.auth.domain.model.UnauthorizedException;
import ee.avok.consultation.auth.service.AuthService;
import ee.avok.consultation.domain.model.ConsultationRequest;
import ee.avok.consultation.domain.model.ConsultationStatus;
import ee.avok.consultation.dto.FeedBackDTO;
import ee.avok.consultation.dto.PostConsultationForm;
import ee.avok.consultation.service.ConsultationService;

@Controller
public class RequestController {

	private static Logger LOG = LoggerFactory.getLogger(RequestController.class);
	@Autowired
	ConsultationService conServ;
	@Autowired
	AuthService authServ;

	@RequestMapping(value = "/request", method = RequestMethod.GET)
	public String createConsultation(Model model) {
		model.addAttribute("consultation", new ConsultationRequest());
		return "general/request";
	}

	@RequestMapping(value = "/request", method = RequestMethod.POST)
	public String createConsulation(@ModelAttribute ConsultationRequest conReq,
			@RequestParam("manualfile") MultipartFile file, Model model) {

		LOG.info("Received file: " + file.getOriginalFilename());
		LOG.info("Saving Consultation " + conReq.getTextType());
		model.addAttribute("consultation", new ConsultationRequest());

		conServ.createConsultation(conReq, file);
		return "redirect:" + "/";

	}

	@RequestMapping(value = "/feedback", method = RequestMethod.GET)
	public String createFeedbackForm(Model model) {
		model.addAttribute("feedback", new FeedBackDTO());
		return "general/feedback";
	}

	@RequestMapping("/requests")
	public String indexPage(Model model, @CookieValue(value = "session", defaultValue = "none") String session)
			throws UnauthorizedException {
		Account user = authServ.authenticateRequestForRole(session, Role.CONSULTANT);

		List<ConsultationRequest> conReqs = conServ.findByStatus(ConsultationStatus.RECEIVED);
		LOG.info("Request size:" + conReqs.size());
		model.addAttribute("consultations", conReqs);
		model.addAttribute("username", user.getUsername());
		model.addAttribute("name", user.getName());
		return "shared-between-consultant-and-admin/requests";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/requests/{id}")
	public String setConsultationAccepted(@CookieValue(value = "session", defaultValue = "none") String session,
			@PathVariable int id) throws UnauthorizedException {
		Account user = authServ.authenticateRequestForRole(session, Role.CONSULTANT);
		LOG.info("Consultation request with id {}, set as Accepted, user {}", id, user.getUsername());
		conServ.setAccepted(id, user);
		return "redirect:" + "/requests/ACCEPTED";
	}

	@RequestMapping(value = "/requests/{status}", method = RequestMethod.GET)
	public String requestsWithStatus(Model model, @CookieValue(value = "session", defaultValue = "none") String session,
			@PathVariable ConsultationStatus status) throws UnauthorizedException {
		Account user = authServ.authenticateRequestForRole(session, Role.CONSULTANT);

		List<ConsultationRequest> conReqs = conServ.findByStatusAndConsultant(status, user);
		LOG.info("Requests size: {}, status {}", conReqs.size(), status);
		model.addAttribute("consultations", conReqs);
		model.addAttribute("username", user.getUsername());
		model.addAttribute("name", user.getName());
		return "shared-between-consultant-and-admin/requests";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/requests/detail/{id}")
	public String ConsultationRequestDetail(@PathVariable int id, @ModelAttribute ConsultationRequest conReq,
			Model model, @CookieValue(value = "session", defaultValue = "none") String session)
			throws UnauthorizedException {
		Account user = authServ.authenticateRequestForRole(session, Role.CONSULTANT);

		LOG.info("Consultation request with id {} is sent to show the detail", id);
		model.addAttribute("consultation", conServ.findOne(id));
		model.addAttribute("username", user.getUsername());
		model.addAttribute("name", user.getName());
		return "shared-between-consultant-and-admin/detail";
	}
	
	@RequestMapping(value="/report")
	public String createConsultationReportForm(Model model,
				@CookieValue(value = "session", defaultValue = "none") String session )
				throws UnauthorizedException{
						Account user = authServ.authenticateRequestForRole(session, Role.CONSULTANT);
						model.addAttribute("postConsultationReport", new PostConsultationForm());
						model.addAttribute("username", user.getUsername());
						model.addAttribute("name", user.getName());
						return "shared-between-consultant-and-admin/post_consultation_form";
	}

	@RequestMapping(value = "/requests/admin/completed")
	public String getCompletedRequestsList( @ModelAttribute ConsultationRequest conReq,
			Model model, @CookieValue(value = "session", defaultValue = "none") String session)
			throws UnauthorizedException {
		Account user = authServ.authenticateRequestForRole(session, Role.CONSULTANT);

		
		model.addAttribute("username", user.getUsername());
		model.addAttribute("name", user.getName());
		List<ConsultationRequest> completedRequests = conServ.findByStatus(ConsultationStatus.COMPLETED);
		model.addAttribute("completedRequestsList", completedRequests);
		return "admin/completed_requests";
	}
	
	
	
	@ExceptionHandler(UnauthorizedException.class)
	public String handleNotFound(Exception exc) {
		return "redirect:" + "/";
	}

}

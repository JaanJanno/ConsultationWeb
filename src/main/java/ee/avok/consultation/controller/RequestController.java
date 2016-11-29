package ee.avok.consultation.controller;

import java.text.ParseException;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ee.avok.consultation.auth.domain.model.Account;
import ee.avok.consultation.auth.domain.model.Role;
import ee.avok.consultation.auth.domain.model.UnauthorizedException;
import ee.avok.consultation.auth.service.AuthService;
import ee.avok.consultation.domain.model.ConsultationRequest;
import ee.avok.consultation.dto.CompletedDTO;
import ee.avok.consultation.dto.SetTimeDTO;
import ee.avok.consultation.service.ConsultationService;
import ee.avok.consultation.service.StatisticsService;

@Controller
public class RequestController {

	private static Logger LOG = LoggerFactory.getLogger(RequestController.class);
	@Autowired
	ConsultationService conServ;
	@Autowired
	AuthService authServ;
	@Autowired
	StatisticsService statServ;

	/*
	 * Request creation
	 */
	@RequestMapping(value = "/response", method = RequestMethod.GET)
	public String createAccount(Model model) throws UnauthorizedException {
		return "general/success_failure";
	}

	@RequestMapping(value = "/request", method = RequestMethod.GET)
	public String createConsultation(Model model) {
		model.addAttribute("consultation", new ConsultationRequest());
		return "general/request";
	}

	@RequestMapping(value = "/request", method = RequestMethod.POST)
	public String createConsulation(@ModelAttribute ConsultationRequest conReq,
			@RequestParam("manualfile") MultipartFile file, Model model, RedirectAttributes ra) {

		LOG.info("Received file: " + file.getOriginalFilename());
		LOG.info("Saving Consultation " + conReq.getTextType());
		model.addAttribute("consultation", new ConsultationRequest());

		conServ.createConsultation(conReq, file);

		// There should be other messages
		ra.addFlashAttribute("message", "consultation.success");
		ra.addFlashAttribute("type", "success");
		return "redirect:/";
	}

	/*
	 * Requests by status
	 */

	@RequestMapping(value = { "/requests", "/requests/received" }, method = RequestMethod.GET)
	public String requestsReceived(Model model, @CookieValue(value = "session", defaultValue = "none") String session)
			throws UnauthorizedException {
		authServ.authenticateAndAddToModel(model, session, Role.CONSULTANT);

		List<ConsultationRequest> conReqs = conServ.findByStatus("received");

		LOG.info("Requests size: {}, status {}", conReqs.size(), "received");
		model.addAttribute("consultations", conReqs);
		return "shared-between-consultant-and-admin/requests";
	}

	@RequestMapping(value = "/requests/accepted", method = RequestMethod.GET)
	public String requestsAccepted(Model model, @CookieValue(value = "session", defaultValue = "none") String session)
			throws UnauthorizedException {
		Account user = authServ.authenticateAndAddToModel(model, session, Role.CONSULTANT);

		List<ConsultationRequest> conReqs = conServ.findByStatusAndConsultant("accepted", user);
		LOG.info("Requests size: {}, status {}", conReqs.size(), "accepted");

		model.addAttribute("consultations", conReqs);
		return "shared-between-consultant-and-admin/accepted_requests";
	}

	@RequestMapping(value = "/requests/completed", method = RequestMethod.GET)
	public String requestsCompleted(Model model, @CookieValue(value = "session", defaultValue = "none") String session)
			throws UnauthorizedException {
		Account user = authServ.authenticateAndAddToModel(model, session, Role.CONSULTANT);

		List<CompletedDTO> conReqs = conServ.findCompleted(user);
		LOG.info("Requests size: {}, status {}", conReqs.size(), "completed");

		model.addAttribute("consultations", conReqs);
		// TODO probably move from admin to shared
		return "admin/completed_requests";
	}

	@RequestMapping(value = "/requests/scheduled", method = RequestMethod.GET)
	public String requestsScheduled(Model model, @CookieValue(value = "session", defaultValue = "none") String session)
			throws UnauthorizedException {
		Account user = authServ.authenticateAndAddToModel(model, session, Role.CONSULTANT);
		

		List<ConsultationRequest> conReqs = conServ.findByStatusAndConsultant("scheduled", user);
		LOG.info("Requests size: {}, status {}", conReqs.size(), "scheduled");

		model.addAttribute("consultations", conReqs);
		return "shared-between-consultant-and-admin/scheduled_requests";
	}
	/*
	 * Requests modification
	 */

	@RequestMapping(method = RequestMethod.POST, value = "/requests/{id}")
	public String setConsultationAccepted(@CookieValue(value = "session", defaultValue = "none") String session,
			@PathVariable int id) throws UnauthorizedException {
		Account user = authServ.authenticateRequestForRole(session, Role.CONSULTANT);
		LOG.info("Consultation request with id {}, set as Accepted, user {}", id, user.getUsername());
		conServ.setAccepted(id, user);
		return "redirect:" + "/requests/accepted";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/requests/detail/{id}")
	public String ConsultationRequestDetail(@PathVariable int id, @ModelAttribute ConsultationRequest conReq,
			Model model, @CookieValue(value = "session", defaultValue = "none") String session)
			throws UnauthorizedException {
		authServ.authenticateAndAddToModel(model, session, Role.CONSULTANT);

		LOG.info("Consultation request with id {} is sent to show the detail", id);
		LOG.info("Student feedback uid is {}", conServ.findOne(id).getStudentFeedback().getUid());
		model.addAttribute("consultation", conServ.findOne(id));
		model.addAttribute("studentfeedback", conServ.getStudentFeedbackFor(id));
		model.addAttribute("consultantfeedback", conServ.getConsultantFeedbackFor(id));
		LOG.info("Student feedback is {}", conServ.findOne(id).getStudentFeedback().toString());
		if (conServ.findOne(id).getConsultantFeedback() != null)
			LOG.info("Consultant feedback is {}", conServ.findOne(id).getConsultantFeedback().toString());

		return "shared-between-consultant-and-admin/detail";
	}

	/*
	 * Set meeting page
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/requests/{id}/setmeeting")
	public String setMeetingPage(@PathVariable int id, Model model,
			@CookieValue(value = "session", defaultValue = "none") String session) throws UnauthorizedException {
		Account user = authServ.authenticateAndAddToModel(model, session, Role.CONSULTANT);

		LOG.info("Setting meeting for consulatation {}", id);

		model.addAttribute("consultation", conServ.findOne(id));
		model.addAttribute("events", statServ.getMeetings(user.getId()));
		model.addAttribute("setTime", new SetTimeDTO());

		return "shared-between-consultant-and-admin/setmeeting";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/requests/{id}/setmeeting")
	public String postSetMeetingPage(@PathVariable int id, @ModelAttribute SetTimeDTO setTime,
			@CookieValue(value = "session", defaultValue = "none") String session)
			throws UnauthorizedException, ParseException {
		authServ.authenticateRequestForRole(session, Role.CONSULTANT);
		LOG.info("Set time to {}", setTime.toString());
		conServ.setConsultationDate(id, setTime);
		return "redirect:" + "/requests/scheduled";
	}

	/*
	 * ADMIN
	 */

	@RequestMapping(value = "/requests/admin/completed")
	public String getCompletedRequestsList(Model model,
			@CookieValue(value = "session", defaultValue = "none") String session) throws UnauthorizedException {
		authServ.authenticateAndAddToModel(model, session, Role.ADMINISTRATOR);

		List<CompletedDTO> completedRequests = conServ.findCompleted();

		LOG.info("Admin view for requests. Displayed: {}, status completed", completedRequests.size());
		model.addAttribute("consultations", completedRequests);
		return "admin/completed_requests";
	}

	@ExceptionHandler(UnauthorizedException.class)
	public String handleNotFound(Exception exc) {
		return "redirect:" + "/";
	}

	@ExceptionHandler(ParseException.class)
	public String handleBadDate(Exception exc) {
		return "redirect:" + "/";
	}

}

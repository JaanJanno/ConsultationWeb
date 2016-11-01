package ee.avok.consultation.controller;

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

import ee.avok.consultation.auth.domain.model.Role;
import ee.avok.consultation.auth.domain.model.UnauthorizedException;
import ee.avok.consultation.auth.service.AuthService;
import ee.avok.consultation.domain.model.StudentFeedback;
import ee.avok.consultation.dto.PostConsultationForm;
import ee.avok.consultation.service.ConsultationService;
import ee.avok.consultation.service.FeedbackService;

/**
 * Handles the feedback forms for student and consultant.
 * 
 * @author TKasekamp
 *
 */
@Controller
public class FeedbackController {
	@Autowired
	AuthService authServ;
	@Autowired
	FeedbackService feedServ;
	@Autowired
	ConsultationService consServ;

	@RequestMapping(value = "/requests/{id}/studentfeedback", method = RequestMethod.GET)
	public String studentFeedbackForm(Model model, @PathVariable int id, @RequestParam("uid") String uid)
			throws UnauthorizedException {
		feedServ.verifyStudentFeedbackUID(id, uid); // Check if UID matches
													// parameter given in e-mail
													// for authentication.
		model.addAttribute("feedback", consServ.getStudentFeedbackFor(id));
		return "general/feedback";
	}

	@RequestMapping(value = "/requests/{id}/studentfeedback", method = RequestMethod.POST)
	public String submitStudentFeedbackForm(@ModelAttribute StudentFeedback feedback, Model model, @PathVariable int id,
			@RequestParam("uid") String uid) throws UnauthorizedException {
		feedServ.verifyStudentFeedbackUID(id, uid); // Check if UID matches
													// parameter given in e-mail
													// for authentication.
		feedServ.submitStudentFeedback(id, feedback);
		return "redirect:" + "/";

	}

	@RequestMapping(value = "/report")
	public String createConsultationReportForm(Model model,
			@CookieValue(value = "session", defaultValue = "none") String session) throws UnauthorizedException {
		authServ.authenticateAndAddToModel(model, session, Role.CONSULTANT);

		model.addAttribute("postConsultationReport", new PostConsultationForm());
		return "shared-between-consultant-and-admin/post_consultation_form";
	}

	@ExceptionHandler(UnauthorizedException.class)
	public String handleNotFound(Exception exc) {
		return "redirect:" + "/";
	}
}

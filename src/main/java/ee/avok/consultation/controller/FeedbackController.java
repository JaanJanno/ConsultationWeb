package ee.avok.consultation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ee.avok.consultation.auth.domain.model.Role;
import ee.avok.consultation.auth.domain.model.UnauthorizedException;
import ee.avok.consultation.auth.service.AuthService;
import ee.avok.consultation.dto.FeedBackDTO;
import ee.avok.consultation.dto.PostConsultationForm;

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

	@RequestMapping(value = "/feedback", method = RequestMethod.GET)
	public String createFeedbackForm(Model model) {
		model.addAttribute("feedback", new FeedBackDTO());
		return "general/feedback";
	}

	@RequestMapping(value = "/report")
	public String createConsultationReportForm(Model model,
			@CookieValue(value = "session", defaultValue = "none") String session) throws UnauthorizedException {
		authServ.authenticateAndAddToModel(model, session, Role.CONSULTANT);

		model.addAttribute("postConsultationReport", new PostConsultationForm());
		return "shared-between-consultant-and-admin/post_consultation_form";
	}
}

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ee.avok.consultation.auth.domain.model.Account;
import ee.avok.consultation.auth.domain.model.Role;
import ee.avok.consultation.auth.domain.model.UnauthorizedException;
import ee.avok.consultation.auth.service.AuthService;
import ee.avok.consultation.domain.model.ConsultantFeedback;
import ee.avok.consultation.domain.model.StudentFeedback;
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
	public String studentFeedbackForm(Model model, @PathVariable int id , @RequestParam("uid") String uid)
			throws UnauthorizedException {
		feedServ.verifyStudentFeedbackUID(id, uid); // Check if UID matches
													// parameter given in e-mail
													// for authentication.
		model.addAttribute("feedback", new StudentFeedback());
		return "general/feedback";
	}

	@RequestMapping(value = "/requests/{id}/studentfeedback", method = RequestMethod.POST)
	public String submitStudentFeedbackForm(@ModelAttribute StudentFeedback feedback, Model model, @PathVariable int id,
			@RequestParam("uid") String uid, RedirectAttributes ra) throws UnauthorizedException {
		feedServ.verifyStudentFeedbackUID(id, uid); // Check if UID matches
													// parameter given in e-mail
													// for authentication.
		feedServ.submitStudentFeedback(id, feedback);
		
		ra.addFlashAttribute("message", "student.feedback.success");
		ra.addFlashAttribute("type", "success");
		return "redirect:/";

	}

	@RequestMapping(value = "/requests/{id}/consultantfeedback")
	public String consultantFeedbackForm(Model model, @PathVariable int id
			,@CookieValue(value = "session", defaultValue = "none") String session) throws UnauthorizedException {
		Account user = authServ.authenticateAndAddToModel(model, session, Role.CONSULTANT);
		feedServ.verifyConsultantFeedbackUser(user, id);
		model.addAttribute("feedback", new ConsultantFeedback());
		return "shared-between-consultant-and-admin/post_consultation_form";
	}

	@RequestMapping(value = "/requests/{id}/consultantfeedback", method = RequestMethod.POST)
	public String submitConsultantFeedbackForm(@ModelAttribute ConsultantFeedback feedback, Model model
			,@PathVariable int id, @CookieValue(value = "session", defaultValue = "none") String session, RedirectAttributes ra) throws UnauthorizedException {
		Account user = authServ.authenticateAndAddToModel(model, session, Role.CONSULTANT);
		feedServ.verifyConsultantFeedbackUser(user, id);
		feedServ.submitConsultantFeedback(id, feedback);
		
		// There should be other messages
		ra.addFlashAttribute("message", "consultant.feedback.success");
		ra.addFlashAttribute("type", "success");
		return "redirect:/requests/completed";

	}

	@ExceptionHandler(UnauthorizedException.class)
	public String handleNotFound(Exception exc) {
		return "redirect:" + "/";
	}
	
}

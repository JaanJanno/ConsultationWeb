package ee.avok.consultation.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import ee.avok.consultation.dto.StatisticsDTO;
import ee.avok.consultation.service.StatisticsService;

@Controller
public class StatisticsController {

	private static Logger LOG = LoggerFactory.getLogger(StatisticsController.class);
	@Autowired
	StatisticsService statServ;
	@Autowired
	AuthService authServ;

	@RequestMapping("/statistics")
	public String statisticsPage(Model model  , @CookieValue(value = "session", defaultValue = "none") String session)
			throws UnauthorizedException {
		Account user = authServ.authenticateAndAddToModel(model, session, Role.ADMINISTRATOR);

		LOG.info("Statistics page for admin: {}", user.getId());
		StatisticsDTO stats = statServ.getStatistics("all");

		model.addAttribute("stats", stats);
		model.addAttribute("events", statServ.getAllMeetings());
		// TODO a proper HTML template path
		return "admin/statistics";
	}

	@ExceptionHandler(UnauthorizedException.class)
	public String handleNotFound(Exception exc) {
		return "redirect:" + "/";
	}

}

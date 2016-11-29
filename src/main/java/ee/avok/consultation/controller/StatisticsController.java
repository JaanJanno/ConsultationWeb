package ee.avok.consultation.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ee.avok.consultation.auth.domain.model.Account;
import ee.avok.consultation.auth.domain.model.Role;
import ee.avok.consultation.auth.domain.model.UnauthorizedException;
import ee.avok.consultation.auth.service.AuthService;
import ee.avok.consultation.dto.StatisticsDTO;
import ee.avok.consultation.service.AccountService;
import ee.avok.consultation.service.StatisticsService;

@Controller
public class StatisticsController {

	private static Logger LOG = LoggerFactory.getLogger(StatisticsController.class);
	@Autowired
	StatisticsService statServ;
	@Autowired
	AuthService authServ;
	@Autowired
	AccountService accountServ;

	@RequestMapping("/statistics")
	public String statisticsPage(Model model, @CookieValue(value = "session", defaultValue = "none") String session)
			throws UnauthorizedException {
		Account user = authServ.authenticateAndAddToModel(model, session, Role.ADMINISTRATOR);

		LOG.info("Statistics page for admin: {}", user.getId());
		StatisticsDTO stats = statServ.getStatistics("all");

		model.addAttribute("stats", stats);

		model.addAttribute("cons", accountServ.findAll());
		return "admin/statistics";
	}
	
	@RequestMapping("/statistics/{period}")
	public String getStatistics(Model model,@PathVariable("period") String period){
		
		StatisticsDTO stats = statServ.getStatistics(period);
		model.addAttribute("stats", stats);

		return "fragments/statistic/boxes/aggregated_boxes :: aggregated_boxes";
	} 

	@ExceptionHandler(UnauthorizedException.class)
	public String handleNotFound(Exception exc) {
		return "redirect:" + "/";
	}

}

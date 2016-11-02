package ee.avok.consultation.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import ee.avok.consultation.auth.service.AuthService;
import ee.avok.consultation.service.ConsultationService;

@Controller
public class MainController {

	@SuppressWarnings("unused")
	private static Logger LOG = LoggerFactory.getLogger(RequestController.class);
	@Autowired
	ConsultationService conServ;

	@Autowired
	AuthService authServ;

	@RequestMapping(value = { "/", "index" })
	public String indexPage() {
		return "general/index";
	}

}
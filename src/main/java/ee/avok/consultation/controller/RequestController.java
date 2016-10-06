package ee.avok.consultation.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ee.avok.consultation.domain.model.ConsultationRequest;
import ee.avok.consultation.service.ConsultationService;

@Controller
public class RequestController {

	private static Logger LOG = LoggerFactory.getLogger(RequestController.class);
	@Autowired
	ConsultationService conServ;

	@RequestMapping(value = "/request", method = RequestMethod.GET)
	public String createLocation(Model model) {
		model.addAttribute("consultation", new ConsultationRequest());
		return "request";
	}

	@RequestMapping(value = "/request", method = RequestMethod.POST)
	public void createUnit(@ModelAttribute ConsultationRequest conReq, Model model) {
	
		LOG.info("Saving Consultation "+conReq.getPurpose());
		model.addAttribute("consultation", new ConsultationRequest());
	
		conServ.createConsultation(conReq);
		
	}
}

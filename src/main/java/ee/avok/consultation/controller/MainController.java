package ee.avok.consultation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import ee.avok.consultation.domain.model.ConsultationRequest;
import ee.avok.consultation.domain.model.ConsultationStatus;
import ee.avok.consultation.service.ConsultationService;

@Controller
public class MainController {
	@Autowired
	ConsultationService conServ;
	
	@RequestMapping("/")
	public String indexPage(Model model) {

		ConsultationStatus status = null;
		List<ConsultationRequest> conReqs = conServ.findByStatus(status.RECEIVED);
		System.out.println("Request size:"+conReqs.size() );
		model.addAttribute("consultations", conReqs);
		return "index";
	}

	@RequestMapping("/empty")
	public String emptyPage(Model model) {
		return "empty";
	}

}
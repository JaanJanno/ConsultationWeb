package ee.avok.consultation.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ee.avok.consultation.domain.model.ConsultationRequest;
import ee.avok.consultation.service.ConsultationService;

@RestController
@RequestMapping("/requests")
public class ConsultationController {

	private static Logger LOG = LoggerFactory.getLogger(ConsultationController.class);

	@Autowired
	ConsultationService conServ;

	@RequestMapping(method = RequestMethod.POST)
	public HttpStatus createRequest(@RequestBody ConsultationRequest conReq) {
		LOG.info("Creating new consultation request");

		conServ.createConsultation(conReq);

		return HttpStatus.CREATED;

	}
}

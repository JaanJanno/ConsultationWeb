package ee.avok.consultation.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ee.avok.consultation.domain.model.ConsultationRequest;
import ee.avok.consultation.domain.model.ConsultationStatus;
import ee.avok.consultation.service.ConsultationService;

@RestController
@RequestMapping("/api/requests")
public class ConsultationController {

	private static Logger LOG = LoggerFactory.getLogger(ConsultationController.class);

	@Autowired
	ConsultationService conServ;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<String> createRequest(@RequestBody ConsultationRequest conReq) {
		LOG.info("Creating new consultation request");

		conServ.createConsultationREST(conReq);
		return new ResponseEntity<String>(HttpStatus.CREATED);

	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<ConsultationRequest>> getRequests(
			@RequestParam(value = "status", required = false) ConsultationStatus status) {
		LOG.info("All consultation requests with status {}", status);

		List<ConsultationRequest> conReqs = conServ.findByStatus(status);
		return new ResponseEntity<List<ConsultationRequest>>(conReqs, HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<ConsultationRequest> getRequest(@PathVariable int id) {
		LOG.info("Consultation request with id {}", id);

		ConsultationRequest conReq = conServ.findOne(id);
		return new ResponseEntity<ConsultationRequest>(conReq, HttpStatus.OK);

	}

	// Doesn't do anything, must authenticate user first
	@Deprecated
	@RequestMapping(method = RequestMethod.POST, value = "/{id}")
	public ResponseEntity<String> setConsultationAccepted(@PathVariable int id) {
		LOG.info("Consultation request with id {}, set as Accepted", id);

		// conServ.updateStatus(id, ConsultationStatus.ACCEPTED);
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}

}

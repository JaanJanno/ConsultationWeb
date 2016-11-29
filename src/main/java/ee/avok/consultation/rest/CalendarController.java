package ee.avok.consultation.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ee.avok.consultation.auth.domain.model.Role;
import ee.avok.consultation.auth.domain.model.UnauthorizedException;
import ee.avok.consultation.auth.service.AuthService;
import ee.avok.consultation.dto.CalendarDTO;
import ee.avok.consultation.service.StatisticsService;

@RestController
@RequestMapping("/api/calendar")
public class CalendarController {

	private static Logger LOG = LoggerFactory.getLogger(CalendarController.class);

	@Autowired
	StatisticsService statServ;
	@Autowired
	AuthService authServ;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<CalendarDTO>> getEvents(
			@CookieValue(value = "session", defaultValue = "none") String session) throws UnauthorizedException {
		authServ.authenticateRequestForRole(session, Role.ADMINISTRATOR);

		LOG.info("All calendar events");

		List<CalendarDTO> events = statServ.getAllMeetings();
		return new ResponseEntity<List<CalendarDTO>>(events, HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ResponseEntity<List<CalendarDTO>> eventsForConsultant(@PathVariable int id,
			@CookieValue(value = "session", defaultValue = "none") String session) throws UnauthorizedException {
		authServ.authenticateRequestForRole(session, Role.ADMINISTRATOR);

		LOG.info("All calendar events for consultant {}", id);

		List<CalendarDTO> events = statServ.getMeetings(id);
		return new ResponseEntity<List<CalendarDTO>>(events, HttpStatus.OK);

	}
}

package ee.avok.consultation.service;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import ee.avok.consultation.ConsultationWebApplication;
import ee.avok.consultation.auth.domain.model.Account;
import ee.avok.consultation.auth.domain.repository.AccountRepository;
import ee.avok.consultation.domain.model.ConsultationRequest;
import ee.avok.consultation.domain.repository.ConsultationRequestRepository;
import ee.avok.consultation.dto.CalendarDTO;
import ee.avok.consultation.dto.StatisticsDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ConsultationWebApplication.class)
@ActiveProfiles("test")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
@Transactional
public class StatisticsTest {
	@Autowired
	ConsultationRequestRepository conReqRepo;
	@Autowired
	StatisticsService statServ;
	@Autowired
	AccountRepository accountRepo;

	ConsultationRequest con1;
	ConsultationRequest con2;
	private Date meetingDate;

	@Before
	public void setup() {
		conReqRepo.deleteAll();

		Account consultant = new Account();
		accountRepo.save(consultant);

		con1 = new ConsultationRequest();
		con1.setReceivedDate(new Date());
		con2 = new ConsultationRequest();
		con2.setReceivedDate(new Date());
		con2.setAcceptedDate(new Date());
		con2.setScheduledDate(new Date());
		con2.setConsultant(consultant);

		meetingDate = new Date();
		con2.setMeetingDate(meetingDate);
		con2.setMeetingPlace("Tartu");
		conReqRepo.save(con1);
		conReqRepo.save(con2);
	}

	@Test
	public void statsAll() {
		StatisticsDTO stats = statServ.statsAll();
		assertEquals(2, stats.getReceived());
		assertEquals(1, stats.getAccepted());
		assertEquals(1, stats.getScheduled());
		assertEquals(0, stats.getCompleted());
	}

	@Test
	public void getAllStats() {
		StatisticsDTO stats = statServ.getStatistics("all");
		assertEquals(2, stats.getReceived());
		assertEquals(1, stats.getAccepted());
		assertEquals(1, stats.getScheduled());
		assertEquals(0, stats.getCompleted());
	}

	@Test
	public void getAllMeetings() {
		List<CalendarDTO> events = statServ.getAllMeetings();
		assertEquals(1, events.size());

		CalendarDTO ev = events.get(0);
		assertEquals(con2.getId(), ev.getId());
		assertEquals(con2.getMeetingDate(), ev.getDate());
		assertEquals("/requests/detail/" + con2.getId(), ev.getUrl());

	}

	@Test
	public void getMeetingsForConsultant() {
		List<CalendarDTO> events = statServ.getMeetings(con2.getConsultant().getId());
		assertEquals(1, events.size());

		CalendarDTO ev = events.get(0);
		assertEquals(con2.getId(), ev.getId());
		assertEquals(con2.getMeetingDate(), ev.getDate());
		assertEquals("/requests/detail/" + con2.getId(), ev.getUrl());

	}

}

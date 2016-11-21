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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import ee.avok.consultation.ConsultationWebApplication;
import ee.avok.consultation.auth.domain.model.Account;
import ee.avok.consultation.auth.domain.repository.AccountRepository;
import ee.avok.consultation.domain.model.ConsultationRequest;
import ee.avok.consultation.domain.model.ConsultationStatus;
import ee.avok.consultation.domain.repository.ConsultationRequestRepository;
import ee.avok.consultation.dto.CalendarDTO;
import ee.avok.consultation.dto.StatisticsDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ConsultationWebApplication.class)
@Sql(scripts="requests-dataset.sql") 
@DirtiesContext(classMode= ClassMode.AFTER_EACH_TEST_METHOD)

public class StatisticsTest {
	
	ConsultationStatus status;
	
	@Autowired
	ConsultationRequestRepository conReqRepo;
	@Autowired
	StatisticsService statServ;
	@Autowired
	AccountRepository accountRepo;

	@Test
	public void statsAll() {
		StatisticsDTO stats = statServ.statsAll();
		assertEquals(14, stats.getReceived());
		assertEquals(11, stats.getAccepted());
		assertEquals(8, stats.getScheduled());
		assertEquals(5, stats.getCompleted()); 
	}

	@Test
	public void getAllStats() {
		StatisticsDTO stats = statServ.getStatistics("all");
		assertEquals(14, stats.getReceived());
		assertEquals(11, stats.getAccepted());
		assertEquals(8, stats.getScheduled());
		assertEquals(5, stats.getCompleted());
	}
	/*
	@Test
	public void getAllMeetings() {
		List<CalendarDTO> events = statServ.getAllMeetings();
		assertEquals(1, events.size());

		CalendarDTO ev = events.get(0);
		assertEquals(con2.getId(), ev.getId());
		assertEquals(con2.getMeetingDate(), ev.getDate());
		assertEquals("/requests/detail/" + con2.getId(), ev.getUrl());

	}
*/
	/*
	@Test
	public void getMeetingsForConsultant() {
		List<CalendarDTO> events = statServ.getMeetings(1);
		assertEquals(1, events.size());

		CalendarDTO ev = events.get(0);
		assertEquals(con2.getId(), ev.getId());
		assertEquals(con2.getMeetingDate(), ev.getDate());
		assertEquals("/requests/detail/" + con2.getId(), ev.getUrl());

	}

	@Test
	public void countRequestByStatusAndPeriodTest(){
		int count=statServ.countRequestByStatusAndPeriod(status.RECEIVED, "Daily");
		assertEquals(1, count);
			
	} 
*/
	@Test
	public void setdataShouldbeLoadedTest(){
		long count=conReqRepo.count();
		assertEquals(14, count);
	}

}

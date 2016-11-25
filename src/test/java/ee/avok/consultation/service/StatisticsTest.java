package ee.avok.consultation.service;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ee.avok.consultation.ConsultationWebApplication;
import ee.avok.consultation.auth.domain.repository.AccountRepository;
import ee.avok.consultation.domain.model.ConsultationStatus;
import ee.avok.consultation.domain.repository.ConsultationRequestRepository;
import ee.avok.consultation.dto.StatisticsDTO;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ConsultationWebApplication.class)
@Sql(scripts="requests-dataset.sql") 
@DirtiesContext(classMode= ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
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
		assertEquals(18, stats.getReceived());
		assertEquals(15, stats.getAccepted());
		assertEquals(11, stats.getScheduled());
		assertEquals(6, stats.getCompleted()); 
	}

	@Test
	public void getAllStats() {
		StatisticsDTO stats = statServ.getStatistics("all");
		assertEquals(18, stats.getReceived());
		assertEquals(15, stats.getAccepted());
		assertEquals(11, stats.getScheduled());
		assertEquals(6, stats.getCompleted());
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
*/
	@Test
	@Ignore
	public void findRequestByDateTest(){
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		 StatisticsDTO recievedRequests = statServ.findRequestByDate(c.getTime());
		assertEquals(6, recievedRequests.getReceived());
		assertEquals(3, recievedRequests.getAccepted());
		assertEquals(2, recievedRequests.getScheduled());
		assertEquals(1, recievedRequests.getCompleted());
	}
	
	@Test
	@Ignore
	public void countReceivedByDateTest(){
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		int count=conReqRepo.countReceivedByDate(c.getTime());
		assertEquals(6, count);
		
		
	}
	
	@Test
	@Ignore
	public void getStatisticsToday(){
		StatisticsDTO statistic = statServ.getStatistics("today");
		assertEquals(6, statistic.getReceived());
		assertEquals(3, statistic.getAccepted());
		assertEquals(2, statistic.getScheduled());
		assertEquals(1, statistic.getCompleted());
	}

	@Test
	public void getStatisticsWeek(){
		StatisticsDTO statistic = statServ.getStatistics("week");
		assertEquals(6, statistic.getReceived());
		assertEquals(6, statistic.getAccepted());
		assertEquals(4, statistic.getScheduled());
		assertEquals(1, statistic.getCompleted());
	}
	
	@Test
	public void getStatisticsMonth(){
		StatisticsDTO statistic = statServ.getStatistics("month");
		assertEquals(10, statistic.getReceived());
		assertEquals(11, statistic.getAccepted());
		assertEquals(8, statistic.getScheduled());
		assertEquals(4, statistic.getCompleted());
	}
	@Test
	public void findRequestsByPeriodTotalTest(){
		StatisticsDTO statistic = statServ.getStatistics("all");
		assertEquals(18, statistic.getReceived());
		assertEquals(15, statistic.getAccepted());
		assertEquals(11, statistic.getScheduled());
		assertEquals(6, statistic.getCompleted());
	}

	@Test
	public void setdataShouldbeLoadedTest(){
		long count=conReqRepo.count();
		assertEquals(18, count);
	}

}

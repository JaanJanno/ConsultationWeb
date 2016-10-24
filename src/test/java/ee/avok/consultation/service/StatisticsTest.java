package ee.avok.consultation.service;

import static org.junit.Assert.assertEquals;

import java.util.Date;

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
import ee.avok.consultation.domain.model.ConsultationRequest;
import ee.avok.consultation.domain.repository.ConsultationRequestRepository;
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

	@Before
	public void setup() {
		conReqRepo.deleteAll();

		ConsultationRequest con1 = new ConsultationRequest();
		con1.setReceivedDate(new Date());
		ConsultationRequest con2 = new ConsultationRequest();
		con2.setReceivedDate(new Date());
		con2.setAcceptedDate(new Date());
		con2.setScheduledDate(new Date());
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

}

package ee.avok.consultation.domain.repository;

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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ConsultationWebApplication.class)
@ActiveProfiles("test")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
@Transactional
public class ConsultationRequestRepositoryTest {
	@Autowired
	ConsultationRequestRepository conReqRepo;

	@Before
	public void setup() {
		conReqRepo.deleteAll();

		ConsultationRequest con1 = new ConsultationRequest();
		con1.setReceivedDate(new Date());
		ConsultationRequest con2 = new ConsultationRequest();
		con2.setReceivedDate(new Date());
		con2.setAcceptedDate(new Date());
		con2.setScheduledDate(new Date());
		con2.setCompletedDate(new Date());
		conReqRepo.save(con1);
		conReqRepo.save(con2);
	}

	@Test
	public void countAllReceived() {
		assertEquals(2, conReqRepo.countAllReceived());
	}

	@Test
	public void countAllAccepted() {
		assertEquals(1, conReqRepo.countAllAccepted());
	}

	@Test
	public void countAllScheduled() {
		assertEquals(1, conReqRepo.countAllScheduled());
	}

	@Test
	public void countAllCompleted() {
		assertEquals(1, conReqRepo.countAllCompleted());
	}
}

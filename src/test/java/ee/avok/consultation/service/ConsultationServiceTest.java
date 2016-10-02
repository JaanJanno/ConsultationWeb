package ee.avok.consultation.service;

import static org.junit.Assert.assertEquals;

import javax.transaction.Transactional;

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
import ee.avok.consultation.domain.model.ConsultationStatus;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ConsultationWebApplication.class)
@ActiveProfiles("test")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
@Transactional
public class ConsultationServiceTest {
	@Autowired
	ConsultationService conServ;

	@Test
	public void createAndHasStatusReceived() {
		ConsultationRequest req = new ConsultationRequest();
		req.setName("Bla Bla");
		req.setEmail("bla@bla.bla");
		conServ.createConsultation(req);
		assertEquals(ConsultationStatus.RECEIVED, req.getStatus());
	}

	@Test
	public void changeStatus() {
		ConsultationRequest req = new ConsultationRequest();
		req.setName("Bla Bla");
		req.setEmail("bla@bla.bla");
		conServ.createConsultation(req);
		assertEquals(ConsultationStatus.RECEIVED, req.getStatus());

		conServ.updateStatus(req.getId(), ConsultationStatus.ACCEPTED);

		assertEquals(ConsultationStatus.ACCEPTED, conServ.findOne(req.getId()).getStatus());
	}

}

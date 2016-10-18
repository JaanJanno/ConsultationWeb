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
import ee.avok.consultation.auth.domain.model.Account;
import ee.avok.consultation.auth.domain.model.Role;
import ee.avok.consultation.auth.domain.repository.AccountRepository;
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
	@Autowired
	AccountRepository accountRepo;

	@Test
	public void createAndHasStatusReceived() {
		ConsultationRequest req = new ConsultationRequest();
		req.setName("Bla Bla");
		req.setEmail("bla@bla.bla");
		conServ.createConsultationREST(req);
		assertEquals(ConsultationStatus.RECEIVED, req.getStatus());
	}

	@Test
	public void changeStatus() {
		Account user = new Account();
		user.setUsername("kalevipoeg");
		user.setRole(Role.CONSULTANT);
		accountRepo.save(user);

		ConsultationRequest req = new ConsultationRequest();
		req.setName("Bla Bla");
		req.setEmail("bla@bla.bla");
		conServ.createConsultationREST(req); // Should be normal createConsultation
		assertEquals(ConsultationStatus.RECEIVED, req.getStatus());

		conServ.updateStatus(req.getId(), user, ConsultationStatus.ACCEPTED);

		assertEquals(ConsultationStatus.ACCEPTED, conServ.findOne(req.getId()).getStatus());
		assertEquals("kalevipoeg", conServ.findOne(req.getId()).getConsultant().getUsername());
	}

}

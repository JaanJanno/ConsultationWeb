package ee.avok.consultation.service;

import static org.junit.Assert.assertEquals;

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
import ee.avok.consultation.auth.domain.model.Role;
import ee.avok.consultation.auth.domain.repository.AccountRepository;
import ee.avok.consultation.domain.model.ConsultationRequest;
import ee.avok.consultation.domain.model.ConsultationStatus;
import ee.avok.consultation.domain.repository.ConsultationRequestRepository;
import ee.avok.consultation.dto.CompletedDTO;

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
	@Autowired
	ConsultationRequestRepository conReqRepo;

	@Before
	public void setup() {
		conReqRepo.deleteAll();
		accountRepo.deleteAll();
	}

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
		Account user = new Account();
		user.setUsername("kalevipoeg");
		user.setRole(Role.CONSULTANT);
		accountRepo.save(user);

		ConsultationRequest req = new ConsultationRequest();
		req.setName("Bla Bla");
		req.setEmail("bla@bla.bla");
		conServ.createConsultation(req); // Should be normal createConsultation
		assertEquals(ConsultationStatus.RECEIVED, req.getStatus());

		conServ.setAccepted(req.getId(), user);

		assertEquals(ConsultationStatus.ACCEPTED, conServ.findOne(req.getId()).getStatus());
		assertEquals("kalevipoeg", conServ.findOne(req.getId()).getConsultant().getUsername());
	}

	@Test
	public void completedDTOs() {
		Account user = new Account();
		user.setName("kalevipoeg");
		user.setRole(Role.CONSULTANT);
		accountRepo.save(user);

		ConsultationRequest req = new ConsultationRequest();
		req.setName("Bla Bla");
		req.setEmail("bla@bla.bla");
		req.setStatus(ConsultationStatus.COMPLETED);
		req.setConsultant(user);
		conReqRepo.save(req);

		List<CompletedDTO> dtos = conServ.findCompleted();

		assertEquals(1, dtos.size());

		CompletedDTO dto = dtos.get(0);
		assertEquals(req.getId(), dto.getId());
		assertEquals("Bla Bla", dto.getName());
		assertEquals("kalevipoeg", dto.getConsultantName());
		// TODO add feedback and check their existence
		assertEquals(false, dto.isConsultantFeedback());
		assertEquals(false, dto.isStudentFeedback());
	}

}

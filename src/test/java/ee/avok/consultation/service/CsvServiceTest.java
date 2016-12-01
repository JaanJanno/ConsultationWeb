package ee.avok.consultation.service;

import static org.hamcrest.CoreMatchers.everyItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

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
import ee.avok.consultation.auth.domain.model.Role;
import ee.avok.consultation.auth.domain.repository.AccountRepository;
import ee.avok.consultation.domain.model.ConsultantFeedback;
import ee.avok.consultation.domain.model.ConsultationRequest;
import ee.avok.consultation.domain.model.ConsultationStatus;
import ee.avok.consultation.domain.repository.ConsultantFeedbackRepository;
import ee.avok.consultation.domain.repository.ConsultationRequestRepository;
import ee.avok.consultation.dto.CsvBean;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ConsultationWebApplication.class)
@ActiveProfiles("test")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
@Transactional
public class CsvServiceTest {
	@Autowired
	AccountRepository accountRepo;
	@Autowired
	ConsultationRequestRepository conReqRepo;
	@Autowired
	CsvService csvServ;
	@Autowired
	ConsultantFeedbackRepository conFeedRepo;
	@Autowired
	FeedbackService feedServ;

	private Date now;
	private Account user;

	@Before
	public void setup() {
		conReqRepo.deleteAll();
		accountRepo.deleteAll();

		user = new Account();
		user.setUsername("kalevipoeg");
		user.setName("Kalev Kalevson");
		user.setRole(Role.CONSULTANT);
		accountRepo.save(user);

		now = new Date();

		ConsultantFeedback f = new ConsultantFeedback();
		conFeedRepo.save(f);

		ConsultationRequest con1 = new ConsultationRequest();
		con1.setMeetingDate(now);
		con1.setStatus(ConsultationStatus.COMPLETED);
		con1.setName("Bernard Lowe");
		con1.setLanguage("Estonian");
		con1.setComments("This doesn't look like anything to me");
		con1.setConsultant(user);
		con1.setDegree("Msc");
		con1.setDepartment("Faculty of Science and Technology");
		con1.setProgramme("Programming");
		con1.setTextType("Essay");
		con1.setConsultantFeedback(f);
		feedServ.addStudentFeedback(con1);
		con1.getStudentFeedback().setUid(null);

		// Not suitable con, should not be in CSV
		ConsultationRequest con2 = new ConsultationRequest();
		con2.setAcceptedDate(now);
		con2.setStatus(ConsultationStatus.ACCEPTED);
		con2.setName("Test Testy");
		con2.setLanguage("English");
		con2.setComments("Halp");
		con2.setConsultant(user);
		con2.setDegree("Msc");
		con2.setDepartment("Faculty of Social Sciences");
		con2.setProgramme("History");
		con2.setTextType("Exam");
		con2.setConsultantFeedback(f);
		feedServ.addStudentFeedback(con2);

		ConsultationRequest con3 = new ConsultationRequest();
		con3.setMeetingDate(now);
		con3.setStatus(ConsultationStatus.COMPLETED);
		con3.setName("Jon Snow");
		con3.setLanguage("English");
		con3.setComments("I know nothing");
		con3.setConsultant(user);
		con3.setDegree("Bsc");
		con3.setDepartment("Faculty of Social Sciences");
		con3.setProgramme("Social studies");
		con3.setTextType("Essay");
		con3.setConsultantFeedback(f);
		feedServ.addStudentFeedback(con3);
		con3.getStudentFeedback().setUid(null);

		// Has no student feedback
		ConsultationRequest con4 = new ConsultationRequest();
		con4.setMeetingDate(now);
		con4.setStatus(ConsultationStatus.COMPLETED);
		con4.setName("Jon Snow2");
		con4.setLanguage("English");
		con4.setComments("I know nothing");
		con4.setConsultant(user);
		con4.setDegree("Bsc");
		con4.setDepartment("Faculty of Social Sciences");
		con4.setProgramme("Social studies");
		con4.setTextType("Essay");
		con4.setConsultantFeedback(f);
		feedServ.addStudentFeedback(con4);

		conReqRepo.save(con1);
		conReqRepo.save(con2);
		conReqRepo.save(con3);
		conReqRepo.save(con4);

	}

	@SuppressWarnings("unchecked")
	@Test
	public void createsBeans() {
		List<CsvBean> beans = csvServ.createBeans();

		assertEquals(2, beans.size());

		// Names
		assertThat(beans, containsInAnyOrder(hasProperty("studentName", is("Bernard Lowe")),
				hasProperty("studentName", is("Jon Snow"))));

		// Consultant name
		assertThat(beans, everyItem(hasProperty("consultantName", is("Kalev Kalevson"))));

		assertThat(beans,
				containsInAnyOrder(hasProperty("language", is("English")), hasProperty("language", is("Estonian"))));

		assertThat(beans, everyItem(hasProperty("textType", is("Essay"))));
	}

	/**
	 * Tests if requests more than 30 days old and with no student feedback are
	 * added.
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void beansWithStudentFeedbackTimeout() {
		// Sets time 31 days back
		Date past = new Date();
		past.setTime(past.getTime() - 2678400000l);

		// Has no student feedback, but is more than 30 days old
		ConsultationRequest con5 = new ConsultationRequest();
		con5.setMeetingDate(past);
		con5.setStatus(ConsultationStatus.COMPLETED);
		con5.setName("Bla bla");
		con5.setLanguage("English");
		con5.setComments("I know nothing");
		con5.setConsultant(user);
		con5.setDegree("Bsc");
		con5.setDepartment("Faculty of Social Sciences");
		con5.setProgramme("Social studies");
		con5.setTextType("Essay");

		ConsultantFeedback f1 = new ConsultantFeedback();

		con5.setConsultantFeedback(f1);
		conFeedRepo.save(f1);
		feedServ.addStudentFeedback(con5);
		conReqRepo.save(con5);

		List<CsvBean> beans = csvServ.createBeans();

		assertEquals(3, beans.size());

		// Names
		assertThat(beans, containsInAnyOrder(hasProperty("studentName", is("Bernard Lowe")),
				hasProperty("studentName", is("Jon Snow")), hasProperty("studentName", is("Bla bla"))));

		// Consultant name
		assertThat(beans, everyItem(hasProperty("consultantName", is("Kalev Kalevson"))));

		assertThat(beans, containsInAnyOrder(hasProperty("language", is("English")),
				hasProperty("language", is("English")), hasProperty("language", is("Estonian"))));

		assertThat(beans, everyItem(hasProperty("textType", is("Essay"))));
	}

}

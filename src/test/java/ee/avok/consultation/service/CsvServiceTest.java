package ee.avok.consultation.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

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
import ee.avok.consultation.domain.model.ConsultationRequest;
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

	private Date now;

	@Before
	public void setup() {
		conReqRepo.deleteAll();
		accountRepo.deleteAll();

		Account user = new Account();
		user.setUsername("kalevipoeg");
		user.setName("Kalev Kalevson");
		user.setRole(Role.CONSULTANT);
		accountRepo.save(user);

		now = new Date();

		ConsultationRequest con1 = new ConsultationRequest();
		con1.setMeetingDate(now);
		con1.setName("Bernard Lowe");
		con1.setLanguage("Estonian");
		con1.setComments("This doesn't look like anything to me");
		con1.setConsultant(user);
		con1.setDegree("Msc");
		con1.setDepartment("Faculty of Science and Technology");
		con1.setProgramme("Programming");
		con1.setTextType("Essay");

		// Not suitable con, should not be in CSV
		ConsultationRequest con2 = new ConsultationRequest();
		con2.setAcceptedDate(now);
		con2.setName("Test Testy");
		con2.setLanguage("English");
		con2.setComments("Halp");
		con2.setConsultant(user);
		con2.setDegree("Msc");
		con2.setDepartment("Faculty of Social Sciences");
		con2.setProgramme("History");
		con2.setTextType("Exam");

		ConsultationRequest con3 = new ConsultationRequest();
		con2.setMeetingDate(now);
		con2.setName("Jon Snow");
		con2.setLanguage("English");
		con2.setComments("I know nothing");
		con2.setConsultant(user);
		con2.setDegree("Bsc");
		con2.setDepartment("Faculty of Social Sciences");
		con2.setProgramme("Social studies");
		con2.setTextType("Essay");

		conReqRepo.save(con1);
		conReqRepo.save(con2);
		conReqRepo.save(con3);

	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Test
	public void createsBeans() {
		List<CsvBean> beans = csvServ.createBeans();

		assertEquals(2, beans.size());

		// Names
		assertThat(beans, containsInAnyOrder(hasProperty("studentName", is("Bernard Lowe")),
				hasProperty("studentName", is("Jon Snow"))));

		// Consultant name
		assertThat(beans, containsInAnyOrder(hasProperty("consultantName", is("Kalev Kalevson"))));

		assertThat(beans,
				containsInAnyOrder(hasProperty("language", is("English")), hasProperty("languge", is("Estonian"))));

		assertThat(beans, containsInAnyOrder(hasProperty("textType", is("Essay"))));
	}

}

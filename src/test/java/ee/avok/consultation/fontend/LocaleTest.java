package ee.avok.consultation.fontend;


import static org.junit.Assert.assertEquals;

import java.util.Locale;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import ee.avok.consultation.ConsultationWebApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ConsultationWebApplication.class)
@ActiveProfiles("test")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
@Transactional
public class LocaleTest {

	@Autowired
	private MessageSource messageSource;

	@Test
	public void print() {
		assertEquals(messageSource.getMessage("form.title", null, new Locale("en")), "Please fill the form");
		assertEquals(messageSource.getMessage("form.title", null, new Locale("et")), "Palun täida ankeet");
	}
	

}

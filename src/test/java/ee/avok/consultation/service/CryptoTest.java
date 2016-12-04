package ee.avok.consultation.service;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import ee.avok.consultation.ConsultationWebApplication;
import ee.avok.consultation.auth.service.CryptoServiceImpl;
import ee.avok.consultation.controller.RequestController;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ConsultationWebApplication.class)
@ActiveProfiles("test")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class })
@Transactional
public class CryptoTest {
	
	@Autowired
	CryptoServiceImpl cryptoServ;
	
	private String testPasswordRandom = UUID.randomUUID().toString();
	
	private String testPassword = "nonRandompassword12345?=)";
	
	private static Logger LOG = LoggerFactory.getLogger(RequestController.class);
	
	@Test
	public void testPasswordHashing() {
		String password = testPassword;
		String hash = cryptoServ.encrypt(password);
		LOG.info("Hashed password: "+ password +" to: " + hash);
		assertEquals(cryptoServ.verify(password, hash), true);
	}
	
	@Test
	public void testPasswordRandomHashing() {
		String password = testPasswordRandom;
		String hash = cryptoServ.encrypt(password);
		LOG.info("Hashed password: "+ password +" to: " + hash);
		assertEquals(cryptoServ.verify(password, hash), true);
	}
	
	
	@Test
	public void testVerification() {
		String password = "99a82ac4-7bcd-46ae-b92b-363a6b83fcc7";
		String hash = "$2a$10$QJ845z.bfVWA7pHDVMcP8uu1gbJXjPIkxcrKTeenmwXBlHrMVCqsK";
		LOG.info("Hashed password: "+ password +" to: " + hash);
		assertEquals(cryptoServ.verify(password, hash), true);
	}

}
package ee.avok.consultation.service;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

public class MailTest {

	@Test
	@Ignore
	public void mailTest() {
		System.out.println("ter");

		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		sender.setHost("mailhost.ut.ee");
		sender.setProtocol("smtps");
		sender.setPort(465);

		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		try {
			helper.setTo("jaan911@gmail.com");
			helper.setFrom("jaan911@ut.ee");
			helper.setSubject("Test subject");
			helper.setText("Test text.");

		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		sender.send(message);
	}

	@Ignore
	@Test
	public void test2() {

		final String username = "jaan911@ut.ee";
		final String password = "xxx";

		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
		props.setProperty("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.host", "mailhost.ut.ee");
		props.put("mail.smtp.port", "465");
		props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

			Message message = new MimeMessage(session);

			message.setFrom(new InternetAddress("jaan911@ut.ee"));

			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("jaan911@ut.ee"));

			message.setSubject("Testing Subject");
			message.setText("Dear Mail Crawler, \n\n No spam to my email, please!");

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

}

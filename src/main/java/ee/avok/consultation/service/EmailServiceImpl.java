package ee.avok.consultation.service;

import java.util.Arrays;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import ee.avok.consultation.auth.domain.model.AccountPending;
import ee.avok.consultation.domain.model.ConsultationRequest;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	Environment env;

	@Autowired
	private MessageSource messageSource;

	private final JavaMailSenderImpl sender = new JavaMailSenderImpl();

	private String mailer;

	public EmailServiceImpl() { 
		mailer = getEnv("EMAIL_USER", "no-reply-avok@ut.ee");
		sender.setHost(getEnv("EMAIL_HOST", "mailhost.ut.ee"));
		sender.setProtocol(getEnv("EMAIL_PROTOCOL", "smtps"));
		sender.setPort(getEnvInt("EMAIL_PORT", 465));
		String username = getEnv("EMAIL_USER", null);
		String password = getEnv("EMAIL_PW", null);
		if (password != null && username != null) {
			sender.setUsername(username);
			sender.setPassword(password);
		}
		sender.getSession().getProperties().setProperty("mail.smtp.starttls.enable", getEnvBool("EMAIL_TLS", false).toString());
	}
	
	private static String getEnv(String key, String def) {
		String value = System.getenv(key);
		return value == null ? def : value;
	}
	
	private static Boolean getEnvBool(String key, boolean def) {
		String value = System.getenv(key);
		if (value == null) 
			return def;
		return value.equals("true") ? true : false;
	}
	
	private static Integer getEnvInt(String key, Integer def) {
		String value = System.getenv(key);
		if (value == null)
			return def;	
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			return def;
		}
	}

	@Override
	public void sendAccountCreation(AccountPending account) {
		String subject = messageSource.getMessage("email.accountcreate.subject", null, new Locale("en"));
		String text = messageSource.getMessage("email.accountcreate.text", null, new Locale("en"));
		send(subject, text + "\n" + getBaseUrl() + account.generateUrl(), account.getEmail());
	}

	@Override
	public void sendReminder(ConsultationRequest req) {
		String subject;
		String text;
		if (req.getLanguage().equals("Estonian")) {
			subject = messageSource.getMessage("email.reminder.subject", null, new Locale("et"));
			text = messageSource.getMessage("email.reminder.text", null, new Locale("et"));
		} else {
			subject = messageSource.getMessage("email.reminder.subject", null, new Locale("en"));
			text = messageSource.getMessage("email.reminder.text", null, new Locale("en"));
		}
		send(subject, text + "\n" + req.getMeetingDate().toString() + "\n" + req.getMeetingPlace(), req.getEmail());
	}

	@Override
	public void sendFeedbackRequest(ConsultationRequest req) {
		String subject;
		String text;
		if (req.getLanguage().equals("Estonian")) {
			subject = messageSource.getMessage("email.feedback.subject", null, new Locale("et"));
			text = messageSource.getMessage("email.feedback.text", null, new Locale("et"));
		} else {
			subject = messageSource.getMessage("email.feedback.subject", null, new Locale("en"));
			text = messageSource.getMessage("email.feedback.text", null, new Locale("en"));
		}
		String url = req.generateFeedbackUrl(getBaseUrl());
		send(subject, text + "\n" + url, req.getEmail());
	}

	private void send(String subject, String text, String to) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(mailer);
			message.setTo(to);
			message.setText(text);
			message.setSubject(subject);
			sender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getBaseUrl() {
		if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
			return "http://localhost:8080";
		} else {
			return "http://avok.herokuapp.com";
		}
	}

}
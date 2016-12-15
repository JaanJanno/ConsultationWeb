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

	private final JavaMailSenderImpl sender;

	public EmailServiceImpl() {
		super();
		sender = new JavaMailSenderImpl();
		sender.setHost("mailhost.ut.ee");
		sender.setProtocol("smtps");
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
		System.out.println(req.getLanguage());
		if(req.getLanguage().equals("Estonian")) {
			subject = messageSource.getMessage("email.reminder.subject", null, new Locale("et"));
			text = messageSource.getMessage("email.reminder.text", null, new Locale("et"));
		}
		else {
			subject = messageSource.getMessage("email.reminder.subject", null, new Locale("en"));
			text = messageSource.getMessage("email.reminder.text", null, new Locale("en"));
		}
		send(subject, text + "\n" + req.getMeetingDate().toString() + "\n" + req.getMeetingPlace(),
				req.getEmail());
	}

	@Override
	public void sendFeedbackRequest(ConsultationRequest req) {
		String subject;
		String text;
		if(req.getLanguage().equals("Estonian")) {
			subject = messageSource.getMessage("email.feedback.subject", null, new Locale("et"));
			text = messageSource.getMessage("email.feedback.text", null, new Locale("et"));
		}
		else {
			subject = messageSource.getMessage("email.feedback.subject", null, new Locale("en"));
			text = messageSource.getMessage("email.feedback.text", null, new Locale("en"));
		}
		String url = req.generateFeedbackUrl(getBaseUrl());
		send(subject, text + "\n" + url, req.getEmail());
	}

	private void send(String subject, String text, String to) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom("no-reply-avok@ut.ee");
			message.setTo(to);
			message.setText(text);
			message.setSubject(subject);
			sender.send(message);
		} catch (Exception e) {
			// TODO: handle exception
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
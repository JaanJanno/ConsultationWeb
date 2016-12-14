package ee.avok.consultation.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
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

	private final JavaMailSenderImpl sender;

	public EmailServiceImpl() {
		super();
		sender = new JavaMailSenderImpl();
		sender.setHost("mailhost.ut.ee");
		sender.setProtocol("smtps");
	}

	@Override
	public void sendAccountCreation(AccountPending account) {
		send("AVOK account creation", getBaseUrl() + account.generateUrl(), account.getEmail());
	}

	@Override
	public void sendReminder(ConsultationRequest req) {
		send("AVOK consultation reminder", req.getMeetingDate().toString() + "\n" + req.getMeetingPlace(),
				req.getEmail());
	}

	@Override
	public void sendFeedbackRequest(ConsultationRequest req) {
		String url = req.generateFeedbackUrl(getBaseUrl());
		send("AVOK consultation feedback", url, req.getEmail());
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
			return "localhost:8080";
		} else {
			return "avok.herokuapp.com";
		}
	}

}
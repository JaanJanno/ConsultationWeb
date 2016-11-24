package ee.avok.consultation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import ee.avok.consultation.domain.model.ConsultationRequest;

public class EmailServiceImpl implements EmailService {

	@Autowired
	Environment env;

	private static final JavaMailSenderImpl sender;

	static {
		sender = new JavaMailSenderImpl();
		sender.setHost("mail.ut.ee");
	}

	@Override
	public void sendReminder(ConsultationRequest req) {
		send("AVOK consultation reminder", req.getMeetingDate().toString() + " " + req.getMeetingPlace(),
				req.getEmail());
	}

	@Override
	public void sendFeedbackRequest(ConsultationRequest req) {
		String url = req.generateFeedbackUrl(env);
		send("AVOK consultation feedback", url, req.getEmail());
	}

	private static void send(String subject, String text, String to) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("noreply_avok@ut.ee");
		message.setTo(to);
		message.setText(text);
		message.setSubject(subject);
		sender.send(message);
	}

}
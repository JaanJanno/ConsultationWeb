package ee.avok.consultation.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import ee.avok.consultation.domain.model.ConsultationRequest;

@Service
public class EmailServiceImpl implements EmailService {

	private static final JavaMailSenderImpl sender;

	private static String baseUrl = "localhost:8080";

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
		String url = req.generateFeedbackUrl(baseUrl);
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
	
	public static void setBaseUrl(String url) {
		baseUrl = url;
	}

}
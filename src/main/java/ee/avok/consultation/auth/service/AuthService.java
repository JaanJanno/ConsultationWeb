package ee.avok.consultation.auth.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.avok.consultation.auth.domain.model.Account;
import ee.avok.consultation.auth.domain.model.Role;
import ee.avok.consultation.auth.domain.model.UnauthorizedException;
import ee.avok.consultation.auth.domain.repository.AccountRepository;

@Service
public class AuthService {

	@Autowired
	AccountRepository accountRepo;

	Map<String, Account> sessionMap = new HashMap<>();

	public Account authenticateUser(String username, String password, HttpServletResponse response)
			throws UnauthorizedException {

		Account user = accountRepo.findByUsername(username.toLowerCase());

		if (user == null) {
			throw new UnauthorizedException("Invalid username.");
		}

		if (!user.getPassword().equals(password)) {
			throw new UnauthorizedException("Invalid password.");
		}

		generateSession(user, response);
		return user;
	}

	private void generateSession(Account user, HttpServletResponse response) {
		String rand;
		do {
			rand = UUID.randomUUID().toString();
		} while (sessionMap.containsKey(rand));
		sessionMap.put(rand, user);
		response.addCookie(new Cookie("session", rand));
	}

	public Account authenticateRequestForRole(String session, Role role) throws UnauthorizedException {

		if (!sessionMap.containsKey(session)) {
			throw new UnauthorizedException("Unauthorized account.");
		}

		Account user = sessionMap.get(session);

		if (user.getRole() == Role.ADMINISTRATOR) {
			return user;
		}

		if (role == user.getRole()) {
			return user;		
		} else {
			throw new UnauthorizedException("Unauthorized account.");
		}

	}

	public void endSession(String session) {
		if (sessionMap.containsKey(session)) {
			sessionMap.remove(session);
		}
	}

}

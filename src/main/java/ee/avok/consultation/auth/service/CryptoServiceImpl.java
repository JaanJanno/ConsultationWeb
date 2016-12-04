package ee.avok.consultation.auth.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CryptoServiceImpl implements CryptoService {
	
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	@Override
	public String encrypt(String s) {
		return encoder.encode(s);
	}

	@Override
	public boolean verify(String password, String hashedPassword) {
		return encoder.matches(password, hashedPassword);
	}

}
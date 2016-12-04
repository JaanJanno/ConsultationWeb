package ee.avok.consultation.auth.service;

public interface CryptoService {
	
	public String encrypt(String s);
	
	public boolean verify(String password, String hashedPassword);

}
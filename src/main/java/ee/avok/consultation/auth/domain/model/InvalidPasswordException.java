package ee.avok.consultation.auth.domain.model;

public class InvalidPasswordException extends Exception {

	private static final long serialVersionUID = -746625424775688053L;

	public InvalidPasswordException(String msg) {
		super(msg);
	}

}

package ee.avok.consultation.auth.domain.model;

public class UnauthorizedException extends Exception {

	private static final long serialVersionUID = -4200901140591116087L;

	public UnauthorizedException(String msg) {
		super(msg);
	}

}

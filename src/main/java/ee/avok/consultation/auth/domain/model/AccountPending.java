package ee.avok.consultation.auth.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class AccountPending {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column
	private String email;
	@Column
	private String uid; // SECRET field for authorization.
	
	public String generateUrl() {
		return "/account/create/" + Integer.toString(id) + "/" + uid;
	}

}
package ee.avok.consultation.auth.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(unique = true)
	private String username;
	@Column
	private String name;
	@Column
	private String email;
	@Enumerated(EnumType.STRING)
	private Role role;
	@Column
	private String password;

	public Account() {
	}

	public Account(int id) {
		this.id = id;
	}

}

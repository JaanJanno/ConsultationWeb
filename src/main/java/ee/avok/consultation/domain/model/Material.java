package ee.avok.consultation.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Material {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column
	private String name;
	@Column
	private String link;
	
	public String generateURL() {
		if (link.matches(".*://.*")) {
			return link;
		}
		else {
			return "http://" + link;
		}
	}
}

package ee.avok.consultation.dto;

import lombok.Data;

@Data
public class AccountDTO {
	
	public AccountDTO(int id){
		this.id=id;
	}
	int id;
	String email;
	String oldPassword;
	String newPassword;
}

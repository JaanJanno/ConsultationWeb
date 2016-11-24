package ee.avok.consultation.dto;

import lombok.Data;

@Data

public class StatisticsDTO {

	private int received;
	private int accepted;
	private int scheduled;
	private int completed;
	
	public StatisticsDTO(int received,int accepted,int scheduled,int completed){
		this.received=received;
		this.accepted=accepted;
		this.scheduled=scheduled;
		this.completed=completed;
	}
	public StatisticsDTO(){}
}

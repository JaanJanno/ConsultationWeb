package ee.avok.consultation.dto;

import lombok.Data;

@Data
public class StatisticsDTO {

	private int received;
	private int accepted;
	private int scheduled;
	private int completed;
}

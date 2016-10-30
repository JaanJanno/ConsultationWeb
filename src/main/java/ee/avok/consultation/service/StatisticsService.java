package ee.avok.consultation.service;

import ee.avok.consultation.domain.model.ConsultationRequest;
import ee.avok.consultation.dto.StatisticsDTO;

public interface StatisticsService {

	/**
	 * Generates statistics about consultations.
	 * 
	 * @param time
	 *            Options are today, week, month, all
	 * @return {@link StatisticsDTO}
	 * 
	 */
	StatisticsDTO getStatistics(String time);

	/**
	 * Generates statistics about all {@link ConsultationRequest}. Counts a date
	 * if it is not null.
	 * 
	 * @return {@link StatisticsDTO}
	 */
	StatisticsDTO statsAll();
}

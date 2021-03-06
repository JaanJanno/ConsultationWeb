package ee.avok.consultation.service;

import java.util.Date;
import java.util.List;

import ee.avok.consultation.auth.domain.model.Account;
import ee.avok.consultation.domain.model.ConsultationRequest;
import ee.avok.consultation.dto.CalendarDTO;
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

	/**
	 * Finds all {@link ConsultationRequest} where the meeting date has been
	 * set.
	 * 
	 */
	List<CalendarDTO> getAllMeetings();

	/**
	 * Finds all {@link ConsultationRequest} where the meeting date has been set
	 * for a certain user.
	 * 
	 * @param userId
	 *            {@link Account#getId()}
	 */
	List<CalendarDTO> getMeetings(int userId);

	/**
	 * Generates statistics about all {@link ConsultationRequest}. Counts from date to current date.
	 *  
	 * @param date {@link Date} to count from.
	 * @return {@link StatisticsDTO} dto
	 */
	StatisticsDTO findRequestByDate(Date date);
	
	 
}

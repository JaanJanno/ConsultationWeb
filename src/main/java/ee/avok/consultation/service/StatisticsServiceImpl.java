package ee.avok.consultation.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.avok.consultation.domain.model.ConsultationRequest;
import ee.avok.consultation.domain.model.ConsultationStatus;
import ee.avok.consultation.domain.repository.ConsultationRequestRepository;
import ee.avok.consultation.dto.CalendarDTO;
import ee.avok.consultation.dto.StatisticsDTO;

@Service
public class StatisticsServiceImpl implements StatisticsService {

	private static Logger LOG = LoggerFactory.getLogger(StatisticsServiceImpl.class);

	@Autowired
	ConsultationRequestRepository conReqRepo;
	ConsultationStatus status;

	@Override
	public StatisticsDTO getStatistics(String time) {
		StatisticsDTO stats;
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		switch (time) {
		case "all":
			LOG.info("All records are counted");
			stats = statsAll();
			break;
		case "today":
			Date todayDate = c.getTime();
			LOG.info("Date of today is: " + todayDate);
			stats = findRequestByDate(todayDate);
			break;
		case "week":
			c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			Date lastMondayDate = c.getTime();
			LOG.info("Date of lastMondayDate is: " + lastMondayDate);
			stats = findRequestByDate(lastMondayDate);
			break;
		case "month":
			c.set(Calendar.DAY_OF_MONTH, 1);
			Date lastMonthDate = c.getTime();
			LOG.info("Date of lastMonthDate is: " + lastMonthDate);
			stats = findRequestByDate(lastMonthDate);
			break;
		default:
			LOG.info("Time value {} unrecongized, returning all", time);
			stats = statsAll();
			break;
		}
		return stats;
	}

	@Override
	public StatisticsDTO statsAll() {
		StatisticsDTO stats = new StatisticsDTO();
		stats.setReceived(conReqRepo.countAllReceived());
		stats.setAccepted(conReqRepo.countAllAccepted());
		stats.setScheduled(conReqRepo.countAllScheduled());
		stats.setCompleted(conReqRepo.countAllCompleted());
		return stats;
	}

	@Override
	public List<CalendarDTO> getAllMeetings() {
		List<ConsultationRequest> cons = conReqRepo.findByMeetingDateNotNull();
		return createCalendarDTOs(cons);
	}

	@Override
	public List<CalendarDTO> getMeetings(int userId) {
		List<ConsultationRequest> cons = conReqRepo.findByConsultantIdAndMeetingDateNotNull(userId);
		return createCalendarDTOs(cons);
	}

	private List<CalendarDTO> createCalendarDTOs(List<ConsultationRequest> cons) {
		List<CalendarDTO> events = new ArrayList<>();

		for (ConsultationRequest con : cons) {
			int id = con.getId();
			String title = "Consultation: " + id;
			Date date = con.getMeetingDate();
			String url = "/requests/detail/" + id;

			events.add(new CalendarDTO(id, title, date, url));
		}

		return events;
	}

	@Override
	public StatisticsDTO findRequestByDate(Date Date) {
		int recivedRequest = conReqRepo.countReceivedByDate(Date);
		int acceptedRequest = conReqRepo.countAcceptedByDate(Date);
		int scheduledRequest = conReqRepo.countScheduledByDate(Date);
		int completedRequest = conReqRepo.countCompletedByDate(Date);
		return new StatisticsDTO(recivedRequest, acceptedRequest, scheduledRequest, completedRequest);
	}
}

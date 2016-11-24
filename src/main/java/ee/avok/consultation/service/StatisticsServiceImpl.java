package ee.avok.consultation.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.avok.consultation.domain.model.ConsultationRequest;
import ee.avok.consultation.domain.model.ConsultationStatus;
import ee.avok.consultation.domain.repository.ConsultationRequestRepository;
import ee.avok.consultation.dto.CalendarDTO;
import ee.avok.consultation.dto.StatisticsDTO;

@Service
public class StatisticsServiceImpl implements StatisticsService {

	@Autowired
	ConsultationRequestRepository conReqRepo;
	ConsultationStatus status;

	@Override
	public StatisticsDTO getStatistics(String time) {
		StatisticsDTO stats = new StatisticsDTO();
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		switch (time) {
			case "all":
				System.out.println("All records are counted" );
				return statsAll(stats);
			case "Daily":
				Date todayDate =c.getTime();
				System.out.println("Date of today is: "+todayDate);
				return findRequestByDate(todayDate);
								
			case "Weekly":
				System.out.println("Weekly");
				c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
				Date lastMondayDate =c.getTime();
				System.out.println("Date of lastMondayDate is: "+lastMondayDate);
				return findRequestByDate(lastMondayDate);
				
			case "Monthly":
				c.set(Calendar.DAY_OF_MONTH, 1);
				Date lastMonthDate =c.getTime();
				System.out.println("Monthly");
				System.out.println("Date of lastMonthDate is: "+lastMonthDate);
				return findRequestByDate(lastMonthDate);
	
		}
		return stats;
	}

	@Override
	public StatisticsDTO statsAll() {
		return statsAll(new StatisticsDTO());  //why do u pass an empty object?
	}

	private StatisticsDTO statsAll(StatisticsDTO stats) { 
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
		
		int recivedRequest= conReqRepo.countReceivedByDate(Date);
		int acceptedRequest= conReqRepo.countAcceptedByDate(Date);
		int scheduledRequest= conReqRepo.countScheduledByDate(Date);
		int completedRequest= conReqRepo.countCompletedByDate(Date);
		return new StatisticsDTO(recivedRequest,acceptedRequest,scheduledRequest,completedRequest);
	}
}

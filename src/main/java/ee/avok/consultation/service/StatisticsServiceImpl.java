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
		switch (time) {
		case "all":
			statsAll(stats);
			break;

		default:
			break;
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


	public  StatisticsDTO findRequestByPeriod(String period) {
		
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		
		switch(period){
			case "Daily":
				String todayDate = dateFormatter(c.getTime());
				return getStatistic(todayDate,"Daily");
								
			case "Weekly":
				System.out.println("Weekly");
				c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
				String lastMondayDate = dateFormatter(c.getTime());		
				return getStatistic(lastMondayDate,"Weekly");
				
			case "Monthly":
				c.set(Calendar.DAY_OF_MONTH, 1);
				String lastMonthDate = dateFormatter(c.getTime());
				System.out.println("Monthly");
				return getStatistic(lastMonthDate,"Monthly");
				
			case "Total":
				System.out.println("Total"); 
				return statsAll();
				
			}
		return null;
	}
	private StatisticsDTO getStatistic(String Date, String period) {
		
		int receivedRequests=countRecievedRequests(status.RECEIVED,Date,period);
		int acceptedRequests=countAcceptedRequests(status.ACCEPTED,Date,period);
		int scheduleRequests=countScheduleRequests(status.SCHEDULED,Date,period);
		int completedRequests=countCompletedRequests(status.COMPLETED,Date,period);
		StatisticsDTO dailyStatistic=new StatisticsDTO(receivedRequests,acceptedRequests,scheduleRequests,completedRequests);
		return dailyStatistic;
	}

	private int countCompletedRequests(ConsultationStatus completed, String startDate, String period) {
		List<ConsultationRequest> allRequestsByStatus = conReqRepo.findByStatus(completed);
		int count=0;
		for(int i=0;i<allRequestsByStatus.size();i++){
			String date=dateFormatter(allRequestsByStatus.get(i).getCompletedDate());
			if(countBasedOnPeriod(date,startDate,period)) count++;
				
			System.out.println("Date: "+date+" startDate is: "+startDate);
		}
		return count;
	}

	private int countScheduleRequests(ConsultationStatus scheduled, String startDate, String period) {
		List<ConsultationRequest> allRequestsByStatus = conReqRepo.findByStatus(scheduled);
		int count=0;
		for(int i=0;i<allRequestsByStatus.size();i++){
			String date=dateFormatter(allRequestsByStatus.get(i).getScheduledDate());
			if(countBasedOnPeriod(date,startDate,period)) count++;
				
			System.out.println("Date: "+date+" startDate is: "+startDate);
		}
		return count;
	}

	private int countAcceptedRequests(ConsultationStatus accepted, String startDate, String period) {
		List<ConsultationRequest> allRequestsByStatus = conReqRepo.findByStatus(accepted);
		int count=0;
		for(int i=0;i<allRequestsByStatus.size();i++){
			String date=dateFormatter(allRequestsByStatus.get(i).getAcceptedDate());
			if(countBasedOnPeriod(date,startDate,period)) count++;
				
			System.out.println("Date: "+date+" startDate is: "+startDate);
		}
		return count;
	}

	private int countRecievedRequests(ConsultationStatus status, String startDate, String period) {
		List<ConsultationRequest> allRequestsByStatus = conReqRepo.findByStatus(status);
		int count=0;
		for(int i=0;i<allRequestsByStatus.size();i++){
			String date=dateFormatter(allRequestsByStatus.get(i).getReceivedDate());
			if(countBasedOnPeriod(date,startDate,period)) count++;
				
			System.out.println("Date: "+date+" startDate is: "+startDate);
		}
		return count;
	}

	private boolean countBasedOnPeriod(String date, String startDate, String period) {
		switch(period){
		case "Daily":
			return date.equals(startDate);
		case "Weekly":
			System.out.println("Condition is: "+date.compareTo(startDate ));
			return date.compareTo(startDate )>0 ? true:false;
		case "Monthly":
			return date.compareTo(startDate )>0 ? true:false;
		default:
			return true;
		}
	}

	private String dateFormatter(Date Date) {
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDateOfRequest = format1.format(Date);
		return formattedDateOfRequest;
	}
/*
	private String todayDate() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = format1.format(c.getTime());
		return formattedDate;
		
	}*/

	public List<ConsultationRequest> findRequestsByStatus(ConsultationStatus status){
		return conReqRepo.findByStatus(status);
	}
}

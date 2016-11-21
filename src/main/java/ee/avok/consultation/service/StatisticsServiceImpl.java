package ee.avok.consultation.service;

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

	@Override
	public int countRequestByStatusAndPeriod(ConsultationStatus status, String period) {
		List<ConsultationRequest> allRequestsByStatus = conReqRepo.findByStatus(status);
		int count=findRequestsByPeriod(allRequestsByStatus,period);
		return count;
	}

	private int findRequestsByPeriod(List<ConsultationRequest> allRequestsByStatus,String period) {
		
		Calendar c = Calendar.getInstance();
	
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		int count=0;
		switch(period){
			case "Daily":
				System.out.println("Date " + c.getTime());
				for(int i=0;i<allRequestsByStatus.size();i++)
					if(allRequestsByStatus.get(i).getReceivedDate().after(c.getTime()))
						count++;
				System.out.println("Daily");
				break;
			case "Weekly":
				c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
				for(int i=0;i<allRequestsByStatus.size();i++)
					if(allRequestsByStatus.get(i).getReceivedDate().after(c.getTime()))
						count++;
				System.out.println("Weekly");
				break;	
			case "Monthly":
				c.set(Calendar.DAY_OF_MONTH, 1);
				for(int i=0;i<allRequestsByStatus.size();i++)
					if(allRequestsByStatus.get(i).getReceivedDate().after(c.getTime()))
						count++;
				System.out.println("Monthly");
				break;
			case "Total":
				count=allRequestsByStatus.size();
				System.out.println("Total");
				break;
			}
		System.out.println("Count is:   "+count);
		return count;
	}

}

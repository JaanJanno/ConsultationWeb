package ee.avok.consultation.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.avok.consultation.domain.model.ConsultationRequest;
import ee.avok.consultation.domain.repository.ConsultationRequestRepository;
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
		return statsAll(new StatisticsDTO());
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

}

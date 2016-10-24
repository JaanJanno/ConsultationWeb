package ee.avok.consultation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}

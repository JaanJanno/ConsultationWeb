package ee.avok.consultation.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.avok.consultation.domain.model.ConsultationRequest;
import ee.avok.consultation.domain.model.ConsultationStatus;
import ee.avok.consultation.dto.CsvBean;

@Service
public class CsvServiceImpl implements CsvService {

	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
	@Autowired
	ConsultationService conServ;

	@Override
	public List<CsvBean> createBeans() {
		List<ConsultationRequest> cons = conServ.findByStatus(ConsultationStatus.COMPLETED);
		List<CsvBean> beans = new ArrayList<>();
		for (ConsultationRequest c : cons) {
			String date = df.format(c.getMeetingDate());
			String time = tf.format(c.getMeetingDate());

			beans.add(new CsvBean(c.getName(), c.getConsultant().getName(), date, time, c.getLanguage(),
					c.getProgramme(), c.getDegree(), c.getDepartment(), c.getTextType(), c.getComments()));
		}
		return beans;
	}

}

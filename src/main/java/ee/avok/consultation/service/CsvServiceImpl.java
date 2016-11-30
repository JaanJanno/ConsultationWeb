package ee.avok.consultation.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import ee.avok.consultation.domain.model.ConsultationRequest;
import ee.avok.consultation.domain.model.ConsultationStatus;
import ee.avok.consultation.dto.CsvBean;

@Service
public class CsvServiceImpl implements CsvService {

	private static Logger LOG = LoggerFactory.getLogger(CsvServiceImpl.class);

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

	@Override
	public void writeCSV(PrintWriter printWriter) throws IOException {
		ICsvBeanWriter csvWriter = new CsvBeanWriter(printWriter, CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

		// Header will go to header
		String[] header = { "Student name", "Consultant name", "Consultation date", "Consultation time", "Language",
				"Programme", "Degree", "Department", "Text type", "Comments" };
		// Mapping must match header positions
		String[] dataMapping = { "studentname", "consultantname", "Date", "Time", "Language", "Programme", "Degree",
				"Department", "TextType", "Comments" };

		csvWriter.writeHeader(header);

		List<CsvBean> beans = createBeans();
		LOG.info("Writing {} items to CSV", beans.size());
		for (CsvBean c : beans) {
			csvWriter.write(c, dataMapping);
		}

		csvWriter.close();

	}

}

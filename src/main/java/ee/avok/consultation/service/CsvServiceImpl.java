package ee.avok.consultation.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import ee.avok.consultation.domain.model.ConsultantFeedback;
import ee.avok.consultation.domain.model.ConsultationRequest;
import ee.avok.consultation.domain.model.ConsultationStatus;
import ee.avok.consultation.domain.model.StudentFeedback;
import ee.avok.consultation.dto.CsvBean;

@Service
public class CsvServiceImpl implements CsvService {

	public static final long STUDENT_TIMEOUT_PERIOD = 2592000000l;
	private static Logger LOG = LoggerFactory.getLogger(CsvServiceImpl.class);

	SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
	SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
	@Autowired
	ConsultationService conServ;

	@Override
	public List<CsvBean> createBeans() {
		List<ConsultationRequest> cons = conServ.findByStatus(ConsultationStatus.COMPLETED);
		List<CsvBean> beans = new ArrayList<>();
		for (ConsultationRequest c : cons) {
			// Dont work if Have no feedback and is less then month old
			if (!c.hasStudentFeedback() && !studentFeedbackTimeout(c))
				continue;
			CsvBean bean = new CsvBean();			
			addConsultation(bean, c);
			addConsultantFeedback(bean, c.getConsultantFeedback());
			addStudentFeedback(bean, c.getStudentFeedback());
			beans.add(bean);
		}
		return beans;
	}

	@Override
	public void writeCSV(PrintWriter printWriter) throws IOException {
		ICsvBeanWriter csvWriter = new CsvBeanWriter(printWriter, CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);

		// Header will go to header
		// Header can have spaces, mapping cannot
		String[] header = { "Student name", "Consultant name", "Consultation date", "Consultation time", "Language",
				"Programme", "Degree", "Department", "Text type", "Comments", "Discussion element",
				"Discussed material", "Covered issues", "Summary", "Further consultation", "First consultation?",
				"Useful?", "Provided support?", "Coming back?", "Student comments" };
		// Mapping must match header positions
		String[] dataMapping = { "studentname", "consultantname", "Date", "Time", "Language", "Programme", "Degree",
				"Department", "TextType", "Comments", "discussionElement", "discussedMaterial", "coveredIssues",
				"summary", "suggestedNewConsultation", "firstConsultation", "useful", "providedSupport", "comingBack",
				"studentComments" };

		csvWriter.writeHeader(header);

		List<CsvBean> beans = createBeans();
		LOG.info("Writing {} items to CSV", beans.size());
		for (CsvBean c : beans) {
			csvWriter.write(c, dataMapping);
		}

		csvWriter.close();

	}

	/**
	 * Returns true if the meeting took place more time ago than
	 * {@value #STUDENT_TIMEOUT_PERIOD}.
	 * 
	 * @param con
	 * @return
	 */
	private boolean studentFeedbackTimeout(ConsultationRequest con) {
		Date now = new Date();
		if (now.getTime() - con.getMeetingDate().getTime() > STUDENT_TIMEOUT_PERIOD) {
			return true;
		} else
			return false;

	}

	private void addConsultation(CsvBean bean, ConsultationRequest c) {
		String date = df.format(c.getMeetingDate());
		String time = tf.format(c.getMeetingDate());

		bean.setStudentName(c.getName());
		bean.setConsultantName(c.getConsultant().getName());
		bean.setDate(date);
		bean.setTime(time);
		bean.setLanguage(c.getLanguage());
		bean.setProgramme(c.getProgramme());
		bean.setDegree(c.getDegree());
		bean.setDepartment(c.getDepartment());
		bean.setTextType(c.getTextType());
		bean.setComments(c.getComments());

	}

	private void addConsultantFeedback(CsvBean bean, ConsultantFeedback f) {
		bean.setDiscussedMaterial(f.getDiscussedMaterial());
		bean.setCoveredIssues(f.getCoveredIssues());
		bean.setDiscussionElement(f.getDiscussionElement());
		bean.setSummary(f.getSummary());
		bean.setSuggestedNewConsultation(f.getSuggestedNewConsultation());
	}

	private void addStudentFeedback(CsvBean bean, StudentFeedback s) {
		bean.setFirstConsultation(s.getFirstConsultation());
		bean.setUseful(s.getUseful());
		bean.setProvidedSupport(s.getProvidedSupport());
		bean.setComingBack(s.getComingBack());
		bean.setStudentComments(s.getComments());
	}

}

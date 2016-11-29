package ee.avok.consultation.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import ee.avok.consultation.dto.CsvBean;

public interface CsvService {

	List<CsvBean> createBeans();

	void writeCSV(PrintWriter printWriter) throws IOException;
}

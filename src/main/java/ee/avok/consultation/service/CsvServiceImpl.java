package ee.avok.consultation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.avok.consultation.dto.CsvBean;

@Service
public class CsvServiceImpl implements CsvService {

	@Autowired
	ConsultationService conServ;
	
	@Override
	public List<CsvBean> createBeans() {
		// TODO Auto-generated method stub
		return null;
	}

}

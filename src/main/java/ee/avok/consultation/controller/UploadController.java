package ee.avok.consultation.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import ee.avok.consultation.domain.model.ConsultationRequest;
import ee.avok.consultation.domain.model.Upload;
import ee.avok.consultation.service.ConsultationService;
import ee.avok.consultation.service.UploadService;

@Controller
public class UploadController {
	
	private static Logger LOG = LoggerFactory.getLogger(UploadController.class);
	@Autowired
	UploadService upServ;

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/uploads/{id}/{filename:.+}")
	public HttpEntity<byte[]> ConsultationRequestDetail(@PathVariable int id, @PathVariable("filename") String fileName, HttpServletResponse response) {	
		Upload upload = upServ.retrieveUpload(id);
		
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	    response.setHeader("Content-Disposition", "attachment; filename=" + upload.getFilename());

		return new HttpEntity<byte[]>(upload.getUpload(), headers);
		
	}

}

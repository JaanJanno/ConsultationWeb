package ee.avok.consultation.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ee.avok.consultation.auth.domain.model.Role;
import ee.avok.consultation.auth.domain.model.UnauthorizedException;
import ee.avok.consultation.auth.service.AuthService;
import ee.avok.consultation.domain.model.Upload;
import ee.avok.consultation.service.UploadService;

@Controller
public class UploadController {

	@Autowired
	UploadService upServ;

	@Autowired
	AuthService authServ;

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/uploads/{id}/{filename:.+}")
	public HttpEntity<byte[]> retrieveUpload(@PathVariable int id, @PathVariable("filename") String fileName,
			HttpServletResponse response, @CookieValue(value = "session", defaultValue = "none") String session)
			throws UnauthorizedException {
		authServ.authenticateRequestForRole(session, Role.CONSULTANT);

		Upload upload = upServ.retrieveUpload(id);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		response.setHeader("Content-Disposition", "attachment; filename=" + upload.getFilename());

		return new HttpEntity<byte[]>(upload.getUpload(), headers);

	}

	@ExceptionHandler(UnauthorizedException.class)
	public String handleNotFound(Exception exc) {
		return "redirect:" + "/login";
	}

}

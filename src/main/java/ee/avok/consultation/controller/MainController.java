package ee.avok.consultation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

	@RequestMapping("/")
	public String indexPage(Model model) {
		return "index";
	}

	@RequestMapping("/empty")
	public String emptyPage(Model model) {
		return "empty";
	}

}
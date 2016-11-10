package ee.avok.consultation.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ee.avok.consultation.auth.domain.model.Role;
import ee.avok.consultation.auth.domain.model.UnauthorizedException;
import ee.avok.consultation.auth.service.AuthService;
import ee.avok.consultation.domain.model.Material;
import ee.avok.consultation.service.MaterialService;

/**
 * CRUD methods for {@link Material}
 * 
 * @author Tõnis Kasekamp
 *
 */
@Controller
public class MaterialController {
	private static final Logger LOG = LoggerFactory.getLogger(MaterialController.class);

	@Autowired
	MaterialService matServ;
	@Autowired
	AuthService authServ;

	/**
	 * HTML list of all {@link Material}.
	 * 
	 * @return "materials/materials"
	 */
	@RequestMapping(value = "/materials", method = RequestMethod.GET)
	public String materials(Model model, @CookieValue(value = "session", defaultValue = "none") String session)
			throws UnauthorizedException {
		authServ.authenticateAndAddToModel(model, session, Role.CONSULTANT);
		model.addAttribute("materials", matServ.findAll());
		return "shared-between-consultant-and-admin/materials";
	}

	/**
	 * Form to create a new {@link Material}.
	 * 
	 * @return "materials/create"
	 */
	@RequestMapping(value = "/materials/create", method = RequestMethod.GET)
	public String createMaterial(Model model, @CookieValue(value = "session", defaultValue = "none") String session)
			throws UnauthorizedException {
		authServ.authenticateAndAddToModel(model, session, Role.ADMINISTRATOR);
		model.addAttribute("material", new Material());
		return "admin/materials/create";
	}

	/**
	 * Saves the {@link Material} sent to DB.
	 * 
	 * @param loc
	 *            {@link Material}
	 * @return "redirect:/materials"
	 */
	@RequestMapping(value = "/materials/create", method = RequestMethod.POST)
	public String createMaterial(@ModelAttribute Material mat, Model model,
			@CookieValue(value = "session", defaultValue = "none") String session) throws UnauthorizedException {
		authServ.authenticateAndAddToModel(model, session, Role.ADMINISTRATOR);
		LOG.info("Saving Material {}", mat.getName());
		matServ.save(mat);
		return "redirect:/materials";
	}

	/**
	 * Populates the create view with the {@link Material} specified for
	 * editing.
	 * 
	 * @param id
	 *            {@link Material#getId()}
	 * @return "materials/create"
	 */
	@RequestMapping(value = "/materials/{id}/edit", method = RequestMethod.GET)
	public String editMaterial(@PathVariable int id, Model model,
			@CookieValue(value = "session", defaultValue = "none") String session) throws UnauthorizedException {
		authServ.authenticateAndAddToModel(model, session, Role.ADMINISTRATOR);
		model.addAttribute("Material", matServ.findOne(id));
		return "admin/materials/create";
	}

	/**
	 * Saves the {@link Material} sent to DB.
	 * 
	 * @param id
	 *            {@link Material#getId()}
	 * @param loc
	 *            {@link Material}
	 * @return "redirect:/materials"
	 */
	@RequestMapping(value = "/materials/{id}/edit", method = RequestMethod.POST)
	public String editMaterial(@PathVariable int id, @ModelAttribute Material mat, Model model,
			@CookieValue(value = "session", defaultValue = "none") String session) throws UnauthorizedException {
		authServ.authenticateAndAddToModel(model, session, Role.ADMINISTRATOR);
		LOG.info("Editing Material {}", mat.getName());
		matServ.edit(mat);
		return "materials";
	}

	/**
	 * Deletes the {@link Material} from DB.
	 * 
	 * @param id
	 *            {@link Material#getId()}
	 * @return "redirect:/materials"
	 */
	@RequestMapping(value = "/materials/{id}/delete", method = RequestMethod.GET)
	public String deleteMaterial(@PathVariable int id, Model model,
			@CookieValue(value = "session", defaultValue = "none") String session) throws UnauthorizedException {
		authServ.authenticateAndAddToModel(model, session, Role.ADMINISTRATOR);
		LOG.info("Deleting Material {}", id);
		matServ.delete(id);
		return "redirect:/materials";
	}

}

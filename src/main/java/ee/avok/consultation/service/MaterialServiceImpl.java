package ee.avok.consultation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.avok.consultation.domain.model.Material;
import ee.avok.consultation.domain.repository.MaterialRepository;

@Service
public class MaterialServiceImpl implements MaterialService {
	@Autowired
	MaterialRepository matRepo;

	@Override
	public void delete(int id) {
		matRepo.delete(id);

	}

	@Override
	public void edit(Material material) {
		save(material);

	}

	@Override
	public Material findOne(int id) {
		return matRepo.findOne(id);
	}

	@Override
	public List<Material> findAll() {
		return (List<Material>) matRepo.findAll();
	}

	@Override
	public void save(Material material) {
		matRepo.save(material);

	}

}

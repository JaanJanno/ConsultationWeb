package ee.avok.consultation.service;

import java.util.List;

import ee.avok.consultation.domain.model.Material;

public interface MaterialService {
	void delete(int id);

	void edit(Material material);

	Material findOne(int id);

	List<Material> findAll();

	void save(Material material);
}

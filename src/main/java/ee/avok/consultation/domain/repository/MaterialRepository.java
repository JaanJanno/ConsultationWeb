package ee.avok.consultation.domain.repository;

import org.springframework.data.repository.CrudRepository;

import ee.avok.consultation.domain.model.Material;

public interface MaterialRepository extends CrudRepository<Material, Integer> {

}

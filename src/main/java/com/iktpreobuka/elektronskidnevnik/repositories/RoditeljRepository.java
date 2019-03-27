package com.iktpreobuka.elektronskidnevnik.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.elektronskidnevnik.entities.RoditeljEntity;

public interface RoditeljRepository extends CrudRepository<RoditeljEntity, Integer> {

	List<RoditeljEntity> findByImeRoditelja(String imeRoditelja);

}

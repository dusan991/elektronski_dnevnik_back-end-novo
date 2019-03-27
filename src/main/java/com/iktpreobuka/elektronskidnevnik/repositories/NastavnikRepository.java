package com.iktpreobuka.elektronskidnevnik.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.elektronskidnevnik.entities.NastavnikEntity;

public interface NastavnikRepository extends CrudRepository<NastavnikEntity, Integer> {

	List<NastavnikEntity> findByImeNastavnika(String imeNastavnika);

}

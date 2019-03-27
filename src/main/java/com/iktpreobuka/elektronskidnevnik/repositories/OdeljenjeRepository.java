package com.iktpreobuka.elektronskidnevnik.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.elektronskidnevnik.entities.OdeljenjeEntity;

public interface OdeljenjeRepository extends CrudRepository<OdeljenjeEntity, Integer> {

	List<OdeljenjeEntity> findBySkolskaGodinaRazreda(String skolskaGodina);

}

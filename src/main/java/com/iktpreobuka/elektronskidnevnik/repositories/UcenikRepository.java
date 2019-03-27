package com.iktpreobuka.elektronskidnevnik.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.elektronskidnevnik.entities.OcenaEntity;
import com.iktpreobuka.elektronskidnevnik.entities.OdeljenjeEntity;
import com.iktpreobuka.elektronskidnevnik.entities.RoditeljEntity;
import com.iktpreobuka.elektronskidnevnik.entities.UcenikEntity;

public interface UcenikRepository extends CrudRepository<UcenikEntity, Integer> {

	List<UcenikEntity> findByImeUcenika(String imeUcenika);

	List<UcenikEntity> findByRoditelj(RoditeljEntity roditelj);

	List<UcenikEntity> findByOdeljenjeUcenika(OdeljenjeEntity odeljenjeUcenika);

}

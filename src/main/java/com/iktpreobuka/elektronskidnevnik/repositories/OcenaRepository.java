package com.iktpreobuka.elektronskidnevnik.repositories;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.elektronskidnevnik.entities.OcenaEntity;
import com.iktpreobuka.elektronskidnevnik.entities.OdeljenjePredmetNastavnikEntity;
import com.iktpreobuka.elektronskidnevnik.entities.UcenikEntity;

public interface OcenaRepository extends CrudRepository<OcenaEntity, Integer> {

	List<OcenaEntity> findByUcenik(UcenikEntity ucenik);

	List<OcenaEntity> findByOdeljenjePredmetNastavnik(OdeljenjePredmetNastavnikEntity nekiPredmet);


	List<OcenaEntity> findByDatumOcene(Date datumOcene);

	


}

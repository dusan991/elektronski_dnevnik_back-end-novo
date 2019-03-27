package com.iktpreobuka.elektronskidnevnik.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.iktpreobuka.elektronskidnevnik.entities.NastavnikEntity;
import com.iktpreobuka.elektronskidnevnik.entities.OdeljenjeEntity;
import com.iktpreobuka.elektronskidnevnik.entities.OdeljenjePredmetNastavnikEntity;

public interface OdeljenjePredmetNastavnikRepository extends CrudRepository<OdeljenjePredmetNastavnikEntity, Integer> {

	List<OdeljenjePredmetNastavnikEntity> findByNastavnik(NastavnikEntity nastavnik);

	List<OdeljenjePredmetNastavnikEntity> findByOdeljenje(OdeljenjeEntity odeljenjeUcenika);

}

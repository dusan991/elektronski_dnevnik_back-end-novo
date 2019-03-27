package com.iktpreobuka.elektronskidnevnik.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.elektronskidnevnik.controllers.util.RESTError;
import com.iktpreobuka.elektronskidnevnik.entities.OdeljenjeEntity;
import com.iktpreobuka.elektronskidnevnik.entities.OdeljenjePredmetNastavnikEntity;
import com.iktpreobuka.elektronskidnevnik.entities.PredmetEntity;
import com.iktpreobuka.elektronskidnevnik.repositories.OdeljenjePredmetNastavnikRepository;
import com.iktpreobuka.elektronskidnevnik.repositories.OdeljenjeRepository;
import com.iktpreobuka.elektronskidnevnik.repositories.PredmetRepository;

@RestController
@RequestMapping(path = "/API/V1/predmet")
@CrossOrigin(origins = "http://localhost:4200")
public class PredmetController {

	@Autowired
	public PredmetRepository predmetRepo;

	@Autowired
	public OdeljenjeRepository odeljenjeRepo;

	@Autowired
	private OdeljenjePredmetNastavnikRepository odeljenjePredmetNastavnikRepo;

	// 2.1 dodati predmet
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> dodatiPredmet(@Valid @RequestBody PredmetEntity predmet, BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		List<PredmetEntity> predmeti = (List<PredmetEntity>) predmetRepo.findAll();
		PredmetEntity noviPredmet = new PredmetEntity();
		noviPredmet.setImePredmeta(predmet.getImePredmeta());
		noviPredmet.setFondCasovaNedeljni(predmet.getFondCasovaNedeljni());
		return new ResponseEntity<PredmetEntity>(predmetRepo.save(noviPredmet), HttpStatus.OK);
	}

	private Object createErrorMessage(BindingResult result) {
		// TODO Auto-generated method stub
		return null;
	}

	// 2.2 promeniti predmet po id
	@RequestMapping(value = "/{idPredmeta}", method = RequestMethod.PUT)
	public ResponseEntity<?> izmenitiPredmet(@Valid @RequestBody PredmetEntity predmet,
			@PathVariable Integer idPredmeta, BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		if (predmetRepo.findById(idPredmeta).isPresent() == false) {
			return new ResponseEntity<RESTError>(new RESTError("Predmet za ovaj id ne postoji"), HttpStatus.NOT_FOUND);
		}
		PredmetEntity predmetPromenjen = predmetRepo.findById(idPredmeta).get();
		predmetPromenjen.setImePredmeta(predmet.getImePredmeta());
		predmetPromenjen.setFondCasovaNedeljni(predmet.getFondCasovaNedeljni());
		return new ResponseEntity<PredmetEntity>(predmetRepo.save(predmetPromenjen), HttpStatus.OK);
	}

	// 2.3 izbrisati trazeni predmet po id, ako nije dodeljen
	@RequestMapping(value = "/{idPredmeta}", method = RequestMethod.DELETE)
	public ResponseEntity<?> izbrisatiPredmet(@PathVariable Integer idPredmeta) {

		PredmetEntity predmet = predmetRepo.findById(idPredmeta).get();
		if (predmetRepo.findById(idPredmeta).isPresent() == false) {
			return new ResponseEntity<RESTError>(new RESTError("Predmet za ovaj id ne postoji"), HttpStatus.NOT_FOUND);
		}
		if (predmet.getOdeljenjaPredmeta().size() > 0) {
			return new ResponseEntity<RESTError>(
					new RESTError("Predmet imam svoja odeljenja i nastavnike i ne moze se izbrisati"),
					HttpStatus.NOT_FOUND);
		}
		predmetRepo.deleteById(idPredmeta);
		return new ResponseEntity<PredmetEntity>(predmet, HttpStatus.OK);
	}

	// 2.4 trazeni predmet po id
	@RequestMapping(value = "/{idPredmeta}", method = RequestMethod.GET)
	public ResponseEntity<?> FpronaciPredmet(@PathVariable Integer idPredmeta) {

		if (predmetRepo.findById(idPredmeta).isPresent() == false) {
			return new ResponseEntity<RESTError>(new RESTError("Predmet za ovaj id ne postoji"), HttpStatus.NOT_FOUND);
		}
		PredmetEntity predmet = predmetRepo.findById(idPredmeta).get();
		return new ResponseEntity<PredmetEntity>(predmet, HttpStatus.OK);
	}

	// 2.5 spisak svih predmeta
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> prikazatiPredmete() {
		List<PredmetEntity> predmeti = new ArrayList<>();
		predmeti = (List<PredmetEntity>) predmetRepo.findAll();
		if (predmeti.size() == 0) {
			return new ResponseEntity<RESTError>(new RESTError("Ne postoji ni jedan predmet"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Iterable<PredmetEntity>>(predmetRepo.findAll(), HttpStatus.OK);
	}

	// 2.6 predmeti odeljenja
	@RequestMapping(value = "/predmeti-odeljenja/{idOdeljenja}", method = RequestMethod.GET)
	public ResponseEntity<?> pronaciPredmeteOdeljenja(@PathVariable Integer idOdeljenja) {

		if (odeljenjeRepo.findById(idOdeljenja).isPresent() == false) {
			return new ResponseEntity<RESTError>(new RESTError("Odeljenje za ovaj id ne postoji"),
					HttpStatus.NOT_FOUND);
		}
		OdeljenjeEntity odeljenje = odeljenjeRepo.findById(idOdeljenja).get();
		List<OdeljenjePredmetNastavnikEntity> odeljenePredmetNastavnici = odeljenjePredmetNastavnikRepo
				.findByOdeljenje(odeljenje);
		List<PredmetEntity> predmeti = new ArrayList<>();
		for (OdeljenjePredmetNastavnikEntity nekoOdeljenePredmetNastavnik : odeljenePredmetNastavnici) {
			PredmetEntity predmet = new PredmetEntity();
			predmet = nekoOdeljenePredmetNastavnik.getPredmet();
			predmeti.add(predmet);
		}
		return new ResponseEntity<List<PredmetEntity>>(predmeti, HttpStatus.OK);
	}
}

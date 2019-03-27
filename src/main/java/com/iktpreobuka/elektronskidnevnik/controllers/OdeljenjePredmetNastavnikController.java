package com.iktpreobuka.elektronskidnevnik.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.elektronskidnevnik.controllers.util.RESTError;
import com.iktpreobuka.elektronskidnevnik.entities.NastavnikEntity;
import com.iktpreobuka.elektronskidnevnik.entities.OdeljenjeEntity;
import com.iktpreobuka.elektronskidnevnik.entities.OdeljenjePredmetNastavnikEntity;
import com.iktpreobuka.elektronskidnevnik.entities.PredmetEntity;
import com.iktpreobuka.elektronskidnevnik.repositories.NastavnikRepository;
import com.iktpreobuka.elektronskidnevnik.repositories.OdeljenjePredmetNastavnikRepository;
import com.iktpreobuka.elektronskidnevnik.repositories.OdeljenjeRepository;
import com.iktpreobuka.elektronskidnevnik.repositories.PredmetRepository;

@RestController
@RequestMapping(path = "/API/V1/odeljenje-predmet-nastavnik")
@CrossOrigin(origins = "http://localhost:4200")
public class OdeljenjePredmetNastavnikController {

	@Autowired
	OdeljenjePredmetNastavnikRepository odeljenjePredmetNastavnikRepo;

	@Autowired
	public PredmetRepository predmetRepo;

	@Autowired
	public NastavnikRepository nastavnikRepo;

	@Autowired
	public OdeljenjeRepository odeljenjeRepo;

	// 3.1 dodati vezu odeljenje predmet nastavnik
	@RequestMapping(path = "/odeljenje/{idOdeljenja}/predmet/{idPredmeta}/nastavnik/{idNastavnika}", method = RequestMethod.POST)
	public ResponseEntity<?> dodatiOdeljenjaPredmetNastavnik(@PathVariable Integer idPredmeta,
			@PathVariable Integer idOdeljenja, @PathVariable Integer idNastavnika) {

		if (odeljenjeRepo.findById(idOdeljenja).isPresent() == false) {
			return new ResponseEntity<RESTError>(new RESTError("Predmet za ovaj id ne postoji"), HttpStatus.NOT_FOUND);
		} else if (predmetRepo.findById(idPredmeta).isPresent() == false) {
			return new ResponseEntity<RESTError>(new RESTError("Nastavnik za ovaj id ne postoji"),
					HttpStatus.NOT_FOUND);
		} else if (nastavnikRepo.findById(idNastavnika).isPresent() == false) {
			return new ResponseEntity<RESTError>(new RESTError("Odeljenje za ovaj id ne postoji"),
					HttpStatus.NOT_FOUND);
		}
		OdeljenjePredmetNastavnikEntity noviOdeljenjePredmetNastavnik = new OdeljenjePredmetNastavnikEntity();
		OdeljenjeEntity odeljenje = odeljenjeRepo.findById(idOdeljenja).get();
		PredmetEntity predmet = predmetRepo.findById(idPredmeta).get();
		NastavnikEntity nastavnik = nastavnikRepo.findById(idNastavnika).get();
		noviOdeljenjePredmetNastavnik.setOdeljenje(odeljenje);
		noviOdeljenjePredmetNastavnik.setPredmet(predmet);
		noviOdeljenjePredmetNastavnik.setNastavnik(nastavnik);
		return new ResponseEntity<OdeljenjePredmetNastavnikEntity>(
				odeljenjePredmetNastavnikRepo.save(noviOdeljenjePredmetNastavnik), HttpStatus.OK);
	}

	// 3.2 obrisati vezu odeljenje predmet nastavnik
	@RequestMapping(value = "/{idOdeljenjePredmetNastavnik}", method = RequestMethod.DELETE)
	public ResponseEntity<?> obrisatiOdeljenjePredmetNastavnik(@PathVariable Integer idOdeljenjePredmetNastavnik) {

		if (odeljenjePredmetNastavnikRepo.findById(idOdeljenjePredmetNastavnik).isPresent() == false) {
			return new ResponseEntity<RESTError>(new RESTError("Odeljnje predmet nastavnik za ovaj id ne postoji"),
					HttpStatus.NOT_FOUND);
		}
		OdeljenjePredmetNastavnikEntity odeljenjePredmetNastavnik = odeljenjePredmetNastavnikRepo
				.findById(idOdeljenjePredmetNastavnik).get();
		if (odeljenjePredmetNastavnik.getOcene().size() > 0) {
			return new ResponseEntity<RESTError>(
					new RESTError("Odeljnje predmet nastavnik sa ovim id ima date ocene i ne moze se izbrisati"),
					HttpStatus.NOT_FOUND);
		}
		odeljenjePredmetNastavnikRepo.deleteById(idOdeljenjePredmetNastavnik);
		return new ResponseEntity<OdeljenjePredmetNastavnikEntity>(odeljenjePredmetNastavnik, HttpStatus.OK);
	}

	// 3.3 spisak svih veza odeljenje predmet nastavnik
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> prikazatiOdeljenjePredmetNastavnik() {

		List<OdeljenjePredmetNastavnikEntity> odeljenjePredmetNastavnik = new ArrayList<>();
		odeljenjePredmetNastavnik = (List<OdeljenjePredmetNastavnikEntity>) odeljenjePredmetNastavnikRepo.findAll();
		if (odeljenjePredmetNastavnik.size() == 0) {
			return new ResponseEntity<RESTError>(new RESTError("Ne postoji ni jedna veza odeljenje predmet nastavnik"),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Iterable<OdeljenjePredmetNastavnikEntity>>(odeljenjePredmetNastavnikRepo.findAll(),
				HttpStatus.OK);
	}

	// 3.4 trazena veza odeljenje predmet nastavnik po id
	@RequestMapping(value = "/{idOdeljenjePredmetNastavnik}", method = RequestMethod.GET)
	public ResponseEntity<?> pronaciOdeljenjePredmetNastavnik(@PathVariable Integer idOdeljenjePredmetNastavnik) {

		if (odeljenjePredmetNastavnikRepo.findById(idOdeljenjePredmetNastavnik).isPresent() == false) {
			return new ResponseEntity<RESTError>(new RESTError("Odeljnje predmet nastavnik za ovaj id ne postoji"),
					HttpStatus.NOT_FOUND);
		}
		OdeljenjePredmetNastavnikEntity odeljenjePredmetNastavnik = odeljenjePredmetNastavnikRepo
				.findById(idOdeljenjePredmetNastavnik).get();
		return new ResponseEntity<OdeljenjePredmetNastavnikEntity>(odeljenjePredmetNastavnik, HttpStatus.OK);
	}

	// 3.5 promeniti odeljenje vezi odeljenje predmet nastavnik
	@RequestMapping(value = "/{idOdeljenjePredmetNastavnik}/odeljenje/{idOdeljenja}", method = RequestMethod.PUT)
	public ResponseEntity<?> izmenitiOdeljenjeVezi(@PathVariable Integer idOdeljenjePredmetNastavnik,
			@PathVariable Integer idOdeljenja) {

		if (odeljenjePredmetNastavnikRepo.findById(idOdeljenjePredmetNastavnik).isPresent() == false) {
			return new ResponseEntity<RESTError>(new RESTError("Odeljenje predmet nastavnik za ovaj id ne postoji"),
					HttpStatus.NOT_FOUND);
		}
		if (odeljenjeRepo.findById(idOdeljenja).isPresent() == false) {
			return new ResponseEntity<RESTError>(new RESTError("Odeljenje za ovaj id ne postoji"),
					HttpStatus.NOT_FOUND);
		}
		OdeljenjePredmetNastavnikEntity odeljenjePredmetNastavnik = odeljenjePredmetNastavnikRepo
				.findById(idOdeljenjePredmetNastavnik).get();
		OdeljenjeEntity odeljenje = odeljenjeRepo.findById(idOdeljenja).get();
		odeljenjePredmetNastavnik.setOdeljenje(odeljenje);
		return new ResponseEntity<OdeljenjePredmetNastavnikEntity>(
				odeljenjePredmetNastavnikRepo.save(odeljenjePredmetNastavnik), HttpStatus.OK);
	}

	// 3.6 promeniti predmet vezi odeljenje predmet nastavnik
	@RequestMapping(value = "/{idOdeljenjePredmetNastavnik}/predmet/{idPredmeta}", method = RequestMethod.PUT)
	public ResponseEntity<?> izmenitiPredmetVezi(@PathVariable Integer idOdeljenjePredmetNastavnik,
			@PathVariable Integer idPredmeta) {

		if (odeljenjePredmetNastavnikRepo.findById(idOdeljenjePredmetNastavnik).isPresent() == false) {
			return null;
		}
		if (predmetRepo.findById(idPredmeta).isPresent() == false) {
			return null;
		}
		OdeljenjePredmetNastavnikEntity odeljenjePredmetNastaavnik = odeljenjePredmetNastavnikRepo
				.findById(idOdeljenjePredmetNastavnik).get();
		PredmetEntity predmet = predmetRepo.findById(idPredmeta).get();
		odeljenjePredmetNastaavnik.setPredmet(predmet);
		return new ResponseEntity<OdeljenjePredmetNastavnikEntity>(
				odeljenjePredmetNastavnikRepo.save(odeljenjePredmetNastaavnik), HttpStatus.OK);
	}

	// 3.7 promeniti nastavnikA vezi odeljenje predmet nastavnik
	@RequestMapping(value = "/{idOdeljenjePredmetNastavnik}/nastavnik/{idNastavnika}", method = RequestMethod.PUT)
	public ResponseEntity<?> izmenitiNastavnikaVezi(@PathVariable Integer idOdeljenjePredmetNastavnik,
			@PathVariable Integer idNastavnika) {

		if (odeljenjePredmetNastavnikRepo.findById(idOdeljenjePredmetNastavnik).isPresent() == false) {
			return new ResponseEntity<RESTError>(
					new RESTError("Predmetni nastavnik odeljenja sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);
		}
		if (nastavnikRepo.findById(idNastavnika).isPresent() == false) {
			return new ResponseEntity<RESTError>(new RESTError("Predmet sa prosledjenim ID brojem ne postoji."),
					HttpStatus.NOT_FOUND);
		}
		OdeljenjePredmetNastavnikEntity odeljenjePredmetNastaavnik = odeljenjePredmetNastavnikRepo
				.findById(idOdeljenjePredmetNastavnik).get();
		NastavnikEntity nastavnik = nastavnikRepo.findById(idNastavnika).get();
		odeljenjePredmetNastaavnik.setNastavnik(nastavnik);
		return new ResponseEntity<OdeljenjePredmetNastavnikEntity>(
				odeljenjePredmetNastavnikRepo.save(odeljenjePredmetNastaavnik), HttpStatus.OK);
	}

	// 3.8 nastavnci i predmeti odeljenja
	@RequestMapping(value = "/nastavnici-predmeti/{idOdeljenja}", method = RequestMethod.GET)
	public ResponseEntity<?> pronaciNastavnikeOdeljenja(@PathVariable Integer idOdeljenja) {

		if (odeljenjeRepo.findById(idOdeljenja).isPresent() == false) {
			return new ResponseEntity<RESTError>(new RESTError("Odeljenje za ovaj id ne postoji"),
					HttpStatus.NOT_FOUND);
		}
		OdeljenjeEntity odeljenje = odeljenjeRepo.findById(idOdeljenja).get();
		List<OdeljenjePredmetNastavnikEntity> predmetiNastavnici = odeljenjePredmetNastavnikRepo
				.findByOdeljenje(odeljenje);
		return new ResponseEntity<List<OdeljenjePredmetNastavnikEntity>>(predmetiNastavnici, HttpStatus.OK);
	}
}

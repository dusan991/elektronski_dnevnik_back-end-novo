package com.iktpreobuka.elektronskidnevnik.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.elektronskidnevnik.controllers.util.RESTError;
import com.iktpreobuka.elektronskidnevnik.entities.NastavnikEntity;
import com.iktpreobuka.elektronskidnevnik.entities.OdeljenjeEntity;
import com.iktpreobuka.elektronskidnevnik.repositories.OdeljenjeRepository;

@RestController
@RequestMapping(path = "/API/V1/odeljenje")
@CrossOrigin(origins = "http://localhost:4200")
public class OdeljenjeController {

	@Autowired
	public OdeljenjeRepository odeljenjeRepo;

	// 5.1 dodati odeljenje
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> dodatiOdeljenje(@Valid @RequestBody OdeljenjeEntity odeljenje, BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		List<OdeljenjeEntity> odeljenja = (List<OdeljenjeEntity>) odeljenjeRepo.findAll();
		for (OdeljenjeEntity nekoOdeljenje : odeljenja) {
			if (nekoOdeljenje.getImeOdeljenja().equals(odeljenje.getImeOdeljenja())) {
				return new ResponseEntity<RESTError>(new RESTError("Vec postoji ime odeljenja"), HttpStatus.NOT_FOUND);
			}
		}
		OdeljenjeEntity novoOdeljenje = new OdeljenjeEntity();
		novoOdeljenje.setImeOdeljenja(odeljenje.getImeOdeljenja());
		novoOdeljenje.setRazred(odeljenje.getRazred());
		novoOdeljenje.setSkolskaGodinaRazreda(odeljenje.getSkolskaGodinaRazreda());
		return new ResponseEntity<OdeljenjeEntity>(odeljenjeRepo.save(novoOdeljenje), HttpStatus.OK);
	}

	private Object createErrorMessage(BindingResult result) {
		// TODO Auto-generated method stub
		return null;
	}

	// 5.2 promeniti odeljenje po id
	@RequestMapping(value = "/{idOdeljenja}", method = RequestMethod.PUT)
	public ResponseEntity<?> izmenitiOdeljenje(@Valid @RequestBody OdeljenjeEntity odeljenje,
			@PathVariable Integer idOdeljenja, BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		if (odeljenjeRepo.findById(idOdeljenja).isPresent() == false) {
			return new ResponseEntity<RESTError>(new RESTError("Odeljenje za ovaj id ne postoji"),
					HttpStatus.NOT_FOUND);
		}
		OdeljenjeEntity promenjenoOdeljenje = odeljenjeRepo.findById(idOdeljenja).get();
		promenjenoOdeljenje.setImeOdeljenja(odeljenje.getImeOdeljenja());
		promenjenoOdeljenje.setRazred(odeljenje.getRazred());
		promenjenoOdeljenje.setSkolskaGodinaRazreda(odeljenje.getSkolskaGodinaRazreda());
		return new ResponseEntity<OdeljenjeEntity>(odeljenjeRepo.save(promenjenoOdeljenje), HttpStatus.OK);
	}

	// 5.3 izbrisati trazeno odeljenje po id, ako nema ucenike i ako nije dodeljen
	@RequestMapping(value = "/{idOdeljenja}", method = RequestMethod.DELETE)
	public ResponseEntity<?> izbrisatiOdeljenje(@PathVariable Integer idOdeljenja) {

		OdeljenjeEntity odeljenje = odeljenjeRepo.findById(idOdeljenja).get();
		if (odeljenjeRepo.findById(idOdeljenja).isPresent() == false) {
			return new ResponseEntity<RESTError>(new RESTError("Odeljenje za ovaj id ne postoji"),
					HttpStatus.NOT_FOUND);
		}
		if (odeljenje.getUcenici().size() > 0) {
			return new ResponseEntity<RESTError>(new RESTError("Odeljenje ima ucenike i ne moze se izbrisati"),
					HttpStatus.NOT_FOUND);
		}
		if (odeljenje.getPredmetiOdeljenja().size() > 0) {
			return new ResponseEntity<RESTError>(
					new RESTError("Odeljenje ima predmete i nastavnike i ne moze se izbrisati"), HttpStatus.NOT_FOUND);
		}
		odeljenjeRepo.deleteById(idOdeljenja);
		return new ResponseEntity<OdeljenjeEntity>(odeljenje, HttpStatus.OK);
	}

	// 5.4 trazeno odeljenje po id
	@RequestMapping(value = "/{idOdeljenja}", method = RequestMethod.GET)
	public ResponseEntity<?> pronaciOdeljenje(@PathVariable Integer idOdeljenja) {

		if (odeljenjeRepo.findById(idOdeljenja).isPresent() == false) {
			return new ResponseEntity<RESTError>(new RESTError("Odeljenje za ovaj id ne postoji"),
					HttpStatus.NOT_FOUND);

		}
		OdeljenjeEntity odeljenje = odeljenjeRepo.findById(idOdeljenja).get();
		return new ResponseEntity<OdeljenjeEntity>(odeljenje, HttpStatus.OK);
	}

	// 5.5 spisak svih odeljenja
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> prikazatiOdeljenja() {
		List<OdeljenjeEntity> odeljenja = new ArrayList<>();
		odeljenja = (List<OdeljenjeEntity>) odeljenjeRepo.findAll();
		if (odeljenja.size() == 0) {
			return new ResponseEntity<RESTError>(new RESTError("Ne postoji ni jedno odeljenje"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Iterable<OdeljenjeEntity>>(odeljenjeRepo.findAll(), HttpStatus.OK);
	}
}

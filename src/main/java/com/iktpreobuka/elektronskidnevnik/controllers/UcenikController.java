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
import com.iktpreobuka.elektronskidnevnik.entities.NastavnikEntity;
import com.iktpreobuka.elektronskidnevnik.entities.OdeljenjeEntity;
import com.iktpreobuka.elektronskidnevnik.entities.RoditeljEntity;
import com.iktpreobuka.elektronskidnevnik.entities.RoleEntity;
import com.iktpreobuka.elektronskidnevnik.entities.UcenikEntity;
import com.iktpreobuka.elektronskidnevnik.entities.dto.UcenikDTO;
import com.iktpreobuka.elektronskidnevnik.repositories.OdeljenjeRepository;
import com.iktpreobuka.elektronskidnevnik.repositories.RoditeljRepository;
import com.iktpreobuka.elektronskidnevnik.repositories.RoleRepository;
import com.iktpreobuka.elektronskidnevnik.repositories.UcenikRepository;
import com.iktpreobuka.elektronskidnevnik.security.util.Encryption;

@RestController
@RequestMapping(value = "/API/V1/ucenik")
@CrossOrigin(origins = "http://localhost:4200")
public class UcenikController {

	@Autowired
	private UcenikRepository ucenikRepo;

	@Autowired
	private RoditeljRepository roditeljRepo;

	@Autowired
	private OdeljenjeRepository odeljenjeRepo;

	@Autowired
	private RoleRepository roleRepo;

	// 6.1 dodati ucenika
	@RequestMapping(value = "/{idRoditelja}", method = RequestMethod.POST)
	public ResponseEntity<?> dodatiUcenika(@Valid @RequestBody UcenikEntity ucenik, @PathVariable Integer idRoditelja,
			BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		List<UcenikEntity> ucenici = (List<UcenikEntity>) ucenikRepo.findAll();
		for (UcenikEntity nekiUcenik : ucenici) {
			if (ucenik.getKorisnickoImeUcenika().equals(nekiUcenik.getKorisnickoImeUcenika())) {
				return new ResponseEntity<RESTError>(new RESTError("Vec postoji korisnicko ime ucenika"),
						HttpStatus.NOT_FOUND);
			}
		}
		if (roditeljRepo.findById(idRoditelja).isPresent() == false) {
			return new ResponseEntity<RESTError>(new RESTError("Roditelj za ovaj id ne postoji"), HttpStatus.NOT_FOUND);
		}
		UcenikEntity noviUcenik = new UcenikEntity();
		RoditeljEntity roditelj = roditeljRepo.findById(idRoditelja).get();
		noviUcenik.setImeUcenika(ucenik.getImeUcenika());
		noviUcenik.setPrezimeUcenika(ucenik.getPrezimeUcenika());
		noviUcenik.setKorisnickoImeUcenika(ucenik.getKorisnickoImeUcenika());
		noviUcenik.setSifraUcenika(Encryption.getPassEncoded(ucenik.getSifraUcenika()));
		noviUcenik.setRoditelj(roditelj);
		RoleEntity role = roleRepo.findByName("ROLE_UCENIK");
		noviUcenik.setRole(role);
		return new ResponseEntity<UcenikEntity>(ucenikRepo.save(noviUcenik), HttpStatus.OK);
	}

	private Object createErrorMessage(BindingResult result) {
		// TODO Auto-generated method stub
		return null;
	}

	// 6.2 promeniti ucenika po id
	@RequestMapping(value = "/{idUcenika}", method = RequestMethod.PUT)
	public ResponseEntity<?> izmenitiUcenika(@Valid @RequestBody UcenikEntity ucenik, @PathVariable Integer idUcenika,
			BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		if (ucenikRepo.findById(idUcenika).isPresent() == false) {
			return new ResponseEntity<RESTError>(new RESTError("Ucenik za ovaj id ne postoji"), HttpStatus.NOT_FOUND);
		}
		UcenikEntity promenjeniUcenik = ucenikRepo.findById(idUcenika).get();
		promenjeniUcenik.setOdeljenjeUcenika(ucenik.getOdeljenjeUcenika());
		promenjeniUcenik.setImeUcenika(ucenik.getImeUcenika());
		promenjeniUcenik.setPrezimeUcenika(ucenik.getPrezimeUcenika());
		promenjeniUcenik.setKorisnickoImeUcenika(ucenik.getKorisnickoImeUcenika());
		return new ResponseEntity<UcenikEntity>(ucenikRepo.save(promenjeniUcenik), HttpStatus.OK);
	}

	// 6.3 izbrisati trazenog ucenika po id
	@RequestMapping(value = "/{idUcenika}", method = RequestMethod.DELETE)
	public ResponseEntity<?> brisatiUcenika(@PathVariable Integer idUcenika) {

		if (ucenikRepo.findById(idUcenika).isPresent() == false) {
			return new ResponseEntity<RESTError>(new RESTError("Ucenik za ovaj id ne postoji"), HttpStatus.NOT_FOUND);
		}
		UcenikEntity ucenik = ucenikRepo.findById(idUcenika).get();
		if (ucenik.getOcene().size() > 0) {
			return new ResponseEntity<RESTError>(new RESTError("Ucenik za ovaj id ima ocene i ne moze se izbrisati"),
					HttpStatus.NOT_FOUND);
		}
		ucenikRepo.deleteById(idUcenika);
		return new ResponseEntity<UcenikEntity>(ucenik, HttpStatus.OK);
	}

	// 6.4 trazeni ucenik po id
	@RequestMapping(value = "/{idUcenika}", method = RequestMethod.GET)
	public ResponseEntity<?> pronaciUcenika(@PathVariable Integer idUcenika) {

		if (ucenikRepo.findById(idUcenika).isPresent() == false) {
			return new ResponseEntity<RESTError>(new RESTError("Ucenik za ovaj id ne postoji"), HttpStatus.NOT_FOUND);
		}
		UcenikEntity ucenik = ucenikRepo.findById(idUcenika).get();
		return new ResponseEntity<UcenikEntity>(ucenik, HttpStatus.OK);
	}

	// 6.5 spisak svih ucenika
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> prikazatiUcenike() {

		List<UcenikEntity> ucenici = new ArrayList<>();
		ucenici = (List<UcenikEntity>) ucenikRepo.findAll();
		if (ucenici.size() == 0) {
			return new ResponseEntity<RESTError>(new RESTError("Nije pronadjen ni jedan ucenik."),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Iterable<UcenikEntity>>(ucenikRepo.findAll(), HttpStatus.OK);
	}

	// 6.6 promeniti odeljenje uceniku odnosno kada je odeljenje null
	@RequestMapping(value = "/{idUcenika}/odeljenje/{idOdeljenja}", method = RequestMethod.PUT)
	public ResponseEntity<?> izmenitiOdeljenjeUceniku(@PathVariable Integer idUcenika,
			@PathVariable Integer idOdeljenja) {

		if (ucenikRepo.findById(idUcenika).isPresent() == false) {
			return new ResponseEntity<RESTError>(new RESTError("Ucenik za ovaj id ne postoji"), HttpStatus.NOT_FOUND);
		}
		if (odeljenjeRepo.findById(idOdeljenja).isPresent() == false && idOdeljenja != 0) {
			return new ResponseEntity<RESTError>(new RESTError("Odeljenje za ovaj id ne postoji"),
					HttpStatus.NOT_FOUND);
		}
		UcenikEntity ucenik = ucenikRepo.findById(idUcenika).get();
		OdeljenjeEntity odeljenje;

		if (idOdeljenja == 0)
			odeljenje = null;
		else
			odeljenje = odeljenjeRepo.findById(idOdeljenja).get();
		ucenik.setOdeljenjeUcenika(odeljenje);
		return new ResponseEntity<UcenikEntity>(ucenikRepo.save(ucenik), HttpStatus.OK);
	}

	// 6.7 promeniti roditelja uceniku
	@RequestMapping(value = "/{idUcenika}/roditelj/{idRoditelja}", method = RequestMethod.PUT)
	public ResponseEntity<?> izmenitiRoditeljaUceniku(@PathVariable Integer idUcenika,
			@PathVariable Integer idRoditelja) {

		if (ucenikRepo.findById(idUcenika).isPresent() == false) {
			return new ResponseEntity<RESTError>(new RESTError("Ucenik za ovaj id ne postoji"), HttpStatus.NOT_FOUND);
		}
		if (roditeljRepo.findById(idRoditelja).isPresent() == false) {
			return new ResponseEntity<RESTError>(new RESTError("Roditelj za ovaj id ne postoji"), HttpStatus.NOT_FOUND);
		}
		UcenikEntity ucenik = ucenikRepo.findById(idUcenika).get();
		RoditeljEntity roditelj = roditeljRepo.findById(idRoditelja).get();
		ucenik.setRoditelj(roditelj);
		return new ResponseEntity<UcenikEntity>(ucenikRepo.save(ucenik), HttpStatus.OK);
	}

	// 6.8 trazeni ucenici odeljenja po id
	@RequestMapping(value = "/ucenici-odeljenja/{idOdeljenja}", method = RequestMethod.GET)
	public ResponseEntity<?> pronaciUcenikeOdeljenja(@PathVariable Integer idOdeljenja) {

		if (odeljenjeRepo.findById(idOdeljenja).isPresent() == false) {
			return new ResponseEntity<RESTError>(new RESTError("Odeljenje za ovaj id ne postoji"),
					HttpStatus.NOT_FOUND);
		}
		OdeljenjeEntity odeljenjeUcenika = odeljenjeRepo.findById(idOdeljenja).get();
		List<UcenikEntity> ucenici = ucenikRepo.findByOdeljenjeUcenika(odeljenjeUcenika);
		return new ResponseEntity<List<UcenikEntity>>(ucenici, HttpStatus.OK);
	}

	// 6.9 ucenici bez odeljenja
	@RequestMapping(value = "/ucenici-bez-odeljenja", method = RequestMethod.GET)
	public ResponseEntity<?> pronaciUcenikeBezOdeljenja() {
		List<UcenikEntity> ucenici = new ArrayList<>();
		ucenici = (List<UcenikEntity>) ucenikRepo.findAll();
		List<UcenikEntity> uceniciBezOdeljenja = new ArrayList<>();
		for (UcenikEntity nekiUcenik : ucenici) {
			if (nekiUcenik.getOdeljenjeUcenika() == null) {
				uceniciBezOdeljenja.add(nekiUcenik);
			}
		}
		return new ResponseEntity<List<UcenikEntity>>(uceniciBezOdeljenja, HttpStatus.OK);
	}
	@RequestMapping(value = "/ime/{imeUcenika}/prezime/{prezimeUcenika}", method = RequestMethod.GET)
	public ResponseEntity<?> pronaciNastavnikaPremaImenuIPrezimenu(@PathVariable String imeUcenika,
			@PathVariable String prezimeUcenika) {

		List<UcenikEntity> uceniciPoImenu = ucenikRepo.findByImeUcenika(imeUcenika);
		if ((uceniciPoImenu.size() == 0)) {
			return new ResponseEntity<RESTError>(new RESTError("Ucenik sa ovim imenom ne postoji"),
					HttpStatus.NOT_FOUND);
		}
		List<UcenikEntity> uceniciPoImenuPrezimenu = new ArrayList<>();
		for (UcenikEntity nastavnik : uceniciPoImenu) {
			if (nastavnik.getPrezimeUcenika().equalsIgnoreCase(prezimeUcenika)) {
				uceniciPoImenuPrezimenu.add(nastavnik);
			}
		}
		return new ResponseEntity<Iterable<UcenikEntity>>(uceniciPoImenuPrezimenu, HttpStatus.OK);
	}
}

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
import com.iktpreobuka.elektronskidnevnik.entities.OcenaEntity;
import com.iktpreobuka.elektronskidnevnik.entities.OdeljenjeEntity;
import com.iktpreobuka.elektronskidnevnik.entities.OdeljenjePredmetNastavnikEntity;
import com.iktpreobuka.elektronskidnevnik.entities.RoleEntity;
import com.iktpreobuka.elektronskidnevnik.entities.UcenikEntity;
import com.iktpreobuka.elektronskidnevnik.entities.dto.OceneUcenikaPredmetaDTO;
import com.iktpreobuka.elektronskidnevnik.entities.dto.OdeljenjePredmetDTO;
import com.iktpreobuka.elektronskidnevnik.entities.dto.UcenikDTO;
import com.iktpreobuka.elektronskidnevnik.repositories.NastavnikRepository;
import com.iktpreobuka.elektronskidnevnik.repositories.OcenaRepository;
import com.iktpreobuka.elektronskidnevnik.repositories.OdeljenjePredmetNastavnikRepository;
import com.iktpreobuka.elektronskidnevnik.repositories.OdeljenjeRepository;
import com.iktpreobuka.elektronskidnevnik.repositories.RoleRepository;
import com.iktpreobuka.elektronskidnevnik.repositories.UcenikRepository;
import com.iktpreobuka.elektronskidnevnik.security.util.Encryption;

@RestController
@RequestMapping(path = "/API/V1/nastavnik")
@CrossOrigin(origins = "http://localhost:4200")
public class NastavnikController {

	@Autowired
	private NastavnikRepository nastavnikRepo;

	@Autowired
	private OdeljenjePredmetNastavnikRepository odeljenjePredmetNastavnikRepo;

	@Autowired
	private UcenikRepository ucenikRepo;

	@Autowired
	public OcenaRepository ocenaRepo;

	@Autowired
	public OdeljenjeRepository odeljenjeRepo;

	@Autowired
	private RoleRepository roleRepo;

	// 1.1 dodati nastavnika
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> dodatiNastavnika(@Valid @RequestBody NastavnikEntity nastavnik, BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);

		}
		List<NastavnikEntity> nastavnici = (List<NastavnikEntity>) nastavnikRepo.findAll();
		for (NastavnikEntity nekiNastavnik : nastavnici) {
			if (nekiNastavnik.getKorisnickoImeNastavnika().equals(nastavnik.getKorisnickoImeNastavnika())) {
				return new ResponseEntity<RESTError>(new RESTError("Vec postoji korisnicko ime nastavnika"),
						HttpStatus.NOT_FOUND);
			}
		}
		NastavnikEntity noviNastavnik = new NastavnikEntity();
		noviNastavnik.setImeNastavnika(nastavnik.getImeNastavnika());
		noviNastavnik.setPrezimeNastavnika(nastavnik.getPrezimeNastavnika());
		noviNastavnik.setKorisnickoImeNastavnika(nastavnik.getKorisnickoImeNastavnika());
		noviNastavnik.setSifraNastavnika(Encryption.getPassEncoded(nastavnik.getSifraNastavnika()));
		RoleEntity role = roleRepo.findByName("ROLE_NASTAVNIK");
		noviNastavnik.setRole(role);
		return new ResponseEntity<NastavnikEntity>(nastavnikRepo.save(noviNastavnik), HttpStatus.OK);
	}

	private Object createErrorMessage(BindingResult result) {
		// TODO Auto-generated method stub
		return null;
	}

	// 1.2 promeniti nastavnika po id
	@RequestMapping(value = "/{idNastavnika}", method = RequestMethod.PUT)
	public ResponseEntity<?> izmenitiNastavnika(@Valid @RequestBody NastavnikEntity nastavnik,
			@PathVariable Integer idNastavnika, BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		if (nastavnikRepo.findById(idNastavnika).isPresent() == false) {
			return new ResponseEntity<RESTError>(new RESTError("Nastavnik za ovaj id ne postoji"),
					HttpStatus.NOT_FOUND);
		}
		NastavnikEntity promenjenNastavnik = nastavnikRepo.findById(idNastavnika).get();
		promenjenNastavnik.setImeNastavnika(nastavnik.getImeNastavnika());
		promenjenNastavnik.setPrezimeNastavnika(nastavnik.getPrezimeNastavnika());
		promenjenNastavnik.setKorisnickoImeNastavnika(nastavnik.getKorisnickoImeNastavnika());
		return new ResponseEntity<NastavnikEntity>(nastavnikRepo.save(promenjenNastavnik), HttpStatus.OK);
	}

	// 1.3 izbrisati trazenog nastavnika po id, ako nije dodeljen
	@RequestMapping(value = "/{idNastavnika}", method = RequestMethod.DELETE)
	public ResponseEntity<?> izbrisatiNastavnika(@PathVariable Integer idNastavnika) {

		NastavnikEntity nastavnik = nastavnikRepo.findById(idNastavnika).get();
		if (nastavnikRepo.findById(idNastavnika).isPresent() == false) {
			return new ResponseEntity<RESTError>(new RESTError("Nastavnik za ovaj id ne postoji"),
					HttpStatus.NOT_FOUND);
		}
		if (nastavnik.getPredmetiOdeljenjaNastavnika().size() > 0) {
			return new ResponseEntity<RESTError>(
					new RESTError("Nastavnik imam svoja odeljenja i predmete i ne moze se izbrisati"),
					HttpStatus.NOT_FOUND);
		}
		nastavnikRepo.deleteById(idNastavnika);
		return new ResponseEntity<NastavnikEntity>(nastavnik, HttpStatus.OK);
	}

	// 1.4 trazeni nastavnik po id
	@RequestMapping(value = "/{idNastavnika}", method = RequestMethod.GET)
	public ResponseEntity<?> pronaciNastavnika(@PathVariable Integer idNastavnika) {

		if (nastavnikRepo.findById(idNastavnika).isPresent() == false) {
			return new ResponseEntity<RESTError>(new RESTError("Nastavnik za ovaj id ne postoji"),
					HttpStatus.NOT_FOUND);
		}
		NastavnikEntity nastavnik = nastavnikRepo.findById(idNastavnika).get();
		return new ResponseEntity<NastavnikEntity>(nastavnik, HttpStatus.OK);
	}

	// 1.5 spisak svih nastavnika
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> prikazatiNastavnike() {
		List<NastavnikEntity> nastavnici = new ArrayList<>();
		nastavnici = (List<NastavnikEntity>) nastavnikRepo.findAll();
		if (nastavnici.size() == 0) {
			return new ResponseEntity<RESTError>(new RESTError("Ne postoji ni jedan nastavnik"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Iterable<NastavnikEntity>>(nastavnikRepo.findAll(), HttpStatus.OK);
	}

	// 1.6 naci nastavnike po imenu i prezimenu
	@RequestMapping(value = "/ime/{imeNastavnika}/prezime/{prezimeNastavnika}", method = RequestMethod.GET)
	public ResponseEntity<?> pronaciNastavnikaPremaImenuIPrezimenu(@PathVariable String imeNastavnika,
			@PathVariable String prezimeNastavnika) {

		List<NastavnikEntity> nastavniciPoImenu = nastavnikRepo.findByImeNastavnika(imeNastavnika);
		if ((nastavniciPoImenu.size() == 0)) {
			return new ResponseEntity<RESTError>(new RESTError("Nastavnik sa ovim imenom ne postoji"),
					HttpStatus.NOT_FOUND);
		}
		List<NastavnikEntity> nastavniciPoImenuPrezimenu = new ArrayList<>();
		for (NastavnikEntity nastavnik : nastavniciPoImenu) {
			if (nastavnik.getPrezimeNastavnika().equalsIgnoreCase(prezimeNastavnika)) {
				nastavniciPoImenuPrezimenu.add(nastavnik);
			}
		}
		return new ResponseEntity<Iterable<NastavnikEntity>>(nastavniciPoImenuPrezimenu, HttpStatus.OK);
	}

	// 1.7 odeljenja-predmeti koje drzi nastavnik
	@RequestMapping(value = "/odeljenja-predmeti/{idNastavnika}", method = RequestMethod.GET)
	public ResponseEntity<?> pronaciOdeljenjaIPredmeteNastavnik(@PathVariable Integer idNastavnika) {

		if (nastavnikRepo.findById(idNastavnika).isPresent() == false) {
			return new ResponseEntity<RESTError>(new RESTError("Nastavnik za prosledjeni id ne postoji"),
					HttpStatus.NOT_FOUND);
		}
		NastavnikEntity nastavnik = nastavnikRepo.findById(idNastavnika).get();
		List<OdeljenjePredmetNastavnikEntity> odeljenjePredmeti = odeljenjePredmetNastavnikRepo
				.findByNastavnik(nastavnik);
		List<OdeljenjePredmetDTO> odeljenjaPredmetiDTO = new ArrayList<OdeljenjePredmetDTO>();
		for (OdeljenjePredmetNastavnikEntity nekoOdeljenjePredmet : odeljenjePredmeti) {
			OdeljenjePredmetDTO odeljenjePredmetDTO = new OdeljenjePredmetDTO();
			odeljenjePredmetDTO.setImePredmeta(nekoOdeljenjePredmet.getPredmet().getImePredmeta());
			odeljenjePredmetDTO.setImeOdeljenja(nekoOdeljenjePredmet.getOdeljenje().getImeOdeljenja());
			odeljenjaPredmetiDTO.add(odeljenjePredmetDTO);
		}
		return new ResponseEntity<Iterable<OdeljenjePredmetDTO>>(odeljenjaPredmetiDTO, HttpStatus.OK);
	}

	@RequestMapping(value = "/nastavnici-odeljenja/{idOdeljenja}", method = RequestMethod.GET)
	public ResponseEntity<?> pronaciNastavnikeOdeljenja(@PathVariable Integer idOdeljenja) {

		if (odeljenjeRepo.findById(idOdeljenja).isPresent() == false) {
			return new ResponseEntity<RESTError>(new RESTError("Odeljenje za ovaj id ne postoji"),
					HttpStatus.NOT_FOUND);

		}
		OdeljenjeEntity odeljenje = odeljenjeRepo.findById(idOdeljenja).get();
		List<OdeljenjePredmetNastavnikEntity> odeljenePredmetNastavnici = odeljenjePredmetNastavnikRepo
				.findByOdeljenje(odeljenje);
		List<NastavnikEntity> nastavnici = new ArrayList<>();
		for (OdeljenjePredmetNastavnikEntity nekoOdeljenePredmetNastavnik : odeljenePredmetNastavnici) {
			NastavnikEntity nastavnik = new NastavnikEntity();
			nastavnik = nekoOdeljenePredmetNastavnik.getNastavnik();
			nastavnici.add(nastavnik);
		}
		return new ResponseEntity<List<NastavnikEntity>>(nastavnici, HttpStatus.OK);
	}
}

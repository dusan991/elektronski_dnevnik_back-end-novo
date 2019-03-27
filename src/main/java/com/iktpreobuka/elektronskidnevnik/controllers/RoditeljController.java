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
import com.iktpreobuka.elektronskidnevnik.entities.RoditeljEntity;
import com.iktpreobuka.elektronskidnevnik.entities.RoleEntity;
import com.iktpreobuka.elektronskidnevnik.entities.UcenikEntity;
import com.iktpreobuka.elektronskidnevnik.entities.dto.UcenikDTO;
import com.iktpreobuka.elektronskidnevnik.repositories.RoditeljRepository;
import com.iktpreobuka.elektronskidnevnik.repositories.RoleRepository;
import com.iktpreobuka.elektronskidnevnik.repositories.UcenikRepository;
import com.iktpreobuka.elektronskidnevnik.security.util.Encryption;

@RestController
@RequestMapping(path = "/API/V1/roditelj")
@CrossOrigin(origins = "http://localhost:4200")
public class RoditeljController {

	@Autowired
	private RoditeljRepository roditeljRepo;

	@Autowired
	private UcenikRepository ucenikRepo;

	@Autowired
	private RoleRepository roleRepo;

	// 4.1 dodati roditelja
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> dodatiRoditelja(@Valid @RequestBody RoditeljEntity roditelj, BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		List<RoditeljEntity> roditelji = (List<RoditeljEntity>) roditeljRepo.findAll();
		for (RoditeljEntity nekiRoditelj : roditelji) {
			if (nekiRoditelj.getKorisnickoImeRoditelja().equals(roditelj.getKorisnickoImeRoditelja())) {
				return new ResponseEntity<RESTError>(new RESTError("Vec postoji korisnicko ime roditelja"),
						HttpStatus.NOT_FOUND);
			}
		}
		RoditeljEntity noviRoditelj = new RoditeljEntity();
		noviRoditelj.setImeRoditelja(roditelj.getImeRoditelja());
		noviRoditelj.setPrezimeRoditelja(roditelj.getPrezimeRoditelja());
		noviRoditelj.setKorisnickoImeRoditelja(roditelj.getKorisnickoImeRoditelja());
		noviRoditelj.setSifraRoditelja(Encryption.getPassEncoded(roditelj.getSifraRoditelja()));
		noviRoditelj.setEmailRoditelja(roditelj.getEmailRoditelja());
		RoleEntity role = roleRepo.findByName("ROLE_RODITELJ");
		noviRoditelj.setRole(role);
		return new ResponseEntity<RoditeljEntity>(roditeljRepo.save(noviRoditelj), HttpStatus.OK);
	}

	private Object createErrorMessage(BindingResult result) {
		// TODO Auto-generated method stub
		return null;
	}

	// 4.2 promeniti roditelja po id
	@RequestMapping(value = "/{idRoditelja}", method = RequestMethod.PUT)
	public ResponseEntity<?> izmenitiRoditelja(@Valid @RequestBody RoditeljEntity roditelj,
			@PathVariable Integer idRoditelja, BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		if (roditeljRepo.findById(idRoditelja).isPresent() == false) {
			return new ResponseEntity<RESTError>(new RESTError("Roditelj za ovaj id ne postoji"), HttpStatus.NOT_FOUND);
		}
		RoditeljEntity promenjenRoditelj = roditeljRepo.findById(idRoditelja).get();
		promenjenRoditelj.setImeRoditelja(roditelj.getImeRoditelja());
		promenjenRoditelj.setPrezimeRoditelja(roditelj.getPrezimeRoditelja());
		promenjenRoditelj.setKorisnickoImeRoditelja(roditelj.getKorisnickoImeRoditelja());
		promenjenRoditelj.setEmailRoditelja(roditelj.getEmailRoditelja());
		return new ResponseEntity<RoditeljEntity>(roditeljRepo.save(promenjenRoditelj), HttpStatus.OK);
	}

	// 4.3 izbrisati trazenog roditelja po id, ako nema ucenika
	@RequestMapping(value = "/{idRoditelja}", method = RequestMethod.DELETE)
	public ResponseEntity<?> izbrisatiRoditelja(@PathVariable Integer idRoditelja) {

		RoditeljEntity roditelj = roditeljRepo.findById(idRoditelja).get();
		if (roditeljRepo.findById(idRoditelja).isPresent() == false) {
			return new ResponseEntity<RESTError>(new RESTError("Roditelj za ovaj id ne postoji"), HttpStatus.NOT_FOUND);
		}
		if (roditelj.getUceniciRoditelja().size() > 0) {
			return new ResponseEntity<RESTError>(new RESTError("Roditelj imam decu i ne moze se izbrisati"),
					HttpStatus.NOT_FOUND);
		}
		roditeljRepo.deleteById(idRoditelja);
		return new ResponseEntity<RoditeljEntity>(roditelj, HttpStatus.OK);
	}

	// 4.4 trazeni roditelj po id
	@RequestMapping(value = "/{idRoditelja}", method = RequestMethod.GET)
	public ResponseEntity<?> pronaciRoditelja(@PathVariable Integer idRoditelja) {

		if (roditeljRepo.findById(idRoditelja).isPresent() == false) {
			return new ResponseEntity<RESTError>(new RESTError("Roditelj za ovaj id ne postoji"), HttpStatus.NOT_FOUND);
		}
		RoditeljEntity roditelj = roditeljRepo.findById(idRoditelja).get();
		return new ResponseEntity<RoditeljEntity>(roditelj, HttpStatus.OK);
	}

	// 4.5 spisak svih roditelja
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> prikazatiRoditelje() {

		List<RoditeljEntity> roditelji = new ArrayList<>();
		roditelji = (List<RoditeljEntity>) roditeljRepo.findAll();
		if (roditelji.size() == 0) {
			return new ResponseEntity<RESTError>(new RESTError("Ne postoji ni jedan roditelj"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Iterable<RoditeljEntity>>(roditeljRepo.findAll(), HttpStatus.OK);
	}

	// 4.6 naci roditelje po imenu i prezimenu
	@RequestMapping(value = "/ime/{imeRoditelja}/prezime/{prezimeRoditelja}", method = RequestMethod.GET)
	public ResponseEntity<?> pronadjiRoditeljaPremaImenuPrezimenu(@PathVariable String imeRoditelja,
			@PathVariable String prezimeRoditelja) {

		List<RoditeljEntity> roditeljiPoImenu = roditeljRepo.findByImeRoditelja(imeRoditelja);
		if ((roditeljiPoImenu.size() == 0)) {
			return new ResponseEntity<RESTError>(new RESTError("Roditelj sa prosledjenim imenom postoji."),
					HttpStatus.NOT_FOUND);
		}
		List<RoditeljEntity> roditeljiPoImenuPrezimenu = new ArrayList<>();
		for (RoditeljEntity roditelj : roditeljiPoImenu) {
			if (roditelj.getPrezimeRoditelja().equalsIgnoreCase(prezimeRoditelja)) {
				roditeljiPoImenuPrezimenu.add(roditelj);
			}
		}
		return new ResponseEntity<Iterable<RoditeljEntity>>(roditeljiPoImenuPrezimenu, HttpStatus.OK);
	}

	// 4.7 deca roditelja koji je izabran po id
	@RequestMapping(value = "/ucenici/{idRoditelja}", method = RequestMethod.GET)
	public ResponseEntity<?> pronaciUcenikeRoditelja(@PathVariable Integer idRoditelja) {

		if (roditeljRepo.findById(idRoditelja).isPresent() == false) {
			return new ResponseEntity<RESTError>(new RESTError("Roditelj sa prosledjenim imenom postoji"),
					HttpStatus.NOT_FOUND);
		}
		RoditeljEntity roditelj = roditeljRepo.findById(idRoditelja).get();
		List<UcenikEntity> ucenici = ucenikRepo.findByRoditelj(roditelj);
		List<UcenikDTO> uceniciDTO = new ArrayList<UcenikDTO>();
		for (UcenikEntity nekiUcenik : ucenici) {
			UcenikDTO ucenikDTO = new UcenikDTO();
			ucenikDTO.setImeUcenika(nekiUcenik.getImeUcenika());
			ucenikDTO.setPrezimeUcenika(nekiUcenik.getPrezimeUcenika());
			ucenikDTO.setIdUcenika(nekiUcenik.getIdUcenika());
			uceniciDTO.add(ucenikDTO);
		}
		return new ResponseEntity<Iterable<UcenikDTO>>(uceniciDTO, HttpStatus.OK);
	}
}

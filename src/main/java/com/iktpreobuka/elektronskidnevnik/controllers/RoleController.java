package com.iktpreobuka.elektronskidnevnik.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iktpreobuka.elektronskidnevnik.controllers.util.RESTError;
import com.iktpreobuka.elektronskidnevnik.entities.NastavnikEntity;
import com.iktpreobuka.elektronskidnevnik.entities.RoleEntity;
import com.iktpreobuka.elektronskidnevnik.repositories.RoleRepository;
import com.iktpreobuka.elektronskidnevnik.repositories.UcenikRepository;

@RestController
@RequestMapping(path = "/API/V1/uloga")
@CrossOrigin(origins = "http://localhost:4200")
public class RoleController {

	@Autowired
	public RoleRepository roleRepo;

	// spisak svih nastavnika
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> prikazatiUloge() {
		List<RoleEntity> uloge = new ArrayList<>();
		uloge = (List<RoleEntity>) roleRepo.findAll();
		if (uloge.size() == 0) {
			return new ResponseEntity<RESTError>(new RESTError("Ne postoji ni jedana uloga"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Iterable<RoleEntity>>(roleRepo.findAll(), HttpStatus.OK);
	}

	// dodati nastavnika
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> dodatiUlogu(@Valid @RequestBody RoleEntity uloga, BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);

		}
		List<RoleEntity> uloge = (List<RoleEntity>) roleRepo.findAll();
		for (RoleEntity nekaUloga : uloge) {
			if (nekaUloga.getName().equals(uloga.getName())) {
				return new ResponseEntity<RESTError>(new RESTError("Vec postoji uloga sa ovim imenom"),
						HttpStatus.NOT_FOUND);
			}
		}
		RoleEntity novaUloga = new RoleEntity();
		novaUloga.setName(uloga.getName());
		return new ResponseEntity<RoleEntity>(roleRepo.save(novaUloga), HttpStatus.OK);
	}

	private Object createErrorMessage(BindingResult result) {
		// TODO Auto-generated method stub
		return null;
	}
}

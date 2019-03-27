package com.iktpreobuka.elektronskidnevnik.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import com.iktpreobuka.elektronskidnevnik.entities.AdminEntity;
import com.iktpreobuka.elektronskidnevnik.entities.RoditeljEntity;
import com.iktpreobuka.elektronskidnevnik.entities.RoleEntity;
import com.iktpreobuka.elektronskidnevnik.repositories.AdminRepository;
import com.iktpreobuka.elektronskidnevnik.repositories.RoleRepository;
import com.iktpreobuka.elektronskidnevnik.repositories.UcenikRepository;
import com.iktpreobuka.elektronskidnevnik.security.util.Encryption;

@RestController
@RequestMapping(path = "/API/V1/admin")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {

	@Autowired
	public AdminRepository adminRepo;

	@Autowired
	public UcenikRepository ucenikRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	ServletContext context;

	// 8.1 dodati admina
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> dodatiAdmina(@Valid @RequestBody AdminEntity admin, BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);

		}
		List<AdminEntity> admini = (List<AdminEntity>) adminRepo.findAll();
		for (AdminEntity nekiAdmin : admini) {
			if (nekiAdmin.getKorisnickoImeAdmina().equals(admin.getKorisnickoImeAdmina())) {
				return new ResponseEntity<RESTError>(new RESTError("Vec postoji korisnicko ime admina"),
						HttpStatus.NOT_FOUND);
			}
		}
		AdminEntity noviAdmin = new AdminEntity();
		noviAdmin.setKorisnickoImeAdmina(admin.getKorisnickoImeAdmina());
		noviAdmin.setSifraAdmina(Encryption.getPassEncoded(admin.getSifraAdmina()));
		RoleEntity role = roleRepo.findByName("ROLE_ADMIN");
		noviAdmin.setRole(role);
		return new ResponseEntity<AdminEntity>(adminRepo.save(noviAdmin), HttpStatus.OK);
	}

	private Object createErrorMessage(BindingResult result) {
		// TODO Auto-generated method stub
		return null;
	}

	// 8.2 spisak svih admina
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> prikazatiAdmine() {
		List<AdminEntity> admini = (List<AdminEntity>) adminRepo.findAll();
		if (admini.size() == 0) {
			return new ResponseEntity<RESTError>(new RESTError("Nije pronadjen ni jedan nastavnik."),
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Iterable<AdminEntity>>(adminRepo.findAll(), HttpStatus.OK);
	}

	// skidanje log fajla
	@RequestMapping("/logs/{fileName:.+}")
	public void downloader(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("fileName") String fileName) {

		System.out.println("Downloading file :- " + fileName);

		String downloadFolder = context.getRealPath("/logs/");
		Path file = Paths.get(downloadFolder, fileName);
		// proveriti da li fajl postoji
		if (Files.exists(file)) {
			// set content type
			response.setContentType("application/log");
			// add response header
			response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
			try {
				// kopirati sve sa fajla na output stream
				Files.copy(file, response.getOutputStream());
				// flushes output stream
				response.getOutputStream().flush();
			} catch (IOException e) {
				System.out.println("Error :- " + e.getMessage());
			}
		} else {
			System.out.println("Sorry File not found!!!!");
		}
	}
}

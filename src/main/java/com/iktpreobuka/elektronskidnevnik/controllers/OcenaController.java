package com.iktpreobuka.elektronskidnevnik.controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.elektronskidnevnik.controllers.util.RESTError;
import com.iktpreobuka.elektronskidnevnik.entities.NastavnikEntity;
import com.iktpreobuka.elektronskidnevnik.entities.OcenaEntity;
import com.iktpreobuka.elektronskidnevnik.entities.OdeljenjeEntity;
import com.iktpreobuka.elektronskidnevnik.entities.OdeljenjePredmetNastavnikEntity;
import com.iktpreobuka.elektronskidnevnik.entities.UcenikEntity;
import com.iktpreobuka.elektronskidnevnik.entities.dto.OceneDatumDTO;
import com.iktpreobuka.elektronskidnevnik.entities.dto.OceneUcenikaDTO;
import com.iktpreobuka.elektronskidnevnik.entities.dto.OceneUcenikaPredmetaDTO;
import com.iktpreobuka.elektronskidnevnik.entities.enumerations.TipOcene;
import com.iktpreobuka.elektronskidnevnik.repositories.OcenaRepository;
import com.iktpreobuka.elektronskidnevnik.repositories.OdeljenjePredmetNastavnikRepository;
import com.iktpreobuka.elektronskidnevnik.repositories.OdeljenjeRepository;
import com.iktpreobuka.elektronskidnevnik.repositories.UcenikRepository;
import com.iktpreobuka.elektronskidnevnik.services.EmailService;

@RestController
@RequestMapping(path = "/API/V1/ocena")
@CrossOrigin(origins = "http://localhost:4200")
public class OcenaController {

	@Autowired
	public EmailService emailService;

	@Autowired
	public OcenaRepository ocenaRepo;

	@Autowired
	public UcenikRepository ucenikRepo;

	@Autowired
	public OdeljenjeRepository odeljenjeRepo;

	@Autowired
	public OdeljenjePredmetNastavnikRepository odeljenjePredmetNastavnikRepo;

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	// 7.1 dodati ocenu
	@RequestMapping(value = "/ucenik/{idUcenika}/odeljenjePredmetNastavnik/{idOdeljenjePredmetNastavnik}", method = RequestMethod.POST)
	public ResponseEntity<?> dodatiOcenu(@Valid @RequestBody OcenaEntity ocena, @PathVariable Integer idUcenika,
			@PathVariable Integer idOdeljenjePredmetNastavnik, BindingResult result) throws Exception {

		// logger.debug("This is a debug message");
		// logger.info("This is an info message");
		// logger.warn("This is a warn message");
		// logger.error("This is an error message");

		if (result.hasErrors()) {
			System.out.println("Ovde sam");
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		if (ucenikRepo.findById(idUcenika).isPresent() == false) {
			return new ResponseEntity<RESTError>(new RESTError("Ucenik za ovaj id ne postoji"), HttpStatus.NOT_FOUND);
		} else if (odeljenjePredmetNastavnikRepo.findById(idOdeljenjePredmetNastavnik).isPresent() == false) {
			return new ResponseEntity<RESTError>(
					new RESTError("Odeljenje predmet nastavnik veza za ovaj id ne postoji"), HttpStatus.NOT_FOUND);
		}
		OcenaEntity novaOcena = new OcenaEntity();
		UcenikEntity ucenik = ucenikRepo.findById(idUcenika).get();
		OdeljenjePredmetNastavnikEntity odeljenjePredmetNastavnik = odeljenjePredmetNastavnikRepo
				.findById(idOdeljenjePredmetNastavnik).get();
		if (ucenik.getOdeljenjeUcenika() != odeljenjePredmetNastavnik.getOdeljenje()) {
			return new ResponseEntity<RESTError>(
					new RESTError("Odeljenje ucenika nema trazenu vezu odeljenje predmet nastavnik "),
					HttpStatus.BAD_REQUEST);
		}
		novaOcena.setOcena(ocena.getOcena());
		novaOcena.setDatumOcene(ocena.getDatumOcene());
		novaOcena.setTipOcene(ocena.getTipOcene());
		novaOcena.setUcenik(ucenik);
		novaOcena.setOdeljenjePredmetNastavnik(odeljenjePredmetNastavnik);
		OcenaEntity ocenaSacuvana = ocenaRepo.save(novaOcena);
		// emailService.sendTemplateMessage(ocenaSacuvana);
		return new ResponseEntity<OcenaEntity>(ocenaSacuvana, HttpStatus.OK);
	}

	private Object createErrorMessage(BindingResult result) {
		// TODO Auto-generated method stub
		return null;
	}

	// 7.2 promeniti ocenu po id
	@RequestMapping(value = "/{idOcene}", method = RequestMethod.PUT)
	private ResponseEntity<?> izmenitiOcenu(@Valid @RequestBody OcenaEntity ocena, @PathVariable Integer idOcene,
			BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		if (!ocenaRepo.findById(idOcene).isPresent()) {
			return new ResponseEntity<RESTError>(new RESTError("Ocena za ovaj id ne postoji"), HttpStatus.NOT_FOUND);
		}
		OcenaEntity novaOcena = ocenaRepo.findById(idOcene).get();
		novaOcena.setOcena(ocena.getOcena());
		novaOcena.setDatumOcene(ocena.getDatumOcene());
		novaOcena.setTipOcene(ocena.getTipOcene());
		return new ResponseEntity<OcenaEntity>(ocenaRepo.save(novaOcena), HttpStatus.OK);
	}

	// 7.3 izbrisati trazenu ocenu po id
	@RequestMapping(value = "/{idOcene}", method = RequestMethod.DELETE)
	public ResponseEntity<?> brisatiOcenu(@PathVariable Integer idOcene) {

		if (ocenaRepo.findById(idOcene).isPresent() == false) {
			return new ResponseEntity<RESTError>(new RESTError("Ocena za ovaj id ne postoji"), HttpStatus.NOT_FOUND);
		}
		OcenaEntity ocena = ocenaRepo.findById(idOcene).get();
		ocenaRepo.deleteById(idOcene);
		return new ResponseEntity<OcenaEntity>(ocena, HttpStatus.OK);
	}

	// 7.4 trazena ocena po id
	@RequestMapping(value = "/{idOcene}", method = RequestMethod.GET)
	public ResponseEntity<?> pronaciOcenu(@PathVariable Integer idOcene) {

		if (ocenaRepo.findById(idOcene).isPresent() == false) {
			return new ResponseEntity<RESTError>(new RESTError("Ocena za ovaj id ne postoji"), HttpStatus.NOT_FOUND);
		}
		OcenaEntity ocena = ocenaRepo.findById(idOcene).get();
		return new ResponseEntity<OcenaEntity>(ocenaRepo.save(ocena), HttpStatus.OK);
	}

	// 7.5 spisak svih ocena
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> prikazatiOcene() {

		List<OcenaEntity> ocene = new ArrayList<>();
		ocene = (List<OcenaEntity>) ocenaRepo.findAll();
		if (ocene.size() == 0) {
			return new ResponseEntity<RESTError>(new RESTError("Ne postoji ni jedna ocena"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Iterable<OcenaEntity>>(ocenaRepo.findAll(), HttpStatus.OK);
	}

	// 7.6 trazene ocene ucenika koji je izabran po id
	@RequestMapping(value = "/ucenik/{idUcenika}", method = RequestMethod.GET)
	public ResponseEntity<?> pronaciOceneUcenika(@PathVariable Integer idUcenika) {

		if (ucenikRepo.findById(idUcenika).isPresent() == false) {
			return new ResponseEntity<RESTError>(new RESTError("Ucenik za ovaj id ne postoji"), HttpStatus.NOT_FOUND);
		}
		UcenikEntity ucenik = ucenikRepo.findById(idUcenika).get();
		List<OdeljenjePredmetNastavnikEntity> predmetiUcenika = odeljenjePredmetNastavnikRepo
				.findByOdeljenje(ucenik.getOdeljenjeUcenika());
		List<OceneUcenikaDTO> oceneUcenikaDTO = new ArrayList<OceneUcenikaDTO>();
		for (OdeljenjePredmetNastavnikEntity nekiPredmet : predmetiUcenika) {
			OceneUcenikaDTO ocenaUcenikaDTO = new OceneUcenikaDTO();
			List<OcenaEntity> trazeneOceneKontrolni = new ArrayList<OcenaEntity>();
			List<OcenaEntity> trazeneOcenePismeni = new ArrayList<OcenaEntity>();
			List<OcenaEntity> trazeneOceneZakljucna = new ArrayList<OcenaEntity>();
			List<OcenaEntity> ocenePredmeta = new ArrayList<OcenaEntity>();
			ocenePredmeta = ocenaRepo.findByOdeljenjePredmetNastavnik(nekiPredmet);
			for (OcenaEntity nekaOcena : ocenePredmeta) {
				if (nekaOcena.getUcenik().getIdUcenika().equals(idUcenika)) {
					if (nekaOcena.getTipOcene().equals(TipOcene.KONTROLNI)) {
						trazeneOceneKontrolni.add(nekaOcena);
					} else if (nekaOcena.getTipOcene().equals(TipOcene.PISMENI)) {
						trazeneOcenePismeni.add(nekaOcena);
					} else
						trazeneOceneZakljucna.add(nekaOcena);
				}
			}
			Integer idPredmeta = nekiPredmet.getPredmet().getIdPredmeta();
			Integer idOdeljenjePredmetNastavnik = nekiPredmet.getIdOdeljenjePredmetNastavnik();
			String imePredmeta = nekiPredmet.getPredmet().getImePredmeta();
			ocenaUcenikaDTO.setImePredmeta(imePredmeta);
			ocenaUcenikaDTO.setIdPredmeta(idPredmeta);
			ocenaUcenikaDTO.setIdOdeljenjePredmetNastavnik(idOdeljenjePredmetNastavnik);
			ocenaUcenikaDTO.setOcenaKontrolniUcenika(trazeneOceneKontrolni);
			ocenaUcenikaDTO.setOcenaPismeniUcenika(trazeneOcenePismeni);
			ocenaUcenikaDTO.setOcenaZakljucnaUcenika(trazeneOceneZakljucna);
			oceneUcenikaDTO.add(ocenaUcenikaDTO);
		}
		return new ResponseEntity<Iterable<OceneUcenikaDTO>>(oceneUcenikaDTO, HttpStatus.OK);
	}

	// 7.7 ocene odeljenja ili ti predmeta ucenika tog razreda- negde sam promenio nesto i sada ne radi :)
	@RequestMapping(value = "/ocene-ucenika/{idOdeljenjePredmetNastavnik}", method = RequestMethod.GET)
	public ResponseEntity<?> pronaciOceneUcenikaPredmeta(@PathVariable Integer idOdeljenjePredmetNastavnik) {

		if (odeljenjePredmetNastavnikRepo.findById(idOdeljenjePredmetNastavnik).isPresent() == false) {
			return new ResponseEntity<RESTError>(new RESTError("Odeljnje predmet nastavnik za ovaj id ne postoji"),
					HttpStatus.NOT_FOUND);
		}
		OdeljenjePredmetNastavnikEntity odeljenjePredmetNastavnik = odeljenjePredmetNastavnikRepo
				.findById(idOdeljenjePredmetNastavnik).get();
		OdeljenjeEntity odeljenje = odeljenjePredmetNastavnik.getOdeljenje();
		List<UcenikEntity> ucenici = (List<UcenikEntity>) ucenikRepo.findAll();
		List<UcenikEntity> trazeniUcenici = new ArrayList<UcenikEntity>();
		List<OceneUcenikaPredmetaDTO> uceniciOcene = new ArrayList<OceneUcenikaPredmetaDTO>();
		for (UcenikEntity nekiUcenik : ucenici) {
			if (nekiUcenik.getOdeljenjeUcenika().equals(odeljenje)) {
				trazeniUcenici.add(nekiUcenik);
			}
		}
		for (UcenikEntity nekiUcenikTrazeni : trazeniUcenici) {
			OceneUcenikaPredmetaDTO ucenikOcene = new OceneUcenikaPredmetaDTO();
			List<Integer> trazeneOceneKontrolni = new ArrayList<Integer>();
			List<Integer> trazeneOcenePismeni = new ArrayList<Integer>();
			List<Integer> trazeneOceneZakljucna = new ArrayList<Integer>();
			List<OcenaEntity> ocene = ocenaRepo.findByUcenik(nekiUcenikTrazeni);
			for (OcenaEntity nekeOcene : ocene) {
				if (nekeOcene.getOdeljenjePredmetNastavnik().equals(odeljenjePredmetNastavnik)) {
					if (nekeOcene.getTipOcene().equals(TipOcene.KONTROLNI)) {
						trazeneOceneKontrolni.add(nekeOcene.getOcena());
					} else if (nekeOcene.getTipOcene().equals(TipOcene.PISMENI)) {
						trazeneOcenePismeni.add(nekeOcene.getOcena());
					} else
						trazeneOceneZakljucna.add(nekeOcene.getOcena());
				}
			}
			ucenikOcene.setImeUcenika(nekiUcenikTrazeni.getImeUcenika());
			ucenikOcene.setPrezimeUcenika(nekiUcenikTrazeni.getPrezimeUcenika());
			ucenikOcene.setOcenaKontrolniUcenika(trazeneOceneKontrolni);
			ucenikOcene.setOcenaPismeniUcenika(trazeneOcenePismeni);
			ucenikOcene.setOcenaZakljucnaUcenika(trazeneOceneZakljucna);
			uceniciOcene.add(ucenikOcene);
		}
		return new ResponseEntity<List<OceneUcenikaPredmetaDTO>>(uceniciOcene, HttpStatus.OK);
	}
	//naci ocenu po datumu za danas ili bilo koji drugi dan
	/*@RequestMapping(value = "/datum", method = RequestMethod.GET)
	public ResponseEntity<?> pronaciOcenePoDatumu(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam
            ("datumOcene") Date datumOcene) {

		List<OcenaEntity> ocenePoDatumu = new ArrayList<>();
		 ocenePoDatumu = (List<OcenaEntity>)ocenaRepo.findByDatumOcene(datumOcene);
		if ((ocenePoDatumu.size() == 0)) {
			return new ResponseEntity<RESTError>(new RESTError("Ocene za ovaj datum nema"),
					HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Iterable<OcenaEntity>>(ocenePoDatumu, HttpStatus.OK);
	}*/
	
	//naci ocenu po datumu za danas ili bilo koji drugi dan
	@RequestMapping(value = "/datumom", method = RequestMethod.GET)
		public ResponseEntity<?> pronaciOcenePoDatumuNovo(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam
	            ("datumOcene") Date datumOcene) {

			List<OcenaEntity> ocenePoDatumu = new ArrayList<>();
			 ocenePoDatumu = (List<OcenaEntity>)ocenaRepo.findByDatumOcene(datumOcene);
			if ((ocenePoDatumu.size() == 0)) {
				return new ResponseEntity<RESTError>(new RESTError("Ocene za ovaj datum nema"),
						HttpStatus.NOT_FOUND);
			}
			OceneDatumDTO ocene = new OceneDatumDTO();
			ocene.setDatumOcene(datumOcene);
			Integer x= (Integer)ocenePoDatumu.size();
			ocene.setDateOcene(x);
			return new ResponseEntity<OceneDatumDTO>(ocene, HttpStatus.OK);
		}
	
	//naci ocenu po datumu za danas ili bilo koji drugi dan
	/*	@RequestMapping(value = "/dat", method = RequestMethod.GET)
			public ResponseEntity<?> pronaciOcenePoDvaDatumuNovo(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam
		            ("startDatum") Date startDatum,@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam
            ("krajDatum") Date krajDatum) throws ParseException {
			
			 Calendar start = Calendar.getInstance();
			 Calendar kraj = Calendar.getInstance();
			 start.setTime(startDatum);
			 start.setTime(krajDatum);
			 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			 
			 List< OceneDatumDTO> sveOcene = new ArrayList<>();
			if(start.compareTo(kraj)>0){
				
				String output = sdf.format(start.getTime());
				 
				Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(output);
				
				List<OcenaEntity> ocenePoDatumu = new ArrayList<>();
				 ocenePoDatumu = (List<OcenaEntity>)ocenaRepo.findByDatumOcene(date1);
				 OceneDatumDTO ocene = new OceneDatumDTO();
					ocene.setDatumOcene(date1);
					Integer x= (Integer)ocenePoDatumu.size();
					ocene.setDateOcene(x);
					sveOcene.add(ocene);
					
				 start.add(Calendar.DATE, 1);
			}
			 
				return new ResponseEntity<List<OceneDatumDTO>>(sveOcene, HttpStatus.OK);
			}
	*/
	
		@RequestMapping(value = "/datumi", method = RequestMethod.GET)
		public ResponseEntity<?> pronaciOcenePoDvaDatumuNovooo(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam
	            ("startDatum") Date startDatum,@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam
        ("krajDatum") Date krajDatum) throws ParseException {
		
		 Calendar start = Calendar.getInstance();
		 Calendar kraj = Calendar.getInstance();
		 start.setTime(startDatum);
		 kraj.setTime(krajDatum); 
		 kraj.add(Calendar.DATE, 1);
		 
		 List< OceneDatumDTO> sveOcene = new ArrayList<>();
		 		while(start.before(kraj)){
			 
		 			Date currentDatePlusOnevvvvvvvvvvvvvvvvvv=new Date();
			 currentDatePlusOnevvvvvvvvvvvvvvvvvv =  start.getTime();
			 
			// SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			 //Date datumOcene=formatter.parse(formatter.format(currentDatePlusOnevvvvvvvvvvvvvvvvvv));
			 
			List<OcenaEntity> ocenePoDatumu = new ArrayList<>();
			 ocenePoDatumu = (List<OcenaEntity>)ocenaRepo.findByDatumOcene(currentDatePlusOnevvvvvvvvvvvvvvvvvv);
			 OceneDatumDTO ocene = new OceneDatumDTO();
				ocene.setDatumOcene(currentDatePlusOnevvvvvvvvvvvvvvvvvv);
				Integer x= (Integer)ocenePoDatumu.size();
				ocene.setDateOcene(x);
				sveOcene.add(ocene);
				start.add(Calendar.DATE, 1);
				x=0;
		}
			return new ResponseEntity<List<OceneDatumDTO>>(sveOcene, HttpStatus.OK);
		}
		
	//naci ocenu po datumu za danas ili bilo koji drugi dan
		/*	@RequestMapping(value = "/datum/{datumOcene}", method = RequestMethod.GET)
			public ResponseEntity<?> pronaciOcenePoDatumima( @PathVariable
		            ("datumOcene") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date datumOcene) {

				List<OcenaEntity> ocenePoDatumu = new ArrayList<>();
				 ocenePoDatumu = (List<OcenaEntity>)ocenaRepo.findByDatumOcene(datumOcene);
				if ((ocenePoDatumu.size() == 0)) {
					return new ResponseEntity<RESTError>(new RESTError("Ocene za ovaj datum nema"),
							HttpStatus.NOT_FOUND);
				}
				
				return new ResponseEntity<Iterable<OcenaEntity>>(ocenePoDatumu, HttpStatus.OK);
			}
	*/
}

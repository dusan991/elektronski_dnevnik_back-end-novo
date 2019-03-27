package com.iktpreobuka.elektronskidnevnik.entities.dto;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.iktpreobuka.elektronskidnevnik.entities.OcenaEntity;
import com.iktpreobuka.elektronskidnevnik.entities.enumerations.TipOcene;

public class OceneUcenikaDTO {

	private String imePredmeta;

	private Integer idPredmeta;
	
	private Integer idOdeljenjePredmetNastavnik;
	
	private List<OcenaEntity> ocenaKontrolniUcenika;

	private List<OcenaEntity> ocenaPismeniUcenika;

	private List<OcenaEntity> ocenaZakljucnaUcenika;

	public List<OcenaEntity> getOcenaKontrolniUcenika() {
		return ocenaKontrolniUcenika;
	}

	public void setOcenaKontrolniUcenika(List<OcenaEntity> ocenaKontrolniUcenika) {
		this.ocenaKontrolniUcenika = ocenaKontrolniUcenika;
	}

	public List<OcenaEntity> getOcenaPismeniUcenika() {
		return ocenaPismeniUcenika;
	}

	public void setOcenaPismeniUcenika(List<OcenaEntity> ocenaPismeniUcenika) {
		this.ocenaPismeniUcenika = ocenaPismeniUcenika;
	}

	public List<OcenaEntity> getOcenaZakljucnaUcenika() {
		return ocenaZakljucnaUcenika;
	}

	public void setOcenaZakljucnaUcenika(List<OcenaEntity> ocenaZakljucnaUcenika) {
		this.ocenaZakljucnaUcenika = ocenaZakljucnaUcenika;
	}

	public OceneUcenikaDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getImePredmeta() {
		return imePredmeta;
	}

	public void setImePredmeta(String imePredmeta) {
		this.imePredmeta = imePredmeta;
	}
	
	public Integer getIdPredmeta() {
		return idPredmeta;
	}

	public void setIdPredmeta(Integer idPredmeta) {
		this.idPredmeta = idPredmeta;
	}

	public Integer getIdOdeljenjePredmetNastavnik() {
		return idOdeljenjePredmetNastavnik;
	}

	public void setIdOdeljenjePredmetNastavnik(Integer idOdeljenjePredmetNastavnik) {
		this.idOdeljenjePredmetNastavnik = idOdeljenjePredmetNastavnik;
	}


}

package com.iktpreobuka.elektronskidnevnik.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idOdeljenjePredmetNastavnik")
public class OdeljenjePredmetNastavnikEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer idOdeljenjePredmetNastavnik;

	private Integer idNastavnika;
	
	// @JsonManagedReference
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "odeljenje")
	private OdeljenjeEntity odeljenje;

	// @JsonManagedReference
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "predmet")
	private PredmetEntity predmet;

	// @JsonManagedReference
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "nastavnik")
	private NastavnikEntity nastavnik;

	// @JsonBackReference
	@OneToMany(mappedBy = "odeljenjePredmetNastavnik", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JsonIgnore
	private List<OcenaEntity> ocene;

	public OdeljenjePredmetNastavnikEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getIdOdeljenjePredmetNastavnik() {
		return idOdeljenjePredmetNastavnik;
	}

	public void setIdOdeljenjePredmetNastavnik(Integer idOdeljenjePredmetNastavnik) {
		this.idOdeljenjePredmetNastavnik = idOdeljenjePredmetNastavnik;
	}

	public OdeljenjeEntity getOdeljenje() {
		return odeljenje;
	}

	public void setOdeljenje(OdeljenjeEntity odeljenje) {
		this.odeljenje = odeljenje;
	}

	public PredmetEntity getPredmet() {
		return predmet;
	}

	public void setPredmet(PredmetEntity predmet) {
		this.predmet = predmet;
	}

	public NastavnikEntity getNastavnik() {
		return nastavnik;
	}

	public void setNastavnik(NastavnikEntity nastavnik) {
		this.nastavnik = nastavnik;
	}

	public List<OcenaEntity> getOcene() {
		return ocene;
	}

	public void setOcene(List<OcenaEntity> ocene) {
		this.ocene = ocene;
	}

	public Integer getIdNastavnika() {
		return nastavnik.getIdNastavnika();
	}

	public void setIdNastavnika(Integer idNastavnika) {
		nastavnik.setIdNastavnika(idNastavnika);
	}
	
	

}

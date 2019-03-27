package com.iktpreobuka.elektronskidnevnik.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idOdeljenja")
public class OdeljenjeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer idOdeljenja;

	@NotNull(message = "Ime odeljenja treba obezbediti")
	private String imeOdeljenja;

	@NotNull(message = "Broj razreda treba obezbediti")
	@Max(value = 8, message = "Razred treba uneti kao jednocifreni broj izmedju 1 i 8.")
	@Min(value = 1, message = "Razred treba uneti kao jednocifreni broj izmedju 1 i 8.")
	private Integer razred;

	@NotNull(message = "Skolsku godinu treba obezbediti")
	private String skolskaGodinaRazreda;

	// @JsonBackReference
	@OneToMany(mappedBy = "odeljenjeUcenika", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JsonIgnore
	private List<UcenikEntity> ucenici;

	// @JsonBackReference
	@OneToMany(mappedBy = "odeljenje", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JsonIgnore
	private List<OdeljenjePredmetNastavnikEntity> predmetiOdeljenja;

	public OdeljenjeEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getIdOdeljenja() {
		return idOdeljenja;
	}

	public void setIdOdeljenja(Integer idOdeljenja) {
		this.idOdeljenja = idOdeljenja;
	}

	public String getImeOdeljenja() {
		return imeOdeljenja;
	}

	public void setImeOdeljenja(String imeOdeljenja) {
		this.imeOdeljenja = imeOdeljenja;
	}

	public Integer getRazred() {
		return razred;
	}

	public void setRazred(Integer razred) {
		this.razred = razred;
	}

	public String getSkolskaGodinaRazreda() {
		return skolskaGodinaRazreda;
	}

	public void setSkolskaGodinaRazreda(String skolskaGodinaRazreda) {
		this.skolskaGodinaRazreda = skolskaGodinaRazreda;
	}

	public List<UcenikEntity> getUcenici() {
		return ucenici;
	}

	public void setUcenici(List<UcenikEntity> ucenici) {
		this.ucenici = ucenici;
	}

	public List<OdeljenjePredmetNastavnikEntity> getPredmetiOdeljenja() {
		return predmetiOdeljenja;
	}

	public void setPredmetiOdeljenja(List<OdeljenjePredmetNastavnikEntity> predmetiOdeljenja) {
		this.predmetiOdeljenja = predmetiOdeljenja;
	}

}

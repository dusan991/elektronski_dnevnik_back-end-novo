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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idPredmeta")
public class PredmetEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer idPredmeta;

	@NotNull(message = "Ime predmeta  treba obezbediti")
	private String imePredmeta;

	@Max(value = 5, message = "Fond casova nedeljni treba uneti kao jednocifren broj izmedju 1 i 5")
	@Min(value = 1, message = "Fond casova nedeljni treba uneti kao jednocifren broj izmedju 1 i 5")
	private Integer fondCasovaNedeljni;

	// @JsonBackReference
	@OneToMany(mappedBy = "predmet", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JsonIgnore
	private List<OdeljenjePredmetNastavnikEntity> odeljenjaPredmeta;

	public PredmetEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getIdPredmeta() {
		return idPredmeta;
	}

	public void setIdPredmeta(Integer idPredmeta) {
		this.idPredmeta = idPredmeta;
	}

	public String getImePredmeta() {
		return imePredmeta;
	}

	public void setImePredmeta(String imePredmeta) {
		this.imePredmeta = imePredmeta;
	}

	public Integer getFondCasovaNedeljni() {
		return fondCasovaNedeljni;
	}

	public void setFondCasovaNedeljni(Integer fondCasovaNedeljni) {
		this.fondCasovaNedeljni = fondCasovaNedeljni;
	}

	public List<OdeljenjePredmetNastavnikEntity> getOdeljenjaPredmeta() {
		return odeljenjaPredmeta;
	}

	public void setOdeljenjaPredmeta(List<OdeljenjePredmetNastavnikEntity> odeljenjaPredmeta) {
		this.odeljenjaPredmeta = odeljenjaPredmeta;
	}

}

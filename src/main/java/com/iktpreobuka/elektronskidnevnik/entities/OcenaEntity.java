package com.iktpreobuka.elektronskidnevnik.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.elektronskidnevnik.entities.enumerations.TipOcene;

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class OcenaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer idOcene;

	@Max(value = 5, message = "Ocenu treba uneti kao jednocifreni broj izmedju 1 i 5")
	@Min(value = 1, message = "Ocenu treba uneti kao jednocifreni broj izmedju 1 i 5")
	private Integer ocena;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date datumOcene;

	private TipOcene tipOcene;

	// @JsonManagedReference
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "ucenik")
	@JsonIgnore
	private UcenikEntity ucenik;

	// @JsonManagedReference

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "odeljenjePredmetNastavnik")
	@JsonIgnore
	private OdeljenjePredmetNastavnikEntity odeljenjePredmetNastavnik;

	public OcenaEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getIdOcene() {
		return idOcene;
	}

	public void setIdOcene(Integer idOcene) {
		this.idOcene = idOcene;
	}

	public Integer getOcena() {
		return ocena;
	}

	public void setOcena(Integer ocena) {
		this.ocena = ocena;
	}

	public Date getDatumOcene() {
		return datumOcene;
	}

	public void setDatumOcene(Date datumOcene) {
		this.datumOcene = datumOcene;
	}

	public TipOcene getTipOcene() {
		return tipOcene;
	}

	public void setTipOcene(TipOcene tipOcene) {
		this.tipOcene = tipOcene;
	}

	public UcenikEntity getUcenik() {
		return ucenik;
	}

	public void setUcenik(UcenikEntity ucenik) {
		this.ucenik = ucenik;
	}

	public OdeljenjePredmetNastavnikEntity getOdeljenjePredmetNastavnik() {
		return odeljenjePredmetNastavnik;
	}

	public void setOdeljenjePredmetNastavnik(OdeljenjePredmetNastavnikEntity odeljenjePredmetNastavnik) {
		this.odeljenjePredmetNastavnik = odeljenjePredmetNastavnik;
	}

}

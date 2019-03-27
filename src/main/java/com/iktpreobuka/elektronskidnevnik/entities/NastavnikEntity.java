package com.iktpreobuka.elektronskidnevnik.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.iktpreobuka.elektronskidnevnik.entities.enumerations.TipKorisnika;

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idNastavnika")
public class NastavnikEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer idNastavnika;

	@NotNull(message = "Ime nastavnika treba obezbediti")
	@Size(min = 2, max = 20, message = "Ime nastavnika treba da je izmedju {min} i {max} karaktera")
	private String imeNastavnika;

	@NotNull(message = "Prezime nastavnika treba obezbediti")
	@Size(min = 2, max = 20, message = "Prezime nastavnika treba da je izmedju {min} i {max} karaktera")
	private String prezimeNastavnika;

	@NotNull(message = "Korisnicko ime nastavnika treba obezbediti")
	@Size(min = 5, max = 10, message = "Korisnicko ime nastavnika treba da je izmedju {min} i {max} karaktera")
	private String korisnickoImeNastavnika;


//	@NotNull(message = "Sifra nastavnika treba obezbediti")
//	@Size(min = 5, max = 10, message = "Sifra nastavnika treba da je izmedju {min} i {max} karaktera")
	//@JsonIgnore
	private String sifraNastavnika;

	private TipKorisnika korisnikNastavnik = TipKorisnika.ROLE_NASTAVNIK;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "role")
	@JsonIgnore
	private RoleEntity role;

	public RoleEntity getRole() {
		return role;
	}

	public void setRole(RoleEntity role) {
		this.role = role;
	}

	// @JsonBackReference
	@OneToMany(mappedBy = "nastavnik", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JsonIgnore
	private List<OdeljenjePredmetNastavnikEntity> predmetiOdeljenjaNastavnika;

	public NastavnikEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getIdNastavnika() {
		return idNastavnika;
	}

	public void setIdNastavnika(Integer idNastavnika) {
		this.idNastavnika = idNastavnika;
	}

	public String getImeNastavnika() {
		return imeNastavnika;
	}

	public void setImeNastavnika(String imeNastavnika) {
		this.imeNastavnika = imeNastavnika;
	}

	public String getPrezimeNastavnika() {
		return prezimeNastavnika;
	}

	public void setPrezimeNastavnika(String prezimeNastavnika) {
		this.prezimeNastavnika = prezimeNastavnika;
	}

	public String getKorisnickoImeNastavnika() {
		return korisnickoImeNastavnika;
	}

	public void setKorisnickoImeNastavnika(String korisnickoImeNastavnika) {
		this.korisnickoImeNastavnika = korisnickoImeNastavnika;
	}

	public String getSifraNastavnika() {
		return sifraNastavnika;
	}

	public void setSifraNastavnika(String sifraNastavnika) {
		this.sifraNastavnika = sifraNastavnika;
	}

	public TipKorisnika getKorisnikNastavnik() {
		return korisnikNastavnik;
	}

	public void setKorisnikNastavnik(TipKorisnika korisnikNastavnik) {
		this.korisnikNastavnik = korisnikNastavnik;
	}

	public List<OdeljenjePredmetNastavnikEntity> getPredmetiOdeljenjaNastavnika() {
		return predmetiOdeljenjaNastavnika;
	}

	public void setPredmetiOdeljenjaNastavnika(List<OdeljenjePredmetNastavnikEntity> predmetiOdeljenjaNastavnika) {
		this.predmetiOdeljenjaNastavnika = predmetiOdeljenjaNastavnika;
	}

}
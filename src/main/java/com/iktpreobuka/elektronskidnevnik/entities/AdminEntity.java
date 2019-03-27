package com.iktpreobuka.elektronskidnevnik.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.elektronskidnevnik.entities.enumerations.TipKorisnika;

@Entity
public class AdminEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer idAdmina;

	@NotNull(message = "Korisnicko ime admina treba obezbediti")
	@Size(min = 2, max = 10, message = "Korisnicko ime admina treba da je izmedju {min} i {max} karaktera")
	private String korisnickoImeAdmina;

	// @NotNull(message = "Sifra mora admina treba obezbediti")
	// @Size(min = 2, max = 10, message = "Sifra admina treba da je izmedju
	// {min} i {max} karaktera")
	// @JsonIgnore
	private String sifraAdmina;

	private TipKorisnika korisnikAdmina = TipKorisnika.ROLE_ADMIN;

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

	public AdminEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getIdAdmina() {
		return idAdmina;
	}

	public void setIdAdmina(Integer idAdmina) {
		this.idAdmina = idAdmina;
	}

	public String getKorisnickoImeAdmina() {
		return korisnickoImeAdmina;
	}

	public void setKorisnickoImeAdmina(String korisnickoImeAdmina) {
		this.korisnickoImeAdmina = korisnickoImeAdmina;
	}

	public String getSifraAdmina() {
		return sifraAdmina;
	}

	public void setSifraAdmina(String sifraAdmina) {
		this.sifraAdmina = sifraAdmina;
	}

	public TipKorisnika getKorisnikAdmina() {
		return korisnikAdmina;
	}

	public void setKorisnikAdmina(TipKorisnika korisnikAdmina) {
		this.korisnikAdmina = korisnikAdmina;
	}

}

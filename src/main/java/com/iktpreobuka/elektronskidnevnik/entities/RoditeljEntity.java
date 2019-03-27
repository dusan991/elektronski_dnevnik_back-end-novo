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
import javax.validation.constraints.Pattern;
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
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idRoditelja")
public class RoditeljEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer idRoditelja;

	private String imeRoditelja;

	private String prezimeRoditelja;

	private String korisnickoImeRoditelja;

	private String sifraRoditelja;

	private String emailRoditelja;

	private TipKorisnika ulogaRoditelja = TipKorisnika.ROLE_RODITELJ;

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
	@OneToMany(mappedBy = "roditelj", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JsonIgnore
	private List<UcenikEntity> uceniciRoditelja;

	public RoditeljEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getIdRoditelja() {
		return idRoditelja;
	}

	public void setIdRoditelja(Integer idRoditelja) {
		this.idRoditelja = idRoditelja;
	}

	public String getImeRoditelja() {
		return imeRoditelja;
	}

	public void setImeRoditelja(String imeRoditelja) {
		this.imeRoditelja = imeRoditelja;
	}

	public String getPrezimeRoditelja() {
		return prezimeRoditelja;
	}

	public void setPrezimeRoditelja(String prezimeRoditelja) {
		this.prezimeRoditelja = prezimeRoditelja;
	}

	public String getKorisnickoImeRoditelja() {
		return korisnickoImeRoditelja;
	}

	public void setKorisnickoImeRoditelja(String korisnickoImeRoditelja) {
		this.korisnickoImeRoditelja = korisnickoImeRoditelja;
	}

	public String getSifraRoditelja() {
		return sifraRoditelja;
	}

	public void setSifraRoditelja(String sifraRoditelja) {
		this.sifraRoditelja = sifraRoditelja;
	}

	public String getEmailRoditelja() {
		return emailRoditelja;
	}

	public void setEmailRoditelja(String emailRoditelja) {
		this.emailRoditelja = emailRoditelja;
	}

	public TipKorisnika getUlogaRoditelja() {
		return ulogaRoditelja;
	}

	public void setUlogaRoditelja(TipKorisnika ulogaRoditelja) {
		this.ulogaRoditelja = ulogaRoditelja;
	}

	public List<UcenikEntity> getUceniciRoditelja() {
		return uceniciRoditelja;
	}

	public void setUceniciRoditelja(List<UcenikEntity> uceniciRoditelja) {
		this.uceniciRoditelja = uceniciRoditelja;
	}

}

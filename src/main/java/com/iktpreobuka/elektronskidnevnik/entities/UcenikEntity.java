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
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.iktpreobuka.elektronskidnevnik.entities.enumerations.TipKorisnika;

@Entity
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "idUcenika")
public class UcenikEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer idUcenika;

	@NotNull(message = "Ime ucenika treba obezbediti")
	@Size(min = 2, max = 20, message = "Ime ucenika treba da je izmedju {min} i {max} karaktera")
	private String imeUcenika;

	@NotNull(message = "Prezime ucenika mora biti uneto.")
	@Size(min = 2, max = 20, message = "Prezime ucenika treba da je izmedju {min} i {max} karaktera")
	private String prezimeUcenika;

	@NotNull(message = "Korisnicko ime ucenika treba obezbediti")
	@Size(min = 5, max = 10, message = "Korisnicko ime ucenika treba da je izmedju {min} i {max} karaktera")
	private String korisnickoImeUcenika;


	//@JsonIgnore
	private String sifraUcenika;

	private TipKorisnika korisnikUcenika = TipKorisnika.ROLE_UCENIK;

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

	// @JsonManagedReference
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "roditelj")
	@JsonIgnore
	private RoditeljEntity roditelj;

	private Integer IdRoditelja;
	
	// @JsonManagedReference
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "odeljenjeUcenika")
	@JsonIgnore
	private OdeljenjeEntity odeljenjeUcenika;

	// @JsonBackReference
	@OneToMany(mappedBy = "ucenik", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JsonIgnore
	private List<OcenaEntity> ocene;

	public UcenikEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getIdUcenika() {
		return idUcenika;
	}

	public void setIdUcenika(Integer idUcenika) {
		this.idUcenika = idUcenika;
	}

	public String getImeUcenika() {
		return imeUcenika;
	}

	public void setImeUcenika(String imeUcenika) {
		this.imeUcenika = imeUcenika;
	}

	public String getPrezimeUcenika() {
		return prezimeUcenika;
	}

	public void setPrezimeUcenika(String prezimeUcenika) {
		this.prezimeUcenika = prezimeUcenika;
	}

	public String getKorisnickoImeUcenika() {
		return korisnickoImeUcenika;
	}

	public void setKorisnickoImeUcenika(String korisnickoImeUcenika) {
		this.korisnickoImeUcenika = korisnickoImeUcenika;
	}

	public String getSifraUcenika() {
		return sifraUcenika;
	}

	public void setSifraUcenika(String sifraUcenika) {
		this.sifraUcenika = sifraUcenika;
	}

	public TipKorisnika getKorisnikUcenika() {
		return korisnikUcenika;
	}

	public void setKorisnikUcenika(TipKorisnika korisnikUcenika) {
		this.korisnikUcenika = korisnikUcenika;
	}

	public RoditeljEntity getRoditelj() {
		return roditelj;
	}

	public void setRoditelj(RoditeljEntity roditelj) {
		this.roditelj = roditelj;
	}

	public OdeljenjeEntity getOdeljenjeUcenika() {
		return odeljenjeUcenika;
	}

	public void setOdeljenjeUcenika(OdeljenjeEntity odeljenjeUcenika) {
		this.odeljenjeUcenika = odeljenjeUcenika;
	}

	public List<OcenaEntity> getOcene() {
		return ocene;
	}

	public void setOcene(List<OcenaEntity> ocene) {
		this.ocene = ocene;
	}

	public Integer getIdRoditelja() {
		return roditelj.getIdRoditelja();
	}

	public void setIdRoditelja(Integer idRoditelja) {
		roditelj.setIdRoditelja(idRoditelja);;
	}
	
	

}

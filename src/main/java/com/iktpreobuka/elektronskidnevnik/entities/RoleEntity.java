package com.iktpreobuka.elektronskidnevnik.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class RoleEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)

	private Integer id;
	private String name;

	@JsonIgnore
	@OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	private List<UcenikEntity> ucenici = new ArrayList<>();

	@JsonIgnore
	@OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	private List<RoditeljEntity> roditelji = new ArrayList<>();

	@JsonIgnore
	@OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	private List<NastavnikEntity> nastavnici = new ArrayList<>();

	@JsonIgnore
	@OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	private List<AdminEntity> admini = new ArrayList<>();

	public List<RoditeljEntity> getRoditelji() {
		return roditelji;
	}

	public void setRoditelji(List<RoditeljEntity> roditelji) {
		this.roditelji = roditelji;
	}

	public List<NastavnikEntity> getNastavnici() {
		return nastavnici;
	}

	public void setNastavnici(List<NastavnikEntity> nastavnici) {
		this.nastavnici = nastavnici;
	}

	public List<AdminEntity> getAdmini() {
		return admini;
	}

	public void setAdmini(List<AdminEntity> admini) {
		this.admini = admini;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<UcenikEntity> getUcenici() {
		return ucenici;
	}

	public void setUcenici(List<UcenikEntity> ucenici) {
		this.ucenici = ucenici;
	}

	public RoleEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

}

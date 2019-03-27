package com.iktpreobuka.elektronskidnevnik.entities.dto;

import java.util.List;

public class OceneUcenikaPredmetaDTO {

	private String imeUcenika;

	private String prezimeUcenika;

	private List<Integer> ocenaKontrolniUcenika;

	private List<Integer> ocenaPismeniUcenika;

	private List<Integer> ocenaZakljucnaUcenika;

	public List<Integer> getOcenaKontrolniUcenika() {
		return ocenaKontrolniUcenika;
	}

	public void setOcenaKontrolniUcenika(List<Integer> ocenaKontrolniUcenika) {
		this.ocenaKontrolniUcenika = ocenaKontrolniUcenika;
	}

	public List<Integer> getOcenaPismeniUcenika() {
		return ocenaPismeniUcenika;
	}

	public void setOcenaPismeniUcenika(List<Integer> ocenaPismeniUcenika) {
		this.ocenaPismeniUcenika = ocenaPismeniUcenika;
	}

	public List<Integer> getOcenaZakljucnaUcenika() {
		return ocenaZakljucnaUcenika;
	}

	public void setOcenaZakljucnaUcenika(List<Integer> ocenaZakljucnaUcenika) {
		this.ocenaZakljucnaUcenika = ocenaZakljucnaUcenika;
	}

	public OceneUcenikaPredmetaDTO() {
		super();
		// TODO Auto-generated constructor stub
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

}

package com.iktpreobuka.elektronskidnevnik.entities.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class OceneDatumDTO {
	
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date datumOcene;
	
	private Integer dateOcene;
	

	public OceneDatumDTO() {
		super();
	}

	public Date getDatumOcene() {
		return datumOcene;
	}

	public void setDatumOcene(Date datumOcene) {
		this.datumOcene = datumOcene;
	}

	public Integer getDateOcene() {
		return dateOcene;
	}

	public void setDateOcene(Integer dateOcene) {
		this.dateOcene = dateOcene;
	}

}

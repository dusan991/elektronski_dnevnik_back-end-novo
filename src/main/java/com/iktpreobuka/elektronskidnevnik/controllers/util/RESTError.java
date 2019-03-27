package com.iktpreobuka.elektronskidnevnik.controllers.util;

public class RESTError {
	private String message;

	public RESTError(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}

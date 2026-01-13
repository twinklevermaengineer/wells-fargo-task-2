package com.wellsfargo.counselor.rest;

public class AdvisorCreationException extends RuntimeException {
	
	public AdvisorCreationException(String message) {
		super(message);
	}

	public AdvisorCreationException(String message, Throwable cause) {
		super(message, cause);
	}

}

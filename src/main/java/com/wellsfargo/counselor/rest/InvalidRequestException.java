package com.wellsfargo.counselor.rest;

public class InvalidRequestException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4547655157237228196L;

	public InvalidRequestException(String message) {
	super(message);
}
}

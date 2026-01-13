package com.wellsfargo.counselor.rest;


public class UserNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 544555935376385145L;

	public UserNotFoundException(String message) {
		super(message);
	}
}

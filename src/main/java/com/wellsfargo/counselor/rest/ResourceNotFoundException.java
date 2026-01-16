package com.wellsfargo.counselor.rest;


public class ResourceNotFoundException extends RuntimeException{


	/**
	 * checks serialized uid == class uid, if same then allows deserialization else InvalidClassException
	 */
	private static final long serialVersionUID = 544555935376385145L;

	public ResourceNotFoundException(String message) {
		super(message);
	}
}

package com.wellsfargo.counselor.rest;

public class ResourceCreationException extends RuntimeException {
	
	

	/**
	 * checks serialized uid == class uid, if same then allows deserialization else InvalidClassException
	 */
	private static final long serialVersionUID = -3330183677072886655L;

	public ResourceCreationException(String message) {
		super(message);
	}

	public ResourceCreationException(String message, Throwable cause) {
		super(message, cause);
	}

}

package com.wellsfargo.counselor.rest;

public class InvalidRequestException extends RuntimeException{

	/**
	 * checks serialized uid == class uid, if same then allows deserialization else InvalidClassException
	 */
	private static final long serialVersionUID = -4547655157237228196L;

	public InvalidRequestException(String message) {
	super(message);
}
}

package com.wellsfargo.counselor.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

@ExceptionHandler(ResourceNotFoundException.class)
public ResponseEntity<CustomErrorResponse>handleUserNotFoundException(ResourceNotFoundException e){
	logger.warn("User not found : {} " ,e.getMessage());
	CustomErrorResponse error = new CustomErrorResponse();
	
	error.setStatus(HttpStatus.NOT_FOUND.value());
	error.setMessage(e.getMessage());
	
	return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
}

@ExceptionHandler(ResourceCreationException.class)
public ResponseEntity<CustomErrorResponse>handleAdvisorCreationException(ResourceCreationException e){
	logger.warn("Resource creation failed: {} ", e.getMessage());
	CustomErrorResponse error = new CustomErrorResponse();
	error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
	error.setMessage(e.getMessage());
	
	return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
}

@ExceptionHandler(InvalidRequestException.class)
public ResponseEntity<CustomErrorResponse>handleInvalidRequest(InvalidRequestException e){
	logger.warn("Invalid resource request: {} ", e.getMessage());
	CustomErrorResponse error = new CustomErrorResponse();
	error.setStatus(HttpStatus.BAD_REQUEST.value());
	error.setMessage(e.getMessage());
	
	return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
}

@ExceptionHandler(ServerException.class)
public ResponseEntity<CustomErrorResponse>handleServerException(ServerException e){
	logger.warn("Server exception occured: {} ", e.getMessage(), e);
	CustomErrorResponse error = new CustomErrorResponse();
	error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
	error.setMessage(e.getMessage());
	
	return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	
}

@ExceptionHandler(Exception.class)
public ResponseEntity<CustomErrorResponse>handleExceptions(Exception e){
	logger.warn("Unhandled exception occured : {} ", e.getMessage());
	CustomErrorResponse error = new CustomErrorResponse();
	error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
	error.setMessage("An unexpected error occured " + e.getMessage());
	return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
 }
}
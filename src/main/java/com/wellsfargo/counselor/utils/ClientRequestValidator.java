package com.wellsfargo.counselor.utils;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.wellsfargo.counselor.entity.Client;
import com.wellsfargo.counselor.model.request.ClientRequest;
import com.wellsfargo.counselor.repository.ClientRepository;

@Component
public class ClientRequestValidator {
	
	
	private static final Logger logger = LoggerFactory.getLogger(ClientRequestValidator.class);
	final Pattern EMAIL_PATTERN = Pattern.compile(
			"^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
		    "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
	
	private ClientRepository clientRepository;
	
	public ClientRequestValidator(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}

	public void validateClientRequest(
			ClientRequest clientRequest
			,Long inputClientId,
			List<String> errors, String action) {
		
		basicClientValidation(clientRequest, errors);
		logger.info("Validating client request");
		if(errors.size() > 0) {
			logger.error("Basic validation failed for client request");
		}
		
		String email = clientRequest.getEmail();
		if (email != null && !email.isBlank()) {
		    List<Client> existingClientListUsingEmail = clientRepository.findByEmail(email);
		    if (!existingClientListUsingEmail.isEmpty()) {
		        if ("create".equalsIgnoreCase(action)) {
		            logger.warn("Client with similar email found");
		            errors.add("Client with similar email found");
		        } else if ("update".equalsIgnoreCase(action)) {
		            for (Client clientFromDB : existingClientListUsingEmail) {
		                if (!Objects.equals(clientFromDB.getClientId(), inputClientId)) {
		                    logger.warn("Different client with similar email found");
		                    errors.add("Different client with similar email found");
		                }
		            }
		        }
		    }
		}
		
		String phone = clientRequest.getPhone();
		if (phone != null && !phone.isBlank()) {
		    List<Client> existingClientListUsingPhone = clientRepository.findByPhone(phone);
		    if (!existingClientListUsingPhone.isEmpty()) {
		        if ("create".equalsIgnoreCase(action)) {
		            logger.warn("Client with similar phone number found");
		            errors.add("Client with similar phone number found");
		        } else if ("update".equalsIgnoreCase(action)) {
		            for (Client client : existingClientListUsingPhone) {
		                if (!Objects.equals(client.getClientId(), inputClientId)) {
		                    logger.warn("Different client with similar phone number found");
		                    errors.add("Different client with similar phone number found");
		                }
		            }
		        }
		    }
		}
	}
	
	protected void basicClientValidation(ClientRequest clientRequest, List<String> errors) {
		
		logger.info("Validating first name {} ", clientRequest.getFirstName());
		isFirstNameValid(clientRequest.getFirstName(), errors);
		
		logger.info("Validating last name {} ", clientRequest.getLastName());
		isLastNameValid(clientRequest.getLastName(), errors);
		
		logger.info("Validating email address {} ", clientRequest.getEmail());
		isEmailValid(clientRequest.getEmail(), errors);
		
		logger.info("Validating phone number {} ", clientRequest.getPhone());
		isPhoneValid(clientRequest.getPhone(), errors);
		
		logger.info("Validating address {} ", clientRequest.getAddress());
		isAddressValid(clientRequest.getAddress(), errors);
	}

	protected void isFirstNameValid(String firstName, List<String> errors) {
		if(firstName == null) {
			logger.error("First name cannot be null");
			errors.add("First name cannot be null");	
		}
		else if(firstName.trim().length() > 100) {
			logger.error("First name cannot exceed 100 alpha characters");
			errors.add("First name can have only 100 alpha characters");
		}
		else if(!firstName.matches("^[A-Za-z' -]+$")) {
			logger.error("First name can have only alpha characters");
			errors.add("First name can have only alpha characters");
		}
	}
	
	protected void isLastNameValid(String lastName, List<String> errors) {
		if(lastName == null) {
			logger.error("Last name cannot be null");
			errors.add("Last name cannot be null");
		}
		else if(lastName.trim().length() > 100) {
			logger.error("Last name cannot exceed 100 alpha characters");
			errors.add("Last name can only have 100  alpha characters");
		}
		else if(!lastName.matches("^[A-Za-z' -]+$")) {
			logger.error("Last name can have only alpha characters");
			errors.add("Last name can have only alpha characters");
		}
	}
	
	protected void isEmailValid(String email, List<String> errors) {
		
		if(email == null || email.isBlank()) {
			logger.error("Email cannot be null or blank");
			errors.add("Email cannot be null or blank");
		}
		else if(!EMAIL_PATTERN.matcher(email).matches()) {
			logger.error("Invalid email entered");
			errors.add("Invalid email entered");
		}
	}
	
	protected void isPhoneValid(String phone, List<String> errors) {
		if(phone == null || phone.isBlank()) {
			logger.error("Phone number cannot be null or blank");
			errors.add("Phone number cannot be null or blank");
		}
		else if(!phone.matches("\\d{10}")){
			logger.error("Phone number can have only 10digit numeric numbers");
			errors.add("Phone number can have only 10digit numeric numbers");
		}
	}
	
	protected void isAddressValid(String address, List<String> errors) {
		if(address == null || address.isBlank()) {
			logger.error("Address cannot be null or blank");
			errors.add("Address cannot be null or blank");
		}
	}	
}
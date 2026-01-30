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

/**
 * Utility class responsible for validating {@link ClientRequest} objects.
 * <p>
 * This class provides methods to validate client requests for both creation
 * and update operations. It performs basic field validations (first name,
 * last name, email, phone, and address) and checks for duplicate email or 
 * phone entries in database using {@link ClientRepository}.
 * </p>
 * <p>
 * <pre>
 * List<String> errors = new ArrayList<>();
 * clientRequestValidator.validateClientRequest(clientRequest, clientId, errors, "create");
 * if (!errors.isEmpty()) {
 *   // handle validation errors
 * }
 *
 * </pre>
 * </p> 
*/

@Component
public class ClientRequestValidator {
	
	private static final Logger logger =
			LoggerFactory.getLogger(ClientRequestValidator.class);

	/* Regular expression pattern to validate email address	*/
	final Pattern EMAIL_PATTERN = Pattern.compile(
			"^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
		    "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
	
	private ClientRepository clientRepository;
	
	/**
	 * Constructs an {@code ClientRequestValidator} with the given repository.
	 * 
	 * @param clientRepository the {@link ClientRepository} used to check for
	 *                         existing client data
	 */

	public ClientRequestValidator(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}

	/** Validates client request for creation and update.
	 * 
	 *@param clientRequest the client request to validate
	 *@param inputClientId the ID of the client being updated, or {@code null} if creating a new client
	 *@param errors        a list to collect validation error messages 
	 *@param action        the action being performed, either create or update
	 */
	
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
	
	/** Performs basic validation on all client fields such as first name, last name,
	 * email, phone number, and address
	 * 
	 * @param clientRequest the client request to validate
	 * @param errors        a list to collect validation error messages
	 */
	protected void basicClientValidation(ClientRequest clientRequest, List<String> errors) {
		
		logger.info("Validating first name {} ",clientRequest.getFirstName());
		isFirstNameValid(clientRequest.getFirstName(), errors);
		
		logger.info("Validating last name {} ",clientRequest.getLastName());
		isLastNameValid(clientRequest.getLastName(), errors);
		
		logger.info("Validating email address {} ",clientRequest.getEmail());
		isEmailValid(clientRequest.getEmail(), errors);
		
		logger.info("Validating phone number {} ",clientRequest.getPhone());
		isPhoneValid(clientRequest.getPhone(), errors);
		
		logger.info("Validating address {} ",clientRequest.getAddress());
		isAddressValid(clientRequest.getAddress(), errors);
	}

	/**
	 * Validates the client's first name
	 * 
	 * @param firstName the first name to validate
	 * @param errors    a list to collect validation error messages
	*/
	protected void isFirstNameValid(String firstName, List<String> errors) {
		if(firstName == null) {
			logger.error("First name cannot be null");
			errors.add("First name cannot be null");	
		}
		else if(firstName.trim().length() > 100) {
			logger.error("First name cannot exceed 100 alpha characters");
			errors.add("First name can have only 100 alpha characters");
		}
		else if(!firstName.matches("^[\\p{L}' .-]+$")) {
			logger.error("First name can have only alpha characters");
			errors.add("First name can have only alpha characters");
		}
	}
	
	/**
	 * Validates the client's last name
	 * 
	 * @param lastName the last name to validate
	 * @param errors   a list to collect validation error messages
	*/
	protected void isLastNameValid(String lastName, List<String> errors) {
		if(lastName == null) {
			logger.error("Last name cannot be null");
			errors.add("Last name cannot be null");
		}
		else if(lastName.trim().length() > 100) {
			logger.error("Last name cannot exceed 100 alpha characters");
			errors.add("Last name can only have 100 alpha characters");
		}
		else if(!lastName.matches("^[\\p{L}' .-]+$")) {
			logger.error("Last name can have only alpha characters");
			errors.add("Last name can have only alpha characters");
		}
	}

	/**
	 * Validates the client's email address
	 * 
	 * @param email  the email address to validate
	 * @param errors a list to collect validation error messages
	*/
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
	
	/**
	 * Validates the client's phone number
	 * 
	 * @param phone  the phone number to validate
	 * @param  errors a list to collect validation error messages 
	*/
	
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

	/**
	 * Validates the client's address
	 * 
	 * @param address the address to validate
	 * @param errors  a list to collect validation error messages
	*/
	protected void isAddressValid(String address, List<String> errors) {
		if(address == null || address.isBlank()) {
			logger.error("Address cannot be null or blank");
			errors.add("Address cannot be null or blank");
		}
	}	
}
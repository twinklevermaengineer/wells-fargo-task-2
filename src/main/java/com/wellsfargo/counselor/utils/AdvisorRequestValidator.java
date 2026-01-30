package com.wellsfargo.counselor.utils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.wellsfargo.counselor.entity.Advisor;
import com.wellsfargo.counselor.model.request.AdvisorRequest;
import com.wellsfargo.counselor.repository.AdvisorRepository;

/**
 * Utility class responsible for validating {@link AdvisorRequest} objects
 * for both creation and update operations.This class ensures that the 
 * advisor data meets the required format and uniqueness constraints before
 * persisting or updating in the database.
 * 
 * <p>Validations include:
 * <ul>
 * 		<li>Basic field checks (first name, last name, email, phone, address)</li>
 * 		<li>Email format validation</li>
 * 		<li>Phone number format validation</li>
 * 		<li>Uniqueness checks against existing advisors in the database</li>
 * </ul>
 *<p>
 *Usage example:
 *<pre>
 *	   List<String> errors = new ArrayList<>();
 *	   advisorRequestValidator.validateAdvisorRequest(advisorRequest, null, errors);
 *	   if (!errors.isEmpty()) {
 *		//handle validation errors
 *	}
 * </pre>
*/

@Component
public class AdvisorRequestValidator {
	
	private static final Logger logger = LoggerFactory.getLogger(AdvisorRequestValidator.class);
	
	/** Regular expression pattern to validate email address */
	final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
	
	private AdvisorRepository advisorRepository;
	
	/**
	 * Constructs an {@code AdvisorRequestValidator} with the given repository.
	 * 
	 * @param advisorRepository the {@link AdvisorRepository} used to check for 
	 * 							existing advisor data
	 */
	
	public AdvisorRequestValidator(AdvisorRepository advisorRepository) {
		this.advisorRepository = advisorRepository;
	}
	
	/**
	 * Validates an advisor request for creation or update.
	 * 
	 * @param advisorRequest the advisor request to validate
	 * @param advisorId      the ID of the advisor being updated, or {@code null} if creating a new advisor
	 * @param errors          a list to collect validation error messages 
	 */

	public void validateAdvisorRequest(AdvisorRequest advisorRequest, Long advisorId, List<String> errors) {
		if(advisorId == null){
			logger.info("Validate New Advisor Request");
			validateForCreate(advisorRequest, errors);
		}else {
			logger.info("Validate Update Advisor Request");
			logger.debug("Validating advisor update request for ID: {}", advisorId); 
			validateForUpdate(advisorRequest, advisorId, errors);
		}
	}
	
	/**
	 * Validate a new advisor request, including uniqueness checks for 
	 * email address, phone number, and mailing address.
	 * 
	 * @param advisorRequest the advisor request to validate
	 * @param errors         a list to collect validation error messages
	 */
	public void validateForCreate(AdvisorRequest advisorRequest, List<String> errors) {	
		
		// Perform basic validation
		basicValidation(advisorRequest,errors);
		
		if(!errors.isEmpty()){
			logger.error("Basic validation failed for create advisor request");
		}
		
		List<Advisor> existingAdvisorEmail = advisorRepository
				.findByEmail(advisorRequest.getEmail());
		
		if(!existingAdvisorEmail.isEmpty()) {
			logger.error("Advisor found during create new advisor with same email");
			errors.add("Advisor found with similar email");
		}
		
		List<Advisor> existingAdvisorPhone = advisorRepository
				.findByPhone(advisorRequest.getPhone());
		
		if(!existingAdvisorPhone.isEmpty()) {
			logger.error("Advisor found during create new advisor with same phone");
			errors.add("Advisor found with similar phone number");
		}
		
		Optional<Advisor> existingAdvisorAddress = advisorRepository
				.findByAddress(advisorRequest.getAddress());
		
		if(existingAdvisorAddress.isPresent()) {
			logger.error("Advisor found during create new advisor with same address");
			errors.add("Advisor found with similar address");
		}
	}
	/**
	 * Validates an existing advisor request for update. Checks for null ID,
	 * performs basic validation, and ensures email, phone, and address are
	 * unique among other advisors.
	 * 
	 * @param advisorRequest  the advisor request to validate
	 * @param inputAdvisorId  the ID of the advisor being updated
	 * @param errors          a list to collect validation error messages
	 */
	
	public void validateForUpdate(AdvisorRequest advisorRequest, Long inputAdvisorId, List<String> errors) {
		
		if(inputAdvisorId == null) {
			logger.error("AdvisorId is required for update");
			errors.add("Advisor id is required for update");
		}

    	basicValidation(advisorRequest, errors);

		if(!errors.isEmpty()) {
			logger.error("Failed basic validation for update");
		}
		
		String email = advisorRequest.getEmail();
			List<Advisor> existingEmail = advisorRepository.findByEmail(email);
			for(Advisor advisor: existingEmail) {
				if(!Objects.equals(advisor.getAdvisorId(), inputAdvisorId)) {
					logger.error("Duplicate email found");
					errors.add("Duplicate email found");
			}		
       }
		String phone = advisorRequest.getPhone();
			List<Advisor> existingPhoneNumber = advisorRepository.findByPhone(phone);
			for(Advisor advisor: existingPhoneNumber) {
				if(!Objects.equals(advisor.getAdvisorId(), inputAdvisorId)) {
					logger.error("Duplicate phone number found");
					errors.add("Duplicate phone number found");
			}
		}
		String address = advisorRequest.getAddress();
		advisorRepository.findByAddress(address)
        .ifPresent(advisor -> {
            if (!Objects.equals(advisor.getAdvisorId(), inputAdvisorId)) {
                logger.error("Duplicate address found");
                errors.add("Duplicate address found");
            }
		});
   }
	/** 
	 * Performs basic validation on advisor fields such as first name, last name,
	 * email, phone number, and mailing address 
	 * 
	 *@param advisorRequest  the advisor request to validate
	 *@param errors          a list to collect validation error messages
	 */

	protected void basicValidation(AdvisorRequest advisorRequest, List<String> errors) {
		logger.info("Validating the first name: {} ", advisorRequest.getFirstName());
		isFirstNameValid(advisorRequest.getFirstName(),errors);
		
		logger.info("Validating the last name: {} ", advisorRequest.getLastName());
		isLastNameValid(advisorRequest.getLastName(),errors);
		
		logger.info("Validating the email address: {} ", advisorRequest.getEmail());
		isEmailValid(advisorRequest.getEmail(),errors);
		
		logger.info("Validating the phone number: {} ", advisorRequest.getPhone());
		isPhoneNumberValid(advisorRequest.getPhone(),errors);
		
		logger.info("Validating the address: {} ", advisorRequest.getAddress());
		isAddressValid(advisorRequest.getAddress(),errors);
		
	}
	/**
	 * Validates the first name	
	 * 
	 * @param firstName the first name to validate
	 * @param errors    a list to collect validation error messages
	 */
	protected void isFirstNameValid(String firstName, List<String> errors) {
		if(firstName == null) {
			logger.error("First name is null");
			errors.add("First name cannot be null");
		}else if(firstName.trim().length() > 100) {
			logger.error("First name exceeds max length");
			errors.add("First name can have only 100 characters");
		}		
		else if (!firstName.matches("^[\\p{L}' .-]+$")) {
			logger.error("First name can have only alpha characters");
			errors.add("First name can have only alpha characters");
		}
	} 
	
	/**
	 * Validates the last name 
	 * 
	 *@param lastName the last name to validate
	 *@param errors   a list to collect validation error messages
	 */
	protected void isLastNameValid(String lastName, List<String> errors) {
		if(lastName == null) {
			logger.error("Last name is null");
			errors.add("Last name cannot be null");
		}else if(lastName.trim().length() > 100) {
			logger.error("Last name exceeds max length");
			errors.add("Last name can only have 100 characters");
		}
		else if (!lastName.matches("^[\\p{L}' .-]+$")) {
			logger.error("Last name can only have alpha characters");
			errors.add("Last name can only have alpha characters");
		}
	}
	
	/** 
	 * Validates the address 
	 * 
	 *@param address the address to validate
	 *@param errors  a list to collect validation error messages
	 */
	
	protected void isAddressValid(String address, List<String> errors) {
		if (address == null || address.isBlank()) { 
			logger.error("Address is null or blank {} ", address);
			errors.add("Address cannot be null or blank");
		}
	}
	
	/** 
	 * Validates the phone number (must be 10 digit number)
	 * 
	 * @param phone  the phone number to validate
	 * @param errors a list to collect validation error messages
	 */
	
	protected void isPhoneNumberValid(String phone, List<String> errors) {
		if (phone == null || phone.isBlank()) { 
			logger.error("Phone number is null or blank");
			errors.add("Phone number cannot be null or blank");
		}
		else if (!phone.matches("\\d{10}")) {
		   logger.error("Phone number can have only 10digit numeric numbers");
		   errors.add("Phone number can have only 10digit numeric numbers");
	   }
	 }
	
		/**
		 * Validates the email format.
		 * 
		 * @param email  the email to validate
		 * @param errors a list to collect validation error messages
		 */
	protected void isEmailValid(String email, List<String> errors) {		
	    if (email == null || email.isBlank()) {
	       logger.error("Email cannot be null or blank");
	       errors.add("Email cannot be null or blank");
	    }
	    else if(!EMAIL_PATTERN.matcher(email).matches()){
	    	logger.error("Invalid email entered");
	    	errors.add("Invalid email entered");
	    }
	}

}
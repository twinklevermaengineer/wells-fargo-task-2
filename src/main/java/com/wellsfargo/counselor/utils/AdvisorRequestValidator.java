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

@Component
public class AdvisorRequestValidator {
	private static final Logger logger = LoggerFactory.getLogger(AdvisorRequestValidator.class);
	final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
	
	private AdvisorRepository advisorRepository;
	
	public AdvisorRequestValidator(AdvisorRepository advisorRepository) {
		this.advisorRepository = advisorRepository;
	}
	
	public void validateAdvisorRequest(AdvisorRequest advisorRequest, Long advisorId, List<String> errors) {
		if(advisorId == null){
			logger.info("Validate New Advisor Request");
			validateForCreate(advisorRequest, errors);
		}else {
			logger.info("Validate Update Advisor Request");
			validateForUpdate(advisorRequest, advisorId, errors);
			System.out.println("checking");
		}
	}
	
	public void validateForCreate(AdvisorRequest advisorRequest, List<String> errors) {	
		
		// Perform basic validation
		basicValidation(advisorRequest,errors);
		if(errors.size() > 0){
			logger.error("Basic validation failed for create advisor request");
		};
		
		List<Advisor> existingAdvisorEmail = advisorRepository
				.findByEmail(advisorRequest.getEmail());
		
		if(existingAdvisorEmail.size() > 0) {
			logger.error("Advisor found during create new advisor with same email");
			errors.add("Advisor found with similar email");
		}
		
		List<Advisor> existingAdvisorPhone = advisorRepository
				.findByPhone(advisorRequest.getPhone());
		
		if(existingAdvisorPhone.size() > 0) {
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
	
	protected void basicValidation(AdvisorRequest advisorRequest, List<String> errors) {
	
		// Validate FirstName
		logger.info("Validating the first name: {} ", advisorRequest.getFirstName());
		isFirstNameValid(advisorRequest.getFirstName(),errors);
		
		// Validate LastName
		logger.info("Validating the last name: {} ", advisorRequest.getLastName());
		isLastNameValid(advisorRequest.getLastName(),errors);
		
		// Validate Email
		logger.info("Validating the email address: {} ", advisorRequest.getEmail());
		isEmailValid(advisorRequest.getEmail(),errors);
		
		// Validate PhoneNumber
		logger.info("Validating the phone number: {} ", advisorRequest.getPhone());
		isPhoneNumberValid(advisorRequest.getPhone(),errors);
		
		// Validate Address
		logger.info("Validating the address: {} ", advisorRequest.getAddress());
		isAddressValid(advisorRequest.getAddress(),errors);
		
	}
	
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
	
	protected void isAddressValid(String address, List<String> errors) {
		if (address == null || address.isBlank()) { 
			logger.error("Address is null or blank {} ", address);
			errors.add("Address cannot be null or blank");
		}
	}
	
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



package com.wellsfargo.counselor.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.wellsfargo.counselor.model.request.SecurityRequest;

@Component
public class SecurityRequestValidator {

	private static final Logger logger = 
			LoggerFactory.getLogger(SecurityRequestValidator.class);
	
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public void validateSecurityRequest(SecurityRequest securityRequest, List<String> errors) {
		
		if(securityRequest == null) {
			logger.error("Empty security request");
			errors.add("Empty security request");
			return;
		}

		if(securityRequest.getPortfolioId() == null) {
		    errors.add("Portfolio ID cannot be null");
		}

		basicRequestValidation(securityRequest, errors);
	}

	protected void basicRequestValidation(SecurityRequest securityRequest, List<String> errors) {
		
		logger.debug("Validating name: {} ", securityRequest.getName());
		validateName(securityRequest.getName(), errors);
		
		logger.debug("Validating category: {} ", securityRequest.getCategory());
		validateCategory(securityRequest.getCategory(), errors);
		
		logger.debug("Validating purchase price: {} ", securityRequest.getPurchasePrice());
		validatePurchasePrice(securityRequest.getPurchasePrice(), errors);
		
		logger.debug("Validating purchase date: {} ", securityRequest.getPurchaseDate());
		validatePurchaseDate(securityRequest.getPurchaseDate(),errors);
		
		logger.debug("Validating quantity: {} ", securityRequest.getQuantity());
		validateQuantity(securityRequest.getQuantity(), errors);
	}

	protected void validateName(String name, List<String> errors) {
		
		if(name == null || name.isBlank()) {
			logger.error("Name cannot be null or blank");
			errors.add("Name cannot be null or blank");
			return;
		}
		if(name.length() > 100) {
			logger.error("Name can have only 100 characters");
			errors.add("Name can have only 100 characters");
		}
		else if(name.contains("  ")) {
			logger.error("Name cannot contain multiple consecutive spaces: {}", name);
		    errors.add("Name cannot contain multiple consecutive spaces");
		}
		else if(!name.matches("^[\\p{L}' .-]+$")) {
			logger.error("Name can contain letters, spaces, apostrophes, periods, and hyphens {} ", name);
			errors.add("Name can contain letters, spaces, apostrophes, periods, and hyphens");
		}
	}

	protected void validateCategory(String categoryName, List<String> errors) {

		if(categoryName == null || categoryName.isBlank()) {
			logger.error("Category cannot be null or blank");
			errors.add("Category cannot be null or blank");
			return;
		}
		/*
		 *  Only for validation, 
		 *  will not be updated 
		 *  to SecurityRequest intentionally
		 */

		if(categoryName.length() > 20) {
			logger.error("Category name can have only 20 characters {} ", categoryName);
			errors.add("Category name can have only 20 characters");
		}

		else if(!SecurityType.isValid(categoryName.toUpperCase())) {
			logger.error("Not a valid category {} ", categoryName);
			errors.add("Not a valid category");
		}
	}

	protected void validatePurchasePrice(BigDecimal price, List<String> errors) {

		if(price == null) {
			logger.error("Price cannot be null");
			errors.add("Price cannot be null");	
			return;
		} else if(price.signum() <= 0) {
		    logger.error("Price needs to be positive {} ", price);
		    errors.add("Price needs to be positive");
		} else if (price.scale() > 2) {
			logger.error("Price can have at most 2 decimal places");
			errors.add("Price can have at most 2 decimal places");
		} else if (price.precision() - price.scale() > 8) {
			logger.error("Price can have at most 8 digits before decimal");
		    errors.add("Price can have at most 8 digits before decimal");
		}
	}
	
	protected void validatePurchaseDate(String purchaseDate, List<String> errors) {
	
		if(purchaseDate == null || purchaseDate.isBlank()) {
			logger.error("Purchase date cannot be null or blank");
			errors.add("Purchase date cannot be null or blank");
			return;
		} 
		// Date format - yyyy-mm-dd
		try {
	        LocalDate.parse(purchaseDate, formatter);
	    } catch (DateTimeParseException e) {
	        logger.error("Purchase date must be in yyyy-mm-dd format: {}", purchaseDate);
	        errors.add("Purchase date must be in yyyy-mm-dd format");
	    }
	}

	protected void validateQuantity(Integer quantity, List<String> errors) {
		
		if(quantity == null) {
			logger.error("Quantity cannot be null");
			errors.add("Quantity cannot be null");
			return;
		} else if(quantity <= 0) {
			logger.error("Quantity must be greater than zero {} ", quantity);
			errors.add("Quantity must be greater than zero");
		}
	}
}
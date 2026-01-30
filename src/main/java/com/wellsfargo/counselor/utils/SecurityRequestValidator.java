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

/**
 * Validator component for {@link com.wellsfargo.counselor.model.request.SecurityRequest}.
 * <p>
 * This class performs validation on incoming security request data and collects
 * validation errors without throwing exceptions. Each validation rule appends
 * a human-readable error message to the provided {@code errors} list.
 * <p>
 * Validation includes:
 * <ul>
 *   <li>Portfolio ID presence</li>
 *   <li>Security name format and length</li>
 *   <li>Category validity against {@link SecurityType}</li>
 *   <li>Purchase price constraints</li>
 *   <li>Purchase date format</li>
 *   <li>Quantity constraints</li>
 * </ul>
 *
 * This class is intended to be used as a Spring-managed component.
*/
@Component
public class SecurityRequestValidator {

	private static final Logger logger = 
			LoggerFactory.getLogger(SecurityRequestValidator.class);
	
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	/**
	 * Validates the given {@link SecurityRequest} and collects validation errors.
	 * <p>
	 * This method performs high-level validation, including:
	 * <ul>
	 *   <li>Checking that the request itself is not {@code null}</li>
	 *   <li>Validating the presence of a portfolio ID</li>
	 *   <li>Delegating field-level validation to internal helper methods</li>
	 * </ul>
	 * <p>
	 * Validation errors are added to the provided {@code errors} list.
	 * This method does not throw exceptions.
	 *
	 * @param securityRequest the security request to validate, may be {@code null}
	 * @param errors          a list of error messages to which validation errors will be added
    */
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
	
	/**
	 * Validates the given security category and adds error messages to the provided list if validation fails.
	 * <p>
	 * This method performs the following checks:
	 * <ul>
	 *   <li> If {@code categoryName} is {@code null}, or blank, logs an error and 
	 *        add a corresponding message to {@code errors}. </li>
	 *   <li> If {@code categoryName} exceeds 20 characters, logs an error and
	 *   	  add a corresponding messages to {@code errors}. </li>
	 *   <li> If {@code categoryName} does not match a valid {@link SecurityType}, logs an error
	 *   	  and add a corresponding messages to {@code errors}. </li>
	 * </ul>
	 * <p>
	 * 
	 * @param categoryName the category name to validate, may be {@code null}
	 * @param errors       a list of error message to which validation error will be added
	*/
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

	/**
	 * Validates the given purchase price and adds error messages to the provided list if validation fails.
	 * <p>
	 * The method performs the following checks:
	 * <ul>
	 *   <li> If {@code price} is {@code null}, logs an error and adds acorresponding message to {@code errors}. </li>
	 *   <li> If {@code price} is less than equal to 0, logs an error and adds a corresponding message to {@code errors}. </li>
	 *   <li> If {@code price} is greater than 2 decimal places, logs an error and adds a corresponding message to {@code errors}. </li>
	 *   <li> If {@code price} is greater than 8 digits before decimal places, logs an error and add a corresponding message to {@code errors}. </li>
	 * </ul>
	 * 
	 * @param price  the price to validate, may be {@code null}
	 * @param errors a list of error message to which validation error will be added
	*/
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

	/**
	 * Validates the given purchase date and adds error messages to the provided list if validation fails.
	 * <p>
	 * The method performs the following checks:
	 * <ul>
	 *   <li> If {@code purchaseDate} is {@code null}, or blank, logs an error and adds a corresponding message to {@code errors}. </li>
	 *   <li> If {@code purchaseDate} is not in date format, logs an error and adds a corresponding messages to {@code errors}. </li>
	 * </ul>
	 * 
	 * @param purchaseDate the purchase date to validate, may be {@code null}
	 * @param errors       a list of error message to which validation error will be added 
	*/
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
	
	/**
	 * Validates the given quantity and adds error messages to the provided list if validation fails.
	 * <p>
	 * The method performs the following checks:
	 * <ul>
	 *   <li> If {@code quantity} is {@code null}, logs an error and adds a corresponding message to {@code errors}. </li>
	 *   <li> If {@code quantity} is less than or equal to 0, logs an error and a corresponding message to {@code errors}. </li> 
	 * </ul>
	 * 
	 * @param quantity the quantity to validate, may be {@code null}
	 * @param errors   a list of error message to which validation errors will be added
	*/
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
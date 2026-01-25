package com.wellsfargo.counselor.utils;

import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.wellsfargo.counselor.model.request.SecurityRequest;
import com.wellsfargo.counselor.rest.InvalidRequestException;

@Component
public class SecurityRequestValidator {

	private static final Logger logger = 
			LoggerFactory.getLogger(SecurityRequestValidator.class);

	public void validateSecurityRequest(SecurityRequest securityRequest) {
		
		if(securityRequest == null) {
			logger.error("Empty security request");
			throw new InvalidRequestException("Empty security request");
		}
		
		if(securityRequest.getPortfolioId() == null) {
		    throw new InvalidRequestException("Portfolio ID cannot be null");
		}

		logger.info("Validating name {} ", securityRequest.getName());
		isNameValid(securityRequest.getName());
		
		logger.info("Validating category {} ", securityRequest.getCategory());
		isCategoryValid(securityRequest.getCategory());
		
		logger.info("Validating purchase price {} ", securityRequest.getPurchasePrice());
		isPurchasePriceValid(securityRequest.getPurchasePrice());
		
		logger.info("Validating purchase date {} ", securityRequest.getPurchaseDate());
		isPurchaseDateValid(securityRequest.getPurchaseDate());
		
		logger.info("Validating quantity {} ", securityRequest.getQuantity());
		isQuantityValid(securityRequest.getQuantity());
	}

	protected void isNameValid(String name) {
		if(name == null || name.isBlank()) {
			logger.error("Name cannot be null or blank");
			throw new InvalidRequestException("Name cannot be null or blank");
		}
		
		 name = name.trim();
		 
		if(name.contains("  ")) {
			logger.error("Name cannot contain multiple consecutive spaces: {}", name);
		    throw new InvalidRequestException("Name cannot contain multiple consecutive spaces");
		}
		else if(name.trim().length() > 100) {
			logger.error("Name can have only 100 characters {} ", name);
			throw new InvalidRequestException("Name can have only 100 characters");
		}
		else if(!name.matches("^[\\p{L}' .-]+$")) {
			logger.error("Name can have only alpha characters {} ", name);
			throw new InvalidRequestException("Name can have only alpha characters");
		}
	}

	protected void isCategoryValid(String categoryName) {

		if(categoryName == null || categoryName.isBlank()) {
			logger.error("Category cannot be null or blank");
			throw new InvalidRequestException("Category cannot be null or blank");
		}
		categoryName = categoryName.trim().toUpperCase();
		
		if(categoryName.length() > 20) {
			logger.error("Category name can have only 20 characters {} ", categoryName);
			throw new InvalidRequestException("Category name can have only 20 characters");
		}

		else if(!SecurityType.isValid(categoryName)) {
			logger.error("Not a valid category {} ", categoryName);
			throw new InvalidRequestException("Not a valid category");
		}
	}

	protected void isPurchasePriceValid(BigDecimal price) {
		if(price == null) {
			logger.error("Price cannot be null");
			throw new InvalidRequestException("Price cannot be null");
		}
		  else if(price.compareTo(BigDecimal.ZERO) < 0) {
		        logger.error("Price cannot be negative {} ", price);
		        throw new InvalidRequestException("Price cannot be negative");
		} 
		  else if(price.compareTo(BigDecimal.ZERO) == 0) {
			    throw new InvalidRequestException("Price must be greater than zero");
		}
		  else if (price.scale() > 2) {
		    throw new InvalidRequestException("Price can have at most 2 decimal places");
		}
		  else if (price.precision() - price.scale() > 8) {
		    throw new InvalidRequestException("Price can have at most 8 digits before decimal");
		}

	}
	
	protected void isPurchaseDateValid(String purchaseDate) {
		if(purchaseDate == null || purchaseDate.isBlank()) {
			logger.error("Purchase date cannot be null or blank");
			throw new InvalidRequestException("Purchase date cannot be null or blank");
		}
		else if(purchaseDate.trim().length() > 19) {
			logger.error("Purchase date can only have 19 character {} ", purchaseDate);
			throw new InvalidRequestException("Purchase date can only have 19 character");
		}
	}
	
	protected void isQuantityValid(Integer quantity) {
		if(quantity == null) {
			logger.error("Quantity cannot be null");
			throw new InvalidRequestException("Quantity cannot be null");
		}
		else if(quantity <= 0) {
			logger.error("Quantity must be greater than zero {} ", quantity);
			throw new InvalidRequestException("Quantity must be greater than zero");
		}
	}
}
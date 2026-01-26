package com.wellsfargo.counselor.utils;

import static org.assertj.core.api.Assertions.assertThat;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import com.wellsfargo.counselor.model.request.SecurityRequest;

@ExtendWith(MockitoExtension.class)
public class SecurityRequestValidatorTest {

	@InjectMocks
	private SecurityRequestValidator validator;
	
	private SecurityRequest securityRequest;
	
	List<String> errors;
	
	@BeforeEach
	void setUp() {
		errors = new ArrayList<>();
		
		securityRequest = new SecurityRequest();
			securityRequest.setName("Microsoft Corp.");
			securityRequest.setCategory("Bond");
			securityRequest.setPurchasePrice(new BigDecimal("98.75"));
			securityRequest.setPurchaseDate("2026-01-25");
			securityRequest.setQuantity(23);
			securityRequest.setPortfolioId(null);
	}

	@Test
	void shouldNotReturnError_whenValidateSecurityRequestHasValidInput() {
		
		//Arrange
		securityRequest.setPortfolioId(23L);
		
		//Act
		validator.validateSecurityRequest(securityRequest, errors);
		
		//Assert
		assertThat(errors)
			.as("Valid input for validating request cannot produce error")
			.isEmpty();

	}
	
	@Test
	void shouldAddError_whenSecurityRequestIsNull() {
		
		//Arrange
		securityRequest = null;
		
		//Act
		validator.validateSecurityRequest(securityRequest, errors);
		
		//Assert
		assertThat(errors)
			.as("Security request cannot be null")
			.contains("Empty security request");
	}
	
	@Test
	void shouldAddError_whenSecurityRequestHasPortfolioIdNull() {
		
		//Arrange
		securityRequest.setPortfolioId(null);
		
		//Act
		validator.validateSecurityRequest(securityRequest, errors);
		
		//Assert
		assertThat(errors)
			.as("Security request must have portfolio id")
			.contains("Portfolio ID cannot be null");
	}
	
	
	@Test
	void shouldNotReturnValidationError_whenInputValid() {
		
		//Act
		validator.basicRequestValidation(securityRequest, errors);
		
		//Assert
		assertThat(errors)
			.as("Valid request cannot produce error")
			.isEmpty();
	}

	@Test
	void shouldReturnValidationError_whenNameIsInvalid() {
		//Arrange
		securityRequest.setName(null);
		
		//Act
		validator.basicRequestValidation(securityRequest, errors);
		
		//Assert
		assertThat(errors)
			.as("Validation error when name is not valid")
			.contains("Name cannot be null or blank");
	}

	@Test
	void shouldReturnValidationError_whenInvalidInput() {
		
		//Arrange
		securityRequest.setCategory("Stck");
		securityRequest.setQuantity(null);
		
		//Act
		validator.basicRequestValidation(securityRequest, errors);
		
		//Assert
		assertThat(errors)
			.as("Validation error when input is invalid")
			.containsExactlyInAnyOrder(
					"Not a valid category",
					"Quantity cannot be null"
			);
	}
	
	@Test
	void shouldReturnValidationError_whenPurchasePriceIsNull() {
		
		//Arrange
		securityRequest.setPurchasePrice(null);
		
		//Act
		validator.basicRequestValidation(securityRequest, errors);
		
		//Assert
		assertThat(errors)
			.as("Price cannot be null")
			.contains("Price cannot be null");
	}
	
	@Test
	void shouldReturnError_whenPurchaseDateInWrongFormat() {
		
		//Arrange
		securityRequest.setPurchaseDate("2026.10.24");
		
		//Act
		validator.basicRequestValidation(securityRequest, errors);
		
		//Assert
		assertThat(errors)
			.as("Validation error when purchase date is in wrong format")
			.contains("Purchase date must be in yyyy-mm-dd format");
	}
	
	@Test
	void shouldReturnError_whenQuantityIsNull() {
		
		//Arrange
		securityRequest.setQuantity(null);
		
		//Act
		validator.basicRequestValidation(securityRequest, errors);
		
		//Assert
		assertThat(errors)
			.as("Validation error when quantity is null")
			.contains("Quantity cannot be null");
		
	}


	
	@Test
	void shouldNotAddErrors_whenNameIsValid() {
		//Arrange
		String name = "Apple";

		//Act
		validator.validateName(name, errors );
	
		//Assert
		assertThat(errors)
		 	.as("Valid name should not produce any validation errors")
			.isEmpty();
	}

	@Test
	void shouldAddErrors_whenNameIsNullOrBlank() {
		//Arrange
		String name = null;

		//Act
		validator.validateName(name, errors);

		//Assert
		assertThat(errors)
			.as("Validation error when name is null or blank")
			.contains("Name cannot be null or blank");
	}

	@Test
	void shouldAddError_whenNameExceedsMaxLength() {
		//Arrange
		String name = ("Amazon").repeat(101);
		
		//Act
		validator.validateName(name, errors);
		
		//Assert
		assertThat(errors)
			.as("Validation error when name exceeds max length")
			.contains("Name can have only 100 characters");

	}
	
	@Test
	void shouldAddError_whenNameContainMultipleSpace() {
		//Arrange
		String name = "  John Doe";
		
		//Act
		validator.validateName(name, errors);
		
		//Assert
		assertThat(errors)
			.as("Validation error when name contains multiple white space")
			.contains("Name cannot contain multiple consecutive spaces");
	}
	
	@Test
	void shouldAddError_whenNamePatternNotMatch() {
		//Arrange
		String name = "Name123";
		
		//Act
		validator.validateName(name, errors);
		
		//Assert
		assertThat(errors)
			.as("Validation error when name does not match pattern")
			.contains("Name can contain letters, spaces, apostrophes, periods, and hyphens");
	}
	
	@Test
	void shouldNotAddError_whenCategoryIsValid() {
		//Arrange
		String categoryName = "Stock";
		
		//Act
		validator.validateCategory(categoryName, errors);
		
		//Assert
		assertThat(errors)
			.as("Valid category name should not produce any validation error")
			.isEmpty();
	}
	
	@Test
	void shouldAddError_whenCategoryIsNull() {
		//Arrange
		String categoryName = null;
		
		//Act
		validator.validateCategory(categoryName, errors);
		
		//Assert
		assertThat(errors)
			.as("Validation error when category is null or blank")
			.contains("Category cannot be null or blank");
	}
	
	@Test
	void shouldAddError_whenCategoryIsBlank() {
		//Arrange
		String categoryName = " ";
		
		//Act
		validator.validateCategory(categoryName, errors);
		
		//Assert
		assertThat(errors)
			.as("Validation error when category is null or blank")
			.contains("Category cannot be null or blank");
	}

	
	@Test
	void shouldAddError_whenCategoryLengthExceedsMaxLength() {
		//Arrange
		String category = "Stock".repeat(101);
		
		//Act
		validator.validateCategory(category, errors);
		
		//Assert
		assertThat(errors)
			.as("Validation error when category name exceeds max length")
			.contains("Category name can have only 20 characters");
		
	}
	
	@Test
	void shouldAddError_whenMismatchCategory() {
		//Arrange
		String categoryName = "Stok";
		
		//Act
		validator.validateCategory(categoryName, errors);
		
		//Assert
		assertThat(errors)
			.as("Validation error when category does not match security type")
			.contains("Not a valid category");
		
	}
	
	@Test
	void shouldNotAddError_whenPurchasePriceIsValid() {
		//Arrange
		BigDecimal price = new BigDecimal("98.45");
		
		//Act 
		validator.validatePurchasePrice(price, errors);
		
		//Assert
		assertThat(errors)
			.as("Valid price should not add any validation error")
			.isEmpty();
	}
	
	@Test
	void shouldAddError_whenPurchasePriceIsNull() {
		//Arrange
		BigDecimal price = null;
		
		//Act
		validator.validatePurchasePrice(price, errors);
		
		//Assert
		assertThat(errors)
			.as("Validation error when price is null")
			.contains("Price cannot be null");
	}
	
	@Test
	void shouldAddError_whenPurchasePriceIsEqualToOrLessthanZero() {
		//Arrange
		BigDecimal price = new BigDecimal("-456");
		
		//Act
		validator.validatePurchasePrice(price, errors);
		
		//Assert
		assertThat(errors)
			.as("Validation error when price is less than or equal to zero")
			.contains("Price needs to be positive");
	}
	@Test
	void shouldAddError_whenPurchasePriceContainsMoreThanTwoDecimal() {
		//Arrange
		BigDecimal price = new BigDecimal("456.4568");
		
		//Act
		validator.validatePurchasePrice(price, errors);
		
		//Assert
		assertThat(errors)
			.as("Validation error when price contains more than 2 decimal places")
			.contains("Price can have at most 2 decimal places");
	}
	
	@Test
	void shouldAddError_whenPurchasePriceBeforeDecimalExceedsMaxLength() {
		//Arrange
		BigDecimal price = new BigDecimal("123456789.56");
		
		//Act
		validator.validatePurchasePrice(price, errors);
		
		//Assert
		assertThat(errors)
			.as("Validation error because price can have at most 8 digits before decimal")
			.contains("Price can have at most 8 digits before decimal");
	}
	
	@Test
	void shouldNotAddError_whenPurchaseDateIsValid() {
		//Arrange
		String date = "2026-01-20";
		
		//Act
		validator.validatePurchaseDate(date, errors);
		
		//Assert
		assertThat(errors)
			.as("Valid purchase date should not produce any error")
			.isEmpty();
	}
	
	@Test
	void shouldAddError_whenPurchaseDateIsNull() {
		//Arrange
		String date = null;
		
		//Act
		validator.validatePurchaseDate(date, errors);
		
		//Assert
		assertThat(errors)
			.as("Validation error when purchase date is null")
			.contains("Purchase date cannot be null or blank");
	}
	
	@Test
	void shouldAddError_whenPurchaseDateIsBlank() {
		//Arrange
		String date = " ";
		
		//Act
		validator.validatePurchaseDate(date, errors);
		
		//Assert
		assertThat(errors)
			.as("Validation error when purchase date is blank")
			.contains("Purchase date cannot be null or blank");
	}
	
	@Test
	void shouldAddError_whenPurchaseDateContainsWhiteSpace() {
		//Arrange
		String date = "  2026-01-21 ";
		
		//Act
		validator.validatePurchaseDate(date, errors);
		
		//Assert
		assertThat(errors)
			.as("Purchase date must be in format yyyy-mm-dd")
			.contains("Purchase date must be in yyyy-mm-dd format");
	}
	
	@Test
	void shouldAddError_whenPurchaseDateHasInvalidFormat() {
		//Arrange
		String date = "2026/01/20";
		
		//Act
		validator.validatePurchaseDate(date, errors);
		
		//Assert
		assertThat(errors)
			.as("Purchas date must be in format yyyy-mm-dd")
			.contains("Purchase date must be in yyyy-mm-dd format");
	}
	
	@Test
	void shouldAddError_whenPurchaseDateIsInvalidCalenderDate() {
		//Arrange
		String date = "2026-30-02";
		
		//Act
		validator.validatePurchaseDate(date, errors);
		
		//Assert
		assertThat(errors)
			.as("Purchase date must be in format yyyy-mm-dd")
			.contains("Purchase date must be in yyyy-mm-dd format");
	}
	
	@Test
	void shouldNotAddError_whenQuantityIsValid() {
		//Arrange
		Integer quantity = 20;
		
		//Act
		validator.validateQuantity(quantity, errors);
		
		//Assert
		assertThat(errors)
			.as("Valid quantity should not produce any error")
			.isEmpty();
	}
	
	@Test
	void shouldAddError_whenQuantityIsNull() {
		//Arrange
		Integer quantity = null;
		
		//Act
		validator.validateQuantity(quantity, errors);
		
		//Assert
		assertThat(errors)
			.as("Validation error when quantity is null")
			.contains("Quantity cannot be null");
	}
	
	@Test
	void shouldAddError_whenQuantityIsLessThanZero() {
		//Arrange
		Integer quantity = -25;
		
		//Act
		validator.validateQuantity(quantity, errors);
		
		//Assert
		assertThat(errors)
			.as("Validation error when quantity is less than zero")
			.contains("Quantity must be greater than zero");
	}
	
	@Test
	void shouldAddError_whenQuantityIsZero() {
		//Arrange
		Integer quantity = 0;
		
		//Act
		validator.validateQuantity(quantity, errors);
		
		//Assert
		assertThat(errors)
			.as("Validation error when quantity is zero")
			.contains("Quantity must be greater than zero");
	}
}
package com.wellsfargo.counselor.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.wellsfargo.counselor.entity.Advisor;
import com.wellsfargo.counselor.model.request.AdvisorRequest;
import com.wellsfargo.counselor.repository.AdvisorRepository;

@ExtendWith(MockitoExtension.class)
public class AdvisorRequestValidatorTest {

	@Mock
	private AdvisorRepository advisorRepository;
	
	@InjectMocks
	private AdvisorRequestValidator validator;
	
	private AdvisorRequest validRequest;
	
	private List<String> errors;
	
	//This method runs before each test
	@BeforeEach
	void setUp() {
		errors = new ArrayList<>();
		validRequest = new AdvisorRequest();
		validRequest.setFirstName("Johnson");
		validRequest.setLastName("Doe");
		validRequest.setAddress("1234, newyork");
		validRequest.setPhone("1234567890");
		validRequest.setEmail("doejohnson@gmail.com");
	}

	@Test
	void validateForCreate_returnEmptyList_success() {
		
		//Arrange
		when(advisorRepository.findByEmail(anyString())).thenReturn(Collections.emptyList());
		when(advisorRepository.findByPhone(anyString())).thenReturn(Collections.emptyList());
		when(advisorRepository.findByAddress(anyString())).thenReturn(Optional.empty());
		
		//Act
		validator.validateAdvisorRequest(validRequest, null, errors);
		
		//Assert
		assertThat(errors).isEmpty();
		
		verify(advisorRepository, times(1)).findByEmail(anyString());
		verify(advisorRepository, times(1)).findByPhone(anyString());
		verify(advisorRepository, times(1)).findByAddress(anyString());
	}

	@Test
	void validateForCreate_returnList_duplicateEmail() {
		
		//Arrange
		when(advisorRepository.findByEmail(anyString())).thenReturn(List.of(new Advisor()));
		when(advisorRepository.findByPhone(anyString())).thenReturn(Collections.emptyList());
		when(advisorRepository.findByAddress(anyString())).thenReturn(Optional.empty());
		
		//Act
		validator.validateAdvisorRequest(validRequest, null, errors);
		
		//Assert
		assertThat(errors).isNotEmpty();
		assertThat(errors).contains("Advisor found with similar email");
		
		verify(advisorRepository).findByEmail(validRequest.getEmail());
		verify(advisorRepository).findByPhone(anyString());
		verify(advisorRepository, times(1)).findByAddress(anyString());
	}

	@Test
	void validateForCreate_returnList_duplicatePhone() {
		
		//Arrange
		when(advisorRepository.findByEmail(anyString())).thenReturn(Collections.emptyList());
		when(advisorRepository.findByPhone(anyString())).thenReturn(List.of(new Advisor()));
		when(advisorRepository.findByAddress(anyString())).thenReturn(Optional.empty());
		
		//Act
		validator.validateAdvisorRequest(validRequest, null, errors);
		
		//Assert
		assertThat(errors).isNotEmpty();
		assertThat(errors).contains("Advisor found with similar phone number");
		
		verify(advisorRepository, times(1)).findByEmail(anyString());
		verify(advisorRepository, times(1)).findByPhone(validRequest.getPhone());
		verify(advisorRepository, times(1)).findByAddress(anyString());
	}
	
	@Test
	void validateForCreate_returnList_duplicateAddress() {
		
		//Arrange
		when(advisorRepository.findByEmail(anyString())).thenReturn(Collections.emptyList());
		when(advisorRepository.findByPhone(anyString())).thenReturn(Collections.emptyList());
		when(advisorRepository.findByAddress(anyString())).thenReturn(Optional.of(new Advisor()));
		
		//Act
		validator.validateAdvisorRequest(validRequest, null, errors);
		
		//Assert
		assertThat(errors).isNotEmpty();
		assertThat(errors).contains("Advisor found with similar address");
		
		verify(advisorRepository).findByEmail(anyString());
		verify(advisorRepository).findByPhone(anyString());
		verify(advisorRepository).findByAddress(validRequest.getAddress());
	}

	@Test
	void validateForUpdate_returnEmptyList_success() {
		
		//Arrange
		when(advisorRepository.findByEmail(anyString())).thenReturn(Collections.emptyList());
		when(advisorRepository.findByPhone(anyString())).thenReturn(Collections.emptyList());
		when(advisorRepository.findByAddress(anyString())).thenReturn(Optional.empty());
		
		//Act
		validator.validateForUpdate(validRequest, 1L, errors);
		
		//Assert
		assertThat(errors).isEmpty();
		
		verify(advisorRepository, times(1)).findByAddress(anyString());
		verify(advisorRepository, times(1)).findByPhone(anyString());
		verify(advisorRepository, times(1)).findByEmail(anyString());

	}

	@Test
	void validateForUpdate_returnList_emailAlreadyExists() {
		
		//Arrange
		Advisor existingAdvisor = new Advisor();
		existingAdvisor.setEmail("johndoe@gmail.com");
		existingAdvisor.setAdvisorId(2L);
		
		List<Advisor> existingAdvisorList = new ArrayList<>();
		existingAdvisorList.add(existingAdvisor);
		
		when(advisorRepository.findByEmail(anyString())).thenReturn(existingAdvisorList);
		when(advisorRepository.findByPhone(anyString())).thenReturn(Collections.emptyList());
		when(advisorRepository.findByAddress(anyString())).thenReturn(Optional.empty());
		
		//Act
		validator.validateForUpdate(validRequest, 1L, errors);
		
		//Assert
		assertThat(errors).isNotEmpty();
		assertThat(errors).contains("Duplicate email found");
		
		verify(advisorRepository, times(1)).findByEmail(validRequest.getEmail());
		verify(advisorRepository, times(1)).findByPhone(anyString());
		verify(advisorRepository, times(1)).findByAddress(anyString());
	}
	
	@Test
	void validateForUpdate_returnList_phoneNumberAlreadyExists() {
		
		//Arrange
		Advisor existingAdvisor = new Advisor();
		existingAdvisor.setPhone("1234567899");
		existingAdvisor.setAdvisorId(2L);
		
		List<Advisor> existingAdvisorList = List.of(existingAdvisor);
		
		when(advisorRepository.findByEmail(anyString())).thenReturn(Collections.emptyList());
		when(advisorRepository.findByPhone(anyString())).thenReturn(existingAdvisorList);
		when(advisorRepository.findByAddress(anyString())).thenReturn(Optional.empty());
		
		//Act
		validator.validateForUpdate(validRequest, 1L, errors);
		
		//Assert
		assertThat(errors).isNotEmpty();
		assertThat(errors).contains("Duplicate phone number found");
		
		verify(advisorRepository, times(1)).findByEmail(anyString());
		verify(advisorRepository, times(1)).findByPhone(validRequest.getPhone());
		verify(advisorRepository, times(1)).findByAddress(anyString());
	}

	@Test
	void validateForUpdate_returnList_addressAlreadyExists() {
		
		//Arrange
		Advisor existingAdvisor = new Advisor();
		existingAdvisor.setAddress("456, NewYork");
		existingAdvisor.setAdvisorId(3L);
		
		when(advisorRepository.findByEmail(anyString())).thenReturn(Collections.emptyList());
		when(advisorRepository.findByPhone(anyString())).thenReturn(Collections.emptyList());
		when(advisorRepository.findByAddress(anyString())).thenReturn(Optional.of(existingAdvisor));
		
		//Act
		validator.validateForUpdate(validRequest, 1L, errors);

		//Assert
		assertThat(errors).isNotEmpty();
		assertThat(errors).contains("Duplicate address found");
		
		verify(advisorRepository, times(1)).findByEmail(anyString());
		verify(advisorRepository, times(1)).findByPhone(anyString());
		verify(advisorRepository, times(1)).findByAddress(validRequest.getAddress());
	
	}
	
	@Test
	void basicValidationTest_success_errors_empty() {		

		//Act
		validator.basicValidation(validRequest,errors);
		
		//Assert
		assertThat(errors).isEmpty();
	}
	
	@Test
	void basicValidationTest_fail_errors_notEmpty() {
		
		//Arrange
		AdvisorRequest inValidRequest = new AdvisorRequest();
		inValidRequest.setFirstName(" John123  ");
		inValidRequest.setLastName("Doe");
		inValidRequest.setEmail(null);
		inValidRequest.setPhone("1234567899");
		inValidRequest.setAddress("1355,NewYork");
		
		//Act
		validator.basicValidation(inValidRequest, errors);
		
		//Assert
		assertThat(errors).isNotEmpty();
		assertThat(errors).contains("First name can have only alpha characters");
		assertThat(errors).contains("Email cannot be null or blank");
		
	}

	@Test
	void emailValidationTest_validInput_success() {
		
		//Arrange
		String inputEmail = "abc@xyz.com";
		
		//Act
		validator.isEmailValid(inputEmail, errors);	
		
		//Assert
		assertThat(errors).isEmpty();
	}

	@Test
	void emailValidationTest_blankValue_fail() {
		
		//Arrange
		String inputEmail = " ";
		
		//Act
		validator.isEmailValid(inputEmail, errors);
		
		//Assert
		assertThat(errors).isNotEmpty();
		assertThat(errors).contains("Email cannot be null or blank");
	}

	@Test
	void emailValidationTest_nullValue_fail() {
		
		//Arrange
		String inputEmail = null;
		
		//Act
		validator.isEmailValid(inputEmail, errors);
		
		//Assert
		assertThat(errors).isNotEmpty();
		assertThat(errors).contains("Email cannot be null or blank");
	}
	
	@Test
	void emailValidationTest_patternMismatch_fail() {

		//Arrange
		String inputEmailPattern = "InvalidEmail";
		
		//Act
		validator.isEmailValid(inputEmailPattern, errors);
		
		//Assert
		assertThat(errors).isNotEmpty();
		assertThat(errors).contains("Invalid email entered");
	}	
	
	@Test
	void firstNameValidationTest_validInput_success() {
		
		//Arrange
		String firstName = "John";
		
		//Act
		validator.isFirstNameValid(firstName, errors);
		
		//Assert
		assertThat(errors).isEmpty();
	}
	
	@Test
	void firstNameValidationTest_nullValue_fail() {
		
		//Arrange
		String firstName = null;
		
		//Act
		validator.isFirstNameValid(firstName, errors);
		
		//Assert
		assertThat(errors).isNotEmpty();
		assertThat(errors).contains("First name cannot be null");
	}
	
	@Test
	void firstNameValidationTest_patternMismatch_fail() {
		
		//Arrange
		String firstNamePattern = "Name 123";
		
		//Act
		validator.isFirstNameValid(firstNamePattern, errors);
		
		//Assert
		assertThat(errors).isNotEmpty();
		assertThat(errors).contains("First name can have only alpha characters");
	}
	
	@Test
	void firstNameValidationTest_maxLength_fail() {
		
		//Arrange
		String firstName = "erfw".repeat(101);
		
		//Act
		validator.isFirstNameValid(firstName, errors);
		
		//Assert
		assertThat(errors).isNotEmpty();
		assertThat(errors).contains("First name can have only 100 characters");	
	}
	
	@Test
	void lastNameValidationTest_validInput_success() {
		
		//Arrange
		String lastName = "Doe";

		//Act
		validator.isLastNameValid(lastName, errors);
		
		//Assert
		assertThat(errors).isEmpty();
	}
	
	@Test
	void lastNameValidationTest_nullValue_fail() {
		
		//Arrange
		String lastName = null;
		
		//Act
		validator.isLastNameValid(lastName, errors);
		
		//Act
		assertThat(errors).isNotEmpty();
		assertThat(errors).contains("Last name cannot be null");
	}
	
	@Test
	void lastNameValidationTest_patternMismatch_fail() {
		
		//Arrange
		String lastName = "Name123";
		
		//Act
		validator.isLastNameValid(lastName, errors);
		
		//Assert
		assertThat(errors).isNotEmpty();
		assertThat(errors).contains("Last name can only have alpha characters");
	}
	
	@Test
	void lastNameValidationTest_maxLength_fail() {
		
		//Arrange
		String lastName = "dew".repeat(101);
		
		//Act
		validator.isLastNameValid(lastName, errors);
		
		//Assert
		assertThat(errors).isNotEmpty();
		assertThat(errors).contains("Last name can only have 100 characters");
	}
	
	@Test
	void phoneNumberValidationTest_validInput_success() {
		
		//Arrange
		String phoneNumber = "1234567899";
		
		//Act
		validator.isPhoneNumberValid(phoneNumber, errors);
		
		//Assert
		assertThat(errors).isEmpty();
	}
	
	@Test
	void phoneNumberValidationTest_nullValue_fail() {
		
		//Arrange
		String phoneNumber = null;
		
		//Act
		validator.isPhoneNumberValid(phoneNumber, errors);
		
		//Assert
		assertThat(errors).isNotEmpty();
		assertThat(errors).contains("Phone number cannot be null or blank");
	}
	
	@Test
	void phoneNumberValidationTest_blankValue_fail() {

		//Arrange
		String phoneNumber = " ";

		//Act
		validator.isPhoneNumberValid(phoneNumber, errors);
		
		//Assert
		assertThat(errors).isNotEmpty();
		assertThat(errors).contains("Phone number cannot be null or blank");
	}
	
	@Test
	void phoneNumberValidationTest_patternMismatch_fail() {
	
	//Arrange
	String phone = "Phone123";
	
	//Act
	validator.isPhoneNumberValid(phone, errors);
	
	//Assert
	assertThat(errors).isNotEmpty();
	assertThat(errors).contains("Phone number can have only 10digit numeric numbers");
	}
	
	@Test
	void phoneNumberValidationTest_exceedsLength_fail() {
		
	//Arrange
	String phone = "894578996789";
	
	//Act
	validator.isPhoneNumberValid(phone, errors);
	
	//Assert
	assertThat(errors).isNotEmpty();
	assertThat(errors).contains("Phone number can have only 10digit numeric numbers");
		
	}
	
	@Test
	void addressValidationTest_validInput_success() {
		
		//Arrange
		String address = "1234, newyork";
		
		//Act
		validator.isAddressValid(address, errors);
		
		//Assert
		assertThat(errors).isEmpty();
	}
	
	@Test
	void addressValidationTest_nullValue_fail() {
		
		//Arrange
		String address = null;
		
		//Act
		validator.isAddressValid(address, errors);
		
		//Assert
		assertThat(errors).isNotEmpty();
		assertThat(errors).contains("Address cannot be null or blank");
	}
	
	@Test
	void addressValidationTest_blankValue_fail() {
		
		//Arrange
		String address = " ";
		
		//Act
		validator.isAddressValid(address, errors);
		
		//Assert
		assertThat(errors).isNotEmpty();
		assertThat(errors).contains("Address cannot be null or blank");
	}
}
package com.wellsfargo.counselor.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
		validRequest.setEmailAddress("doejohnson@gmail.com");
		validRequest.setPhoneNumber("1234567890");
	}
	
	//Test for successfull validation during creation
	@Test
	void validateForCreate_success() {
		//Mock repository to return empty list
		when(advisorRepository.findByEmail(anyString())).thenReturn(Collections.emptyList());
		when(advisorRepository.findByPhone(anyString())).thenReturn(Collections.emptyList());
		when(advisorRepository.findByAddress(anyString())).thenReturn(Collections.emptyList());
		
		validator.validateAdvisorRequest(validRequest, null, errors);
		
		//Assert that no errors were found
		assertTrue(errors.isEmpty());	
	}
	
	//Test for duplicate email during creation
	@Test
	void validateForCreate_duplicateEmail() {
		//Mock repository to return one Advisor with same email
		when(advisorRepository.findByEmail(anyString())).thenReturn(List.of(new Advisor()));
		when(advisorRepository.findByPhone(anyString())).thenReturn(Collections.emptyList());
		when(advisorRepository.findByAddress(anyString())).thenReturn(Collections.emptyList());
		
		validator.validateAdvisorRequest(validRequest, null, errors);
		//Assert that error message for duplicate email is present
		assertTrue(errors.contains("Advisor found with similar email"));
	}
	
	//Test for duplicate phone number during creation
	@Test
	void validateForCreate_duplicatePhone() {
		//Mock repository to return one Advisor with same phone number
		when(advisorRepository.findByEmail(anyString())).thenReturn(Collections.emptyList());
		when(advisorRepository.findByPhone(anyString())).thenReturn(List.of(new Advisor()));
		when(advisorRepository.findByAddress(anyString())).thenReturn(Collections.emptyList());
		validator.validateAdvisorRequest(validRequest, null, errors);
		
		//Assert that error message for duplicate phone number is present
		assertTrue(errors.contains("Advisor found with similar phone number"));
		
	}
	
	//Test for duplicate address during creation
	@Test
	void validateForCreate_duplicateAddress() {
		when(advisorRepository.findByEmail(anyString())).thenReturn(Collections.emptyList());
		when(advisorRepository.findByPhone(anyString())).thenReturn(Collections.emptyList());
		when(advisorRepository.findByAddress(anyString())).thenReturn(List.of(new Advisor()));
		
		validator.validateAdvisorRequest(validRequest, null, errors);
		//Assert that error message for duplicate address is present
		assertTrue(errors.contains("Advisor found with similar address"));
	}
	
	//Test for successfull validation during update when no duplicates exists
	@Test
	void validateForUpdate_emptyList_success() {
		Advisor existingAdvisor = new Advisor();
		existingAdvisor.setFirstName("Abc");
		existingAdvisor.setLastName("Xyz");
		existingAdvisor.setEmail("abc@gmail.com");
		existingAdvisor.setPhone("1234987659");
		existingAdvisor.setAddress("1564, newyork");
		
		List<Advisor> advisorList = new ArrayList<>();
		advisorList.add(existingAdvisor);
	
		//Mock repository to return no duplicates
		when(advisorRepository.findByEmail(anyString())).thenReturn(Collections.emptyList());
		when(advisorRepository.findByPhone(anyString())).thenReturn(Collections.emptyList());
		when(advisorRepository.findByAddress(anyString())).thenReturn(Collections.emptyList());
		validator.validateForUpdate(validRequest, 1L, errors);
		//Assert that no errors found
		assertTrue(errors.isEmpty());

	}
	
	//Test for duplicate email during update
	@Test
	void validateForUpdate_emailAlreadyExists() {
		Advisor existingAdvisor = new Advisor();
		existingAdvisor.setEmail("johndoe@gmail.com");
		
		List<Advisor> existingAdvisorList = new ArrayList<>();
		existingAdvisorList.add(existingAdvisor);
		
		when(advisorRepository.findByEmail(anyString())).thenReturn(existingAdvisorList);
		when(advisorRepository.findByPhone(anyString())).thenReturn(Collections.emptyList());
		when(advisorRepository.findByAddress(anyString())).thenReturn(Collections.emptyList());
		
		validator.validateForUpdate(validRequest, 1L, errors);
		assertTrue(errors.contains("Duplicate email found"));
	}
	
	//Test for duplicate phone number during update
	@Test
	void validateForUpdate_phoneNumberAlreadyExists() {
		Advisor existingAdvisor = new Advisor();
		existingAdvisor.setPhone("1234567899");
		
		List<Advisor> existingAdvisorList = new ArrayList<>();
		existingAdvisorList.add(existingAdvisor);
		
		when(advisorRepository.findByEmail(anyString())).thenReturn(Collections.emptyList());
		when(advisorRepository.findByPhone(anyString())).thenReturn(existingAdvisorList);
		when(advisorRepository.findByAddress(anyString())).thenReturn(Collections.emptyList());
		
		validator.validateForUpdate(validRequest, 1L, errors);
		assertTrue(errors.contains("Duplicate phone number found"));
	}
	
	//Test for duplicate address during update
	@Test
	void validateForUpdate_addressAlreadyExists() {
		Advisor existingAdvisor = new Advisor();
		existingAdvisor.setAddress("456, NewYork");
		
		List<Advisor> existingAdvisorList = new ArrayList<>();
		existingAdvisorList.add(existingAdvisor);
		
		when(advisorRepository.findByEmail(anyString())).thenReturn(Collections.emptyList());
		when(advisorRepository.findByPhone(anyString())).thenReturn(Collections.emptyList());
		when(advisorRepository.findByAddress(anyString())).thenReturn(existingAdvisorList);
		
		validator.validateForUpdate(validRequest, 1L, errors);
		assertTrue(errors.contains("Duplicate address found"));
	
	}
	
	@Test
	void basicValidationTest_success_errors_empty() {		
		List<String> errors =new ArrayList<>();
		validator.basicValidation(validRequest,errors);
		assertTrue(errors.isEmpty());
	}
	
	@Test
	void basicValidationTest_fail_errors_notEmpty() {
		List<String> errors = new ArrayList<>();
		AdvisorRequest inValidRequest = new AdvisorRequest();
		inValidRequest.setFirstName("     ");
		inValidRequest.setLastName("Doe");
		inValidRequest.setEmailAddress(null);
		inValidRequest.setPhoneNumber("1234567899");
		inValidRequest.setAddress("1355,NewYork");
		validator.basicValidation(inValidRequest, errors);
		assertFalse(errors.isEmpty());
		
	}

	@Test
	void emailValidationTest_success() {
		
		String inputEmail = "abc@xyz.com";
		List<String> errors = new ArrayList<>();	
		
		validator.isEmailValid(inputEmail, errors);		
		assertEquals(errors.isEmpty(),true);
	}
	
	@Test
	void emailValidationTest_fail_blankValue() {
		
		String inputEmail = " ";
		List<String> errors = new ArrayList<>();
		
		validator.isEmailValid(inputEmail, errors);
		assertTrue(errors.contains("Email cannot be null or blank"));
	}
	
	@Test
	void emailValidationTest_fail_nullValue() {
		
		String inputEmail = null;
		List<String> errors = new ArrayList<>();
		
		validator.isEmailValid(inputEmail, errors);
		assertTrue(errors.contains("Email cannot be null or blank"));
	}
	
	@Test
	void emailValidationTest_fail_patternMismatch() {
		
		String inputEmailPattern = "InvalidEmail";
		List<String> errors = new ArrayList<>();
		
		validator.isEmailValid(inputEmailPattern, errors);
		assertFalse(errors.isEmpty());
	}	
	
	@Test
	void firstNameValidationTest_success() {
		
		String firstName = "John";
		List<String> errors = new ArrayList<>();
		
		validator.isFirstNameValid(firstName, errors);
		assertTrue(errors.isEmpty());
	}
	
	@Test
	void firstNameValidationTest_fail_nullValue() {
		
		String firstName = null;
		List<String> errors = new ArrayList<>();
		
		validator.isFirstNameValid(firstName, errors);
		assertTrue(errors.contains("First name cannot be null"));
	}
	
	@Test
	void firstNameValidationTest_fail_patternMismatch() {
		
		String firstNamePattern = "Name 123";
		List<String> errors = new ArrayList<>();
		
		validator.isFirstNameValid(firstNamePattern, errors);
		assertEquals(errors.isEmpty(), false);
		assertTrue(errors.contains("First name can have only alpha characters"));
	}
	
	@Test
	void firstNameValidationTest_fail_maxLength() {
		
		String firstName = "erfw".repeat(101);
		List<String> errors = new ArrayList<>();
		
		validator.isFirstNameValid(firstName, errors);
		assertTrue(errors.contains("First name can have only 100 characters"));	
	}
	
	@Test
	void lastNameValidationTest_success() {
		
		String lastName = "Doe";
		List<String> errors = new ArrayList<>();
		
		validator.isLastNameValid(lastName, errors);
		assertTrue(errors.isEmpty());
	}
	
	@Test
	void lastNameValidationTest_fail_nullValue() {
		
		String lastName = null;
		List<String> errors = new ArrayList<>();
		
		validator.isLastNameValid(lastName, errors);
		assertTrue(errors.contains("Last name cannot be null"));
	}
	
	@Test
	void lastNameValidationTest_fail_patternMismatch() {
		
		String lastName = "Name123";
		List<String> errors = new ArrayList<>();
		
		validator.isLastNameValid(lastName, errors);
		assertTrue(errors.contains("Last name can only have alpha characters"));
	}
	
	@Test
	void lastNameValidationTest_fail_maxLength() {
		
		String lastName = "dew".repeat(101);
		List<String> errors = new ArrayList<>();
		
		validator.isLastNameValid(lastName, errors);
		assertTrue(errors.contains("Last name can only have 100 characters"));
	}
	
	@Test
	void phoneNumberValidationTest_suuccess() {
		
		String phoneNumber = "1234567899";
		List<String> errors = new ArrayList<>();
		
		validator.isPhoneNumberValid(phoneNumber, errors);
		assertTrue(errors.isEmpty());
	}
	
	@Test
	void phoneNumberValidationTest_fail_blankValue() {
		
		String phoneNumber = " ";
		List<String> errors = new ArrayList<>();
		
		validator.isPhoneNumberValid(phoneNumber, errors);
		assertTrue(errors.contains("Phone number cannot be null or blank"));
	}
	
	@Test
	void phoneNumberValidationTest_fail_nullValue() {
		
		String phoneNumber = null;
		List<String> errors = new ArrayList<>();
		
		validator.isPhoneNumberValid(phoneNumber, errors);
		assertTrue(errors.contains("Phone number cannot be null or blank"));
	}
	
	@Test
	void addressValidationTest_success() {
		
		String address = "1234, newyork";
		List<String> errors = new ArrayList<>();
		
		validator.isAddressValid(address, errors);
		assertTrue(errors.isEmpty());
	}
	
	@Test
	void addressValidationTest_fail_nullValue() {
		
		String address = null;
		List<String> errors = new ArrayList<>();
		
		validator.isAddressValid(address, errors);
		assertTrue(errors.contains("Address cannot be null or blank"));
	}
	
	@Test
	void addressValidationTest_fail_blankValue() {
		
		String address = " ";
		List<String> errors = new ArrayList<>();
		
		validator.isAddressValid(address, errors);
		assertTrue(errors.contains("Address cannot be null or blank"));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
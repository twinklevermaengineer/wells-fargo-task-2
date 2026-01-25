package com.wellsfargo.counselor.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.wellsfargo.counselor.entity.Client;
import com.wellsfargo.counselor.model.request.ClientRequest;
import com.wellsfargo.counselor.repository.ClientRepository;

@ExtendWith(MockitoExtension.class)
public class ClientRequestValidatorTest {

	@Mock
	private ClientRepository clientRepository;
	
	@InjectMocks
	private ClientRequestValidator validator;
	
	private ClientRequest clientRequest;
	
	private Client client;
	
	private List<String> errors;
	
	@BeforeEach
	void setUp() {

	errors = new ArrayList<>();
	
	clientRequest = new ClientRequest();
	clientRequest.setFirstName("Jose");
	clientRequest.setLastName("Richard");
	clientRequest.setAddress("568, Miami Fl");
	clientRequest.setPhone("9987896545");
	clientRequest.setEmail("joserichard@gmail.com");
  }

	@Test
	void validateClientRequest_actionCreate_emptyList_success() {
		
	//Arrange
	when(clientRepository.findByEmail(anyString())).thenReturn(List.of());
	when(clientRepository.findByPhone(anyString())).thenReturn(List.of());
	
	//Act
	validator.validateClientRequest(clientRequest, null, errors, "create");
	
	//Assert
	assertThat(errors).isEmpty();
   }
	
	@Test
	void validateClientRequest_actionCreate_emailExists_error_fail() {

	//Arrange
	client = new Client();
	client.setEmail(clientRequest.getEmail());
	client.setClientId(7L);
	List<Client> clientList = List.of(client);
	when(clientRepository.findByEmail(anyString())).thenReturn(clientList);
	when(clientRepository.findByPhone(anyString())).thenReturn(List.of());

	
	//Act
	validator.validateClientRequest(clientRequest, null, errors, "create");
	
	//Assert
	assertThat(errors).isNotEmpty();
	assertThat(errors).contains("Client with similar email found");
   }

	@Test
	void validateClientRequestTest_actionCreate_phoneExists_error_fail() {
		
	//Arrange
	client = new Client();
	client.setPhone(clientRequest.getPhone());
	client.setClientId(7L);
	List<Client> clientList = List.of(client);
	when(clientRepository.findByEmail(anyString())).thenReturn(List.of());
	when(clientRepository.findByPhone(anyString())).thenReturn(clientList);

	
	//Act
	validator.validateClientRequest(clientRequest, null, errors, "create");
	
	//Assert
	assertThat(errors).isNotEmpty();
	assertThat(errors).contains("Client with similar phone number found");
   }

	@Test
	void validateClientRequest_actionUpdate_emptyList_success() {
		
	//Arrange
	when(clientRepository.findByEmail(anyString())).thenReturn(List.of());
	when(clientRepository.findByPhone(anyString())).thenReturn(List.of());
	
	//Act
	validator.validateClientRequest(clientRequest, null, errors, "update");
	
	//Assert
	assertThat(errors).isEmpty();
   }

	@Test
	void validatingClientRequest_actionUpdate_emailExistsWithSameId_success() {
		
	//Arrange
	Long id = 5L;
	client = new Client(
			clientRequest.getFirstName(),clientRequest.getLastName(),
			clientRequest.getEmail(), clientRequest.getPhone(),
			clientRequest.getAddress());
	client.setClientId(id);
	List<Client> clientList = new ArrayList<>();
	clientList.add(client);
	when(clientRepository.findByEmail(anyString())).thenReturn(clientList);
	when(clientRepository.findByPhone(anyString())).thenReturn(List.of());
	
	//Act
	validator.validateClientRequest(clientRequest, id, errors, "update");
	
	//Assert
	assertThat(errors).isEmpty();
	}
	
	@Test
	void validatingClientRequest_actionUpdate_emailExistsWithDifferentId_fail() {
		
	//Arrange
	Long id = 5L;
	client = new Client(
			clientRequest.getFirstName(),clientRequest.getLastName(),
			clientRequest.getEmail(), clientRequest.getPhone(),
			clientRequest.getAddress());
	client.setClientId(id);
	List<Client> clientList = new ArrayList<>();
	clientList.add(client);
	when(clientRepository.findByEmail(anyString())).thenReturn(clientList);
	when(clientRepository.findByPhone(anyString())).thenReturn(List.of());
	
	//Act
	validator.validateClientRequest(clientRequest, 6L, errors, "update");
	
	//Assert
	assertThat(errors).isNotEmpty();
	assertThat(errors).contains("Different client with similar email found");
	}
	
	@Test
	void validatingClientRequest_actionUpdate_phoneExistsWithSameId_success() {
	//Arrange
	Long id = 5L;
	client = new Client(
			clientRequest.getFirstName(),clientRequest.getLastName(),
			clientRequest.getEmail(), clientRequest.getPhone(),
			clientRequest.getAddress());
	client.setClientId(id);
	
	List<Client> clientList = new ArrayList<>();
	clientList.add(client);
	
	when(clientRepository.findByEmail(anyString())).thenReturn(List.of());
	when(clientRepository.findByPhone(anyString())).thenReturn(clientList);
	
	//Act
	validator.validateClientRequest(clientRequest, id, errors, "update");
	
	//Assert
	assertThat(errors).isEmpty();
	}

	@Test
	void validatingClientRequest_actionUpdate_phoneExistsWithDifferentId_fail() {
	//Arrange
	Long id = 5L;
	client = new Client(
			clientRequest.getFirstName(),clientRequest.getLastName(),
			clientRequest.getEmail(), clientRequest.getPhone(),
			clientRequest.getAddress());
	client.setClientId(id);
	
	List<Client> clientList = new ArrayList<>();
	clientList.add(client);
	
	when(clientRepository.findByEmail(anyString())).thenReturn(List.of());
	when(clientRepository.findByPhone(anyString())).thenReturn(clientList);
	
	//Act
	validator.validateClientRequest(clientRequest, 9L, errors, "update");
	
	//Assert
	assertThat(errors).isNotEmpty();
	assertThat(errors).contains("Different client with similar phone number found");
	}
	
	@Test
	void basicValidationTest_emptyList_noErrors_success() {
	
	//Act
	validator.basicClientValidation(clientRequest, errors);
	
	//Assert
	assertThat(errors).isEmpty();
	}
	
	@Test
	void basicValidationTest_errors_fail() {
	
	//Arrange
	ClientRequest invalidRequest = new ClientRequest();
	invalidRequest.setFirstName(null);
	invalidRequest.setLastName(clientRequest.getLastName());
	invalidRequest.setEmail(" ");
	invalidRequest.setPhone(clientRequest.getPhone());
	invalidRequest.setAddress(clientRequest.getAddress());
	
	//Act
	validator.basicClientValidation(invalidRequest, errors);
	
	//Assert
	assertThat(errors).isNotEmpty();
	assertThat(errors).contains("First name cannot be null");
	assertThat(errors).contains("Email cannot be null or blank");
   }
	
	@Test
	void isFirstNameValid_validInput_success() {
	
	//Arrange
	String firstName = "Rose";
		 
	//Act
	validator.isFirstNameValid(firstName, errors);
	
	//Assert
	assertThat(errors).isEmpty();
  }
	
	@Test
	void isFirstNameValid_nullValue_fail() {
	
	//Arrange
	String firstName = null;
	
	//Act
	validator.isFirstNameValid(firstName, errors);
	
	//Assert
	assertThat(errors).isNotEmpty();
	assertThat(errors).contains("First name cannot be null");
  }
	
	@Test
	void isFirstNameValid_exceedsLength_fail() {
		
	//Arrange
	String firstName = "nkj".repeat(101);
		
	//Act
	validator.isFirstNameValid(firstName, errors);
	
	//Assert
	assertThat(errors).isNotEmpty();
	assertThat(errors).contains("First name can have only 100 alpha characters");
  }
	
	@Test
	void isFirstNameValid_patternMismatch_fail() {
		
	//Arrange
	String firstName = "Name467";
		
	//Act
	validator.isFirstNameValid(firstName, errors);
	
	//Assert
	assertThat(errors).isNotEmpty();
	assertThat(errors).contains("First name can have only alpha characters");
  }
	
	@Test
	void isLastNameValid_validInput_success() {
		
	//Arrange
	String lastName = "Doe";
	
	//Act
	validator.isLastNameValid(lastName, errors);
	
	//Assert
	assertThat(errors).isEmpty();
  }
	
	@Test
	void isLastNameValid_nullValue_fail() {
		
	//Arrange
	String lastName = null;
	
	//Act
	validator.isLastNameValid(lastName, errors);
	
	//Assert
	assertThat(errors).isNotEmpty();
	assertThat(errors).contains("Last name cannot be null");
   }
	
	@Test
	void isLastNameValid_exceedsLength_fail() {
		
	//Arrange
	String lastName = "Nmae".repeat(101);
	
	//Act
	validator.isLastNameValid(lastName, errors);
	
	//Assert
	assertThat(errors).isNotEmpty();
	assertThat(errors).contains("Last name can only have 100  alpha characters");
		
	}
	
	@Test
	void isLastNameValid_patternMismatch_fail() {
		
	//Arrange
	String lastName = "NAj798";
	
	//Act
	validator.isLastNameValid(lastName, errors);
	
	//Assert
	assertThat(errors).isNotEmpty();
	assertThat(errors).contains("Last name can have only alpha characters");
  }
	
	@Test
	void isEmailValid_validInput_success() {
	
	//Arrange
	String email = "johnmartin@gmail.com";
	
	//Act
	validator.isEmailValid(email, errors);
	
	//Assert
	assertThat(errors).isEmpty();
  }
	
	@Test
	void isEmailValid_nullValue_fail() {
		
	//Arrange
	String email = null;
	
	//Act
	validator.isEmailValid(email, errors);
	
	//Assert
	assertThat(errors).isNotEmpty();
	assertThat(errors).contains("Email cannot be null or blank");
  }
	
	@Test
	void isEmailValid_blankValue_fail() {
	
	//Arrange
	String email = " ";
	
	//Act
	validator.isEmailValid(email, errors);
	
	//Assert
	assertThat(errors).isNotEmpty();
	assertThat(errors).contains("Email cannot be null or blank");
   }
	
	@Test
	void isEmailValid_patternMismatch_fail() {
		
	//Arrange
	String email = "InvalidEmail";
	
	//Act
	validator.isEmailValid(email, errors);
	
	//Assert
	assertThat(errors).isNotEmpty();
	assertThat(errors).contains("Invalid email entered");
	
	}
	
	@Test
	void isPhoneNumberValid_validInput_success() {
	
	//Arrange
	String phone = "9889613245";
	
	//Act
	validator.isPhoneValid(phone, errors);
	
	//Assert
	assertThat(errors).isEmpty();
   }
	
	@Test
	void isPhoneNumberValid_nullValue_fail() {
	
	//Arrange
	String phone = null;
	
	//Act
	validator.isPhoneValid(phone, errors);
	
	//Assert
	assertThat(errors).isNotEmpty();
	assertThat(errors).contains("Phone number cannot be null or blank");
		
	}
	
	@Test
	void isPhoneNumberValid_blankValue_fail() {
		
	//Arrange
	String phone = " ";
	
	//Act
	validator.isPhoneValid(phone, errors);
	
	//Assert
	assertThat(errors).isNotEmpty();
	assertThat(errors).contains("Phone number cannot be null or blank");	
   }
	
	@Test
	void isPhoneNumberValid_patternMismatch_fail() {
		
	//Arrange
	String phone = "phone123";
	
	//Act
	validator.isPhoneValid(phone, errors);
	
	//Assert
	assertThat(errors).isNotEmpty();
	assertThat(errors).contains("Phone number can have only 10digit numeric numbers");
   }
	
	@Test
	void isPhoneNumberValid_exceedsLength_fail() {
		
	//Arrange
	String phone = "1234567891015";
	
	//Act
	validator.isPhoneValid(phone, errors);
	
	//Assert
	assertThat(errors).isNotEmpty();
	assertThat(errors).contains("Phone number can have only 10digit numeric numbers");
   }
	
	@Test
	void isAddressValid_validInput_success() {
		
	//Arrange
	String address = "435, Miami FL";
	
	//Act
	validator.isAddressValid(address, errors);
	
	//Assert
	assertThat(errors).isEmpty();
   }
	
	@Test
	void isAddressValid_nullValue_fail() {
		
	//Arrange
	String address = null;
	
	//Act
	validator.isAddressValid(address, errors);
	
	//Assert
	assertThat(errors).isNotEmpty();
	assertThat(errors).contains("Address cannot be null or blank");
   }
	
	@Test
	void isAddressValid_blankValue_fail() {
	
	//Arrange
	String address = " ";
	
	//Act
	validator.isAddressValid(address, errors);
	
	//Assert
	assertThat(errors).isNotEmpty();
	assertThat(errors).contains("Address cannot be null or blank");
		
	}
}

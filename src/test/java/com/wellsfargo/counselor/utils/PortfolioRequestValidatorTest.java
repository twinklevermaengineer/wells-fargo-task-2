package com.wellsfargo.counselor.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.wellsfargo.counselor.model.request.ClientRequest;
import com.wellsfargo.counselor.model.request.PortfolioRequest;
import com.wellsfargo.counselor.model.request.SecurityRequest;
import com.wellsfargo.counselor.repository.ClientRepository;
import com.wellsfargo.counselor.rest.InvalidRequestException;

@ExtendWith(MockitoExtension.class)
public class PortfolioRequestValidatorTest {

	@Mock
	private ClientRepository clientRepository;

	@InjectMocks
	private PortfolioRequestValidator validator;

	private PortfolioRequest portfolioRequest;

	@BeforeEach
	void setUp() {	

	ClientRequest clientRequest = new ClientRequest();
	Long id = 2L;
		clientRequest.setClientId(id);
		clientRequest.setFirstName("John");
		clientRequest.setLastName("Doe");
		clientRequest.setAddress("564. Miami Fl");
		clientRequest.setPhone("9874563215");
		clientRequest.setEmail("johndoe@gmail.com");

	SecurityRequest securityRequest = new SecurityRequest();
		securityRequest.setName("Apple");
	    securityRequest.setCategory("Stock");
	    securityRequest.setPurchasePrice(BigDecimal.valueOf(100));
	    securityRequest.setPurchaseDate("2026-01-23");
	    securityRequest.setQuantity(25);
		
	    portfolioRequest = new PortfolioRequest();
	    portfolioRequest.setCreationDate("2026-01-20");
	    portfolioRequest.setClient(clientRequest);
	    portfolioRequest.setSecurities(List.of(securityRequest));
	}
	
	@Test
	void validatePortfolioRequest_validRequest_success() {
	
		when(clientRepository.existsById(2L)).thenReturn(true);
		validator.validatePortfolioRequest(portfolioRequest);
	}
	
	@Test
	void validatePortfolioRequest_inValidPortfolioRequest_fail() {

		//Arrange
		portfolioRequest = null;

		//Act and Assert
		InvalidRequestException exception = assertThrows(
				InvalidRequestException.class, () -> {
					validator.validatePortfolioRequest(portfolioRequest);
				});
		
		assertThat(exception).isNotNull();
		assertThat(exception)
				.isInstanceOf(InvalidRequestException.class)
					.hasMessageContaining("Empty portfolio request");
	}

	@Test
	void validatePortfolioRequest_invalidClient_fail() {
		
		//Arrange
		portfolioRequest.setClient(null);
		
		//Act and Assert
		InvalidRequestException exception = assertThrows(
					InvalidRequestException.class, () -> {
						validator.validatePortfolioRequest(portfolioRequest);
				});
		
		assertThat(exception).isNotNull();
		assertThat(exception)
				.isInstanceOf(InvalidRequestException.class)
						.hasMessageContaining("Empty client found");
	}
	
	@Test
	void validatePortfolioRequest_invalidClientId_fail() {
		
		//Arrange
		ClientRequest clientRequest = new ClientRequest();
		clientRequest.setClientId(null);
		
		portfolioRequest.setClient(clientRequest);

		//Act and Assert
		InvalidRequestException exception = assertThrows(
				InvalidRequestException.class, () -> {
					validator.validatePortfolioRequest(portfolioRequest);
			});

		assertThat(exception).isNotNull();
		assertThat(exception)
				.isInstanceOf(InvalidRequestException.class)
					.hasMessageContaining("Invalid client id");
	}

	@Test
	void validatePortfolioRequest_notExistsById_fail() {
		
		//Arrange
		ClientRequest clientRequest = new ClientRequest();
		clientRequest.setClientId(232L);

		SecurityRequest securityRequest = new SecurityRequest();
		securityRequest.setName("Apple");
	    securityRequest.setCategory("Stock");
	    securityRequest.setPurchasePrice(BigDecimal.valueOf(100));
	    securityRequest.setPurchaseDate("2026-01-23");
	    securityRequest.setQuantity(25);
		
	    portfolioRequest = new PortfolioRequest();
	    portfolioRequest.setCreationDate("2026-01-20");
	    portfolioRequest.setClient(clientRequest);
	    portfolioRequest.setSecurities(List.of(securityRequest));
		
		when(clientRepository.existsById(clientRequest.getClientId())).thenReturn(false);
		
		//Act and Assert
		InvalidRequestException exception = assertThrows(
				InvalidRequestException.class, () -> {
					validator.validatePortfolioRequest(portfolioRequest);
			});
		
		assertThat(exception).isNotNull();
		assertThat(exception)
				.isInstanceOf(InvalidRequestException.class)
						.hasMessageContaining("Client not found with id : 232");
		}	
}

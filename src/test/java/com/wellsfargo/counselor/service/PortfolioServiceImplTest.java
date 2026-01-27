package com.wellsfargo.counselor.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.wellsfargo.counselor.entity.Client;
import com.wellsfargo.counselor.entity.Portfolio;
import com.wellsfargo.counselor.entity.Security;
import com.wellsfargo.counselor.model.request.ClientRequest;
import com.wellsfargo.counselor.model.request.PortfolioRequest;
import com.wellsfargo.counselor.model.request.SecurityRequest;
import com.wellsfargo.counselor.model.response.PortfolioResponse;
import com.wellsfargo.counselor.model.response.SecurityResponse;
import com.wellsfargo.counselor.repository.ClientRepository;
import com.wellsfargo.counselor.repository.PortfolioRepository;
import com.wellsfargo.counselor.rest.ResourceNotFoundException;
import com.wellsfargo.counselor.utils.PortfolioRequestValidator;

@ExtendWith(MockitoExtension.class)
public class PortfolioServiceImplTest {

	@Mock
	private PortfolioRepository portfolioRepository;
	
	@Mock
	private PortfolioRequestValidator validator;
	
	@Mock
	private ClientRepository clientRepository;

	@InjectMocks
	private PortfolioServiceImpl portfolioServiceImpl;

	@Test
	void findById_portfolioFound_success() {
		
		//Arrange
		Long id = 1L;
		Client client = new Client();
		client.setClientId(2L);
		client.setFirstName("John");
		client.setLastName("Doe");
		client.setAddress("564. Miami Fl");
		client.setPhone("9874563215");
		client.setEmail("johndoe@gmail.com");
		
		Security security = new Security();
		security.setSecurityId(3L);
		security.setName("Apple");
		security.setCategory("Stock");
		security.setPurchasePrice(BigDecimal.valueOf(100));
		security.setPurchaseDate("2026-01-23");
		security.setQuantity(25);
		
		Portfolio portfolio = new Portfolio();
		portfolio.setPortfolioId(id);
		portfolio.setCreationDate("2026-01-23");
		portfolio.setClient(client);
		portfolio.setSecurities(List.of(security));
		
		when(portfolioRepository.findById(id)).thenReturn(Optional.of(portfolio));
		
		//Act
		PortfolioResponse response = portfolioServiceImpl.findById(id);
		
		//Assert
		assertThat(response).isNotNull();
		assertThat(response.getPortfolioId()).isEqualTo(id);
		assertThat(response.getClient().getClientId()).isEqualTo(client.getClientId());
		assertThat(response.getSecurities()).hasSize(1);
		verify(portfolioRepository, times(1)).findById(id);
	}
		
	@Test
	void findById_portfolioNotFound_fail() {
		//Arrange
		Long id = 2L;
		
		when(portfolioRepository.findById(id)).thenReturn(Optional.empty());

		//Act and Assert
		ResourceNotFoundException exception = assertThrows(
					ResourceNotFoundException.class, () -> {
							portfolioServiceImpl.findById(id);
				});

		assertThat(exception)
				.isInstanceOf(ResourceNotFoundException.class)
					.hasMessageContaining("Portfolio not found, id: " + id);
		
		verify(portfolioRepository, times(1)).findById(id);
	}

	@Test
	void findAll_portfolioFound_success() {

		//Arrange
		Long id = 3L;
	
		Client client = new Client();
		client.setClientId(2L);
		client.setFirstName("John");
		client.setLastName("Doe");
		client.setAddress("564. Miami Fl");
		client.setPhone("9874563215");
		client.setEmail("johndoe@gmail.com");
		
		Security security = new Security();
		security.setSecurityId(3L);
		security.setName("Apple");
		security.setCategory("Stock");
		security.setPurchasePrice(BigDecimal.valueOf(100));
		security.setPurchaseDate("2026-01-23");
		security.setQuantity(25);
	
		Portfolio portfolioEntity = new Portfolio(
				id, "2026-01-20", List.of(security), client);
		
		List<Portfolio> portfolioList = new ArrayList<>();
		portfolioList.add(portfolioEntity);
		
		when(portfolioRepository.findAll()).thenReturn(portfolioList);
		
		//Act
		List<PortfolioResponse> response = portfolioServiceImpl.findAll();
		
		//Assert
		assertThat(response).isNotNull();
		
		PortfolioResponse portfolioResponse = response.get(0);
		
		assertThat(portfolioResponse.getPortfolioId()).isEqualByComparingTo(id);
		assertThat(portfolioResponse.getCreationDate()).isEqualTo("2026-01-20");
		assertThat(portfolioResponse.getClient().getClientId()).isEqualTo(2L);
		assertThat(portfolioResponse.getClient().getFirstName()).isEqualTo("John");
		assertThat(portfolioResponse.getSecurities().get(0).getName()).isEqualTo("Apple");

		verify(portfolioRepository, times(1)).findAll();
	}
	
	@Test
	void findAll_noProtfolios_returnEmptyList() {
		//Arrange
		when(portfolioRepository.findAll()).thenReturn(List.of());
		
		//Act
		List<PortfolioResponse> response = portfolioServiceImpl.findAll();
		
		//Assert
		assertThat(response).isEmpty();
		assertThat(response).isNotNull();
		
		verify(portfolioRepository, times(1)).findAll();	
	}
	
	@Test
	void save_portfolioSaved_success() {

		//Arrange
		ClientRequest clientRequest = new ClientRequest();
			clientRequest.setClientId(2L);
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
		
	   PortfolioRequest portfolioRequest = new PortfolioRequest();
		    portfolioRequest.setCreationDate("2026-01-20");
		    portfolioRequest.setClient(clientRequest);
		    portfolioRequest.setSecurities(List.of(securityRequest));
		    
	   Client clientEntity = new Client();
	   		clientEntity.setClientId(2L);
			clientEntity.setFirstName("John");
			clientEntity.setLastName("Doe");
			clientEntity.setAddress("564. Miami Fl");
			clientEntity.setPhone("9874563215");
			clientEntity.setEmail("johndoe@gmail.com");		    

		Security securityEntity = new Security();
			securityEntity.setSecurityId(3L);
			securityEntity.setName("Apple");
			securityEntity.setCategory("Stock");
			securityEntity.setPurchasePrice(BigDecimal.valueOf(100));
			securityEntity.setPurchaseDate("2026-01-23");
			securityEntity.setQuantity(25);
	
		Portfolio portfolioEntity = new Portfolio();
		    portfolioEntity.setPortfolioId(3L);
		    portfolioEntity.setCreationDate("2026-01-20");
		    portfolioEntity.setClient(clientEntity);
		    portfolioEntity.setSecurities(List.of(securityEntity));
		
		when(portfolioRepository.save(any(Portfolio.class))).thenReturn(portfolioEntity);
		when(clientRepository.findById(any(Long.class))).thenReturn(Optional.of(clientEntity));

		//Act
		PortfolioResponse response = portfolioServiceImpl.save(portfolioRequest);

		//Assert
		assertThat(response).isNotNull();
		assertThat(response.getClient().getClientId()).isEqualByComparingTo(2L);
		assertThat(response.getCreationDate()).isEqualTo("2026-01-20");
		assertThat(response.getPortfolioId()).isEqualTo(3L);

		SecurityResponse securityResponse = response.getSecurities().get(0);
		
		assertThat(response.getSecurities()).hasSize(1);
		assertThat(securityResponse.getName()).isEqualTo("Apple");
		assertThat(securityResponse.getCategory()).isEqualTo("Stock");
		assertThat(securityResponse.getPurchasePrice()).isEqualByComparingTo(BigDecimal.valueOf(100));
		assertThat(securityResponse.getPurchaseDate()).isEqualTo("2026-01-23");
		assertThat(securityResponse.getQuantity()).isEqualByComparingTo(25);
		
		verify(validator, times(1)).validatePortfolioRequest(portfolioRequest);
		verify(portfolioRepository, times(1)).save(any(Portfolio.class));
		verify(clientRepository, times(1)).findById(2L);
	}
	
	@Test
	void save_portfolioNotSaved_fail() {
		
		//Arrange
		Long id = 2L;
		ClientRequest clientRequest = new ClientRequest();
			clientRequest.setClientId(id);
			clientRequest.setFirstName("John");
			clientRequest.setLastName("Doe");
			clientRequest.setAddress("564. Miami Fl");
			clientRequest.setPhone("9874563215");
			clientRequest.setEmail("johndoe@gmail.com");

		 PortfolioRequest portfolioRequest = new PortfolioRequest();
		    portfolioRequest.setCreationDate("2026-01-20");
		    portfolioRequest.setClient(clientRequest);
		    portfolioRequest.setSecurities(List.of());

		when(clientRepository.findById(id)).thenReturn(Optional.empty());

		//Act and Assert
		ResourceNotFoundException exception = assertThrows(
				ResourceNotFoundException.class, () -> {
					portfolioServiceImpl.save(portfolioRequest);
				});

		assertThat(exception)
				.isInstanceOf(ResourceNotFoundException.class)
						.hasMessageContaining("Client not found");
		verify(validator,times(1)).validatePortfolioRequest(portfolioRequest);
		verify(clientRepository, times(1)).findById(id);
		verify(portfolioRepository, never()).save(any(Portfolio.class));
	}
	
	
}
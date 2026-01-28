package com.wellsfargo.counselor.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.wellsfargo.counselor.entity.Portfolio;
import com.wellsfargo.counselor.entity.Security;
import com.wellsfargo.counselor.helper.SecurityMapper;
import com.wellsfargo.counselor.model.request.SecurityRequest;
import com.wellsfargo.counselor.model.response.SecurityResponse;
import com.wellsfargo.counselor.repository.PortfolioRepository;
import com.wellsfargo.counselor.repository.SecurityRepository;
import com.wellsfargo.counselor.rest.InvalidRequestException;
import com.wellsfargo.counselor.rest.ResourceNotFoundException;
import com.wellsfargo.counselor.utils.SecurityRequestValidator;

@ExtendWith(MockitoExtension.class)
public class SecurityServiceImplTest {
	
	@Mock
	private SecurityRepository securityRepository;
	
	@Mock
	private SecurityRequestValidator validator;
	
	@Mock
	private SecurityMapper mapper;
	
	@Mock
	private PortfolioRepository portfolioRepository;
	
	@InjectMocks
	private SecurityServiceImpl securityServiceImpl;
	
	private SecurityRequest request;

	private SecurityResponse securityResponse;

	private Security securityEntity;
	
	@BeforeEach
	void setUp() {
		securityEntity = new Security();
		securityEntity.setSecurityId(1L);
		securityEntity.setName("Amazon");
		securityEntity.setCategory("Stock");
		securityEntity.setPurchasePrice(BigDecimal.valueOf(1000.50));
		securityEntity.setPurchaseDate("2026-01-21");
		securityEntity.setQuantity(250);
		
		securityResponse = new SecurityResponse(
		securityEntity.getSecurityId(),
		securityEntity.getName(),
		securityEntity.getCategory(),
		securityEntity.getPurchasePrice(),
		securityEntity.getPurchaseDate(),
		securityEntity.getQuantity(),
		null
	);
}

	@Test
	void shouldNotAddError_whenValidInputForFindById() {
		
	//Arrange
	Long securityId = 1L;

	when(securityRepository.findById(securityId)).thenReturn(Optional.of(securityEntity));
	when(mapper.mapEntityToResponse(securityEntity)).thenReturn(securityResponse);
	
	//Act
	SecurityResponse response = securityServiceImpl.findById(securityId);
	
	//Assert
	assertThat(response).isNotNull();

	assertEquals(securityEntity.getSecurityId(), response.getSecurityId());
	assertEquals(securityEntity.getName(), response.getName());
	assertEquals(securityEntity.getCategory(), response.getCategory());
	assertEquals(securityEntity.getPurchasePrice(), response.getPurchasePrice());
	assertEquals(securityEntity.getPurchaseDate(), response.getPurchaseDate());
	assertEquals(securityEntity.getQuantity(), response.getQuantity());
	
	verify(securityRepository, times(1)).findById(securityId);
	verify(mapper, times(1)).mapEntityToResponse(securityEntity);
	
	}
	
	@Test
	void shouldAddError_whenInvalidId() {
		//Arrange
		Long id = 3L;
		when(securityRepository.findById(id)).thenReturn(Optional.empty());
		
		//Act and Assert
		ResourceNotFoundException exception = 
				assertThrows(ResourceNotFoundException.class, () ->
					securityServiceImpl.findById(id)
						);
		assertThat(exception).isNotNull();
		assertThat(exception.getMessage()).contains("Security not found with id: " + id);
		
		verify(securityRepository, times(1)).findById(id);
	}
	
	@Test
	void shouldNotAddError_whenValidInputforFindByName() {
		//Arrange
		String name = "Amazon";
		when(securityRepository.findByName(name)).thenReturn(Optional.of(securityEntity));
		when(mapper.mapEntityToResponse(securityEntity)).thenReturn(securityResponse);
		
		//Act
		SecurityResponse response = securityServiceImpl.findByName(name);
		
		//Assert
		assertThat(response)
			.as("Valid name input should not add error")
			.isNotNull();
		
		assertEquals(securityEntity.getName(), response.getName());
		
		verify(securityRepository, times(1)).findByName(name);
		verify(mapper, times(1)).mapEntityToResponse(securityEntity);
	}
	
	@Test
	void shouldAddError_whenInvalidNameInput() {
		
		//Arrange
		String name = "Amazon";
		when(securityRepository.findByName(name)).thenReturn(Optional.empty());
	
		//Act and Assert
		ResourceNotFoundException exception = 
				assertThrows(ResourceNotFoundException.class, () ->
								securityServiceImpl.findByName(name)
						);
		
		assertThat(exception)
			.as("Invalid name input throws error")
			.isNotNull();
		
		assertThat(exception.getMessage())
			.contains("Security does not exist with this name: " + name);
		
		verify(securityRepository, times(1)).findByName(name);
		verify(mapper, never()).mapEntityToResponse(any());
		
	}
	
	@Test
	void shouldNotAddError_whenfoundAllSecurities() {
		//Arrange
		 Security security1 = new Security();
		 Security security2 = new Security();

		 List<Security> securityList = new ArrayList<>();
		 securityList.add(security1);
		 securityList.add(security2);

		 SecurityResponse response1 = new SecurityResponse();
		 SecurityResponse response2 = new SecurityResponse();

		 List<SecurityResponse> response = new ArrayList<>();
		 response.add(response1);
		 response.add(response2);
		 
		 when(securityRepository.findAll()).thenReturn(securityList);
		 when(mapper.mapEntityToResponse(security1)).thenReturn(response1);
		 when(mapper.mapEntityToResponse(security2)).thenReturn(response2);

		 //Act
		 List<SecurityResponse> securityResponse = securityServiceImpl.findAll();

		 //Assert
		 assertThat(securityResponse)
		 	.as("Found list of securities, errors not added")
		 	.isNotNull()
		 	.hasSize(2)
		 	.containsExactlyInAnyOrderElementsOf(response);
		 
		 verify(securityRepository, times(1)).findAll();
		 verify(mapper, times(2)).mapEntityToResponse(any(Security.class));
	}
	
	@Test
	void shouldAddError_whenSecuritiesNotFound() {
		//Arrange
		when(securityRepository.findAll()).thenReturn(List.of());
		
		//Act
		List<SecurityResponse> response = securityServiceImpl.findAll();
		
		//Assert
		assertThat(response).isNotNull();

		verify(securityRepository, times(1)).findAll();
		verifyNoInteractions(mapper);
	}

	@Test
	void shouldNotAddError_whenValidInputSaveRequest() {
		
		//Arrange
		Long portfolioId = 2L;
	    request = new SecurityRequest();
		request.setName("Amazon");
		request.setCategory("STOCK");
		request.setPurchasePrice(new BigDecimal(123.54));
		request.setPurchaseDate("2026-01-12");
		request.setQuantity(200);
		request.setPortfolioId(portfolioId);
		
		Portfolio portfolio = new Portfolio();
		portfolio.setPortfolioId(portfolioId);
		
		Security securityEntity = new Security(
		request.getName(),
		request.getCategory(),
		request.getPurchasePrice(),
		request.getPurchaseDate(),
		request.getQuantity(),
		portfolio
	 );
		
		SecurityResponse response = new SecurityResponse();
		response.setName("Amazon");
		
		doNothing().when(validator).validateSecurityRequest(eq(request), anyList());
		when(portfolioRepository.findById(portfolioId)).thenReturn(Optional.of(portfolio));
		when(securityRepository.save(any(Security.class))).thenReturn(securityEntity);
		when(mapper.mapEntityToResponse(securityEntity)).thenReturn(response);
		
		//Act
		SecurityResponse result = securityServiceImpl.save(request);
		
		//Assert
		assertThat(result).isNotNull();
		assertEquals(request.getName(), result.getName());
		
		verify(validator, times(1)).validateSecurityRequest(eq(request), anyList());
		verify(portfolioRepository, times(1)).findById(portfolioId);
		verify(securityRepository, times(1)).save(any(Security.class));
		verify(mapper, times(1)).mapEntityToResponse(securityEntity);
	}
	
	@Test
	void shouldAddError_whenInvaliInputForCreate() {
		
		//Arrange
		request = new SecurityRequest();
		request.setName(null);
		request.setCategory(null);
		request.setPortfolioId(2L);
		
		  doThrow(new InvalidRequestException("Security validation failed"))
	        .when(validator).validateSecurityRequest(eq(request), anyList());

		  InvalidRequestException exception = assertThrows(
		            InvalidRequestException.class,
		            () -> securityServiceImpl.save(request)
		    );

		assertThat(exception).isNotNull();
		assertThat(exception.getMessage()).contains("Security validation failed");
		
		verify(validator, times(1)).validateSecurityRequest(eq(request), anyList());
		verifyNoInteractions(portfolioRepository);
		verifyNoInteractions(securityRepository);
		verifyNoInteractions(mapper);		
	}

	@Test
	void shouldNotAddError_whenValidUpdateRequest() {
		//Arrange
		Long securityId = 1L;
		Long portfolioId = 2L;
		
		request = new SecurityRequest();
		request.setName("Amazon");
		request.setCategory("STOCK");
		request.setPurchasePrice(new BigDecimal(123.54));
		request.setPurchaseDate("2026-01-12");
		request.setQuantity(200);
		request.setPortfolioId(portfolioId);
		
		Security existingSecurity = new Security();
	    existingSecurity.setSecurityId(securityId);
		
		Portfolio portfolio = new Portfolio();
		portfolio.setPortfolioId(portfolioId);
			
		Security securityEntity = new Security(
		request.getName(),
		request.getCategory(),
		request.getPurchasePrice(),
		request.getPurchaseDate(),
		request.getQuantity(),
		portfolio
	);
		securityEntity.setSecurityId(securityId);
				
		SecurityResponse response = new SecurityResponse();
		response.setName("Amazon");
		response.setCategory("STOCK");
				
		doNothing().when(validator).validateSecurityRequest(eq(request), anyList());
		when(securityRepository.findById(securityId)).thenReturn(Optional.of(existingSecurity));
		when(portfolioRepository.findById(portfolioId)).thenReturn(Optional.of(portfolio));
		when(securityRepository.save(any(Security.class))).thenReturn(securityEntity);
		when(mapper.mapEntityToResponse(securityEntity)).thenReturn(response);

		//Act
		SecurityResponse securityResponse = securityServiceImpl.updateSecurity(securityId, request);

		//Assert
		assertThat(securityResponse).isNotNull();
		assertThat(securityResponse.getName()).isEqualTo("Amazon");
		assertThat(securityResponse.getCategory()).isEqualTo("STOCK");

		verify(securityRepository, times(1)).findById(securityId);
	    verify(portfolioRepository, times(1)).findById(portfolioId);
	    verify(securityRepository, times(1)).save(any(Security.class));
	    verify(mapper, times(1)).mapEntityToResponse(securityEntity);
		verify(validator,times(1)).validateSecurityRequest(eq(request), anyList());
		

	}
	
	@Test
	void shouldThrowResourceNotFound_whenSecurityDoesNotExist() {
		//Arrange
		Long securityId = 1L;
		Long portfolioId = 2L;
		
		request = new SecurityRequest();
		request.setName("Amazon");
		request.setCategory("STOCK");
		request.setPurchasePrice(new BigDecimal("123.54"));
		request.setPurchaseDate("2026-01-12");
		request.setQuantity(200);
		request.setPortfolioId(portfolioId);
		
		when(securityRepository.findById(securityId)).thenReturn(Optional.empty());
		
		//Act and Assert
		ResourceNotFoundException exception = 
				assertThrows(ResourceNotFoundException.class, () ->
					securityServiceImpl.updateSecurity(securityId, request)
						);

		//Assert
		assertThat(exception).isNotNull();
		assertThat(exception.getMessage()).contains("Security not found with id");
		
		verify(securityRepository, times(1)).findById(securityId);
		verify(securityRepository, never()).save(any(Security.class));
		
	}
	
}





















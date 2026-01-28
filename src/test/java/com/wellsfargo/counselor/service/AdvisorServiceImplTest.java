package com.wellsfargo.counselor.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
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
import com.wellsfargo.counselor.model.response.AdvisorResponse;
import com.wellsfargo.counselor.repository.AdvisorRepository;
import com.wellsfargo.counselor.rest.ResourceCreationException;
import com.wellsfargo.counselor.rest.ResourceNotFoundException;
import com.wellsfargo.counselor.utils.AdvisorRequestValidator;

@ExtendWith(MockitoExtension.class)
public class AdvisorServiceImplTest {

	@Mock
	AdvisorRepository advisorRepository;
	
	@Mock
	AdvisorRequestValidator validator;
	
	@InjectMocks
	AdvisorServiceImpl advisorServiceImpl;
	
	private AdvisorRequest advisorRequest;
	
	private Advisor advisor;
		
	@BeforeEach
	void setUp() {
	advisorRequest = new AdvisorRequest();
	advisorRequest.setFirstName("Paul");
	advisorRequest.setLastName("Doe");
	advisorRequest.setAddress("1355, Texas");
	advisorRequest.setPhone("1234567895");
	advisorRequest.setEmail("pauldoe@gmail.com");
	}

	@Test
	void findById_whenAdvisorFoundWithId_success(){

		// Arrange
		Long id = 15L;	
	    advisor = new Advisor(id, "135, Albany NewYork","John"
				,"Doe","1234567895","johndoe@gmail.com", null);
		
		when(advisorRepository.findById(id)).thenReturn(Optional.of(advisor));
		
		// Act
		AdvisorResponse result = advisorServiceImpl.findById(id); 

		// Assert
		assertThat(result).isNotNull();
		assertEquals(advisor.getAdvisorId(), result.getAdvisorId());
		assertEquals(advisor.getFirstName(), result.getFirstName());
		assertEquals(advisor.getLastName(), result.getLastName());
		assertEquals(advisor.getAddress(), result.getAddress());
		assertEquals(advisor.getPhone(), result.getPhone());
		assertEquals(advisor.getEmail(), result.getEmail());
		
		verify(advisorRepository, times(1)).findById(id);
	}

	@Test
	void findById_whenNoAdvisorFoundWithId_fail() {

	    // Arrange
	    Long id = 1L;
	    when(advisorRepository.findById(id)).thenReturn(Optional.empty());

	    // Act & Assert
	   ResourceNotFoundException exception =
			   assertThrows(ResourceNotFoundException.class, () -> 
				   			advisorServiceImpl.findById(id)
					  	);
	   
	   assertThat(exception.getMessage().contains("Advisor not found with id " + id));
	   
	   verify(advisorRepository).findById(id);
	}	

	@Test
	void saveAdvisor_whenAdvisorSaved_success() {

		//Arrange
		Long id = 1L;
		Advisor advisorEntity = new Advisor(
				id,advisorRequest.getFirstName(),advisorRequest.getLastName(),
				 advisorRequest.getAddress(), advisorRequest.getPhone(),
				 advisorRequest.getEmail(), null);
		
		when(advisorRepository.save(any(Advisor.class))).thenReturn(advisorEntity);

		///Act
       AdvisorResponse response = advisorServiceImpl.save(advisorRequest);

		//Assert
        assertThat(response).isNotNull();
        assertEquals(advisorRequest.getFirstName(), response.getFirstName());
        assertEquals(advisorRequest.getLastName(), response.getLastName());
        assertEquals(advisorRequest.getAddress(), response.getAddress());
        assertEquals(advisorRequest.getPhone(), response.getPhone());
        assertEquals(advisorRequest.getEmail(), response.getEmail());
        
        verify(validator, times(1))
        	.validateAdvisorRequest(eq(advisorRequest), isNull(), anyList());
        
        verify(advisorRepository, times(1))
        	.save(any(Advisor.class));
	}

	@Test
	void saveAdvisor_whenAdvisorNotSaved_fail() {
		//Arrange
		when(advisorRepository.save(any(Advisor.class))).thenReturn(null);
		
		//Act and Assert
		
		ResourceCreationException exception = assertThrows( 
					ResourceCreationException.class, () -> {
						advisorServiceImpl.save(advisorRequest);
			});
		//Assert
		assertThat(exception).isNotNull();
		
		verify(validator, times(1))
     		.validateAdvisorRequest(eq(advisorRequest), isNull(), anyList());
     
		verify(advisorRepository, times(1))
     		.save(any(Advisor.class));
	}

	@Test
	void findAll_whenAdvisorFound_success() {

	//Arrange
	Advisor advisor = new Advisor(null, "135, Albany NewYork","John"
			,"Doe","1234567895","johndoe@gmail.com", null);
	
	List<Advisor> advisorList = new ArrayList<>();
	advisorList.add(advisor);
	
	when(advisorRepository.findAll()).thenReturn(advisorList);

	//Act
	List<AdvisorResponse> result = advisorServiceImpl.findAll();

	//Assert
	assertThat(result).isNotEmpty();
	assertEquals(1, result.size());
	
	AdvisorResponse response = result.get(0);
	assertEquals(advisor.getAdvisorId(), response.getAdvisorId());
	assertEquals(advisor.getFirstName(), response.getFirstName());
	assertEquals(advisor.getLastName(), response.getLastName());
	assertEquals(advisor.getAddress(), response.getAddress());
	assertEquals(advisor.getPhone(), response.getPhone());
	assertEquals(advisor.getEmail(), response.getEmail());
	verify(advisorRepository).findAll();	
   }
	
	@Test
	void findAll_whenNoAdvisorFound_fail() {
		//Arrange
		when(advisorRepository.findAll()).thenReturn(List.of());
		
		//Act
		List<AdvisorResponse> result = advisorServiceImpl.findAll();
		
		//Assert
		assertThat(result).isEmpty();
		verify(advisorRepository).findAll();
	}

	@Test
	void updateAdvisor_whenAdvisorUpdated_success() {
		//Arrange
		Long id = 1L;
		Advisor advisor = new Advisor(id,
				advisorRequest.getFirstName(), advisorRequest.getLastName(),
				advisorRequest.getAddress(),advisorRequest.getPhone(),
				advisorRequest.getEmail(),null);	
		when(advisorRepository.findById(id)).thenReturn(Optional.of(advisor));
		when(advisorRepository.save(any(Advisor.class))).thenReturn(advisor);

		//Act
		advisorServiceImpl.updateAdvisor(id, advisorRequest);
		
		//Assert
		assertEquals(advisorRequest.getFirstName(), advisor.getFirstName());
		assertEquals(advisorRequest.getLastName(), advisor.getLastName());
		assertEquals(advisorRequest.getAddress(), advisor.getAddress());
		assertEquals(advisorRequest.getPhone(), advisor.getPhone());
		assertEquals(advisorRequest.getEmail(), advisor.getEmail());
		
		verify(validator, times(1))
			.validateAdvisorRequest(eq(advisorRequest), eq(id), anyList());
		verify(advisorRepository, times(1))
			.findById(id);
		verify(advisorRepository, times(1))
			.save(any(Advisor.class));
	}
	
	@Test
	void updateAdvisor_whenNoAdvisorUpdated_fail() {
		//Arrange
		Long id = 1L;
		when(advisorRepository.findById(id)).thenReturn(Optional.empty());
		
		//Act and Assert
		ResourceNotFoundException exception = assertThrows(
				ResourceNotFoundException.class, () -> {
					advisorServiceImpl.updateAdvisor(id, advisorRequest);
				});
		
		assertThat(exception.getMessage()
					.contains("Advisor not found with id " + id));
		
		verify(advisorRepository, times(1)).findById(id);
		verify(advisorRepository, never()).save(any());
	}	
	
	@Test
	void findAdvisorByClientId_advisorFound_success() {
		
		//Arrange
		Long clientId = 4L;
		
		Advisor advisor = new Advisor();
		advisor.setAdvisorId(1L);
		advisor.setFirstName("John");
		advisor.setLastName("Doe");
		advisor.setAddress("546, Miami Fl");
		advisor.setPhone("9856478532");
		advisor.setEmail("johndoe@gmail.com");
		
		when(advisorRepository.findByClients_ClientId(clientId)).thenReturn(Optional.of(advisor));
		
		//Act
		AdvisorResponse response = advisorServiceImpl.findAdvisorByClientId(clientId);
		
		//Assert
		assertThat(response).isNotNull();
		
		verify(advisorRepository, times(1)).findByClients_ClientId(clientId);
	}

	@Test
	void findAdvisorByClientId_advisorNotFound_fail() {
		
		//Arrange
		Long clientId = 5L;
		
		when(advisorRepository.findByClients_ClientId(clientId)).thenReturn(Optional.empty());
		
		//Act and Assert
		ResourceNotFoundException exception = assertThrows(
						ResourceNotFoundException.class, () -> {
							advisorServiceImpl.findAdvisorByClientId(clientId);
						});
		
		//Assert
		assertThat(exception).isNotNull();
		
		verify(advisorRepository, times(1)).findByClients_ClientId(clientId);
	}
	
	
}
package com.wellsfargo.counselor.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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
import com.wellsfargo.counselor.model.request.AdvisorResponse;
import com.wellsfargo.counselor.repository.AdvisorRepository;
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
	advisorRequest.setEmailAddress("pauldoe@gmail.com");
	advisorRequest.setPhoneNumber("1234567895");
	advisorRequest.setAddress("1355, Texas");
	
	}
	
	@Test
	void findById_whenAdvisorFoundWithId_success(){

		// Arrange
	    advisor = new Advisor();
		Long id = 15L;	
		when(advisorRepository.findById(id)).thenReturn(Optional.of(advisor));
		
		// Act
		AdvisorResponse result = advisorServiceImpl.findById(id); 
		
		// Assert
		assertNotNull(result);
		verify(advisorRepository, times(1)).findById(id);
	}

	@Test
	void findById_whenNoAdvisorFoundWithId_fail() {

	    // Arrange
	    Long id = 1L;
	    when(advisorRepository.findById(id)).thenReturn(Optional.empty());

	    // Act & Assert
	   ResourceNotFoundException exception =
			   assertThrows(ResourceNotFoundException.class,
					  () -> {advisorServiceImpl.findById(id);
					  	});
	   assertTrue(exception.getMessage().contains("Advisor not found with id " + id));
	   verify(advisorRepository).findById(id);
	}	

	@Test
	void saveAdvisor_whenAdvisorSaved_success() {
		
		//Arrange
		Advisor advisorEntity = new Advisor(
				null,advisorRequest.getFirstName(),advisorRequest.getLastName(),
				 advisorRequest.getAddress(), advisorRequest.getPhoneNumber(),
				 advisorRequest.getEmailAddress(), null);
        
		when(advisorRepository.save(any(Advisor.class))).thenReturn(advisorEntity);

		///Act
       AdvisorResponse response = advisorServiceImpl.save(advisorRequest);

		//Assert
        assertNotNull(response);
        assertEquals(advisorRequest.getFirstName(), response.getFirstName());
        verify(advisorRepository, times(1)).save(any(Advisor.class));

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
	assertFalse(result.isEmpty());
	verify(advisorRepository).findAll();
		
	}
	
	@Test
	void findAll_whenNoAdvisorFound_fail() {
		//Arrange
		when(advisorRepository.findAll()).thenReturn(List.of());
		
		//Act
		List<AdvisorResponse> result = advisorServiceImpl.findAll();
		
		//Assert
		assertTrue(result.isEmpty());
		verify(advisorRepository).findAll();
		
	}
	
	@Test
	void updateAdvisor_whenAdvisorUpdated_success() {
		//Arrange
		Long id = 1L;
		Advisor advisor = new Advisor(null, advisorRequest.getAddress(),
				advisorRequest.getFirstName(), advisorRequest.getLastName(),
				advisorRequest.getPhoneNumber(),advisorRequest.getEmailAddress(), null);	
		when(advisorRepository.findById(id)).thenReturn(Optional.of(advisor));
		when(advisorRepository.save(any(Advisor.class))).thenReturn(advisor);

		//Act
		advisorServiceImpl.updateAdvisor(id, advisorRequest);
		
		//Assert
		assertEquals(advisorRequest.getFirstName(), advisor.getFirstName());
		assertEquals(advisorRequest.getLastName(), advisor.getLastName());
		assertEquals(advisorRequest.getAddress(), advisor.getAddress());
		assertEquals(advisorRequest.getPhoneNumber(), advisor.getPhone());
		assertEquals(advisorRequest.getEmailAddress(), advisor.getEmail());
		verify(advisorRepository, times(1)).save(any(Advisor.class));
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
		
		assertTrue(exception.getMessage().contains("Advisor not found with id " + id));
		
	}
	
	
}	





















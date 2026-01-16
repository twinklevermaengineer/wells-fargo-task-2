package com.wellsfargo.counselor.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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
	void findById_success(){

		// Arrange
	    advisor = new Advisor();
		Long id = 15L;	
		when(advisorRepository.findById(id)).thenReturn(Optional.of(advisor));
		
		// Act
		AdvisorResponse result = advisorServiceImpl.findById(id); 
		
		// Assert
		assertTrue(result !=null);	
	}

	@Test
	void findById_fail() {
		
		// Arrange
		Long id = 1L;
		when(advisorRepository.findById(id)).thenReturn(Optional.empty());

		// Act
		AdvisorResponse result = advisorServiceImpl.findById(id);
		
		// Assert
		assertFalse(result != null);
	}

	@Test
	void saveAdvisor_success() {
		
		//Arrange
		List<String> errors = new ArrayList<>();

		Advisor advisorEntity = new Advisor(null, advisorRequest.getAddress()
				,advisorRequest.getFirstName(),advisorRequest.getLastName()
				,advisorRequest.getPhoneNumber(),advisorRequest.getEmailAddress(), null);
        
		when(advisorRepository.save(any(Advisor.class))).thenReturn(advisorEntity);

		///Act
        advisorServiceImpl.save(advisorRequest);

		//Assert
		assertTrue(errors.isEmpty());
	}

	@Test
	void findAll_success() {

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

	}
	
	@Test
	void updateAdvisor_success() {
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
	}
}





















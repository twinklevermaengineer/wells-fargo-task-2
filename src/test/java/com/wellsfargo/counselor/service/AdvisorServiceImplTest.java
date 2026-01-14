package com.wellsfargo.counselor.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
		Optional<Advisor> result = advisorServiceImpl.findById(id); 
		
		// Assert
		assertTrue(result.get() !=null);	
	}

	@Test
	void findById_fail() {
		
		// Arrange
		Long id = 1L;
		when(advisorRepository.findById(id)).thenReturn(Optional.empty());

		// Act
		Optional<Advisor> result = advisorServiceImpl.findById(id);
		
		// Assert
		assertTrue(result.isEmpty());
	}

	@Test
	void saveAdvisor_success() {
		
		//Arrange
		List<String> errors = new ArrayList<>();

		Advisor advisorEntity = new Advisor(advisorRequest.getAddress()
				,advisorRequest.getFirstName(),advisorRequest.getLastName()
				,advisorRequest.getPhoneNumber(),advisorRequest.getEmailAddress());
        
		when(advisorRepository.save(any(Advisor.class))).thenReturn(advisorEntity);

		///Act
        advisorServiceImpl.save(advisorRequest);

		//Assert
		assertTrue(errors.isEmpty());
	}

	@Test
	void findAll_success() {

	//Arrange
	Advisor advisor = new Advisor("1325,Plano","John"
			,"Doe","1234567895","johndoe@gmail.com");
	
	List<Advisor> advisorList = new ArrayList<>();
	advisorList.add(advisor);
	
	when(advisorRepository.findAll()).thenReturn(advisorList);

	//Act
	List<Advisor> result = advisorServiceImpl.findAll();

	//Assert
	assertFalse(result.isEmpty());

	}
	
	/*
	@Test
	void updateAdvisor_success() {
		//Arrange
		List<String> errors = new ArrayList<>();
		Long id = 1L;	
		
		
		when(advisorRepository.findById(id)).thenReturn(Optional.of(advisor));			
	}
*/
}





















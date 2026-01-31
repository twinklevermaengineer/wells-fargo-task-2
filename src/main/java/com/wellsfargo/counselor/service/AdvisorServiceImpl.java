package com.wellsfargo.counselor.service;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wellsfargo.counselor.repository.AdvisorRepository;
import com.wellsfargo.counselor.entity.Advisor;
import com.wellsfargo.counselor.helper.AdvisorMapper;
import com.wellsfargo.counselor.model.request.AdvisorRequest;
import com.wellsfargo.counselor.model.response.AdvisorResponse;
import com.wellsfargo.counselor.rest.ResourceCreationException;
import com.wellsfargo.counselor.rest.ResourceNotFoundException;
import com.wellsfargo.counselor.utils.AdvisorRequestValidator;
import jakarta.transaction.Transactional;

/**
 * Implementation of {@link AdvisorService} for managing Advisor entities.
 * Provides operations to create, read, update and delete advisors,
 * as well as fetching advisors by specific criteria such as address or client ID.
 * 
 * <p>
 * This service performs input validation, exception handling, and entity-to-DTO mapping.</p>
 * 
 * @author Twinkle Verma
 * @version 1.0
*/
@Service
public class AdvisorServiceImpl implements AdvisorService {
	private static final Logger logger = LoggerFactory.getLogger(AdvisorServiceImpl.class);
	
	private AdvisorRepository advisorRepository;
	private AdvisorRequestValidator validator;
	
	/**
	 * Constructs an AdvisorServiceImpl with the specified repository and validator.
	 * 
	 * @param advisorRepository the repository used to access advisor data
	 * @param validator the validator used to validate advisor requests
	*/

	@Autowired
	public AdvisorServiceImpl(AdvisorRepository advisorRepository, AdvisorRequestValidator validator) {
		this.advisorRepository = advisorRepository;
		this.validator = validator;
	}

	/**
	 * Find an advisor by its unique ID
	 * 
	 * @param id the ID of the advisor to retrieve
	 * @return the {@link AdvisorResponse} representing the advisor
	 * @throws ResourceNotFoundException if no advisor is found with the given ID
	*/
	@Override
	public AdvisorResponse findById(Long id) {
		logger.info("Fetching advisor with id {}", id);
		
		Advisor advisorFromDB = advisorRepository.findById(id)
				.orElseThrow(() -> 
					new ResourceNotFoundException("Advisor not found with id: " + id)
				);
		 return AdvisorMapper.mapEntityToResponse(advisorFromDB);
		}

	
	/**
	 * Saves a new advisor to the repository after validating the request.
	 * 
	 * @param advisorRequest the advisor data to save
	 * @return the {@link AdvisorResponse} representing the saved advisor
	 * @throws ResourceCreationException if validation fails or saving the advisor fails 
	*/
	@Override
	@Transactional
	public AdvisorResponse save(AdvisorRequest advisorRequest) {
		logger.info("Creating advisor with email: {}", advisorRequest.getEmail());
		// Validate Input request
		List<String> errorMessages = new ArrayList<>();
		validator.validateAdvisorRequest(advisorRequest,null,errorMessages);
		if (!errorMessages.isEmpty()) {
		    throw new ResourceCreationException(
		        "Validation failed: " +  errorMessages);
		}
		try {
		logger.info("Mapping AdvisorRequest object to Advisor entity");
		Advisor advisorEntity = new Advisor(
				null, advisorRequest.getFirstName(),
				advisorRequest.getLastName(),
				advisorRequest.getAddress(),
				advisorRequest.getPhone(),
				advisorRequest.getEmail(), null);

		  Advisor saveAdvisor = advisorRepository.save(advisorEntity);
		  if(saveAdvisor != null) {
			  logger.info("Mapping Advisor entity to AdvisorResponse DTO");
			  return AdvisorMapper.mapEntityToResponse(saveAdvisor);
		  }
		} catch(Exception excp) {	
			logger.error("Exception occured when creating advisor");
			throw new ResourceCreationException("Advisor creation failed, request: " +
						advisorRequest.toString() + ",excp:" + excp.getMessage());
		}
		throw new ResourceCreationException("Advisor creation failed");
	}

	/**
	 * Updates an existing advisor with the given ID using the provided request data.
	 * 
	 * @param id the ID of the advisor to update
	 * @param advisorRequest the new advisor data
	 * @throws ResourceCreationException if validation fails
	 * @throws ResourceNotFoundException if no advisor exists with the given ID
	*/
	@Override
	@Transactional
	public void updateAdvisor(Long id, AdvisorRequest advisorRequest) {
		logger.info("Updating advisor with id: {} ", id);
		List<String> errors = new ArrayList<>();
		validator.validateAdvisorRequest(advisorRequest, id, errors);
		
		if (!errors.isEmpty()) {
		    throw new ResourceCreationException(
		        "Validation failed: " + errors);
	}
		logger.info("Fetching advisor with id {}", id);
		Advisor advisor = advisorRepository.findById(id)
	            .orElseThrow(() -> 
	                  new ResourceNotFoundException("Advisor not found with id " + id)
	                    );

		advisor.setFirstName(advisorRequest.getFirstName());
		advisor.setLastName(advisorRequest.getLastName());
		advisor.setAddress(advisorRequest.getAddress());
		advisor.setPhone(advisorRequest.getPhone());
		advisor.setEmail(advisorRequest.getEmail());
		
		logger.info("Successfully updated advisor with id {}", id);
		advisorRepository.save(advisor);
	}

	/**
	 * Retrieves all advisors from the repository.
	 * 
	 * @return a list of {@link AdvisorResponse} objects representing all advisors
	*/
	@Override
	public List<AdvisorResponse> findAll() {  
		logger.info("Fetching all advisors from DB");
		List<Advisor> advisorsFromDB = advisorRepository.findAll();
		
		List<AdvisorResponse> advisorResponse = new ArrayList<>();
		for(Advisor advisor: advisorsFromDB) {
			
			logger.info("Mapping Advisor entity to AdvisorResponse DTO");
			advisorResponse.add(AdvisorMapper.mapEntityToResponse(advisor));
	    }
	    return advisorResponse;
	}

	/**
	 * Deletes an advisor with the specified ID.
	 * 
	 * @param id the ID of the advisor to delete
	*/
	@Override
	@Transactional
	public void deleteById(Long id) {

		logger.warn("Deleting advisor with id {}", id);
		advisorRepository.deleteById(id);	
	}

	/**
	 * Finds an advisor by their address.
	 * 
	 * @param address the address of the advisor
	 * @return the {@link AdvisorResponse} representing the advisor
	 * @throws ResourceNotFoundException if no advisor is found with the given address
	*/
	@Override
	public AdvisorResponse findByAddress(String address) {
		logger.info("Fetching advisor with address {} ", address);
		
		Advisor advisorFromDB = advisorRepository.findByAddress(address)
				.orElseThrow(() -> 					
					new ResourceNotFoundException("Advisor not found with address " + address));
		
		return AdvisorMapper.mapEntityToResponse(advisorFromDB);
		}
	
	/**
	 * Find the advisor associated with a specific client ID.
	 * 
	 * @param clientId the ID of the client
	 * @return the {@link AdvisorResponse} representing the advisor
	 * @throws ResourceNotFoundException if no advisor is found for the given client ID
	*/
	@Override
	public AdvisorResponse findAdvisorByClientId(Long clientId) {
	
		logger.info("Fetching advisor by client id {} ", clientId);
		Advisor advisor = advisorRepository.findByClients_ClientId(clientId)
				 .orElseThrow(() ->
	                new ResourceNotFoundException("Advisor not found for client id: " + clientId));
	
		logger.info("Mapping advisor entity to AdvisorResponse DTO");
		return AdvisorMapper.mapEntityToResponse(advisor);
	}
}
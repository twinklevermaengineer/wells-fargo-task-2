package com.wellsfargo.counselor.service;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wellsfargo.counselor.repository.AdvisorRepository;
import com.wellsfargo.counselor.entity.Advisor;
import com.wellsfargo.counselor.model.request.AdvisorRequest;
import com.wellsfargo.counselor.model.response.AdvisorResponse;
import com.wellsfargo.counselor.rest.ResourceCreationException;
import com.wellsfargo.counselor.rest.ResourceNotFoundException;
import com.wellsfargo.counselor.utils.AdvisorRequestValidator;
import jakarta.transaction.Transactional;

@Service
public class AdvisorServiceImpl implements AdvisorService {
	private static final Logger logger = LoggerFactory.getLogger(AdvisorServiceImpl.class);
	
	private AdvisorRepository advisorRepository;
	private AdvisorRequestValidator validator;
	
	@Autowired
	public AdvisorServiceImpl(AdvisorRepository advisorRepository, AdvisorRequestValidator validator) {
		this.advisorRepository = advisorRepository;
		this.validator = validator;
	}
	
	@Override
	public AdvisorResponse findById(Long id) {
		logger.info("Finding Advisor, id {} ", id);
		
		Advisor advisorFromDB = advisorRepository.findById(id)
				.orElseThrow(() -> 
					new ResourceNotFoundException("Advisor not found with id " + id)
				);
		return new AdvisorResponse(
					advisorFromDB.getAdvisorId(),
					advisorFromDB.getFirstName(),
					advisorFromDB.getLastName(),
					advisorFromDB.getAddress(),
					advisorFromDB.getPhone(),
					advisorFromDB.getEmail());
		}

	@Override
	@Transactional
	public AdvisorResponse save(AdvisorRequest advisorRequest) {
		
		// Validate Input request
		List<String> errorMessages = new ArrayList<>();
		validator.validateAdvisorRequest(advisorRequest,null,errorMessages);
		if (!errorMessages.isEmpty()) {
		    throw new ResourceCreationException(
		        "Validation failed: " +  errorMessages);
		}
		try {
		// Map Request object to Entity Object
		logger.info("Advisor request object map to asdvisor entity object");
		Advisor advisorEntity = new Advisor(
				null, advisorRequest.getFirstName(),
				advisorRequest.getLastName(),
				advisorRequest.getAddress(),
				advisorRequest.getPhone(),
				advisorRequest.getEmail(), null);

		  Advisor saveAdvisor = advisorRepository.save(advisorEntity);
		  if(saveAdvisor != null) {
			 // Map Entity object to Response object
			  logger.info("Advisor entity object map to advisor response object");
			  AdvisorResponse advisorResponse = new AdvisorResponse(
					  saveAdvisor.getAdvisorId(),
					  saveAdvisor.getFirstName(),
					  saveAdvisor.getLastName(),
					  saveAdvisor.getAddress(),
					  saveAdvisor.getPhone(),
					  saveAdvisor.getEmail());
			  return advisorResponse;
		  }
		} catch(Exception excp) {	
			logger.error("Exception occured when creating advisor");
			throw new ResourceCreationException("Advisor creation failed, request: " +
						advisorRequest.toString() + ",excp:" + excp.getMessage());
		}
		throw new ResourceCreationException("Advisor creation failed");
	}

	@Override
	@Transactional
	public void updateAdvisor(Long id, AdvisorRequest advisorRequest) {
		List<String> errors = new ArrayList<>();
		validator.validateAdvisorRequest(advisorRequest, id, errors);
		
		if (!errors.isEmpty()) {
		    throw new ResourceCreationException(
		        "Validation failed: " + errors);
	}
		logger.info("Find advisor with id {} ", id);
		Advisor advisor = advisorRepository.findById(id)
	            .orElseThrow(() -> 
	                  new ResourceNotFoundException("Advisor not found with id " + id)
	                    );

		advisor.setFirstName(advisorRequest.getFirstName());
		advisor.setLastName(advisorRequest.getLastName());
		advisor.setAddress(advisorRequest.getAddress());
		advisor.setPhone(advisorRequest.getPhone());
		advisor.setEmail(advisorRequest.getEmail());
		
		logger.info("Saved advisor, id {} ", id);
		advisorRepository.save(advisor);
	}
	
	@Override
	public List<AdvisorResponse> findAll() {  
		logger.info("Fetching all advisors from DB");
		List<Advisor> advisorsFromDB = advisorRepository.findAll();
		
		List<AdvisorResponse> advisorResponse = new ArrayList<>();
		for(Advisor advisor: advisorsFromDB) {
			logger.info("Map advisor from DB to advisor response");
			advisorResponse.add(new AdvisorResponse(
					advisor.getAdvisorId(),advisor.getFirstName(),
					advisor.getLastName(), advisor.getAddress(),
					advisor.getPhone(),advisor.getEmail()));
		}		
		return advisorResponse;
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		logger.warn("Deleting advisor, id {} ", id);
		advisorRepository.deleteById(id);	
	}	
	
	@Override
	public AdvisorResponse findByAddress(String address) {
		logger.info("Fetching advisor with address {} ", address);
		
		Advisor advisorFromDb = advisorRepository.findByAddress(address)
				.orElseThrow(() -> 					
					new ResourceNotFoundException("Advisor not found with address " + address));
		
			return new AdvisorResponse(
					advisorFromDb.getAdvisorId(),
					advisorFromDb.getFirstName(), 
					advisorFromDb.getLastName(),
					advisorFromDb.getAddress(),
					advisorFromDb.getPhone(),
					advisorFromDb.getEmail());	
			}

	@Override
	public AdvisorResponse findAdvisorByClientId(Long clientId) {
		
		logger.info("Fetching advisor by client id {} ", clientId);
		Advisor advisor = advisorRepository.findByClients_ClientId(clientId)
				 .orElseThrow(() ->
	                new ResourceNotFoundException("Advisor not found for client id " + clientId));
		
		logger.info("Mapping advisor entity to AdvisorResponse DTO");
	return new AdvisorResponse(
				advisor.getAdvisorId(),
				advisor.getFirstName(),
				advisor.getLastName(),
				advisor.getAddress(),
				advisor.getPhone(),
				advisor.getEmail()
				);
		
	}
	
}
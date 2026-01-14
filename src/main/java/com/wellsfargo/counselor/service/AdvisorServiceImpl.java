package com.wellsfargo.counselor.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.wellsfargo.counselor.repository.AdvisorRepository;
import com.wellsfargo.counselor.entity.Advisor;
import com.wellsfargo.counselor.model.request.AdvisorRequest;

import com.wellsfargo.counselor.rest.AdvisorCreationException;
import com.wellsfargo.counselor.rest.InvalidRequestException;
import com.wellsfargo.counselor.rest.UserNotFoundException;
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
	public Optional<Advisor> findById(Long id) {
		logger.info("Find Advisor using id " + id);
		return advisorRepository.findById(id);
	}

	@Override
	@Transactional
	public Long save(AdvisorRequest advisorRequest) {
		
		// Validate Input request
		List<String> errorMessages = new ArrayList<>();
		validator.validateAdvisorRequest(advisorRequest,null,errorMessages);
		if(!errorMessages.isEmpty()) {
			logger.error("Invalid advisor request");
			errorMessages.add("Invalid advisor request for " + advisorRequest);
			throw new InvalidRequestException("Invalid Advisor Request," + advisorRequest.toString());
		}
	
		// Map Request object to Entity Object
		Advisor advisorEntity = new Advisor(advisorRequest.getFirstName()
				,advisorRequest.getLastName(),advisorRequest.getAddress()
				,advisorRequest.getPhoneNumber(),advisorRequest.getEmailAddress());
		
		try {
		  Advisor saveAdvisor = advisorRepository.save(advisorEntity);
		  if (ObjectUtils.isEmpty(saveAdvisor)) {
			  logger.error("Unexpected error occured while saving Advisor entity");
			  throw new Exception("Unexpected error occured while saving Advisor entity");
		  }
		  logger.info("Save Advisor with id");
		  return saveAdvisor.getAdvisorId();
		}
		catch (DataIntegrityViolationException excp) {
			Throwable rootCause = excp.getRootCause();

			String error = "Unknown Error";
		    if (rootCause != null && rootCause.getMessage() != null) {
		        if (rootCause.getMessage().contains("Duplicate entry")) {
		            error = "Email " + advisorRequest.getEmailAddress()
		            + " already exists. Please use a different email address.";
		        }
	    }		    
			throw new AdvisorCreationException("Error occured while saving advisor object, "
			+ error);
		}
		catch (Exception excp) {
			throw new AdvisorCreationException("Error occured while saving advisor object, "
			+ excp.getMessage());
		}
	}


	@Override
	@Transactional
	public void updateAdvisor(Long id, AdvisorRequest advisorRequest) {
		List<String> errors = new ArrayList<>();
		validator.validateAdvisorRequest(advisorRequest, id, errors);
			if(!errors.isEmpty()) {
				logger.error("Invalid advisor input");
     			throw new InvalidRequestException(
     					"Invalid advisor input, " + advisorRequest.toString());
		}
		
		Advisor advisor = advisorRepository.findById(id)
	            .orElseThrow(() ->
	                    new UserNotFoundException("Advisor not found with id " + id));
	
		
		advisor.setFirstName(advisorRequest.getFirstName());
		advisor.setLastName(advisorRequest.getLastName());
		advisor.setAddress(advisorRequest.getAddress());
		advisor.setPhone(advisorRequest.getPhoneNumber());
		advisor.setEmail(advisorRequest.getEmailAddress());
		
		advisorRepository.save(advisor);
	}
	@Override
	public List<Advisor> findAll() {  
		return advisorRepository.findAll();
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		advisorRepository.deleteById(id);	
	}

}




























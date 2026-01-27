package com.wellsfargo.counselor.controller;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.wellsfargo.counselor.model.request.AdvisorRequest;
import com.wellsfargo.counselor.model.response.AdvisorResponse;
import com.wellsfargo.counselor.rest.ResourceNotFoundException;
import com.wellsfargo.counselor.service.AdvisorService;

@RestController
@RequestMapping("/api/v1/advisors")
public class AdvisorController {
	
private static final Logger logger = LoggerFactory.getLogger(AdvisorController.class);
	
	@Autowired
	private AdvisorService advisorService;
	
	@GetMapping("/")
	public ResponseEntity<List<AdvisorResponse>> getAdvisor(@RequestParam(required = false) String address) {
		
			if(address !=null) {
				logger.info("Fetching advisor, address {} " , address);
			
			AdvisorResponse advisor = advisorService.findByAddress(address);
				
				if(advisor != null) {
					logger.info("Advisor found, address {}", address);
				
				List<AdvisorResponse> advisors = new ArrayList<>();
					advisors.add(advisor);
				return ResponseEntity.ok(advisors);
			}
					logger.error("Advisor not found with address {} " , address);
				throw new ResourceNotFoundException("Advisor not found with address " + address);
		} else {
				logger.info("Fetching all the advisors");
			List<AdvisorResponse> advisor = advisorService.findAll();
			if(advisor.isEmpty()) {
				logger.error("Advisors not found");
				return ResponseEntity.noContent().build();
			}
			logger.info("List of advisors found ", advisor);
			return ResponseEntity.ok(advisor);	
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<AdvisorResponse> getAdvisorById(@PathVariable Long id) {
	logger.info("Fetching advisor, id: {} ", id);	
	AdvisorResponse advisor =	advisorService.findById(id);
		if(advisor != null) {
			logger.info("Found advisor, id {}" , advisor);
			return ResponseEntity.ok(advisor);
		}else {
			logger.error("Advisor not found, id {} ", id);
		throw new ResourceNotFoundException("Advisor not found, id: " + id);
		}
	}
	
	@GetMapping("/{clientId}/advisor")
	public ResponseEntity<AdvisorResponse> getAdvisorByClientId(@PathVariable Long clientId){
		logger.info("Fetching advisor by client id, clientId {} ", clientId);
		AdvisorResponse response = advisorService.findAdvisorByClientId(clientId);
		
			if(response != null) {
				logger.info("Advisor found associated to client id {} ", clientId);
				return ResponseEntity.ok(response);
			}
		logger.error("Advisor not found associated to clientId: {} ", clientId);
		return ResponseEntity.noContent().build();	
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<String> updateAdvisor(
			@PathVariable Long id, 
			@RequestBody AdvisorRequest advisorRequest){
		logger.info("Fetching advisor id for update request {} ", id);
		advisorService.updateAdvisor(id, advisorRequest);
		
		return ResponseEntity.ok("Advisor updated successfully for id: " + id);	
	}
	
	@PostMapping("/")
	public ResponseEntity<AdvisorResponse> addAdvisor(@RequestBody AdvisorRequest advisor) {
		
		logger.info("Received new advisor request {}", advisor.toString());
		AdvisorResponse advisorResponse = advisorService.save(advisor);
		
		logger.info("Advisor created successfully, advisor id {}", advisorResponse.getAdvisorId());
		return ResponseEntity.status(HttpStatus.CREATED).body(advisorResponse);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteAdvisor(@PathVariable Long id) {
		
		logger.warn("Deleting advisor, id: {} ", id);
		
		advisorService.deleteById(id);
		
		return ResponseEntity.noContent().build();

	}
}
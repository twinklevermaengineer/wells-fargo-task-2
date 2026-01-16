package com.wellsfargo.counselor.controller;

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
import com.wellsfargo.counselor.model.request.AdvisorResponse;
import com.wellsfargo.counselor.rest.ResourceNotFoundException;
import com.wellsfargo.counselor.service.AdvisorService;

@RestController
@RequestMapping("/api")
public class AdvisorController {
	
private static final Logger logger = LoggerFactory.getLogger(AdvisorController.class);
	
	@Autowired
	private AdvisorService advisorService;
	
	@GetMapping("/allAdvisors")
		public ResponseEntity<List<AdvisorResponse>> findAllAdvisor() {
		logger.info("Fetching all the advisors");
		List<AdvisorResponse> advisor = advisorService.findAll();
		if(advisor.isEmpty()) {
			logger.error("Advisors not found");
			return ResponseEntity.noContent().build();
		}
		logger.info("List of advisors found ", advisor);
		return ResponseEntity.ok(advisor);	
	}
	
	@GetMapping("/advisors")
	public ResponseEntity<AdvisorResponse> getAdvisor(@RequestParam String address) {
		logger.info("Fetching advisor with address {} " , address);
		AdvisorResponse advisor = advisorService.findByAddress(address);
		if(advisor != null) {
			logger.info("Advisor found with address {}", address);
			return ResponseEntity.ok(advisor);
		}
			logger.error("Advisor not found with address {} " , address);
		throw new ResourceNotFoundException("Advisor not found with address " + address);
		}
	
	@GetMapping("/advisors/{id}")
	public ResponseEntity<AdvisorResponse> getAdvisorById(@PathVariable Long id) {
	logger.info("Fetching advisor b id: {} ", id);	
	AdvisorResponse advisor =	advisorService.findById(id);
		if(advisor != null) {
			logger.info("Found advisor with id {}" , advisor);
			return ResponseEntity.ok(advisor);
		}else {
			logger.error("Advisor not found with id {} ", id);
		throw new ResourceNotFoundException("Advisor not found " + id);
		}
	}
	
	@PostMapping("/add")
	public ResponseEntity<String> addAdvisor(@RequestBody AdvisorRequest advisor) {
		
		logger.info("Received new advisor request {}", advisor.toString());
		Long advisorId = advisorService.save(advisor);
		
		logger.info("Advisor created successfully, advisor id {}", advisorId);
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body("Advisor created successfully, id " + advisorId);
	}
	
	@DeleteMapping("/remove/{id}")
	public ResponseEntity<String> deleteAdvisor(@PathVariable Long id) {
		logger.warn("Deleting advisor with id {} ", id);
		advisorService.deleteById(id);
		return ResponseEntity.ok("Advisor deleted successfully");
	
	}
	
	@PutMapping("/advisors/{id}")
	public ResponseEntity<String> updateAdvisor(
			@PathVariable Long id, 
			@RequestBody AdvisorRequest advisorRequest){
		advisorService.updateAdvisor(id, advisorRequest);
		
		return ResponseEntity.ok("Advisor updated successfully for id " + id);	
	}

}
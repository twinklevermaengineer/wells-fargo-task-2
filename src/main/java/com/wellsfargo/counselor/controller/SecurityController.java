package com.wellsfargo.counselor.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wellsfargo.counselor.model.request.SecurityRequest;
import com.wellsfargo.counselor.model.response.SecurityResponse;
import com.wellsfargo.counselor.service.SecurityService;

@RestController
@RequestMapping("/api/v1/securities")
public class SecurityController {
	
	private static final Logger logger = 
				LoggerFactory.getLogger(SecurityController.class);
	
	private final SecurityService securityService;
	
	public SecurityController(SecurityService securityService) {
		this.securityService = securityService;
	}

	@GetMapping("")
	public ResponseEntity<?> getSecurities(@RequestParam(required = false) String name){
		 logger.info("getSecurities called with name= {} ", name);
		if(name != null && !name.isBlank()) {
		
		SecurityResponse response = securityService.findByName(name);
		logger.info("Fetching security: {} ", response);
		return ResponseEntity.ok(response);
		
	}
		List<SecurityResponse> response = securityService.findAll();
		logger.info("Fetching all securities, count: {}", response.size());
		return ResponseEntity.ok(response);
		
	}
	
	@GetMapping("/id/{id}")
	public ResponseEntity<SecurityResponse> getById(@PathVariable Long id){
		logger.info("Fetching security, id: {} ", id);
		SecurityResponse response = securityService.findById(id);

		return ResponseEntity.ok(response);
	}

	@PostMapping("/")
	public ResponseEntity<SecurityResponse> addSecurity(@RequestBody SecurityRequest request){
		logger.info("Received new security request {} ", request);
		SecurityResponse securityResponse = securityService.save(request); 
		
		logger.info("Security created successfully, security id {} ", securityResponse.getSecurityId());
		return ResponseEntity.status(HttpStatus.CREATED).body(securityResponse);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<SecurityResponse> updateSecurity(
							@PathVariable Long id, 
							@RequestBody SecurityRequest securityRequest){
		logger.info("Received security update request for security id {} ", id);
		SecurityResponse response = securityService.updateSecurity(id, securityRequest);
		
		logger.info("Security updated successfully, security id {} ", id);
		return ResponseEntity.ok(response);
	}

}
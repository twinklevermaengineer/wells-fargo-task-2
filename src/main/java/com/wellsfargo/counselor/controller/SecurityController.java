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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/v1/securities")
public class SecurityController {
	
	private static final Logger logger = 
				LoggerFactory.getLogger(SecurityController.class);
	
	private final SecurityService securityService;
	
	public SecurityController(SecurityService securityService) {
		this.securityService = securityService;
	}

	@Operation(summary = "Get securities by name or all securities if name not provided",
			   description = "Return a list of securities. Optionally filter by name")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
						 description = "Securities retrieved successfully",
						 content = @Content(mediaType = "application/json",
						 					schema = @Schema(implementation = SecurityResponse[].class))),
			@ApiResponse(responseCode = "404", description = "Security not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error")
  })
	@GetMapping
	public ResponseEntity<List<SecurityResponse>> getSecurities(
		    @Parameter(description = "Optional name to filter securities") 
			@RequestParam(required = false) String name){
		 logger.info("getSecurities called with name= {} ", name);
		if(name != null && !name.isBlank()) {
		
		SecurityResponse response = securityService.findByName(name);
		logger.info("Fetching security: {} ", response);
		return ResponseEntity.ok(List.of(response));
		
	}
		List<SecurityResponse> response = securityService.findAll();
		logger.info("Fetching all securities, count: {}", response.size());
		return ResponseEntity.ok(response);	
	}
	@Operation(summary = "Get security by Id",
			   description = "Retrieve security with specific Id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
						 description = "Security found with Id",
						 content = @Content(mediaType = "application/json",
						 					schema = @Schema(implementation = SecurityResponse.class))),
			@ApiResponse(responseCode = "404", description = "Security not found"),
			@ApiResponse(responseCode = "500",description = "Internal server error")
  })
	@GetMapping("/{id}")
	public ResponseEntity<SecurityResponse> getById(
			@Parameter(description = "ID of the security to fetch") 
			@PathVariable Long id){
		
		logger.info("Fetching security, id: {} ", id);
		SecurityResponse response = securityService.findById(id);

		return ResponseEntity.ok(response);
  }	

	@Operation(summary = "Add security",
			   description = "Create new security")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201",
						 description = "Security created successfully",
						 content = @Content(mediaType = "application/json",
						 					schema = @Schema(implementation = SecurityResponse.class))),
			 @ApiResponse(responseCode = "400", description = "Invalid request payload"),
			 @ApiResponse(responseCode = "500", description = "Internal server error")
  })
	@PostMapping
	public ResponseEntity<SecurityResponse> addSecurity(@Valid
			@Parameter(description = "Security request payload")
			@RequestBody SecurityRequest request){
		logger.info("Received new security request {} ", request);
		SecurityResponse securityResponse = securityService.save(request); 
		
		logger.info("Security created successfully, security id {} ", securityResponse.getSecurityId());
		return ResponseEntity.status(HttpStatus.CREATED).body(securityResponse);
}
	@Operation(summary = "Update security",
			   description = "Update security details by Id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
						 description = "Security updated successfully",
						 content = @Content(mediaType = "application/json",
								 			schema = @Schema(implementation = SecurityResponse.class))),
    	    @ApiResponse(responseCode = "400", description = "Invalid request payload"),
		    @ApiResponse(responseCode = "404", description = "Security not found"),
		    @ApiResponse(responseCode = "500", description = "Internal server error")
  })
	@PutMapping("/{id}")
	public ResponseEntity<SecurityResponse> updateSecurity(
			 				@Parameter(description = "ID of the security to update") 
							@PathVariable Long id, 
							@Valid @Parameter(description = "Updated security payload") 
							@RequestBody SecurityRequest securityRequest){
		logger.info("Received security update request for security id {} ", id);
		SecurityResponse response = securityService.updateSecurity(id, securityRequest);
		
		logger.info("Security updated successfully, security id {} ", id);
		return ResponseEntity.ok(response);
	}

}
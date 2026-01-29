package com.wellsfargo.counselor.controller;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/advisors")
@Tag(name = "Advisor Controller",
	 description = "Operations related to advisors")
public class AdvisorController {
	
private static final Logger logger = LoggerFactory.getLogger(AdvisorController.class);

	private AdvisorService advisorService;

	public AdvisorController(AdvisorService advisorService) {
		this.advisorService = advisorService;		
	}

	@Operation(summary = "Get all advisors or filter by address",
			   description = "Returns a list of advisors. Optionally, filter advisors by address.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200",
					 description = "List of advisors retrieved successfully",
					 content = @Content(mediaType = "application/json",
					 					schema = @Schema(implementation = AdvisorResponse.class))),
		@ApiResponse(responseCode = "400",
					 description = "No Advisor found",
					 content = @Content),
		@ApiResponse(responseCode = "404",
					 description = "Advisor not found by address",
					 content = @Content)
  })
	@GetMapping
	public ResponseEntity<List<AdvisorResponse>> getAdvisor(
			@RequestParam(required = false)
			@io.swagger.v3.oas.annotations.Parameter(description = "Filter advisors by address") 
			String address) {

			if(address !=null) {
				logger.info("Fetching advisor, address: {} " , address);
			
			AdvisorResponse advisor = advisorService.findByAddress(address);
				
				if(advisor != null) {
					logger.info("Advisor found, address: {}", address);
				
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
				logger.error("Advisors not found, advisors: {} ");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
			logger.info("List of advisors found, advisors: {} ", advisor);
			return ResponseEntity.ok(advisor);	
		}
	}

	@Operation(summary = "Get advisor by id",
			   description = "Retrieve a specific advisor by their id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
						 description = "Advisor retrieved successfully",
						 content = @Content(mediaType = "application/json",
						 					schema = @Schema(implementation = AdvisorResponse.class))),
			@ApiResponse(responseCode = "404",
						 description = "Advisor not found",
						 content = @Content )
  })
	@GetMapping("/{id}")
	public ResponseEntity<AdvisorResponse> getAdvisorById(@PathVariable Long id) {
	logger.info("Fetching advisor, id: {} ", id);	
	AdvisorResponse advisor =	advisorService.findById(id);
		if(advisor != null) {
			logger.info("Found advisor, id: {} " , advisor);
			return ResponseEntity.ok(advisor);
		}else {
			logger.error("Advisor not found, id: {} ", id);
		throw new ResourceNotFoundException("Advisor not found, id: " + id);
		}
	}
	
	@Operation(summary = "Get advisor by client Id",
			   description = "Retrieve advisor associated with specific client")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
						 description = "Advisor associated with client retrieved successfully",
						 content = @Content(mediaType = "application/json",
						 					schema = @Schema(implementation = AdvisorResponse.class))),
			@ApiResponse(responseCode = "204",
						 description = "No Advisor associated with the Client Id",
						 content = @Content)
  })
	@GetMapping("/{clientId}/advisor")
	public ResponseEntity<AdvisorResponse> getAdvisorByClientId(@PathVariable Long clientId){
		logger.info("Fetching advisor by client id, clientId: {} ", clientId);
		AdvisorResponse response = advisorService.findAdvisorByClientId(clientId);
		
			if(response != null) {
				logger.info("Advisor found associated to client id: {} ", clientId);
				return ResponseEntity.ok(response);
			}
		logger.error("Advisor not found associated to clientId: {} ", clientId);
	    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	@Operation(summary = "Update advisor",
			   description = "Update advisor details by Id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
						 description = "Advisor updated successfully",
						 content = @Content)	
  })
	@PutMapping("/{id}")
	public ResponseEntity<String> updateAdvisor(
			@PathVariable Long id, 
			@RequestBody AdvisorRequest advisorRequest){
		logger.info("Fetching advisor id for update request {} ", id);
		advisorService.updateAdvisor(id, advisorRequest);
		
		return ResponseEntity.ok("Advisor updated successfully for id: " + id);	
	}
	
	@Operation(summary = "Add new advisor",
			   description = "Create new advisor")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201",
				  description = "Advisor created successfully",
				  content = @Content(mediaType = "application/json",
				  					 schema = @Schema(implementation = AdvisorResponse.class)))
  })
	@PostMapping
	public ResponseEntity<AdvisorResponse> addAdvisor(@RequestBody AdvisorRequest advisor) {
		
		logger.info("Received new advisor request {}", advisor.toString());
		AdvisorResponse advisorResponse = advisorService.save(advisor);
		
		logger.info("Advisor created successfully, advisor id {}", advisorResponse.getAdvisorId());
		return ResponseEntity.status(HttpStatus.CREATED).body(advisorResponse);
	}
	
	@Operation(summary = "Delete advisor",
			   description = "Delete an advisor by Id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204",
						 description = "Advisor deleted successfully",
						 content = @Content),
			@ApiResponse(responseCode = "404",
						 description = "Advisor not found",
						 content = @Content)
  })	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteAdvisor(@PathVariable Long id) {
		
		logger.warn("Deleting advisor, id: {} ", id);
		
		advisorService.deleteById(id);
		
		return ResponseEntity.noContent().build();

	}
}
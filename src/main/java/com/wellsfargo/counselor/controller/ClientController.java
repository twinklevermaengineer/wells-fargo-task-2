package com.wellsfargo.counselor.controller;

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
import com.wellsfargo.counselor.entity.Client;
import com.wellsfargo.counselor.model.request.ClientRequest;
import com.wellsfargo.counselor.model.response.ClientResponse;
import com.wellsfargo.counselor.rest.ResourceNotFoundException;
import com.wellsfargo.counselor.service.ClientService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {
	private static final Logger logger = LoggerFactory.getLogger(ClientController.class);
	
	private ClientService clientService;
	
	public ClientController(ClientService clientService) {
		this.clientService = clientService;
	}
	
	@Operation(summary = "Get client by address or all clients if address not provided",
			   description = "Return a list of clients. Optionally filter by address")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
						 description = "Client retrieved succeffully",
						 content = @Content(mediaType ="application/json",
								 	schema = @Schema(implementation = ClientResponse[].class))),
			@ApiResponse(responseCode = "404",
	 			  description = "Client not found with given address",
	 			  content = @Content)
  })
	@GetMapping
	public ResponseEntity<List<ClientResponse>> clientByAddress(@RequestParam(required = false) String address){
		if(address != null) {
			logger.info("Fetching client by address {} ", address);
	
		List<ClientResponse> client = clientService.findByAddress(address); 
		if(client != null) {
			logger.info("Client found with address: " + address);
	
			return ResponseEntity.ok(client);
		}

		logger.error("Client not found with address: " + address);
		throw new ResourceNotFoundException("Client not found with address: " + address);
	}
		else {
			logger.info("Fetching all the clients");
			List<ClientResponse> allClients = clientService.findAll();
				if(allClients.isEmpty()) {
					logger.error("Clients not found");
					return ResponseEntity.ok(Collections.emptyList());
				}
				logger.info("List of clients found " + allClients); 
				return ResponseEntity.ok(allClients);
		}
	}

	@Operation(summary = "Get client by Id",
			   description = "Retrieve a specific client by their Id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
						 description ="Client found with Id",
						 content = @Content(mediaType = "application/json",
						 					schema = @Schema(implementation = ClientResponse.class))),
			@ApiResponse(responseCode = "404",
						 description = "Client not found with Id",
						 content = @Content)
  })
	@GetMapping("/{id}")
	public ResponseEntity<ClientResponse> clientById(@PathVariable Long id){
		ClientResponse client = clientService.findById(id);
		if(client != null){
			logger.info("Client found with id {} ", id);
			return ResponseEntity.ok(client);
		}
			logger.error("Client not found with id {} ", id);
			throw new ResourceNotFoundException("Client not found with id: " + id);	
	}
	
	@Operation(summary = "Get client by advisor Id",
			   description = "Retrieve client associated with advisor Id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
						 description ="Client associated with advisor retrieved successfully",
						 content = @Content(mediaType = "application/json",
								 			schema = @Schema(implementation = ClientResponse.class))),
			@ApiResponse(responseCode = "404",
						 description = "Client not found associated with advisor Id",
						 content = @Content)
  })
	@GetMapping("/advisor-id/{advisorId}")
	public ResponseEntity<List<ClientResponse>> getClientByAdvisorId(@PathVariable Long advisorId){
		
		List<ClientResponse> client = clientService.findClientByAdvisorId(advisorId);
		
		if(client != null && !client.isEmpty()) {
			return ResponseEntity.ok(client);
		}
		return ResponseEntity.ok(Collections.emptyList());
	}
	
	@Operation(summary = "Add client",
			   description = "Create new client")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201",
						 description = "Client created successfully",
						 content = @Content(mediaType = "application/json",
								 			schema = @Schema(implementation = ClientResponse.class)))			
  })
	@PostMapping
	public ResponseEntity<ClientResponse> addClient(@Valid @RequestBody ClientRequest clientRequest){
		logger.info("Received new client request");
		
		ClientResponse response = clientService.save(clientRequest);
		logger.info("Client created successfully,client id: {} ", response.getClientId());
		return ResponseEntity.status(HttpStatus.CREATED).body(response);

	}
	
	@Operation(summary = "Update client",
			   description = "Update client details by Id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
						 description = "Client updated successfully",
						 content = @Content(mediaType = "application/json",
						 					schema = @Schema(implementation = ClientResponse.class)))
  })
	@PutMapping("/{id}")
	public ResponseEntity<ClientResponse> updateClient(
			@PathVariable Long id,
			@Valid @RequestBody ClientRequest request){
		
		Client response = clientService.updateClient(id, request);
		ClientResponse clientUpdate = new ClientResponse();
		clientUpdate.setClientId(response.getClientId());
		clientUpdate.setFirstName(response.getFirstName());
		clientUpdate.setLastName(response.getLastName());
		clientUpdate.setAddress(response.getAddress());
		clientUpdate.setPhone(response.getPhone());
		clientUpdate.setEmail(response.getEmail());

				return ResponseEntity.ok(clientUpdate);
	}
	
}
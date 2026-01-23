package com.wellsfargo.counselor.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.wellsfargo.counselor.model.request.ClientResponse;
import com.wellsfargo.counselor.rest.ResourceNotFoundException;
import com.wellsfargo.counselor.service.ClientService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {
	private static final Logger logger = LoggerFactory.getLogger(ClientController.class);
	
	private ClientService clientService;
	
	public ClientController(ClientService clientService) {
		this.clientService = clientService;
	}
	
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
					return ResponseEntity.noContent().build();
				}
				logger.info("List of clients found " + allClients); 
				return ResponseEntity.ok(allClients);
		}
	}
	
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
	
	@GetMapping("/{advisorId}/clients")
	public ResponseEntity<List<ClientResponse>> getClientByAdvisorId(@PathVariable Long advisorId){
		
		List<ClientResponse> client = clientService.findClientByAdvisorId(advisorId);
		
		if(client != null) {
			return ResponseEntity.ok(client);
		}
		return ResponseEntity.noContent().build();
		
	}
	
	@PostMapping
	public ResponseEntity<ClientResponse> addClient(@RequestBody ClientRequest clientRequest){
		logger.info("Recieved new client request {} ", clientRequest.toString());
		
		ClientResponse response = clientService.save(clientRequest);
		logger.info("Client created successfully,client id: {} ", response.getClientId());
		return ResponseEntity.ok(response);
		
	}

	@PutMapping("/{id}")
	public ResponseEntity<ClientResponse> updateClient(
			@PathVariable Long id,
			@RequestBody ClientRequest request){
		
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
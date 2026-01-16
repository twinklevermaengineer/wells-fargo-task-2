package com.wellsfargo.counselor.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.wellsfargo.counselor.model.request.ClientResponse;
import com.wellsfargo.counselor.rest.ResourceNotFoundException;
import com.wellsfargo.counselor.service.ClientService;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ClientController {
	private static final Logger logger = LoggerFactory.getLogger(ClientController.class);
	
	private ClientService clientService;
	
	public ClientController(ClientService clientService) {
		this.clientService = clientService;
	}
	
	@GetMapping("/client")
	public ResponseEntity<ClientResponse> clientById(@RequestParam String address){
		ClientResponse client = clientService.findByAddress(address); 
		if(client != null) {
			logger.info("Client found with address: " + address);
			return ResponseEntity.ok(client);
		}
		logger.error("Client not found with address: " + address);
		throw new ResourceNotFoundException("Client not found with address: " + address);
	}
	
	@GetMapping("/client/{id}")
	public ResponseEntity<ClientResponse> clientsById(@PathVariable Long id){
		ClientResponse client = clientService.findById(id);
		if(client != null){
			logger.info("Client found with id {} ", id);
			return ResponseEntity.ok(client);
		}
			logger.error("Client not found with id {} ", id);
			throw new ResourceNotFoundException("Client not found with id: " + id);	
	}
	
	@GetMapping("/allClients")
	public ResponseEntity<List<ClientResponse>> findAllClients(){
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




































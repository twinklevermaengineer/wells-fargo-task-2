package com.wellsfargo.counselor.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wellsfargo.counselor.entity.Client;
import com.wellsfargo.counselor.model.request.ClientRequest;
import com.wellsfargo.counselor.model.response.ClientResponse;
import com.wellsfargo.counselor.repository.ClientRepository;
import com.wellsfargo.counselor.rest.InvalidRequestException;
import com.wellsfargo.counselor.rest.ResourceCreationException;
import com.wellsfargo.counselor.rest.ResourceNotFoundException;
import com.wellsfargo.counselor.rest.ServerException;
import com.wellsfargo.counselor.utils.ClientRequestValidator;

/**
 * Implementation of {@link ClientService} for managing Client entities.
 * Provides operations to create, read, update and delete clients,
 * as well as fetching clients by specific criteria such as address or advisor ID
 * 
 * @author Twinkle Verma
 * @version 1.0
*/
@Service
public class ClientServiceImpl implements ClientService{
	private static final Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);

	private ClientRepository clientRepository;
	private ClientRequestValidator validator;
	
	/**
	 * Constructs an ClientServiceImpl with the specified repository and validator.
	 * 
	 * @param clientRepository the repository used to access client data
	 * @param validator the validator used to validate client requests
	*/
	public ClientServiceImpl(ClientRepository clientRepository, ClientRequestValidator validator) {
		this.clientRepository = clientRepository;
		this.validator = validator;
	}

	/**
	 * Find a client by its unique ID
	 * 
	 * @param id the ID of the client to retrieve
	 * @return the {@link ClientResponse} representing the client
	*/
	
	@Override
	public ClientResponse findById(Long id) {
		
		logger.info("Find client with id: " + id);
		
		Optional<Client> clientFromDb = clientRepository.findById(id);

		if(clientFromDb.isPresent()) {
			logger.info("Client found with id {} ", id);
			
			Client client = clientFromDb.get();
	
			ClientResponse clientResponse = new ClientResponse(
										id, client.getFirstName(),
										client.getLastName(),
										client.getAddress(),
										client.getPhone(),
										client.getEmail());
							return clientResponse;
			}
    		return null;
     }
	
	/**
	 * Find the client associated with specific address
	 * 
	 *  @param address the address of the client
	 *  @return a list of {@link ClientResponse} objects representing all client associated with the address
	 *  @throws ResourceNotFoundException if client not found with similar address
	 * 
	*/
	@Override
	public List<ClientResponse> findByAddress(String address) {
		logger.info("Client found with address: " + address);
		
		List<ClientResponse> clientList = new ArrayList<>();
		try {
		List<Client> client = clientRepository.findByAddress(address);
		
			for(Client clientEntity: client) {
			clientList.add(new ClientResponse(
					clientEntity.getClientId(),
					clientEntity.getFirstName(),
					clientEntity.getLastName(),
					clientEntity.getAddress(),
					clientEntity.getPhone(),
					clientEntity.getEmail()));
				}
		}catch(Exception e) {
			throw new ResourceNotFoundException("Client not found with similar address " + address);
		}
		return clientList;
}
	
	/**
	 * Retrieve all the clients from repository.
	 * 
	 * @return a list of {@link ClientResponse} objects representing all clients
	 * @throws ServerException if retrieving the client list data fails
	*/
	@Override
	public List<ClientResponse> findAll() {
		
		List<ClientResponse> clientsList = new ArrayList<>();
		try {
		List<Client> clientFromDb = clientRepository.findAll();		
		
		for(Client clients: clientFromDb) {
			clientsList.add(new ClientResponse(
					clients.getClientId(),
					clients.getFirstName(),clients.getLastName(),
					clients.getAddress(),clients.getPhone(),clients.getEmail()));
		}
		} catch (Exception excp) {
			throw new ServerException("Exception occured while retrieving client list, error:" + excp.getMessage());
		}
		return clientsList;	
	}
	
	/**
	 * Save a new client to the repository after validating the request.
	 * 
	 * @param clientRequest the client data to save
	 * @return the {@link ClientResponse} representing the saved client
	 * @throws InvalidRequestException if validation fails
	 * @throws ResourceCreationException if saving the client fails
	*/
	@Override
	@Transactional
	public ClientResponse save(ClientRequest clientRequest) {
		
		List<String> errors = new ArrayList<>();
		
		validator.validateClientRequest(clientRequest, null, errors, "create");
		
		if(!errors.isEmpty()) {
			errors.add("Invalid client request");
			throw new InvalidRequestException("Invalid client request");
		}
		try {
			
		Client client = new Client(clientRequest.getFirstName(),
				clientRequest.getLastName(),clientRequest.getAddress(),
				clientRequest.getPhone(), clientRequest.getEmail());
		
		Client saveClient = clientRepository.save(client);
		
		if(saveClient != null) {
		
			ClientResponse response = new ClientResponse(
				saveClient.getClientId(),
				saveClient.getFirstName(),
				saveClient.getLastName(),
				saveClient.getAddress(),
				saveClient.getPhone(),
				saveClient.getEmail());
		return response;
	}
		} catch(Exception e){
			throw new ResourceCreationException("Client creation failed, request: " +
						clientRequest.toString() + "exception: " + e.getMessage());
	
	}
		return null;
}
	
	/**
	 * Update an existing client with the given ID using the provided request data.
	 * 
	 * @param id the ID of the client to update
	 * @param clientRequest the new client data
	 * @throws InvalidRequestException if validation fails
	 * @throws ResourceNotFoundException if no client exists with the given ID
	*/
	@Override
	@Transactional
	public Client updateClient(Long id, ClientRequest clientRequest) {
		
		List<String> errors = new ArrayList<>();
		
		validator.validateClientRequest(clientRequest, id, errors, "update");
		
		if(!errors.isEmpty()) {
			logger.error("Invalid client request");
			throw new InvalidRequestException(
					"Invalid client input" + clientRequest.toString());
		}
		
		Client client = clientRepository.findById(id)
				.orElseThrow(() -> 
						new ResourceNotFoundException("Client not found with id " + id));
		
		client.setFirstName(clientRequest.getFirstName());
		client.setLastName(clientRequest.getLastName());
		client.setAddress(clientRequest.getAddress());
		client.setPhone(clientRequest.getPhone());
		client.setEmail(clientRequest.getEmail());
		
		return clientRepository.save(client);
	}
	
	/**
	 * Find the client associated with a specific advisor ID.
	 * 
	 * @param advisorId the ID of the advisor
	 * @return the {@link ClientResponse} representing the list of the client
	 * @throws ResourceNotFoundException if no client is found for the given advisor ID
	*/
	@Override
	public List<ClientResponse> findClientByAdvisorId(Long advisorId) {
		
		List<ClientResponse> clientResponse = new ArrayList<>();
		try {
				List<Client> client = clientRepository.findByAdvisorAdvisorId(advisorId);
				for(Client clientEntity: client) {
					clientResponse.add(new ClientResponse(
							clientEntity.getClientId(),
							clientEntity.getFirstName(),
							clientEntity.getLastName(),
							clientEntity.getAddress(),
							clientEntity.getPhone(),
							clientEntity.getEmail()
					));
			}
		}catch(Exception e) {
			throw new ResourceNotFoundException("Client not found with advisorId: " + advisorId + " " + e.getMessage());
		}
		return clientResponse;
		
		}
	}
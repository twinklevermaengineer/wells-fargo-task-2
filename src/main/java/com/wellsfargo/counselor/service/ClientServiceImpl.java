package com.wellsfargo.counselor.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.wellsfargo.counselor.entity.Client;
import com.wellsfargo.counselor.model.request.ClientResponse;
import com.wellsfargo.counselor.repository.ClientRepository;

@Service
public class ClientServiceImpl implements ClientService{
	private static final Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);

	private ClientRepository clientRepository;
	
	public ClientServiceImpl(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}

	@Override
	public ClientResponse findById(Long id) {
		logger.info("Client find with id: " + id);
		Optional<Client> clientFromDb = clientRepository.findById(id);
		
		if(clientFromDb.isPresent()) {
			Client client = clientFromDb.get();
			ClientResponse clientResponse = new ClientResponse(
					id, client.getFirstName(), client.getLastName(),client.getAddress(),
					client.getPhone(),client.getEmail());
				return clientResponse;
		}
    		return null;
     }

	@Override
	public ClientResponse findByAddress(String address) {
		logger.info("Client found with address: " + address);
		Optional<Client> clientFromDB = clientRepository.findByAddress(address);
		if(clientFromDB.isPresent()) {
			Client client = clientFromDB.get();
			ClientResponse clientResponse = new ClientResponse(
					client.getClientId(), client.getFirstName(), client.getLastName(), client.getAddress(),
					client.getEmail(), client.getPhone());
			return clientResponse;
		}
		return null;
	}

	@Override
	public List<ClientResponse> findAll() {
		List<Client> clientFromDb = clientRepository.findAll();
		
		List<ClientResponse> clientsList = new ArrayList<>();
		
		for(Client clients: clientFromDb) {
			clientsList.add(new ClientResponse(clients.getClientId(),
					clients.getFirstName(),clients.getLastName(),
					clients.getAddress(),clients.getPhone(),clients.getEmail()));
		}
		return clientsList;	
	}
	
}




















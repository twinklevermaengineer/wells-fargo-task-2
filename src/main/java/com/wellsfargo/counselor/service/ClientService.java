package com.wellsfargo.counselor.service;

import java.util.List;

import com.wellsfargo.counselor.entity.Client;
import com.wellsfargo.counselor.model.request.ClientRequest;
import com.wellsfargo.counselor.model.request.ClientResponse;

public interface ClientService {

	ClientResponse findById(Long id);

	List<ClientResponse> findByAddress(String address);
	
	List<ClientResponse> findAll();
	
	ClientResponse save(ClientRequest clientRequest);
	
	Client updateClient(Long id, ClientRequest clientRequest);
}

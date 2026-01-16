package com.wellsfargo.counselor.service;

import java.util.List;

import com.wellsfargo.counselor.model.request.ClientResponse;

public interface ClientService {

	ClientResponse findById(Long id);

	ClientResponse findByAddress(String address);
	
	List<ClientResponse> findAll();
}

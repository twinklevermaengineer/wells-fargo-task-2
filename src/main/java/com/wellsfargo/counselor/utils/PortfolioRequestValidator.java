package com.wellsfargo.counselor.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.wellsfargo.counselor.model.request.ClientRequest;
import com.wellsfargo.counselor.model.request.PortfolioRequest;
import com.wellsfargo.counselor.repository.ClientRepository;
import com.wellsfargo.counselor.rest.InvalidRequestException;

@Component
public class PortfolioRequestValidator {
	
	private static final Logger logger =
			LoggerFactory.getLogger(PortfolioRequestValidator.class);

	private final ClientRepository clientRepository;
	
	public PortfolioRequestValidator(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}

	public void validatePortfolioRequest(PortfolioRequest portfolioRequest) {
		
		if(portfolioRequest == null) {
			String message = "Empty portfolio request";
			logger.error(message);
			throw new InvalidRequestException(message);
		}

		ClientRequest client = portfolioRequest.getClient();
		if (client == null) {
			String message = "Empty client found";
			logger.error(message);
			throw new InvalidRequestException(message);
		}

		Long clientId = client.getClientId();
		if(clientId == null) {
			String message = "Invalid client id";
			logger.error("{} - {}", message, client);
			throw new InvalidRequestException(message);
		}
		 if(!clientRepository.existsById(clientId)) {
			 String message = "Client not found with id";
			 logger.error("{} - {}", message, clientId);
	         throw new InvalidRequestException(message + " : " + clientId);

		 }
	}
}
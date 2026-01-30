package com.wellsfargo.counselor.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.wellsfargo.counselor.model.request.ClientRequest;
import com.wellsfargo.counselor.model.request.PortfolioRequest;
import com.wellsfargo.counselor.repository.ClientRepository;
import com.wellsfargo.counselor.rest.InvalidRequestException;

/**
 * Utility component responsible for validating {@link PortfolioRequest} objects.
 * <p>
 * This class ensures that a portfolio request:
 * <ul>
 *  <li>Is not null.</li>
 *  <li>Contains a valid client.</li>
 *  <li>References an existing client in the database.</li>
 * </ul>
 * If any validation fails, an {@link InvalidRequestException} is thrown and an
 * error is logged.
 * </p>
 * <p>
 * This validator relies on {@link ClientRepository} to verify the existence of clients.
 * </p>
 * 
 * @see PortfolioRequest
 * @see ClientRequest
 * @see ClientRepository
 * @see InvalidRequestException
*/
@Component
public class PortfolioRequestValidator {
	
	private static final Logger logger =
			LoggerFactory.getLogger(PortfolioRequestValidator.class);

	private final ClientRepository clientRepository;
	
	/**
	 * Constructs a new {@code PortfolioRequestValidator} with the given {@link ClientRepository}.
	 * 
	 * @param clientRepository the repository used to verify client existence
	*/	
	public PortfolioRequestValidator(ClientRepository clientRepository) {
		this.clientRepository = clientRepository;
	}

	/**
	 * Validates the provided {@link PortfolioRequest}.
	 * <p>
	 * The validation performs the following checks:
	 * 
	 * <ul>
	 *  <li>The {@code portfolioRequest} is not null.</li>
	 *  <li>The {@code client} field inside the {@code portfolioRequest} is not null.</li>
	 *  <li>The {@code clientId} is present and corresponds to an existing client
	 *  in the repository.</li>
	 * </ul>
	 * <p>
	 * If any of the above validations fail, an {@link InvalidRequestException} is thrown and an appropriate
	 * error message is logged.
	 * </p>
	 *
	 * @param portfolioRequest the portfolio request to validate
	 * @throws InvalidRequestException if the portfolio request or its client is invalid, or if the client
	 * does not exist in the database
	*/	
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
package com.wellsfargo.counselor.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.wellsfargo.counselor.entity.Client;
import com.wellsfargo.counselor.entity.Portfolio;
import com.wellsfargo.counselor.entity.Security;
import com.wellsfargo.counselor.model.request.ClientResponse;
import com.wellsfargo.counselor.model.request.PortfolioRequest;
import com.wellsfargo.counselor.model.request.PortfolioResponse;
import com.wellsfargo.counselor.model.request.SecurityRequest;
import com.wellsfargo.counselor.model.request.SecurityResponse;
import com.wellsfargo.counselor.repository.ClientRepository;
import com.wellsfargo.counselor.repository.PortfolioRepository;
import com.wellsfargo.counselor.rest.ResourceNotFoundException;
import com.wellsfargo.counselor.utils.PortfolioRequestValidator;

@Service
public class PortfolioServiceImpl implements PortfolioService {

	private static final Logger logger = LoggerFactory.getLogger(PortfolioServiceImpl.class);
	
    private final PortfolioRepository portfolioRepository;
    private final ClientRepository clientRepository;
    private PortfolioRequestValidator validator;

    public PortfolioServiceImpl(
    			PortfolioRepository portfolioRepository,
    			ClientRepository clientRepository ,
    			PortfolioRequestValidator validator) {
        
    		this.portfolioRepository = portfolioRepository;
    		this.clientRepository = clientRepository;
    		this.validator = validator;   
    }

    @Override
    public PortfolioResponse findById(Long id) {
    	
    	logger.info("Finding portfolio, id: {} ", id);
       
    	Portfolio portfolioEntity = portfolioRepository.findById(id)
                .orElseThrow(() -> {
                	 logger.warn("Portfolio not found, id: {}", id);
                     return new ResourceNotFoundException("Portfolio not found, id: " + id);
                });

        Client clientEntity = portfolioEntity.getClient();
        
        logger.info("Mapping client entity object to client response object, id: {} ", id);
        
        ClientResponse clientResponse = new ClientResponse(
                clientEntity.getClientId(),
                clientEntity.getFirstName(),
                clientEntity.getLastName(),
                clientEntity.getEmail(),clientEntity.getPhone(), clientEntity.getAddress());
        
       List<SecurityResponse> securityResponseList = new ArrayList<>();
       
       logger.info("Fetching security entity list");

       List<Security> securityEntityList = portfolioEntity.getSecurities();
       logger.info("Found {} securities for portfolio id: {}", securityEntityList.size(), id);

       SecurityResponse securityResponse;

       for (Security securityEntity: securityEntityList) {
    	   logger.info("Mapping security id: {}", securityEntity.getSecurityId());
    	   
    	   securityResponse = new SecurityResponse(
    			    securityEntity.getSecurityId(),
           			securityEntity.getName(),
           			securityEntity.getCategory(),
           			securityEntity.getPurchasePrice(),
           			securityEntity.getPurchaseDate(),
           			securityEntity.getQuantity()
    	   );
    	   
    	   securityResponseList.add(securityResponse);
       }
       logger.info("Successfully built portfolio response for id: {}", id);

        return new PortfolioResponse(
                portfolioEntity.getPortfolioId(),
                portfolioEntity.getCreationDate(),
                securityResponseList,
                clientResponse);
    }

	@Override
	public List<PortfolioResponse> findAll() {
		
		logger.info("Fetching all portfolios");
		
		List<Portfolio> portfolioDb = portfolioRepository.findAll();
		
		List<PortfolioResponse> portfolioResponse = new ArrayList<>();
		
			for(Portfolio portfolioEntity: portfolioDb) {
				
		Client client = portfolioEntity.getClient();
		
		ClientResponse clientResponse = new ClientResponse(
				client.getClientId(), client.getFirstName(),
				client.getLastName(), client.getEmail(),
				client.getPhone(), client.getAddress()
				);
		List<SecurityResponse> securityResponseList = new ArrayList<>();
	    List<Security> securityEntityList = portfolioEntity.getSecurities();
	    
	    SecurityResponse securityResponse;
	     logger.info("Mapping security entity object to security response object");
	    
	     for (Security securityEntity: securityEntityList) {
	    	   securityResponse = new SecurityResponse(
	    			    securityEntity.getSecurityId(),
	           			securityEntity.getName(),
	           			securityEntity.getCategory(),
	           			securityEntity.getPurchasePrice(),
	           			securityEntity.getPurchaseDate(),
	           			securityEntity.getQuantity()
	    	   );
	    	   securityResponseList.add(securityResponse);
	       }
		
			PortfolioResponse response = new PortfolioResponse(
				portfolioEntity.getPortfolioId(),
				portfolioEntity.getCreationDate(),
				securityResponseList, clientResponse
			);
			portfolioResponse.add(response);
		}
			return portfolioResponse;
	}

	@Override
	public PortfolioResponse save(PortfolioRequest portfolioRequest) {

		validator.validatePortfolioRequest(portfolioRequest);
		
		Portfolio portfolioEntity = new Portfolio();
		Long clientId = portfolioRequest.getClient() != null ? portfolioRequest.getClient().getClientId() : 0L;
		Client clientEntity = clientRepository.findById(clientId)
					.orElseThrow(() -> 
							new ResourceNotFoundException("Client not found"));
		
		portfolioEntity.setClient(clientEntity);
		
		portfolioEntity.setCreationDate(
			    portfolioRequest.getCreationDate() != null 
			        ? portfolioRequest.getCreationDate() 
			        : LocalDate.now().toString()
			);

		List<SecurityRequest> securityRequestList = portfolioRequest.getSecurities();
		
		if(CollectionUtils.isEmpty(securityRequestList)) {
			securityRequestList = new ArrayList<>();
		}
		/* 
		 * Securities will not be be provided,
		 * for the new portfolio creation,
		 * will updated after the portfolio is created
		*/
		portfolioEntity.setSecurities(new ArrayList<>());
		Portfolio savedPortfolio = portfolioRepository.save(portfolioEntity);
		
		Client savedClientEntity = savedPortfolio.getClient();
		ClientResponse clientResponse = new ClientResponse(
				savedClientEntity.getClientId(),
				savedClientEntity.getFirstName(),
				savedClientEntity.getLastName(),
				savedClientEntity.getEmail(),
				savedClientEntity.getPhone(),
				savedClientEntity.getAddress()
				);
		
		List<Security> securityEntityList = savedPortfolio.getSecurities();
		
		List<SecurityResponse> securityListResponse = new ArrayList<>();
		for(Security securityEntity: securityEntityList) {
			securityListResponse.add(new SecurityResponse(
					securityEntity.getSecurityId(),
					securityEntity.getName(),
					securityEntity.getCategory(),
					securityEntity.getPurchasePrice(),
					securityEntity.getPurchaseDate(),
					securityEntity.getQuantity()
			));
		}

		PortfolioResponse portfolioResponse = new PortfolioResponse(
				savedPortfolio.getPortfolioId(),
				savedPortfolio.getCreationDate(),
				securityListResponse,
				clientResponse
			);
		
		return portfolioResponse;	
	}
	
}
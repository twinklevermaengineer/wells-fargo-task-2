package com.wellsfargo.counselor.service;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.wellsfargo.counselor.entity.Portfolio;
import com.wellsfargo.counselor.entity.Security;
import com.wellsfargo.counselor.helper.SecurityMapper;
import com.wellsfargo.counselor.model.request.SecurityRequest;
import com.wellsfargo.counselor.model.response.SecurityResponse;
import com.wellsfargo.counselor.repository.PortfolioRepository;
import com.wellsfargo.counselor.repository.SecurityRepository;
import com.wellsfargo.counselor.rest.InvalidRequestException;
import com.wellsfargo.counselor.rest.ResourceNotFoundException;
import com.wellsfargo.counselor.utils.SecurityRequestValidator;

import jakarta.transaction.Transactional;

@Service
public class SecurityServiceImpl implements SecurityService{
	
	private static final Logger logger = 
				LoggerFactory.getLogger(SecurityServiceImpl.class);
	
	private final SecurityRepository securityRepository;
	
	private final SecurityMapper mapper;
	
	private final SecurityRequestValidator validator;
	
	private final PortfolioRepository portfolioRepository;
	
	public SecurityServiceImpl(SecurityRepository securityRepository,
								SecurityMapper mapper,
								SecurityRequestValidator validator,
								PortfolioRepository portfolioRepository) {
		
		this.securityRepository = securityRepository;
		this.mapper = mapper;
		this.validator = validator;
		this.portfolioRepository = portfolioRepository;
	}

	@Override
	public SecurityResponse findById(Long id) {
		
		logger.info("Find security id: {} ", id);
		Security securityEntity = securityRepository.findById(id)
				.orElseThrow(() -> 
				 new ResourceNotFoundException("Security not found with id: " + id)
				);
		
		logger.debug("Map security entity object to security response");
		return mapper.mapEntityToResponse(securityEntity);
	}

	@Override
	public List<SecurityResponse> findAll() {
		
		logger.info("Fetching all securities from DB");
		List<SecurityResponse> responseList = new ArrayList<>();
	
		try {
			List<Security> securityList = securityRepository.findAll();
			
			logger.info("Map securities from DB to security response");
			for(Security security: securityList) {
				responseList.add(mapper.mapEntityToResponse(security));
			}
		} catch(Exception e) {
			logger.error("Exception occurred while retrieving security list", e);
			throw new ResourceNotFoundException(
							"Exception occured while retrieving security list, error: " 
							+ e.getMessage());
		}
		return responseList;
	}

	@Override
	public SecurityResponse findByName(String name) {
		logger.info("Find security by name, {} ", name);
		Security securityEntity = securityRepository.findByName(name)
					.orElseThrow(() -> 
					 new ResourceNotFoundException(
							 "Security does not exists with this name: " + name
						  )
					 );
		
		logger.info("Map security entity to security response");	
		return mapper.mapEntityToResponse(securityEntity);
	}

	@Override
	@Transactional
	public SecurityResponse save(SecurityRequest securityRequest) {
		
		logger.debug("Validate security request object for save request");
		List<String> errors = new ArrayList<>();
		validator.validateSecurityRequest(securityRequest, errors);
		
		if (!errors.isEmpty()) {
		    logger.warn("Security validation failed: {}", errors);
		    throw new InvalidRequestException("Validation failed: " + errors);
		}
		Portfolio portfolioEntity = null;
		logger.info("Fetch portfolio id from security request");
		Long portfolioId = securityRequest.getPortfolioId();

		if (portfolioId != null) {			
		    logger.info("Fetching portfolio entity associated with id {}", portfolioId);
	        portfolioEntity = portfolioRepository.findById(portfolioId).orElse(null);
	    }

		logger.debug("Map security request object to security entity object");
		Security securityEntity = new Security(
			securityRequest.getName(),
			securityRequest.getCategory().toString(),
			securityRequest.getPurchasePrice(),
			securityRequest.getPurchaseDate(),
			securityRequest.getQuantity(),
			portfolioEntity
			);
		
		logger.info("Saving the security object in DB");
		Security savedSecurity = securityRepository.save(securityEntity);
		
		logger.debug("Map security entity object to security response object");
		return mapper.mapEntityToResponse(savedSecurity);
	}

	@Override
	@Transactional
	public SecurityResponse updateSecurity(Long id, SecurityRequest securityRequest) {
		logger.debug("Validating security request object for update request");
		List<String> errors = new ArrayList<>();
		validator.validateSecurityRequest(securityRequest, errors);
		 if (!errors.isEmpty()) {
		        throw new InvalidRequestException("Validation failed: " + String.join(", ", errors));
		    }
		
		logger.info("Fetching security object associated with id: {} ", id);
		Security securityEntity = securityRepository.findById(id)
				.orElseThrow(() -> 
				new ResourceNotFoundException("Security not found with id: " + id)
			);
		
		logger.info("Fetching portfolio id associated with update request");
		Long portfolioId = securityRequest.getPortfolioId();
		
		logger.info("Fetching portfolio entity associated with portfolio id {} ", portfolioId);
		Portfolio portfolio = portfolioRepository.findById(portfolioId)
				.orElseThrow(() -> {
				logger.warn("Portfolio not found, id {} ", portfolioId);
				return new ResourceNotFoundException("Portfolio not found with id: " + portfolioId);
	});
		
		logger.debug("Map security request to security entity");
			securityEntity.setName(securityRequest.getName());
			securityEntity.setCategory(securityRequest.getCategory().toString());
			securityEntity.setPurchasePrice(securityRequest.getPurchasePrice());
			securityEntity.setPurchaseDate(securityRequest.getPurchaseDate());
			securityEntity.setQuantity(securityRequest.getQuantity());
			securityEntity.setPortfolio(portfolio);

		logger.info("Saving updated security object to DB");
		Security savedSecurity = securityRepository.save(securityEntity);
		
		logger.debug("Map security entity to security response");
		 return mapper.mapEntityToResponse(savedSecurity);
	
	}
}
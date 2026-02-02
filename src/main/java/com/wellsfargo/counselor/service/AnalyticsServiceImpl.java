package com.wellsfargo.counselor.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import com.wellsfargo.counselor.entity.Advisor;
import com.wellsfargo.counselor.entity.Client;
import com.wellsfargo.counselor.entity.Portfolio;
import com.wellsfargo.counselor.entity.Security;
import com.wellsfargo.counselor.model.response.AnalyticsResponse;
import com.wellsfargo.counselor.repository.AdvisorRepository;
import com.wellsfargo.counselor.repository.PortfolioRepository;
import com.wellsfargo.counselor.rest.AnalyticsServiceException;

@Service
public class AnalyticsServiceImpl implements AnalyticsService{
	
	private AdvisorRepository advisorRepository;
	private PortfolioRepository portfolioRepository;
	
	/**
	 *Constructs the AnalyticalServiceImpl with required repositories.
	 *
	 *@param advisorRepository repository used to retrieve advisor data
	 *@param portfolioRepository repository used to retrieve portfolio data
	*/
	@Autowired
	public AnalyticsServiceImpl(
			 AdvisorRepository advisorRepository
			,PortfolioRepository portfolioRepository) {
		
		this.advisorRepository = advisorRepository;
		this.portfolioRepository = portfolioRepository;
	}
	
	/**
	 * Retrieves the top advisors based on the total value of securities
	 * across all their client's portfolios.
	 * 
	 * <p>
	 * The method calculates total portfolio per advisor, determines
	 * the top two advisors with the highest total security amounts,
	 * and returns their analytics details.
	 * </p>
	 * 
	 * @return a list {@link AnalyticsResponse.AdvisorAnalyticsResponse}
	 * 			containing advisorId, name, and total portfolio value
	 * @throws AnalyticsServiceException if a database access error occurs
	*/

	@Override
	public List<AnalyticsResponse.AdvisorAnalyticsResponse> findAdvisorsByPortfolioSize() {
		
		List<AnalyticsResponse.AdvisorAnalyticsResponse> responseList = new ArrayList<>();
		Map<Long,BigDecimal> mapAdvisorSecurityAmount = new HashMap<>();

		try {	
			List<Advisor> advisors = advisorRepository.findAll();
			
			// find the clients for each advisor
			
			Map<Long,Advisor> advisorMap = new HashMap<>();
			for(Advisor advisor: advisors) {	

				List<Client> clients = advisor.getClients();
				BigDecimal securityAmountsByClient = getSecurityAmountByClient(clients);
				mapAdvisorSecurityAmount.put(advisor.getAdvisorId(),securityAmountsByClient);
				advisorMap.put(advisor.getAdvisorId(), advisor);
			}
			
			System.out.println(mapAdvisorSecurityAmount);
			List<Long> advisorId = getAdvisorIds(mapAdvisorSecurityAmount);
			for(Long id: advisorId) {
			
			Advisor advisor = advisorMap.get(id);
			String name = advisor.getFirstName() + " " + advisor.getLastName();
			BigDecimal amount = mapAdvisorSecurityAmount.get(id);
			
			 responseList.add(new AnalyticsResponse.AdvisorAnalyticsResponse(id, name, amount));
		
			}
			}catch(DataAccessException e) {
				throw new AnalyticsServiceException(
				        "Error occurred while retrieving advisors from the database", e);
		}
		return responseList;
	}
	
	/**
	 * Calculates the total security value for a list of clients.
	 * 
	 * <p>
	 * For each client, the associated value is retrieved and all
	 * security purchase prices are summed.
	*/
	
	private BigDecimal getSecurityAmountByClient(List<Client> clients) {		
		BigDecimal totalSecurityAmountForAllClient = BigDecimal.ZERO;
		for (Client client: clients) {
			Portfolio portfolioByClientId = portfolioRepository.findPortfolioByClientId(client.getClientId());
			
			if(portfolioByClientId == null) {
				continue;
			}
			
			List<Security> securities = portfolioByClientId.getSecurities();
			totalSecurityAmountForAllClient = 
					totalSecurityAmountForAllClient.add(getSecurityAmount(securities));
		}		
		return totalSecurityAmountForAllClient;
	}

	/**
	 * Calculates the total value of list of securities.
	*/
	
	private BigDecimal getSecurityAmount(List<Security> securities) {
		BigDecimal totalSecurityAmountForClient = BigDecimal.ZERO;
		
		for (Security security: securities) {
			BigDecimal purchasePrice = security.getPurchasePrice();
			totalSecurityAmountForClient = totalSecurityAmountForClient.add(purchasePrice);
		}
		return totalSecurityAmountForClient;
	}
	
	/**
	 * Identifies the top two advisor IDs based on portfolio value.
	 * 
	 * <p>
	 * The method scans the provided map and returns upto two advisor IDs
	 * with the highest associated values.
	 * </p>
	*/

	private List<Long> getAdvisorIds(Map<Long, BigDecimal> map) {

		List<Long> result = new ArrayList<>();
		
		if (map == null || map.isEmpty()) {
		        return result;
		}

		BigDecimal maxValue1 = BigDecimal.ZERO;
		BigDecimal maxValue2 = BigDecimal.ZERO;
		Long key1 = null;
		Long key2 = null;
		
		for(Map.Entry<Long, BigDecimal> entry: map.entrySet()) {

			BigDecimal value = entry.getValue();
			Long key = entry.getKey();
			
			if(value.compareTo(maxValue1) > 0) {

				maxValue2 = maxValue1;
				key2 = key1;
				
				maxValue1 = value;
				key1 = key;
				
			}else if (value.compareTo(maxValue2) > 0) {					
					maxValue2 = value;
					key2 = key;
	 		}
		}
			if(key1 != null) result.add( key1);
			if(key2 != null) result.add(key2);
	
			return result;
      }
}
	
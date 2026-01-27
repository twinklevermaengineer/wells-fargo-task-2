package com.wellsfargo.counselor.helper;

import java.util.Optional;
import org.springframework.stereotype.Component;
import com.wellsfargo.counselor.entity.Portfolio;
import com.wellsfargo.counselor.entity.Security;
import com.wellsfargo.counselor.model.response.SecurityResponse;

@Component
public class SecurityMapper {
	
	 public SecurityResponse mapEntityToResponse(Security security) {
	        Long portfolioId = Optional.ofNullable(security.getPortfolio())
	                .map(Portfolio::getPortfolioId)
	                .orElse(null);

	        return new SecurityResponse(
	                security.getSecurityId(),
	                security.getName(),
	                security.getCategory(),
	                security.getPurchasePrice(),
	                security.getPurchaseDate(),
	                security.getQuantity(),
	                portfolioId
	        );
	 }
}

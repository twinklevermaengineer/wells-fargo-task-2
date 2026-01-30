package com.wellsfargo.counselor.helper;

import java.util.Optional;
import org.springframework.stereotype.Component;
import com.wellsfargo.counselor.entity.Portfolio;
import com.wellsfargo.counselor.entity.Security;
import com.wellsfargo.counselor.model.response.SecurityResponse;

/*
 *A helper component that maps {@link Security} entities to {@link SecurityResponse} DTOs.
 *<p>
 *This class is annotated with {@link Component}, making it a spring managed bean,
 *which can be injected whereever entity-to-DTO mapping is required.
 *</p> 
 */
@Component
public class SecurityMapper {

	/*
	*Converts a {@link Security} entity into a {@link SecurityResponse}.
	*<p>
	*If the {@link Security} entity is assciated with a {@link Portfolio}, it's ID will be
	*included in the response. If no portfolio is linked, the portfolio ID will be {@code Null}.
	*</p>
	*
	*@param security the {@link Security} entity to map; must not be {@code Null}
	*@return a {@link SecurityResponse} containing the data from the entity,
	*		including the portfolio ID if available.
	*
	*/
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
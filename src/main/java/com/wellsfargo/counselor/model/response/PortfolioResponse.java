package com.wellsfargo.counselor.model.response;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "PortfolioResponse",
		description = "Response object representing portfolio details")
public class PortfolioResponse {

	@Schema(description = "Portfolio Unique Identifier")
	private Long portfolioId;
	
	@Schema(description = "Portfolio Creation Date")
	private String creationDate;
	
	@Schema(description = "List of securities in portfolio")
	private List<SecurityResponse> securities;
	
	@Schema(description = "Client information associated with the portfolio")
	private ClientResponse client;

	public PortfolioResponse(Long portfolioId) {
		this.portfolioId = portfolioId;
	}
}

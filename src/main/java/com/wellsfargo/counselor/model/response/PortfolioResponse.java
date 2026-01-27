package com.wellsfargo.counselor.model.response;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;

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
public class PortfolioResponse {
	
	private Long portfolioId;
	private String creationDate;
	private List<SecurityResponse> securities;
	private ClientResponse client;
	
	
	public PortfolioResponse(Long portfolioId) {
		this.portfolioId = portfolioId;
	}
}

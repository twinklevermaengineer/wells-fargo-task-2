package com.wellsfargo.counselor.model.request;

import java.util.List;
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
public class PortfolioResponse {
	
	private Long portfolioId;
	private String creationDate;
	private List<SecurityResponse> securities;
	private ClientResponse client;
	
}

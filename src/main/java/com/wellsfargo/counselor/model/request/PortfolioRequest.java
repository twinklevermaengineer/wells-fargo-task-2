package com.wellsfargo.counselor.model.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioRequest {

	private String creationDate;
	private List<SecurityRequest> securities;
	private ClientRequest client;
}

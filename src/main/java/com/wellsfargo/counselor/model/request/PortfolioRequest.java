package com.wellsfargo.counselor.model.request;

import java.util.List;

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
@Schema(
		name = "PortfolioRequest",
		description = "Request object representing portfolio details",
		requiredProperties = {"creationDate", "client"}
		)
public class PortfolioRequest {
	@Schema(
			description = "Portfolio Creation Date",
			example = "2026-01-21",
			type = "string",
			format = "date"
			)
	private String creationDate;
	
	@Schema(
			description = "List of securities in portfolio",
			example = "[{\"securityName\": \"Amazon\", \"quantity\": 10}, {\"securityName\": \"Apple\", \"quantity\": 5}]"
			)
	private List<SecurityRequest> securities;
	
	@Schema(
			description = "Client information associated with the portfolio",
			example = "{\n" +
		              "  \"firstName\": \"Paul\",\n" +
		              "  \"lastName\": \"Doe\",\n" +
		              "  \"address\": \"213, Miami FL\",\n" +
		              "  \"phone\": \"1234567890\",\n" +
		              "  \"email\": \"pauldoe@gmail.com\"\n" +
		              "}"
			)
	private ClientRequest client;
}

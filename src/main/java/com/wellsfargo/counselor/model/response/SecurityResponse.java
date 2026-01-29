package com.wellsfargo.counselor.model.response;

import java.math.BigDecimal;
import com.wellsfargo.counselor.utils.SecurityType;

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
		name = "SecurityResponse",
		description = "Response object representing securities details"
		)
public class SecurityResponse {

	@Schema(description = "Security Unique Identifier")
	private Long securityId;

	@Schema(description = "Security Name")
	private String name;
	
	@Schema(description = "Security Category")
	private String category;
	
	@Schema(description = "Securities Purchase Price")
	private BigDecimal purchasePrice;
	
	@Schema(description = "Securities Purchase Date")
	private String purchaseDate;
	
	@Schema(description = "Securities Quantity")
	private Integer quantity;
	
	@Schema(description = "Portfolio Id associated with securities")
	private Long portfolioId;
}

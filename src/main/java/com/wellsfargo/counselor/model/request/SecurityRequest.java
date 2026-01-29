package com.wellsfargo.counselor.model.request;

import java.math.BigDecimal;

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
		name = "Security Request",
		description = "Request object representing securities details",
		requiredProperties = {"name", "category", "purchasePrice", "purchaseDate", "quantity"}
		)
public class SecurityRequest {

	@Schema(
			description = "Security Name",
			example = "Microsoft Corp."
			)
	private String name;
	
	@Schema(
			description = "Securities Category",
			example = "STOCK / BOND"
			)
	private String category;
	
	@Schema(
			description = "Purchase Price Of Securities",
			example = "123.50"
			)
	private BigDecimal purchasePrice;
	
	@Schema(
			description = "Securities Purchase Date",
			example = "2026-01-21"
			)
	private String purchaseDate;
	
	@Schema(
			description = "Securities quantity",
			example = "200"
			)
	private Integer quantity;
	
	@Schema(
			description = "Portfolio Id associated with securities"
			)
	private Long portfolioId;
}
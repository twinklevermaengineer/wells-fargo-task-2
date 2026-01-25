package com.wellsfargo.counselor.model.request;

import java.math.BigDecimal;

import com.wellsfargo.counselor.utils.SecurityType;

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
public class SecurityResponse {

	private Long securityId;
	private String name;
	private String category;
	private BigDecimal purchasePrice;
	private String purchaseDate;
	private Integer quantity;
	private Long portfolioId;

	public SecurityResponse(
				Long securityId, String name,
				SecurityType securityType, 
				BigDecimal purchasePrice, 
				String purchaseDate, Integer quantity) {
		
		this.securityId = securityId;
		this.name = name;
		this.category = securityType.getDisplayName();
		this.purchasePrice = purchasePrice;
		this.purchaseDate = purchaseDate;
		this.quantity = quantity;

	}
}

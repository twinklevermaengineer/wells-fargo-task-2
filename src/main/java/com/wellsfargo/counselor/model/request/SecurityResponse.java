package com.wellsfargo.counselor.model.request;

import java.math.BigDecimal;

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
}

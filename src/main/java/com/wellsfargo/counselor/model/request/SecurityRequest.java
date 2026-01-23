package com.wellsfargo.counselor.model.request;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SecurityRequest {

	private String name;
	private String category;
	private BigDecimal purchasePrice;
	private String purchaseDate;
	private Integer quantity;
}

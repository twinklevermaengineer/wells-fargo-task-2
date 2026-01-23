package com.wellsfargo.counselor.entity;

import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "security")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Security {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "security_id")
	private Long securityId;
	
	@NotBlank
	@Column(name = "name",nullable = false)
	private String name;
	
	@NotBlank
	@Column(name = "category",nullable = false)
	private String category;
	
	@NotNull
	@Column(name = "purchase_price",nullable = false)
	private BigDecimal purchasePrice;
	
	//I want to explicitly declare it as String instead of LocalDate so I can handle type conversion
	@NotBlank
	@Column(name = "purchase_date",nullable = false)
	private String purchaseDate;
	
	@NotNull
	@Column(name = "quantity",nullable = false)
	private Integer quantity;
	
	@ManyToOne
	@JoinColumn(name = "portfolio_id")
	private Portfolio portfolio;
	
  }
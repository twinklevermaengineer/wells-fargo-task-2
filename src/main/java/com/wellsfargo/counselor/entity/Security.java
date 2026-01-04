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

@Entity
@Table(name = "security")
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
	
	protected Security() {}
	
    public Security(String name, String category, BigDecimal purchasePrice, String purchaseDate, Integer quantity, Portfolio portfolio) {
		    this.name = name;
		    this.category = category;
		    this.purchasePrice = purchasePrice;
		    this.purchaseDate = purchaseDate;
		    this.quantity = quantity;
		    this.portfolio = portfolio;
		}


	 public Portfolio getPortfolio() {
		    return portfolio;
		}

		public void setPortfolio(Portfolio portfolio) {
		    this.portfolio = portfolio;
		}

	    public Long getSecurityId() {
	        return securityId;
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public String getCategory() {
	        return category;
	    }

	    public void setCategory(String category) {
	        this.category = category;
	    }

	    public BigDecimal getPurchasePrice() {
	        return purchasePrice;
	    }

	    public void setPurchasePrice(BigDecimal purchasePrice) {
	        this.purchasePrice = purchasePrice;
	    }

	    public String getPurchaseDate() {
	        return purchaseDate;
	    }

	    public void setPurchaseDate(String purchaseDate) {
	        this.purchaseDate = purchaseDate;
	    }

	    public Integer getQuantity() {
	        return quantity;
	    }

	    public void setQuantity(Integer quantity) {
	        this.quantity = quantity;
	    }
}

package com.wellsfargo.counselor.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "portfolio")
public class Portfolio {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "portfolio_id")
	private Long portfolioId;
	
	//I want to explicitly declare it as String instead of LocalDate so I can handle type conversion
	@NotNull
	@Column(name = "creation_date",nullable = false)
	private String creationDate;
	
	@ManyToOne
	@JoinColumn(name = "client_id", nullable = false)
	private Client client;
	
	@OneToMany(mappedBy = "portfolio")
	private List<Security> securities;

	protected Portfolio() {}
	public Portfolio(String creationDate, Client client) {
	    this.creationDate = creationDate;
	    this.client = client;
	}

	
	public Client getClient() {
	    return client;
	}

	public void setClient(Client client) {
	    this.client = client;
	}

	public List<Security> getSecurities() {
	    return securities;
	}

	public void setSecurities(List<Security> securities) {
	    this.securities = securities;
	}

	
	 public Portfolio(String creationDate) {
	        this.creationDate = creationDate;
	    }

	public long getPortfolioId() {
		return portfolioId;
	}
	
	 public String getCreationDate() {
	        return creationDate;
	    }

	    public void setCreationDate(String creationDate) {
	        this.creationDate = creationDate;
	    }
	   
}

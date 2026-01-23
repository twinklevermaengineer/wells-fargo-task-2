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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "portfolio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Portfolio {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "portfolio_id")
	private Long portfolioId;

	//Explicitly declare it as String rather than LocalDate, to allow proper type conversion
	@NotNull
	@Column(name = "creation_date",nullable = false)
	private String creationDate;

	@OneToMany(mappedBy = "portfolio")
	private List<Security> securities;

	@ManyToOne
	@JoinColumn(name = "client_id", nullable = false)
	private Client client;

	public Portfolio(String creationDate, List<Security> securities, Client client) {
		this.creationDate = creationDate;
		this.securities = securities;
		this.client = client;
		
	}
	
}
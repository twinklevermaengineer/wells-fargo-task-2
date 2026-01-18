package com.wellsfargo.counselor.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "client")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Client {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "client_id")
	private Long clientId;
	
	@NotBlank
	@Column(name = "first_name", nullable = false)
	private String firstName;
	
	@NotBlank
	@Column(name = "last_name", nullable = false)
	private String lastName;
	
	@NotBlank
	@Column(name = "address", nullable = false)
	private String address;
	
	@NotBlank
	@Column(name = "phone", nullable = false)
	private String phone;
	
	@Email
	@Column(name = "email", nullable = false, unique = true)
	private String email;
	
	@ManyToOne
	@JoinColumn(name="advisor_id", nullable = true)
	// @JsonManagedReference // tells Jackson: serialize this side
	private Advisor advisor;

	public Client(String firstName, String lastName, String address,
			 String phone, String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.phone = phone;
		this.email = email;
	}

}

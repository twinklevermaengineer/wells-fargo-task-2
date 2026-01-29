package com.wellsfargo.counselor.model.request;

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
@ToString(exclude = {"email", "phone"})
@Schema(
		name = "Client Request",
		description = "Request object representing client details",
		requiredProperties = {"firstName", "lastName", "address", "phone", "email"}  
		)
public class ClientRequest {
	
	@Schema(description = "Client Unique Identifier")
	private Long clientId;
	
	@Schema(
			description = "Client's First Name",
			example = "Paul",
			maxLength = 100
			)
	private String firstName;
	
	@Schema(
			description = "Client's Last Name",
			example = "Doe",
			maxLength = 100
			)
	private String lastName;
	
	@Schema(
			description = "Client's Address",
			example = "213, Miami FL",
			maxLength = 250
			)
	private String address;
	
	@Schema(
			description = "Client's Phone Number",
			example = "1234567985",
			pattern = "\\d{10}",  
			maxLength = 10
			)
	private String phone;
	
	@Schema(
			description = "Client's Email",
			example = "pauldoe@gmail.com",
			format = "email",
			minLength = 10,
			maxLength = 100
			)
	private String email;
}
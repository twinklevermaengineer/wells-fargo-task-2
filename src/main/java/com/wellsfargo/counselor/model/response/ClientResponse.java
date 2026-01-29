package com.wellsfargo.counselor.model.response;

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
		name = "ClientResponse",
		description = "Response object representing client details"
		)
public class ClientResponse {

	@Schema(description = "Client Unique Identifier")
	private Long clientId;
	
	@Schema(description = "Client's First Name")
	private String firstName;
	
	@Schema(description = "Client's Last Name")
	private String lastName;
	
	@Schema(description = "Client's Address")
	private String address;
	
	@Schema(description = "Client's Phone Number")
	private String phone;
	
	@Schema(description = "Client's Email Id")
	private String email;

public ClientResponse(String firstName, String lastName) {
	this.firstName = firstName;
	this.lastName = lastName;
}

}

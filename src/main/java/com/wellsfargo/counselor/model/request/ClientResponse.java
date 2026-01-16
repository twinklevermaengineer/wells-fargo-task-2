package com.wellsfargo.counselor.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientResponse {

private Long clientId;
private String firstName;
private String lastName;
private String address;
private String phone;
private String email;

public ClientResponse(String firstName, String lastName) {
	this.firstName = firstName;
	this.lastName = lastName;
}

}

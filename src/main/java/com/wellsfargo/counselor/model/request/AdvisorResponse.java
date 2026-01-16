package com.wellsfargo.counselor.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdvisorResponse {

	private Long advisorId;
	private String firstName;
	private String lastName;
	private String address;
	private String phone;
	private String email;
	
	
}

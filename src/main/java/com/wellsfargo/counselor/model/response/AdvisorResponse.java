package com.wellsfargo.counselor.model.response;

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
public class AdvisorResponse {

	private Long advisorId;
	private String firstName;
	private String lastName;
	private String address;
	private String phone;
	private String email;
	
}

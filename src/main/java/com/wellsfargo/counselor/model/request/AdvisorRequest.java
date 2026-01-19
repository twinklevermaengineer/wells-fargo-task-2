package com.wellsfargo.counselor.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AdvisorRequest {
	private String firstName;
	private String lastName;
	private String address;
	private String phone;
	private String email;
}

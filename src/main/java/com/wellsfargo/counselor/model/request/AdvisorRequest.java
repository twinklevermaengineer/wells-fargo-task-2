package com.wellsfargo.counselor.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "AdvisorRequest",
		description = "Request object representing advisor details",
		requiredProperties = {"firstName", "lastName", "address", "phone", "email"}
		)
public class AdvisorRequest {

	@Schema(
			description = "Advisor First Name",
			example = "John",
			maxLength = 100
			)
	private String firstName;

	@Schema(
			description = "Advisor Last Name",
			example = "Doe",
			maxLength = 100
			)
	private String lastName;

	@Schema(
			description = "Advisor's Address",
			example = "123, Miami FL",
			maxLength = 250
			)
	private String address;

	@Schema(
			description = "Advisor's Contact Number",
			example = "1234567891",
			pattern = "\\d{10}"
			)
	private String phone;

	@Schema(
			description = "Advisor's Email",
			example = "johndoe@gmail.com",
			format = "email",
			maxLength = 100
			)
	private String email;
}
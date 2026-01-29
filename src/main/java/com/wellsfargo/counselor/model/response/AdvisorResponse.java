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
@Schema(name = "AdvisorResponse", 
		description = "Response object representing advisor details")
public class AdvisorResponse {

	@Schema(description = "Advisor Unique Identifier")
	private Long advisorId;
	
	@Schema(description = "Advisor's First Name")
	private String firstName;
	
	@Schema(description = "Advisor's Last Name")
	private String lastName;
	
	@Schema(description = "Advisor's Address")
	private String address;
	
	@Schema(description = "Advisor's Contact Number")
	private String phone;
	
	@Schema(description = "Advisor's email")
	private String email;
}

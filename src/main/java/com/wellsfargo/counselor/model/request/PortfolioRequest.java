package com.wellsfargo.counselor.model.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioRequest {

	@NotBlank(message = "Creation date is required")
	private String creationDate;

	@Valid
	private List<SecurityRequest> securities;

	@NotNull(message = "Client details are required")
    @Valid
	private ClientRequest client;
	
}

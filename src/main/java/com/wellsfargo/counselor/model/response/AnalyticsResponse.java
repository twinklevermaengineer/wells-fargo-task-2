package com.wellsfargo.counselor.model.response;
import java.math.BigDecimal;

public class AnalyticsResponse {

	public record AdvisorAnalyticsResponse(
			Long advisorId
			, String advisorName
			,BigDecimal portfolioSize) 
	{}
}

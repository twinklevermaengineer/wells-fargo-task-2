package com.wellsfargo.counselor.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.wellsfargo.counselor.model.response.AnalyticsResponse.AdvisorAnalyticsResponse;
import com.wellsfargo.counselor.service.AnalyticsServiceImpl;

@RestController
@RequestMapping("/api/v1/analytics")
public class AnalyticsServiceController {
	
	private AnalyticsServiceImpl analyticsServiceImpl;
	
	public AnalyticsServiceController(AnalyticsServiceImpl analyticsServiceImpl) {
		this.analyticsServiceImpl = analyticsServiceImpl;
		
	}

	@GetMapping("")
	public ResponseEntity<List<AdvisorAnalyticsResponse>> advisorList(){
		List<AdvisorAnalyticsResponse> response = analyticsServiceImpl.findAdvisorsByPortfolioSize();	
		return ResponseEntity.ok(response);
	}
}

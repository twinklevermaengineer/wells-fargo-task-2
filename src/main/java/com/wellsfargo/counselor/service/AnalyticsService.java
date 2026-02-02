package com.wellsfargo.counselor.service;

import java.util.List;
import com.wellsfargo.counselor.model.response.AnalyticsResponse;
import com.wellsfargo.counselor.model.response.AnalyticsResponse.AdvisorAnalyticsResponse;

public interface AnalyticsService {
	List<AdvisorAnalyticsResponse> findAdvisorsByPortfolioSize();
}

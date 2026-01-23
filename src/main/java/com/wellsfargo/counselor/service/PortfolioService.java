package com.wellsfargo.counselor.service;

import java.util.List;

import com.wellsfargo.counselor.model.request.PortfolioRequest;
import com.wellsfargo.counselor.model.request.PortfolioResponse;

public interface PortfolioService {

	PortfolioResponse findById(Long id);

	List<PortfolioResponse> findAll();
	
	PortfolioResponse save(PortfolioRequest portfolioRequest);
	
}

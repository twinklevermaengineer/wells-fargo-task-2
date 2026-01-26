package com.wellsfargo.counselor.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.wellsfargo.counselor.model.request.PortfolioRequest;
import com.wellsfargo.counselor.model.request.PortfolioResponse;
import com.wellsfargo.counselor.service.PortfolioService;

@RestController
@RequestMapping("/api/v1/portfolios")
public class PortfolioController{
	
	private static final Logger logger =
			LoggerFactory.getLogger(PortfolioController.class);

	private PortfolioService portfolioService;
	
	public PortfolioController(PortfolioService portfolioService) {
		this.portfolioService = portfolioService;
	}
	
	@PostMapping("/")
	public ResponseEntity<PortfolioResponse> addPortfolio(
		@Valid @RequestBody PortfolioRequest request){
		
		logger.info("Adding portfolio for request " + request);
		
		PortfolioResponse response = portfolioService.save(request);	
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<PortfolioResponse> findPortfolioById(
			@PathVariable Long id){
		
		logger.info("Finding portfolio with id: {} ", id);	
		PortfolioResponse portfolio = portfolioService.findById(id);
	
		logger.info("Portfolio found with id: {} ", id);
		return ResponseEntity.ok(portfolio);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<PortfolioResponse>> getPortfolio(){
		
		logger.info("Fetching all portfolios");
		
		List<PortfolioResponse> portfolio = portfolioService.findAll();
		return ResponseEntity.ok(portfolio);
	}
	
	
	
}

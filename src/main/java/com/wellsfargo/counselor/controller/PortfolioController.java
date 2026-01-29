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
import com.wellsfargo.counselor.model.response.PortfolioResponse;
import com.wellsfargo.counselor.service.PortfolioService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/portfolios")
public class PortfolioController{
	
	private static final Logger logger =
			LoggerFactory.getLogger(PortfolioController.class);

	private PortfolioService portfolioService;
	
	public PortfolioController(PortfolioService portfolioService) {
		this.portfolioService = portfolioService;
	}

	@Operation(summary = "Add portfolio",
			   description = "Create new portfolio")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201",
						 description = "Portfolio created successfully",
						 content = @Content(mediaType = "application/json",
						 					schema = @Schema(implementation = PortfolioResponse.class))),
			 @ApiResponse(responseCode = "400", description = "Invalid request payload"),
			 @ApiResponse(responseCode = "500", description = "Internal server error")
  })
	@PostMapping
	public ResponseEntity<PortfolioResponse> addPortfolio(@RequestBody PortfolioRequest request){
		
		logger.info("Adding portfolio for request {} ", request);
		
		PortfolioResponse response = portfolioService.save(request);	
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@Operation(summary = "Get portfolio by Id",
			   description = "Retreive portfolio details by Id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
						 description = "Portfolio find by Id",
						 content = @Content(mediaType = "application/json",
						 					schema = @Schema(implementation = PortfolioResponse.class))),
			@ApiResponse(responseCode = "400", description = "Portfolio not found"),
			@ApiResponse(responseCode = "500", description = "Internal server error")		
  })
	@GetMapping("/{id}")
	public ResponseEntity<PortfolioResponse> findPortfolioById(
			@PathVariable Long id){
		
		logger.info("Finding portfolio with id: {} ", id);	
		PortfolioResponse portfolio = portfolioService.findById(id);
	
		logger.info("Portfolio found with id: {} ", id);
		return ResponseEntity.ok(portfolio);
	}
	
	@Operation(summary = "Get all portfolio",
			   description = "Get all the list of portfolio")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200",
						 description = "Portfolio list found",
						  content = @Content(mediaType = "application/json",
						  					 schema = @Schema(implementation = PortfolioResponse[].class))),
  })
	@GetMapping
	public ResponseEntity<List<PortfolioResponse>> getPortfolio(){
		
		logger.info("Fetching all portfolios");
		
		List<PortfolioResponse> portfolio = portfolioService.findAll();
		return ResponseEntity.ok(portfolio);
	}
}
package com.wellsfargo.counselor.service;

import java.util.List;
import java.util.Optional;

import com.wellsfargo.counselor.entity.Advisor;
import com.wellsfargo.counselor.model.request.AdvisorRequest;
import com.wellsfargo.counselor.model.request.AdvisorResponse;

public interface AdvisorService {
	AdvisorResponse findById(Long id);

	Long save(AdvisorRequest advisorRequest);
	
	List<AdvisorResponse> findAll();
 
	void deleteById(Long id);
	
	void updateAdvisor(Long id, AdvisorRequest advisorRequest);

	AdvisorResponse findByAddress(String address);
}

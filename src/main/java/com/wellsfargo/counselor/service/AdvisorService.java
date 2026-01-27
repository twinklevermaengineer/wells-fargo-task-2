package com.wellsfargo.counselor.service;

import java.util.List;
import com.wellsfargo.counselor.model.request.AdvisorRequest;
import com.wellsfargo.counselor.model.response.AdvisorResponse;

public interface AdvisorService {
	AdvisorResponse findById(Long id);

	AdvisorResponse save(AdvisorRequest advisorRequest);
	
	List<AdvisorResponse> findAll();
 
	void deleteById(Long id);
	
	void updateAdvisor(Long id, AdvisorRequest advisorRequest);

	AdvisorResponse findByAddress(String address);

	AdvisorResponse findAdvisorByClientId(Long clientId);
}

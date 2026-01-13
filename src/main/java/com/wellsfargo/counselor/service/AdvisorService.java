package com.wellsfargo.counselor.service;

import java.util.List;
import java.util.Optional;

import com.wellsfargo.counselor.entity.Advisor;
import com.wellsfargo.counselor.model.request.AdvisorRequest;

public interface AdvisorService {
	Optional<Advisor> findById(Long id);

	Long save(AdvisorRequest advisorRequest);
	
	List<Advisor> findAll();
 
	void deleteById(Long id);
	
	void updateAdvisor(Long id, AdvisorRequest advisorRequest);
}

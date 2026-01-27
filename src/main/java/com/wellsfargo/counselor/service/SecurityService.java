package com.wellsfargo.counselor.service;

import java.util.List;
import com.wellsfargo.counselor.model.request.SecurityRequest;
import com.wellsfargo.counselor.model.response.SecurityResponse;

public interface SecurityService {
	
	SecurityResponse findById(Long id);

	List<SecurityResponse> findAll();

	SecurityResponse findByName(String name);

	SecurityResponse save(SecurityRequest request);

	SecurityResponse updateSecurity(Long id, SecurityRequest securityRequest);

}

package com.wellsfargo.counselor.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.wellsfargo.counselor.entity.Advisor;
import com.wellsfargo.counselor.entity.Client;

@Repository
public interface AdvisorRepository extends JpaRepository<Advisor, Long> {
	List<Advisor> existsByEmail(String email);
	 
	List<Advisor> existsByPhone(String phone);

	List<Advisor> existsByAddress(String address);
	
	List<Advisor> findByEmailOrPhoneOrAddress(String email, String phone, String address);

	List<Advisor> findByPhone(String phone);

	List<Advisor> findByEmail(String email);
	
	Optional<Advisor> findByAddress(String address);

	Optional<Advisor> findByClients_ClientId(Long clientId);

}

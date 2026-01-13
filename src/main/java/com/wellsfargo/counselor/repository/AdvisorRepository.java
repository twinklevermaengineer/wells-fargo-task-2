package com.wellsfargo.counselor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wellsfargo.counselor.entity.Advisor;

@Repository
public interface AdvisorRepository extends JpaRepository<Advisor, Long> {
	List<Advisor> existsByEmail(String email);
	 
	List<Advisor> existsByPhone(String phone);

	List<Advisor> existsByAddress(String address);
	
	List<Advisor> findByEmailOrPhoneOrAddress(String email, String phone, String address);

	List<Advisor> findByPhone(String phone);

	List<Advisor> findByEmail(String email);

	List<Advisor> findByAddress(String address);
}

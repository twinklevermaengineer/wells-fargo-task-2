package com.wellsfargo.counselor.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.wellsfargo.counselor.entity.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

	List<Client> findByAddress(String address);
	
	List<Client> findByEmail(String email);
	
	List<Client> findByPhone(String phone);

	List<Client> findByAdvisor_AdvisorId(Long advisorId);
}

package com.wellsfargo.counselor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.wellsfargo.counselor.entity.Portfolio;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
	
	@Query("SELECT p FROM Portfolio p WHERE p.client.id = :clientId")
	Portfolio findPortfolioByClientId(@Param("clientId") Long clientId);
	
}

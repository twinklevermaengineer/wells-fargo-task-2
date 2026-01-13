package com.wellsfargo.counselor.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.wellsfargo.counselor.entity.Portfolio;

public interface PortfolioRepository extends JpaRepositoryImplementation<Portfolio, Long> {

}

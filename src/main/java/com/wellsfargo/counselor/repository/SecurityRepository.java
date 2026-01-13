package com.wellsfargo.counselor.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import com.wellsfargo.counselor.entity.Security;

public interface SecurityRepository extends JpaRepositoryImplementation<Security, Long> {

}

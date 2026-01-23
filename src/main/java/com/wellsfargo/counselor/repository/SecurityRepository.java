package com.wellsfargo.counselor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.wellsfargo.counselor.entity.Security;

public interface SecurityRepository extends JpaRepository<Security, Long> {

}

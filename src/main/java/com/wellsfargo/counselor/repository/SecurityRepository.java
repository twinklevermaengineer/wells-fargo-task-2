package com.wellsfargo.counselor.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.wellsfargo.counselor.entity.Security;

public interface SecurityRepository extends JpaRepository<Security, Long> {

	Optional<Security> findByName(String name);

}

package com.wellsfargo.counselor.repository;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.wellsfargo.counselor.entity.Client;
@Repository
public interface ClientRepository extends JpaRepositoryImplementation<Client, Long> {

}

package com.justfun.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.justfun.entity.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
	
	Optional<Client> findByName(String name);

	long countByPhone(String phone);	
	
	long countByEmail(String email);
}


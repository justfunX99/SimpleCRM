package com.justfun.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.justfun.entity.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
	
	@Transactional(readOnly = true)
	Optional<Client> findByName(String name);
	
}

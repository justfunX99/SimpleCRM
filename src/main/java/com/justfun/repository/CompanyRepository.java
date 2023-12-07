package com.justfun.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.justfun.entity.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {

	@Transactional(readOnly = true)
	Optional<Company> findByName(String name);
}
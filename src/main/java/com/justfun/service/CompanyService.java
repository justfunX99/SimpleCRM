package com.justfun.service;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.justfun.dto.CompanyDTO;
import com.justfun.entity.Company;
import com.justfun.mapper.CompanyMapper;
import com.justfun.repository.CompanyRepository;

@Service
public class CompanyService {
	
	@Autowired
	private CompanyMapper mapper;
	
	
	@Autowired
	private CompanyRepository companyRepository;

	@Transactional
	public Company add(CompanyDTO dto) {
		validate(dto);
		
		Company entity = mapper.convertToEntity(dto);
		
		return companyRepository.saveAndFlush(entity);
	}

	@Transactional
	public Company update(Integer id, CompanyDTO dto) {
		validate(dto);
		
		Optional<Company> optCompany = companyRepository.findById(id);
		
		if(optCompany.isPresent()) {
			BeanUtils.copyProperties(dto, optCompany.get());
			
			return companyRepository.save(optCompany.get());			
		}
		
		throw new RuntimeException("fail to update company. id not found.");
	}
	
	// TODO: fix_ companies with clients couldn't be deleted
	@Transactional
	public void delete(Integer id) {
		Optional<Company> optCompany = companyRepository.findById(id);
		
		if(optCompany.isPresent()) {
			companyRepository.delete(optCompany.get());
			return;
		}
		
		throw new RuntimeException("fail to delete company. id not found.");
	}
	
	@Transactional(readOnly = true)
	public List<Company> findAll() {
		return companyRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Company findById(Integer id) {
		return companyRepository.findById(id).orElse(null);
	}

	private void validate(CompanyDTO dto) {
		Assert.isTrue(!StringUtils.isBlank(dto.getName()), "empty name");
		Assert.isTrue(!StringUtils.isBlank(dto.getAddress()), "empty address");
	}

	
}

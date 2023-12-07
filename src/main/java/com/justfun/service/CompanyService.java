package com.justfun.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.justfun.dto.CompanyDTO;
import com.justfun.entity.Company;
import com.justfun.repository.CompanyRepository;

@Service
public class CompanyService {
	
	@Autowired
	private CompanyMapper mapper;
	
	
	@Autowired
	private CompanyRepository companyRepository;

	@Transactional
	public Company add(CompanyDTO dto) {
		Company entity = mapper.convertToEntity(dto);
		
		return companyRepository.saveAndFlush(entity);
	}

	@Transactional
	public Company update(Integer id, CompanyDTO dto) {
		Optional<Company> optCompany = companyRepository.findById(id);
		
		if(optCompany.isPresent()) {
			BeanUtils.copyProperties(dto, optCompany.get());
			
			return companyRepository.save(optCompany.get());			
		}
		
		throw new RuntimeException("fail to update company. id not found.");
	}
	
	
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
		return companyRepository.findById(id).get();
	}


	@Component
	class CompanyMapper{
		public Company convertToEntity(CompanyDTO dto) {
			Company entity = new Company();
			BeanUtils.copyProperties(dto, entity, "id");
			return entity;
		}		
	}



	
}

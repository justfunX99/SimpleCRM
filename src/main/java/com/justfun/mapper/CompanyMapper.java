package com.justfun.mapper;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.justfun.dto.CompanyDTO;
import com.justfun.entity.Company;
import com.justfun.vo.CompanyVO;

@Component
public class CompanyMapper {
	public Company convertToEntity(CompanyDTO dto) {
		Company entity = new Company();
		BeanUtils.copyProperties(dto, entity, "id");
		
		return entity;
	}	
	
	public CompanyVO convertToVO(Company company) {
		CompanyVO vo = new CompanyVO();
		BeanUtils.copyProperties(company, vo);
		
		return vo;
	}
	
}
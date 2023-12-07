package com.justfun.mapper;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.justfun.dto.ClientDTO;
import com.justfun.entity.Client;
import com.justfun.vo.BulkClientItemCellVO;

@Component
public class ClientMapper {
	
	@Autowired
	private CompanyMapper companyMapper;
	
	public Client convertToEntity(ClientDTO dto) {
		Client entity = new Client();
		BeanUtils.copyProperties(dto, entity);
		return entity;
	}
	
	public BulkClientItemCellVO convertToVO(Client client) {
		BulkClientItemCellVO vo = new BulkClientItemCellVO();
		BeanUtils.copyProperties(client, vo);
		
		if(client.getCompany() != null) {
			vo.setCompany(companyMapper.convertToVO(client.getCompany()));	
		}
		
		return vo;
	}
}
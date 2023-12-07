package com.justfun.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.justfun.dto.ClientDTO;
import com.justfun.entity.Client;
import com.justfun.entity.Company;
import com.justfun.repository.ClientRepository;
import com.justfun.repository.CompanyRepository;

@Service
public class ClientService {

	@Autowired
	private ClientMapper clientMapper;
	
	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private CompanyRepository companyRepository;
	
	@Transactional
	public Client add(ClientDTO dto) {
		Client client = clientMapper.convertToEntity(dto);
		
		Integer companyId = dto.getBelongCompanyId();
		if(companyId != null) {
			Optional<Company> optCompany = companyRepository.findById(companyId);
			if(optCompany.isPresent()) {
				client.setCompany(optCompany.get());
			}
		}
		
		return clientRepository.save(client);
	}

	@Transactional
	public Client update(Integer clientId, ClientDTO dto) {
	  	Optional<Client> optClient = clientRepository.findById(clientId);
	  	if(!optClient.isPresent()) {
	  		throw new RuntimeException("fail to update client. client id not found");
	  	}
	  	
	  	Client client = optClient.get();
	  	BeanUtils.copyProperties(dto, client);
		
	  	boolean assignCompany = false;
	  	
	  	if(client.getCompany() == null) {
	  		if(dto.getBelongCompanyId() != null) {
	  			assignCompany = true;
	  		}
	  	}
	  	else if(client.getCompany().getId() != dto.getBelongCompanyId()) {
	  		assignCompany = true;
	  	}
	  	
	  	if(assignCompany) {
	  		Optional<Company> optCompany = companyRepository.findById(dto.getBelongCompanyId());
	  		if(!optCompany.isPresent()) {
	  			throw new RuntimeException("fail to update client. belong companyId not found.");
	  		}
	  		
	  		client.setCompany(optCompany.get());	
	  	}
	  	
	  	return clientRepository.save(client);
	  	
	}

	@Transactional
	public void delete(Integer clientId) {
	  	Optional<Client> optClient = clientRepository.findById(clientId);
	  	if(!optClient.isPresent()) {
	  		throw new RuntimeException("fail to delete client. client id not found");
	  	}
	  	
	  	clientRepository.delete(optClient.get());
	}

	@Transactional(readOnly = true)
	public List<Client> findAll() {
		return clientRepository.findAll();
	}
	
	@Transactional(readOnly = true)
	public Client findById(Integer clientId) {
		return clientRepository.findById(clientId).orElse(new Client());
	}

	
	@Component
	class ClientMapper{
		public Client convertToEntity(ClientDTO dto) {
			Client entity = new Client();
			BeanUtils.copyProperties(dto, entity);
			return entity;
		}
	}
	
}

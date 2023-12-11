package com.justfun.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.justfun.dto.BulkClientDTO;
import com.justfun.dto.ClientDTO;
import com.justfun.entity.Client;
import com.justfun.entity.Company;
import com.justfun.mapper.ClientMapper;
import com.justfun.repository.ClientRepository;
import com.justfun.repository.CompanyRepository;
import com.justfun.vo.BulkClientItemVO;
import com.justfun.vo.BulkClientVO;

@Service
public class ClientService {

	private static final Integer SUCCESS = 0;
	
	private static final Integer FAIL = 1;
	
	@Autowired
	private ClientMapper clientMapper;
	
	@Autowired
	private ClientRepository clientRepository;
	
	@Autowired
	private CompanyRepository companyRepository;

	@Transactional
	public Client add(ClientDTO dto) {
		validate(dto, true);
		
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
	public BulkClientVO add(BulkClientDTO dto) {
		Assert.isTrue(dto.getClients() != null, "empty clients");
		Assert.isTrue(dto.getClients().size() == dto.getTotal(), "number of clients not matched");
		AtomicInteger numOfSuccess = new AtomicInteger(0);
		AtomicInteger numOfFail = new AtomicInteger(0);
		
		BulkClientVO bulkClientVO = createBulkClientVO(dto);
		
		dto.getClients().forEach(clientDTO -> {
			BulkClientItemVO bulkClientItemVO = new BulkClientItemVO();
			
			try {
				Client newClient = clientMapper.convertToEntity(clientDTO);
				
				Optional<Company> optCompany = validateAndGetCompanyIfNeeded(clientDTO);
				if(optCompany.isPresent()) {
					newClient.setCompany(optCompany.get());
				}
				
				Client resClient = clientRepository.save(newClient);
				
				System.out.println("### added client: " + resClient.toString());
				
				numOfSuccess.incrementAndGet();
				bulkClientItemVO.setResult(SUCCESS);
				bulkClientItemVO.setBulkClientItemCellVO(clientMapper.convertToVO(resClient));
			} catch (Exception e) {
				numOfFail.incrementAndGet();
				bulkClientItemVO.setResult(FAIL);
				bulkClientItemVO.setMsg(getMsg(e));
			}
			
			bulkClientVO.getBulkClientItemVOs().add(bulkClientItemVO);
		});

		bulkClientVO.setNumOfSuccess(numOfSuccess.get());
		bulkClientVO.setNumOfFail(numOfFail.get());
		
		return bulkClientVO;
	}

	private BulkClientVO createBulkClientVO(BulkClientDTO dto) {
		BulkClientVO bulkClientVO = new BulkClientVO();
		bulkClientVO.setTotal(dto.getTotal());
		bulkClientVO.setBulkClientItemVOs(new ArrayList<>());
		
		return bulkClientVO;
	}
	
	// TODO: get enough message from related exception
	private String getMsg(Exception e) {
		return e.getMessage();
	}
	
	private Optional<Company> validateAndGetCompanyIfNeeded(ClientDTO clientDTO) throws Exception {
        validate(clientDTO, true);
		
		Integer belongCompanyId = clientDTO.getBelongCompanyId();
		
		if(belongCompanyId == null) {
			return Optional.empty();
		}
		
		Optional<Company> optCompany = companyRepository.findById(belongCompanyId);
		if(!optCompany.isPresent()) {
			throw new RuntimeException("company not found with the id: " + belongCompanyId); //TODO: redefine
		}
		
		return optCompany;
	}
	
	@Transactional
	public Client update(Integer clientId, ClientDTO dto) {
		validate(dto, false);
		
	  	Optional<Client> optClient = clientRepository.findById(clientId);
	  	if(!optClient.isPresent()) {
	  		throw new RuntimeException("fail to update client. client id not found");
	  	}
	  	
	  	Client client = optClient.get();
	  	BeanUtils.copyProperties(dto, client);
		
	    int assignCompany = 0;
	  	
	  	if(client.getCompany() == null) {
	  		if(dto.getBelongCompanyId() != null) {
	  			assignCompany = 1;
	  		}
	  	}
	  	else {
	  		if(dto.getBelongCompanyId() == null) {
	  			assignCompany = -1;
	  		}
	  		else if(client.getCompany().getId() != dto.getBelongCompanyId()) {
	  			assignCompany = 1;
	  		}
	  	}
	  	
	  	if(assignCompany == 1) {
	  		Optional<Company> optCompany = companyRepository.findById(dto.getBelongCompanyId());
	  		if(!optCompany.isPresent()) {
	  			throw new RuntimeException("fail to update client. belong companyId not found.");
	  		}
	  		
	  		client.setCompany(optCompany.get());
	  	}
	  	else if(assignCompany == -1) {
	  		client.setCompany(null);
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
		return clientRepository.findById(clientId).orElse(null);
	}
	
	private void validate(ClientDTO dto, boolean create) {
		Assert.isTrue(!StringUtils.isBlank(dto.getName()), "empty name");
		Assert.isTrue(!StringUtils.isBlank(dto.getPhone()), "empty phone");
		Assert.isTrue(!StringUtils.isBlank(dto.getEmail()), "empty email");
		
		int checkNum = create ? 0 : 1;
		
		
		if(clientRepository.countByPhone(dto.getPhone()) > checkNum) {
			throw new RuntimeException("duplicated phone");
		}
		
		if(clientRepository.countByEmail(dto.getEmail()) > checkNum) {
			throw new RuntimeException("duplicated email");
		}
	
	}
	
}

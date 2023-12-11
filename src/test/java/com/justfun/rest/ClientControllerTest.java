package com.justfun.rest;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.justfun.dto.BulkClientDTO;
import com.justfun.dto.ClientDTO;
import com.justfun.dto.CompanyDTO;
import com.justfun.entity.Company;
import com.justfun.vo.BulkClientVO;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ClientControllerTest extends BaseTest{
	/**
	 * 多筆客戶資料新增，整合測試
	 */
	
	private static Boolean _init = false;
	
	private static String _token;
	
	@BeforeEach
	private void init() throws Exception {
		if(!_init) {
			_token = loginAndGetToken("ray", "ray");
			addTwoCompanies();
			_init = true;
		}
	}
	
    @Test
    public void bulkAddWithAllNormalDataTest() throws Exception {
    	System.out.println("### fourClientSuccessBulkAddTest");
    	
    	List<Company> companies = getCompanies();
    	
    	BulkClientDTO dto = new BulkClientDTO();
    	dto.setClients(new ArrayList<>());
    	dto.getClients().add(new ClientDTO("client1", "client1@gmail.com", "0911111111", companies.get(0).getId()));
    	dto.getClients().add(new ClientDTO("client2", "client2@gmail.com", "0911111112", companies.get(1).getId()));
    	dto.getClients().add(new ClientDTO("client3", "client3@gmail.com", "0911111113", null));
    	dto.getClients().add(new ClientDTO("client4", "client4@gmail.com", "0911111114", null));
    	dto.setTotal(4);

    	MvcResult result = mvc.perform(
      	      addJWTToken(MockMvcRequestBuilders.post("/api/clients/bulkAdd"), _token)
  	    	      .contentType(MediaType.APPLICATION_JSON)
  	              .content(new Gson().toJson(dto))
             )
      	   .andExpect(status().isOk())
      	   .andReturn();
    	
    	MockHttpServletResponse mockHttpResponse = result.getResponse();
    	
    	BulkClientVO vo
     		= new Gson().fromJson(mockHttpResponse.getContentAsString(), BulkClientVO.class);
     	
    	System.out.println("vo: " + vo.toString());
    	
    	assertTrue(vo.getTotal() == 4);
     	assertTrue(vo.getNumOfSuccess() == 4);
     	assertTrue(vo.getNumOfFail() == 0);
    }

	@Test
    public void bulkAddWithDuplicatedDataAndGhostCompanyTest() throws Exception {
		System.out.println("### fourClientFailBulkAddTest");
		
		List<Company> companies = getCompanies();
		
    	BulkClientDTO dto = new BulkClientDTO();
    	dto.setClients(new ArrayList<>());
    	dto.getClients().add(new ClientDTO("client11", "client11@gmail.com", "0933111111", companies.get(0).getId()));
    	
    	// fail. duplicated email
    	dto.getClients().add(new ClientDTO("client12", "client11@gmail.com", "0933111112", null));
    	
    	// fail. duplicated phone
    	dto.getClients().add(new ClientDTO("client13", "client13@gmail.com", "0933111111", null));
    	
    	// fail. company not found with the id: 3
    	dto.getClients().add(new ClientDTO("client14", "client14@gmail.com", "0933111114", 999));
    	
    	dto.getClients().add(new ClientDTO("client15", "client15@gmail.com", "0933111115", companies.get(0).getId()));
    	dto.setTotal(5);

    	MvcResult result = mvc.perform(
      	      addJWTToken(MockMvcRequestBuilders.post("/api/clients/bulkAdd"), _token)
  	    	      .contentType(MediaType.APPLICATION_JSON)
  	              .content(new Gson().toJson(dto))
             )
      	   .andExpect(status().isOk())
      	   .andReturn();
    	
    	MockHttpServletResponse mockHttpResponse = result.getResponse();
    	
    	BulkClientVO vo
     		= new Gson().fromJson(mockHttpResponse.getContentAsString(), BulkClientVO.class);
     	
    	System.out.println("vo: " + vo.toString());
    	
    	assertTrue(vo.getTotal() == 5);
     	assertTrue(vo.getNumOfSuccess() == 2);
     	assertTrue(vo.getNumOfFail() == 3);
    }
    
	private List<Company> addTwoCompanies() throws Exception, UnsupportedEncodingException {
		System.out.println("### addTwoCompanies");
		
		// add company
    	CompanyDTO dto = new CompanyDTO();
    	dto.setName("Google");
    	dto.setAddress("usa1");
        
        String json = new Gson().toJson(dto);
    	
    	mvc.perform(
    	      addJWTToken(MockMvcRequestBuilders.post("/api/companies/add"), _token)
	    	      .contentType(MediaType.APPLICATION_JSON)
	              .content(json)
           )
    	   .andExpect(status().isOk());
    	
    	
    	// add company
    	dto = new CompanyDTO();
    	dto.setName("Facebook");
    	dto.setAddress("usa2");
    	
        json = new Gson().toJson(dto);
    	
    	mvc.perform(
    	      addJWTToken(MockMvcRequestBuilders.post("/api/companies/add"), _token)
	    	      .contentType(MediaType.APPLICATION_JSON)
	              .content(json)
           )
    	   .andExpect(status().isOk());
    	
    	
    	MvcResult result = mvc.perform(
      	      addJWTToken(MockMvcRequestBuilders.get("/api/companies"), _token)
             )
      	   .andExpect(status().isOk())
      	   .andReturn();
    	
     	
     	MockHttpServletResponse mockHttpResponse = result.getResponse();
     	
     	Type listType = new TypeToken<List<Company>>(){}.getType();
     	
     	List<Company> companies
     		= new Gson().fromJson(mockHttpResponse.getContentAsString(), listType);
     	
     	assertTrue(companies.size() == 2);
     	
     	return companies;
	}
	
    private List<Company> getCompanies() throws Exception {
    	System.out.println("### getCompanies");
    	
    	MvcResult result = mvc.perform(
      	      addJWTToken(MockMvcRequestBuilders.get("/api/companies"), _token)
             )
      	   .andExpect(status().isOk())
      	   .andReturn();
     	
     	MockHttpServletResponse mockHttpResponse = result.getResponse();
     	
     	Type listType = new TypeToken<List<Company>>(){}.getType();
     	
     	List<Company> companies
     		= new Gson().fromJson(mockHttpResponse.getContentAsString(), listType);
     	
     	return companies;
	}
    
}

package com.justfun.rest;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.justfun.dto.CompanyDTO;
import com.justfun.entity.Company;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class CompanyControllerTest extends BaseTest{

	private static Boolean _init = false;
	
	private static String _token;
	
	@BeforeEach
	private void init() throws Exception {
		if(!_init) {
			_token = loginAndGetToken("ray", "ray");
			_init = true;
		}
	}
	
	@Test
	@Order(value = 1)
	public void addCompanySuccessTest() throws Exception {
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
    	
	}
	
	@Test
	@Order(value = 2)
	public void addCompanyWithoutNameTest() {
    	CompanyDTO dto = new CompanyDTO();
    	dto.setName("");
    	dto.setAddress("usa1");
        
        String json = new Gson().toJson(dto);
    	
        assertThrows(Exception.class, () -> {
           	mvc.perform(
          	      addJWTToken(MockMvcRequestBuilders.post("/api/companies/add"), _token)
      	    	      .contentType(MediaType.APPLICATION_JSON)
      	              .content(json)
                 );
        });
                
	}
	
	@Test
	@Order(value = 3)
	public void addCompanyWithoutAddressTest() {
    	CompanyDTO dto = new CompanyDTO();
    	dto.setName("Google");
    	dto.setAddress("");
        
        String json = new Gson().toJson(dto);
    	
        assertThrows(Exception.class, () -> {
           	mvc.perform(
          	      addJWTToken(MockMvcRequestBuilders.post("/api/companies/add"), _token)
      	    	      .contentType(MediaType.APPLICATION_JSON)
      	              .content(json)
                 );
        });
                
	}
	
	@Test
	@Order(value = 4)
	public List<Company> listAllCompanies_Test() throws UnsupportedEncodingException, Exception {
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
	
	@Test
	@Order(value = 5)
	public void findComanyById_Test() throws UnsupportedEncodingException, Exception {
		List<Company> companies = listAllCompanies_Test();
		Integer companyIdToFind = companies.get(0).getId();
		
    	MvcResult result = mvc.perform(
      	      	addJWTToken(
      	      			MockMvcRequestBuilders.get("/api/companies/" + companyIdToFind),
      	      			_token
      	      	)
             )
      	   .andExpect(status().isOk())
      	   .andReturn();
    	
     	
     	MockHttpServletResponse mockHttpResponse = result.getResponse();
     	Company company
     		= new Gson().fromJson(mockHttpResponse.getContentAsString(), Company.class);
     	
     	assertTrue(company != null);
     	assertTrue(companyIdToFind == company.getId());
	}
	
	@Test
	@Order(value = 6)
	public void findComanyByGhostId_FailTest() throws Exception {
		Integer companyIdToFind = 999;
		
    	MvcResult result = mvc.perform(
      	      	addJWTToken(
      	      			MockMvcRequestBuilders.get("/api/companies/" + companyIdToFind),
      	      			_token
      	      	)
             )
      	   .andExpect(status().isOk())
      	   .andReturn();
    	
     	
     	MockHttpServletResponse mockHttpResponse = result.getResponse();
     	Company company
     		= new Gson().fromJson(mockHttpResponse.getContentAsString(), Company.class);
     	
     	assertTrue(company == null);
	}
	
	@Test
	@Order(value = 7)
	public void modifyComanyAddress_Test() throws UnsupportedEncodingException, Exception {
		List<Company> companies = listAllCompanies_Test();
		Integer companyIdToUpdate = companies.get(0).getId();
		
		CompanyDTO dto = new CompanyDTO ();
		BeanUtils.copyProperties(companies.get(0), dto);
		dto.setAddress("taipei");
		
		String json = new Gson().toJson(dto);
		
    	MvcResult result = mvc.perform(
      	      	addJWTToken(
      	      			MockMvcRequestBuilders.put("/api/companies/" + companyIdToUpdate),
      	      			_token
      	      	)
      	      .contentType(MediaType.APPLICATION_JSON)
              .content(json)
             )
      	   .andExpect(status().isOk())
      	   .andReturn();
    	
    	
     	MockHttpServletResponse mockHttpResponse = result.getResponse();
     	Company company
     		= new Gson().fromJson(mockHttpResponse.getContentAsString(), Company.class);
     	
     	assertTrue(company != null);
     	assertTrue(company.getId() == companyIdToUpdate);
     	assertTrue(company.getAddress().equals("taipei"));
	}
	
	@Test
	@Order(value = 7)
	public void modifyComanyWithGhostId_FailTest() throws UnsupportedEncodingException, Exception {
		List<Company> companies = listAllCompanies_Test();
		// Integer companyIdToUpdate = companies.get(0).getId();
		Integer companyIdToUpdate = 999;
		
		CompanyDTO dto = new CompanyDTO ();
		BeanUtils.copyProperties(companies.get(0), dto);
		dto.setAddress("taipei");
		
		String json = new Gson().toJson(dto);
		
        assertThrows(Exception.class, () -> {
        	mvc.perform(
          	      	addJWTToken(
          	      			MockMvcRequestBuilders.put("/api/companies/" + companyIdToUpdate),
          	      			_token
          	      	)
          	      .contentType(MediaType.APPLICATION_JSON)
                  .content(json)
            );
        });
		
    	
	}

	@Test
	@Order(value = 8)
	public void deleteComanyWithId_Test() throws UnsupportedEncodingException, Exception {
		List<Company> companies = listAllCompanies_Test();
		Integer companyIdToDelete = companies.get(0).getId();
		
    	mvc.perform(
      	      	addJWTToken(
      	      			MockMvcRequestBuilders.delete("/api/companies/" + companyIdToDelete),
      	      			_token
      	      	)
             )
      	   .andExpect(status().isNoContent()); // 204
	}
	
	@Test
	@Order(value = 9)
	public void deleteComanyWithGhostId_FailTest() throws Exception {
		Integer companyIdToDelete = 999;
		
        assertThrows(Exception.class, () -> {
        	mvc.perform(
        	      	addJWTToken(
        	      			MockMvcRequestBuilders.delete("/api/companies/" + companyIdToDelete),
        	      			_token
        	      	)
                );
        });
      	   
	}
	
}

package com.justfun.rest;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.google.gson.Gson;
import com.justfun.dto.CompanyDTO;
import com.justfun.vo.AccountVO;

public class BaseTest {
	
	@Autowired
	protected MockMvc mvc;
	
    protected final String loginAndGetToken(String username, String pasword) throws Exception {
    	MvcResult result 
		= mvc.perform(MockMvcRequestBuilders.post("/auth/login")
						  .contentType(MediaType.APPLICATION_FORM_URLENCODED)
						  .param("username", username)
						  .param("password", pasword))
  					      .andExpect(status().isMovedTemporarily()) // 302
  					      .andReturn();
	
		MockHttpServletResponse mockHttpResponse = result.getResponse();
		String token = mockHttpResponse.getHeader("authorization");
		return token;
    }
    
    protected final AccountVO authCheck(String token) throws Exception {
    	MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/auth/check");
    	if(!StringUtils.isEmpty(token)) {
    		addJWTToken(builder, token);
    	}

    	MvcResult result = mvc.perform(builder)
							  .andExpect(status().isOk())
							  .andReturn();
		
		MockHttpServletResponse mockHttpResponse = result.getResponse();
		String resJson = mockHttpResponse.getContentAsString();
		
		return new Gson().fromJson(resJson, AccountVO.class);
    }
    
    protected final MockHttpServletRequestBuilder addJWTToken(MockHttpServletRequestBuilder builder, String token) {
    	return builder.header("Authorization", "Bearer " + token);
    }
    
    // for company
    protected void createCompanyAccess(String token, ResultMatcher matcher, boolean throwException) throws Exception {
    	MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/api/companies/add")
																	  .contentType(MediaType.APPLICATION_JSON)
																	  .content("{}");
    	
    	performAndExpect(token, builder, matcher, throwException);
    }

	protected void findCompanyByIdAccess(String token, ResultMatcher matcher, boolean throwException) throws Exception {
	   	MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/api/companies/999");
    	performAndExpect(token, builder, matcher, throwException);
    }
    
    protected void findAllCompaniesAccess(String token, ResultMatcher matcher, boolean throwException) throws Exception {
	   	MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/api/companies");
    	performAndExpect(token, builder, matcher, throwException);
    }
    
    protected void modifyCompanyAccess(String token, ResultMatcher matcher, boolean throwException) throws Exception {
    	MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/api/companies/999")
																	  .contentType(MediaType.APPLICATION_JSON)
																	  .content("{}");
    	
    	performAndExpect(token, builder, matcher, throwException);
    }
    
    protected void deleteCompanyAccess(String token, ResultMatcher matcher, boolean throwException) throws Exception {
    	MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete("/api/companies/999");
    	performAndExpect(token, builder, matcher, throwException);
    }
    
    // for clients
    protected void createMultipleClientsAccess(String token, ResultMatcher matcher, boolean throwException) throws Exception {
    	MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/api/clients/bulkAdd")
														    		  .contentType(MediaType.APPLICATION_JSON)
																	  .content("{}");
    	
    	performAndExpect(token, builder, matcher, throwException);
    }
    
    protected void createClientAccess(String token, ResultMatcher matcher, boolean throwException) throws Exception {
    	MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/api/clients/add")
														    		  .contentType(MediaType.APPLICATION_JSON)
																	  .content("{}");
    	
    	performAndExpect(token, builder, matcher, throwException);
    }
    
    protected void findAllClientsAccess(String token, ResultMatcher matcher, boolean throwException) throws Exception {
	   	MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/api/clients");
    	performAndExpect(token, builder, matcher, throwException);
    }
    
    protected void findClientByIdAccess(String token, ResultMatcher matcher, boolean throwException) throws Exception {
	   	MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/api/clients/999");
    	performAndExpect(token, builder, matcher, throwException);
    }
    
    protected void modifyClientAccess(String token, ResultMatcher matcher, boolean throwException) throws Exception {
    	MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/api/clients/999")
																	  .contentType(MediaType.APPLICATION_JSON)
																	  .content("{}");
    	
    	performAndExpect(token, builder, matcher, throwException);
    }
    
    protected void deleteClientAccess(String token, ResultMatcher matcher, boolean throwException) throws Exception {
    	MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete("/api/clients/999");
    	performAndExpect(token, builder, matcher, throwException);
    }
    
    /// private /////
    private void performAndExpect(
						    		String token,
						    		MockHttpServletRequestBuilder builder,
						    		ResultMatcher matcher,
						    		boolean throwException
    ) throws Exception {
    	 assertTrue(!StringUtils.isEmpty(token));
    	
    	 addJWTToken(builder, token);
    	
    	if(matcher != null) {
    		mvc.perform(builder).andExpect(matcher);
    	}
    	else if(throwException) {
    		// 代表有權限
            assertThrows(Exception.class, () -> {
            	mvc.perform(builder);
            });
    	}else {
    		throw new IllegalArgumentException("parameters misused");
    	}
		
	}
    
}

package com.justfun.rest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.google.gson.Gson;
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
    
}

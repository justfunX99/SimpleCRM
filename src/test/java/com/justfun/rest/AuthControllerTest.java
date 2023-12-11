package com.justfun.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.justfun.vo.AccountVO;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest extends BaseTest{
	/**
	 * 系統登入與JWT驗證，單元測試
	 */
	
    @Test
    public void defaultAccountInfoTest() throws Exception {
    	AccountVO vo = authCheck(null);
        assertEquals("anonymousUser", vo.getUsername());
    }
    
    @Test
    public void InvalidJWTTokenTest() throws Exception {
    	String inValidTokenString = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
    	
    	MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/auth/check");
    	addJWTToken(builder, inValidTokenString);

    	mvc.perform(builder).andExpect(status().isUnauthorized()); // 401
    }
    
    @Test
    public void SuperUserAccountInfoTest() throws Exception {
    	String token = loginAndGetToken("ray", "ray");
    	assertTrue(!StringUtils.isEmpty(token), "jwt token not found.");
    	
    	AccountVO vo = authCheck(token);
        assertEquals("ray", vo.getUsername());
    }

    @Test
    public void ManagerAccountInfoTest() throws Exception {
    	String token = loginAndGetToken("may", "may");
    	assertTrue(!StringUtils.isEmpty(token), "jwt token not found.");
    	
    	AccountVO vo = authCheck(token);
        assertEquals("may", vo.getUsername());
    }
    
    @Test
    public void OperatorAccountInfoTest() throws Exception {
    	String token = loginAndGetToken("jay", "jay");
    	assertTrue(!StringUtils.isEmpty(token), "jwt token not found.");
    	
    	AccountVO vo = authCheck(token);
        assertEquals("jay", vo.getUsername());
    }
    
    @Test
    public void NonSystemUserLoginTest() throws Exception {
    	String username = "aaa";
    	String password = "aaa";
    	
		mvc.perform(MockMvcRequestBuilders.post("/auth/login")
						  .contentType(MediaType.APPLICATION_FORM_URLENCODED)
						  .param("username", username)
						  .param("password", password))
  					      .andExpect(status().isUnauthorized()); // 401
    }
    
}

package com.justfun.rest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OperatorAATest extends BaseTest {
	
	private static Boolean _init = false;
	
	private static String _token;
	
	@BeforeEach
	private void init() throws Exception {
		if(!_init) {
			_token = loginAndGetToken("jay", "jay");
			_init = true;
		}
	}
	
	// for companies
	@Test
	public void create_company_test() throws Exception {
		createCompanyAccess(_token, null, true);
	}
	
	@Test
	public void find_company_by_id_test() throws Exception {
		findCompanyByIdAccess(_token, status().isOk(), false);
	}
	
	@Test
	public void find_all_company_test() throws Exception {
		findAllCompaniesAccess(_token, status().isOk(), false);
	}

	@Test
	public void modify_company_test() throws Exception {
		modifyCompanyAccess(_token, status().isForbidden(), false);
	}
	
	@Test
	public void delete_company_test() throws Exception {
		deleteCompanyAccess(_token, status().isForbidden(), false);
	}
	
	// for clients
	@Test
	public void create_multiple_clients_test() throws Exception {
		createMultipleClientsAccess(_token, null, true);
	}
	
	@Test
	public void create_client_test() throws Exception {
		createClientAccess(_token,  null, true);
	}
	
	@Test
	public void find_client_by_id_test() throws Exception {
		findClientByIdAccess(_token, status().isOk(), false);
	}
	
	@Test
	public void find_all_clients_test() throws Exception {
		findAllClientsAccess(_token, status().isOk(), false);
	}
	
	@Test
	public void modify_client_test() throws Exception {
		modifyClientAccess(_token, status().isForbidden(), false);
	}
	
	@Test
	public void delete_client_test() throws Exception {
		deleteClientAccess(_token, status().isForbidden(), false);
	}
}

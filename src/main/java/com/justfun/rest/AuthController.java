package com.justfun.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.justfun.util.SecurityUtls;
import com.justfun.vo.AccountVO;

@RestController
public class AuthController {
	
	@GetMapping("/auth/check")
	public ResponseEntity<AccountVO> check() {
		return ResponseEntity.ok(SecurityUtls.getAccountInfo());
	}

}

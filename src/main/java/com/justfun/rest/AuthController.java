package com.justfun.rest;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.justfun.security.CustomUserDetail;

@RestController
public class AuthController {

	@GetMapping("/home")
	public String home(Authentication authentication) {
		return "welcome " + getUsername(authentication);
	}

	@PostMapping("/auth/login")
	public String login(Authentication authentication) {
		return "welcome " + getUsername(authentication);
	}
	
	private String getUsername(Authentication authentication) {
		if(authentication == null || authentication.getPrincipal() == null)
			return "nobody";
		
		if(authentication.getPrincipal() instanceof String) {
			return authentication.getPrincipal().toString();
		}
		
		if(authentication.getPrincipal() instanceof CustomUserDetail) {
			return ((CustomUserDetail)authentication.getPrincipal()).getUsername();
		}
		
		return "nobody";
	}

}

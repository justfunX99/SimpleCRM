package com.justfun.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class CustomUsernamePasswordAuthProvider implements AuthenticationProvider {

	@Autowired
	private CustomUserDetailService customUserDetailService;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = (String)authentication.getPrincipal();
		String password = (String)authentication.getCredentials();
		
		UserDetails userDetails = customUserDetailService.loadUserByUsername(username);
		
		// TODO: use passwordEncoder
		if(!userDetails.getPassword().equals(password)) {
			throw new BadCredentialsException("login failed.");
		}
		
		return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

	
}

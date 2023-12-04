package com.justfun.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
	private static final long serialVersionUID = -1610876509135658844L;
	
	private String token;
	
	public JwtAuthenticationToken(String token) {
		super(null);
		setAuthenticated(false);
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return null;
	}

}

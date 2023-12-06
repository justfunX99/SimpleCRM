package com.justfun.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;


public class CustomUsernamePasswordAuthFilter extends OncePerRequestFilter{
	
	private AuthenticationManager authenticationManager;
	
	public CustomUsernamePasswordAuthFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		Assert.hasText(username, "username not found");
		Assert.hasText(password, "password not found");
		
		Authentication authentication 
			= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		// Separate the process of generate JWT Token to the related successHandler
		
		filterChain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return !isPathLogin(request);
	}

	boolean isPathLogin(HttpServletRequest request) {
		return request.getRequestURI().indexOf("/auth/login") != -1;
	}
	
	
}

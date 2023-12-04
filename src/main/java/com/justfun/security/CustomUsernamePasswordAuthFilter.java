package com.justfun.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nimbusds.jose.JOSEException;

public class CustomUsernamePasswordAuthFilter extends OncePerRequestFilter{
	
	private AuthenticationManager authenticationManager;
	
	private JwtTokenHelper jwtTokenHelper;
	
	public CustomUsernamePasswordAuthFilter(AuthenticationManager authenticationManager, JwtTokenHelper jwtTokenHelper) {
		this.authenticationManager = authenticationManager;
		this.jwtTokenHelper = jwtTokenHelper;
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
		
		if (authentication.isAuthenticated()) {
	        try {
	            String token = jwtTokenHelper.generateToken(authentication);
	            Cookie jwtCookie = new Cookie("idToken", token);
	            jwtCookie.setSecure(true);
	            response.addCookie(jwtCookie);
	        } catch (JOSEException e) {
	        	System.err.println(e);
	        }
	    }
		
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

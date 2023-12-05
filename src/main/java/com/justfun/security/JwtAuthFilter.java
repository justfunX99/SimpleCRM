package com.justfun.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthFilter extends OncePerRequestFilter {

	private static final String BEARER = "Bearer ";

	private final AuthenticationManager authenticationManager;

	public JwtAuthFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String token = request.getHeader("Authorization");

		if (!StringUtils.hasText(token) || !token.contains(BEARER)) {
			filterChain.doFilter(request, response);
			return;
		}

		try {
			token = token.replaceFirst(BEARER, "");
			JwtAuthenticationToken authentication = new JwtAuthenticationToken(token);
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

			Authentication authenticationResult = authenticationManager.authenticate(authentication);

			SecurityContext context = SecurityContextHolder.createEmptyContext();
			context.setAuthentication(authenticationResult);
			SecurityContextHolder.setContext(context);

			filterChain.doFilter(request, response);
		} catch (AuthenticationException e) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
		}
	}

}
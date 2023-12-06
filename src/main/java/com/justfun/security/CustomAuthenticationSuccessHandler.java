package com.justfun.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.JOSEException;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	@Autowired
	private JwtTokenHelper jwtTokenHelper;
	
	public CustomAuthenticationSuccessHandler() {
		super.setDefaultTargetUrl("/swagger-ui/index.html");
	}
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
        try {
            String token = jwtTokenHelper.generateToken(authentication);
            response.addHeader("Authorization", token);
            
            handle(request, response, authentication);
        } catch (JOSEException e) {
        	System.err.println(e);
        }
		
	}

}

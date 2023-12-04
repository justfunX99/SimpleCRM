package com.justfun.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthProvider implements AuthenticationProvider {

    private final JwtTokenHelper jwtTokenHelper;

    public JwtAuthProvider(JwtTokenHelper jwtTokenHelper) {
        this.jwtTokenHelper = jwtTokenHelper;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken bearer = (JwtAuthenticationToken) authentication;
        String token = bearer.getToken();

        try {
            return jwtTokenHelper.verifyToken(token);
        } catch (Exception e) {
            throw new JwtAuthException(String.format("Could not verify token %s", token), e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
    
}  
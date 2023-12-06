package com.justfun.util;

import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import com.justfun.vo.AccountVO;

public class SecurityUtls {
	
    private SecurityUtls() {
    	
    }
	
    public static Authentication getAuthentication() {
    	return SecurityContextHolder.getContext().getAuthentication();
    }
    
	public static String getUsername() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(authentication != null) {
			if(authentication instanceof PreAuthenticatedAuthenticationToken) {
				if(authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
					return ((org.springframework.security.core.userdetails.User)authentication.getPrincipal()).getUsername();
				}
			}
			
			if(authentication instanceof AnonymousAuthenticationToken) {
				return authentication.getPrincipal().toString();
			}
		}
		
		return "unknown people";
	}
	
	public static AccountVO getAccountInfo() {
		AccountVO res = new AccountVO();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(authentication != null) {
			if(authentication instanceof PreAuthenticatedAuthenticationToken) {
				User user = (User)((PreAuthenticatedAuthenticationToken)authentication).getPrincipal();
				res = new AccountVO(
						user.getUsername(),
						user.getAuthorities()
							.stream()
							.map(simpleGA -> simpleGA.getAuthority())
							.collect(Collectors.toList())
					);
			}
			else if(authentication instanceof AnonymousAuthenticationToken) {
				res = new AccountVO(
						authentication.getPrincipal().toString(),
						Collections.emptyList()
					);
			}
		}
		
		return res;
	}

}

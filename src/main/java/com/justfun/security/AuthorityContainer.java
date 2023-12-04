package com.justfun.security;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;


@Component("A")
public class AuthorityContainer {
	public static final String VIEW_COMPANY = "VIEW_COMPANY";
	public static final String CREATE_COMPANY = "CREATE_COMPANY";
	public static final String MODIFY_COMPANY = "MODIFY_COMPANY";
	public static final String DELETE_COMPANY = "DELETE_COMPANY";
	
	public List<String> getAuthoritie(RoleEnum role){
		Stream<String> resStream = Stream.empty();
		
		switch(role) {
			case SUPER_USER:
				resStream = Stream.of(VIEW_COMPANY, CREATE_COMPANY, MODIFY_COMPANY, DELETE_COMPANY);
				break;
			case MANAGER:
				resStream = Stream.of(VIEW_COMPANY, MODIFY_COMPANY, DELETE_COMPANY);
				break;
			case OPERATOR:
				resStream = Stream.of(VIEW_COMPANY, CREATE_COMPANY);
				break;
			default:
		}
		
		return resStream.collect(Collectors.toList());
	}
}

package com.justfun.security;

public enum RoleEnum {
	SUPER_USER("SUPER_USER"),
	MANAGER("MANAGER"),
	OPERATOR("OPERATOR"),
	;
	
	RoleEnum(String value) {
		this.value = value;
	}

	private final String value; // not used yet
}

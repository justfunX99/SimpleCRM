package com.justfun.security;

import org.springframework.security.core.AuthenticationException;

public class JwtAuthException extends AuthenticationException{

	private static final long serialVersionUID = -4763223447902052860L;

	public JwtAuthException(String msg, Throwable cause) {
		super(msg, cause);
	}

}

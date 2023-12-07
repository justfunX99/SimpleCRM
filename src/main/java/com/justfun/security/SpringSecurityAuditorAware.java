package com.justfun.security;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import com.justfun.util.SecurityUtls;

@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		return Optional.of(SecurityUtls.getAccountInfo().getUsername());
	}

}
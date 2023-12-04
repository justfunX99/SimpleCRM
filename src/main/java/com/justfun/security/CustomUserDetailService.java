package com.justfun.security;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.justfun.entity.User;
import com.justfun.repository.UserRepository;

@Component
public class CustomUserDetailService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;
 
	@Autowired
	private AuthorityContainer authorityContainer;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> optUser = userRepository.findByUsername(username);
		
		if (!optUser.isPresent()) {
			throw new UsernameNotFoundException("login failed.");
		}
		
		User user = optUser.get();
		
		return new CustomUserDetail(
				user.getUsername(), 
				user.getPassword(), 
				user.getRole().name(), 
				getAuthorities(user.getRole())
		);
	}

	private Set<SimpleGrantedAuthority> getAuthorities(RoleEnum role) {
		return authorityContainer.getAuthoritie(role)
				.stream()
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toSet());
	}
	
}

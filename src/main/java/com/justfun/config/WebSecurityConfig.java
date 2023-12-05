package com.justfun.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.justfun.security.CustomUsernamePasswordAuthFilter;
import com.justfun.security.CustomUsernamePasswordAuthProvider;
import com.justfun.security.JwtAuthFilter;
import com.justfun.security.JwtAuthProvider;
import com.justfun.security.JwtTokenHelper;
import com.nimbusds.jose.JOSEException;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
		
	@Value("${security.token.key}")
	private String kid;
	
	@Autowired
	private CustomUsernamePasswordAuthProvider customUsernamePasswordAuthProvider;
	
	@Autowired
	private JwtAuthProvider jwtAuthProvider;
	
	@Bean
	public JwtTokenHelper jwtTokenHelper() throws JOSEException {
		return new JwtTokenHelper(kid);
	}
	
	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	// got problems before adding @Autowired.
	// customized providers not found in parent AuthenticationManager, and only DaoAuthenticationProvider found
	@Autowired
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(customUsernamePasswordAuthProvider);
		auth.authenticationProvider(jwtAuthProvider);
		super.configure(auth);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
	        .antMatchers(HttpMethod.GET, "/home", "/h2", "/h2-console", "/admin").permitAll()
//	        .antMatchers(HttpMethod.GET, "/swagger-ui/**", "/swagger-resources/*", "/v2/api-docs", "/v2/api-docs/**").permitAll()
	        .antMatchers(HttpMethod.POST, "/auth/login").permitAll()
//	        .antMatchers(HttpMethod.GET, "/swagger-ui").permitAll()
	        .anyRequest().authenticated()
	        .and()
	        .httpBasic();
		
		http.sessionManagement((session) -> {
			session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		});
		
		http.csrf().disable();
		
		http.addFilterAt(
			new CustomUsernamePasswordAuthFilter(
				authenticationManagerBean(),
				jwtTokenHelper()
			),
			BasicAuthenticationFilter.class
		);
		
		http.addFilterBefore(
			new JwtAuthFilter(
				authenticationManagerBean()
			),
			BasicAuthenticationFilter.class
		);
		
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/v2/api-docs",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/webjars/**"
        );
	}
	
	

}

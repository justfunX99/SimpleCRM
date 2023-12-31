package com.justfun.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.justfun.security.CustomAuthenticationFailureHandler;
import com.justfun.security.CustomAuthenticationSuccessHandler;
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
	
	// TODO: user multiple WebSecurityConfigurerAdapter for form login and logic API
	
//	@Value("${security.token.key}")
//	private String kid;
	
	@Lazy
	@Autowired
	private CustomUsernamePasswordAuthProvider customUsernamePasswordAuthProvider;
	
	@Lazy
	@Autowired
	private JwtAuthProvider jwtAuthProvider;
	
	@Autowired
	private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
	
	@Autowired
	private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
	
//	@Bean
//	public JwtTokenHelper jwtTokenHelper() throws JOSEException {
//		return new JwtTokenHelper(kid);
//	}
	
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
		auth.authenticationProvider(jwtAuthProvider);
		auth.authenticationProvider(customUsernamePasswordAuthProvider);
		super.configure(auth);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf()
			.disable()
			.httpBasic()
			.and()
	        .formLogin()
	        .loginProcessingUrl("/auth/login")
	        .successHandler(customAuthenticationSuccessHandler)
	        .failureHandler(customAuthenticationFailureHandler)
			.and()
			.authorizeRequests()
	        .antMatchers(HttpMethod.GET, "/auth/check", "/h2", "/h2-console", "/admin").permitAll()
	        .antMatchers("/auth/login").permitAll()
	        .anyRequest()
	        .authenticated()
	        ;
		
		http.sessionManagement((session) -> {
			session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		});
		
		http.addFilterAt(
			new CustomUsernamePasswordAuthFilter(
				authenticationManagerBean()
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
            "/webjars/**",
            "/h2/**"
        );
	}
	
	

}

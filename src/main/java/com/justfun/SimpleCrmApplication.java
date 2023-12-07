package com.justfun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
public class SimpleCrmApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleCrmApplication.class, args);
	}

}

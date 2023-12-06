package com.justfun.config;

import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.spi.service.contexts.SecurityContext;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SpringFoxConfig {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any()).build().securityContexts(securityContext())
				.securitySchemes(securitySchemes());

	}

	private List<SecurityScheme> securitySchemes() {
		return Collections.singletonList(new ApiKey("JWT", "Authorization", "header"));
	}

	private List<SecurityContext> securityContext() {
		SecurityContext securityContext = SecurityContext.builder().securityReferences(defaultAuth()).build();

		return Collections.singletonList(securityContext);
	}

	private List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] arr = new AuthorizationScope[1];
		arr[0] = authorizationScope;

		return Collections.singletonList(new SecurityReference("JWT", arr));
	}

}

package com.cadaverback.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	private final CorsConfigurator corsConfigurator;
	private final AuthorizeRequestsConfigurator authorizeRequestsConfigurator;

	public WebSecurityConfig(CorsConfigurator corsConfigurator, AuthorizeRequestsConfigurator authorizeRequestsConfigurator) {
		this.corsConfigurator = corsConfigurator;
		this.authorizeRequestsConfigurator = authorizeRequestsConfigurator;
    }

//	@Bean
//	public JwtDecoder jwtDecoder() {
//		// Replace "your-issuer-uri" with the actual issuer URI of your JWT provider
//		return NimbusJwtDecoder.withJwkSetUri("http://localhost/.well-known/jwks.json").build();
//	}

	// https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.cors(this.corsConfigurator::configure)
				.authorizeHttpRequests(authorizeRequestsConfigurator::configure)
				.oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()))
				.sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}
}

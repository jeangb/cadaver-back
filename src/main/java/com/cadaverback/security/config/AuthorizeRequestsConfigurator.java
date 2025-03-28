package com.cadaverback.security.config;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;


@Component
public class AuthorizeRequestsConfigurator {
    public AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorizeRequests) {
        return authorizeRequests
                // don't authenticate this particular request
                .requestMatchers(HttpMethod.POST, "/authenticate", "/register").permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/phrases", "/api/phrases/generaterandom").permitAll()
                // all other requests need to be authenticated
                .anyRequest().authenticated();
    }
}

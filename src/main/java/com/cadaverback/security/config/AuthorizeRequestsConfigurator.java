package com.cadaverback.security.config;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class AuthorizeRequestsConfigurator {
    public AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorizeRequests) {
        return authorizeRequests
                // dont authenticate this particular request
                .antMatchers(HttpMethod.POST, "/authenticate", "/register").permitAll().antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/phrases", "/api/phrases/generaterandom").permitAll()
                // all other requests need to be authenticated
                .anyRequest().authenticated();
    }
}

package com.cadaverback.security.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class CorsConfigurator {
    public CorsConfigurer<HttpSecurity> configure(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
        return httpSecurityCorsConfigurer.configurationSource(this::defineCors);
    }

    private CorsConfiguration defineCors(HttpServletRequest httpServletRequest) {
        final List<String> corsOriginAutorizedUrls = Stream.of("http://localhost:4200", "http://localhost:8080").collect(Collectors.toList());
        final List<String> corsOriginAutorizedMethods = Stream.of("GET", "POST", "PUT", "DELETE", "OPTIONS").collect(Collectors.toList());
        final List<String> corsOriginAutorizedHeaders = Stream.of("*").collect(Collectors.toList());

        final CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowedOrigins(corsOriginAutorizedUrls);
        cors.setAllowedMethods(corsOriginAutorizedMethods);
        cors.setAllowedHeaders(corsOriginAutorizedHeaders);
        cors.setAllowCredentials(true);
        return cors;
    }
}

package com.cadaverback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:4200") // utile ?
@ComponentScan(basePackages = { "com.cadaverback.*" }) // utile ?
@SpringBootApplication
public class CadaverBackApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(CadaverBackApplication.class, args);
    }

    // @Autowired
    // @Bean
    // public SecurityWebFilterChain chain(ServerHttpSecurity http, AuthenticationWebFilter webFilter)
    // {
    // return http.authorizeExchange().anyExchange().permitAll().and().csrf().disable().build();
    // }

}

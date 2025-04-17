package com.example.Tutorials_Eom.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // ปิด CSRF
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // อนุญาตทุก request
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/404") // หากไม่ได้รับอนุญาต
                );

        return http.build();
    }
}

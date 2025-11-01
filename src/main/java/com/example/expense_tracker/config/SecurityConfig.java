package com.example.expense_tracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        // Tell it to allow all requests that start with /api/
                        .requestMatchers("/api/**").permitAll()
                        // All other requests (like /login) can still be authenticated
                        .anyRequest().authenticated()
                )
                // This is critical for Postman to work
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}

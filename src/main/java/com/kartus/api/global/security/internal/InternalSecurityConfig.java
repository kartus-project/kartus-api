package com.kartus.api.global.security.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tools.jackson.databind.ObjectMapper;

@Configuration
@RequiredArgsConstructor
public class InternalSecurityConfig {
    private final InternalTokenValidator internalTokenValidator;
    private final ObjectMapper objectMapper;

    @Bean
    @Order(1)
    public SecurityFilterChain internalFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/internal", "/internal/**")
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .anonymous(AbstractHttpConfigurer::disable)
                .requestCache(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .addFilterBefore(
                        new InternalAuthFilter(internalTokenValidator, objectMapper),
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}

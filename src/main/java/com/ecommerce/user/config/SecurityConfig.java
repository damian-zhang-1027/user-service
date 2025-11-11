package com.ecommerce.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * This configuration is essential for the User Service, even with an API
 * Gateway:
 * 1. It provides the PasswordEncoder (BCrypt) Bean required by the
 * RegistrationService.
 * 2. It defines the security policy for endpoints *within* this service,
 * e.g., permitting public access to /register and /swagger-ui.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Provides the PasswordEncoder Bean for password hashing.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * This is the "default" security filter chain for our application APIs.
     * It handles endpoints like /api/v1/users/register.
     */
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF as we will be using JWTs (stateless)
                .csrf(AbstractHttpConfigurer::disable)

                // Define authorization rules
                .authorizeHttpRequests(authorize -> authorize
                        // Allow public access to the registration API
                        .requestMatchers("/api/v1/users/register").permitAll()

                        // Allow public access to API documentation (Swagger)
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/api-docs/**").permitAll()

                        // All other application requests (if any) must be authenticated
                        .anyRequest().authenticated())

                // Configure session management to be STATELESS
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}

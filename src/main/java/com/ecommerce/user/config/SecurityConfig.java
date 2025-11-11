package com.ecommerce.user.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * This configuration:
 * 1. Provides PasswordEncoder Bean (for Registration).
 * 2. Exposes AuthenticationManager Bean (for Login).
 * 3. Configures the *only* security filter chain.
 */
@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityConfig {

    /**
     * Provides the PasswordEncoder Bean for password hashing.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Exposes the AuthenticationManager as a Bean.
     * Our custom /login service will use this.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * The *only* Security Filter Chain for the User Service.
     */
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                // Disable CSRF as we will be using JWTs (stateless)
                .csrf(AbstractHttpConfigurer::disable)

                // Configure session management to be STATELESS
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Define authorization rules
                .authorizeHttpRequests(authorize -> authorize
                        // Allow public access to Registration API
                        .requestMatchers("/api/v1/users/register").permitAll()

                        // Allow public access to Login API
                        .requestMatchers("/api/v1/users/login").permitAll()

                        // Allow public access to API documentation
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/api-docs/**").permitAll()

                        // Allow public access to the JWKS endpoint for API Gateway
                        .requestMatchers("/.well-known/jwks.json").permitAll()

                        // All other requests are denied
                        .anyRequest().authenticated());

        return http.build();
    }
}

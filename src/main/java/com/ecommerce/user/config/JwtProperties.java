package com.ecommerce.user.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Type-safe configuration for JWT properties
 */
@Validated
@ConfigurationProperties(prefix = "ecommerce.jwt")
public record JwtProperties(
        @NotBlank String issuerUrl,
        @NotNull Long expirationSec) {
}

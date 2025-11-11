package com.ecommerce.user.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;

/**
 * Maps environment variables (from .env) to a type-safe configuration object.
 */
@ConfigurationProperties(prefix = "ecommerce.security")
@Validated
public record SecurityProperties(
                @NotBlank String issuerUrl,
                @NotBlank String clientId,
                @NotBlank String clientSecret) {
}

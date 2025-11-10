package com.ecommerce.user.controller.registration.dto;

import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Response DTO for successful registration.
 */
@Schema(description = "User Registration Success Response DTO")
public record UserResponse(
                @Schema(description = "User ID", example = "123") Long id,
                @Schema(description = "Email", example = "test.user@example.com") String email,
                @Schema(description = "Display Name", example = "Test User") String displayName,
                @Schema(description = "List of assigned roles", example = "[\"ROLE_BUYER_USER\", \"ROLE_SELLER_ADMIN\"]") Set<String> roles) {
}

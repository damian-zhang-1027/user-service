package com.ecommerce.user.controller.registration.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for user registration.
 */
@Schema(description = "User Registration Request DTO")
public record RegistrationRequest(
                @Schema(description = "Login Email", example = "test.user@example.com") @NotBlank(message = "Email cannot be blank") @Email(message = "Invalid email format") @Size(max = 255, message = "Email must be less than 255 characters") String email,
                @Schema(description = "Login Password (min 8 chars)", example = "Password123!") @NotBlank(message = "Password cannot be blank") @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters") String password,
                @Schema(description = "Display Name", example = "Test User") @NotBlank(message = "Display name cannot be blank") @Size(max = 100, message = "Display name must be less than 100 characters") String displayName) {
}

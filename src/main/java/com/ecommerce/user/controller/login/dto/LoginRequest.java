package com.ecommerce.user.controller.login.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "User Login Request DTO")
public record LoginRequest(
        @Schema(description = "Login Email", example = "test.user@example.com") @NotBlank @Email String email,
        @Schema(description = "Login Password", example = "Password123!") @NotBlank String password) {
}

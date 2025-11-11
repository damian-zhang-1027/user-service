package com.ecommerce.user.controller.login.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User Login Success Response DTO (JWT)")
public record LoginResponse(
        @Schema(description = "JWT Access Token", example = "eyJh...") String accessToken,
        @Schema(description = "Token Type", example = "Bearer") String tokenType) {
    public LoginResponse(String accessToken) {
        this(accessToken, "Bearer");
    }
}

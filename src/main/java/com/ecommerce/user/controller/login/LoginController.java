package com.ecommerce.user.controller.login;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.user.controller.login.dto.LoginRequest;
import com.ecommerce.user.controller.login.dto.LoginResponse;
import com.ecommerce.user.framework.response.GlobalResponse;
import com.ecommerce.user.service.login.LoginService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Provides the custom /api/v1/users/login endpoint.
 */
@Tag(name = "User Authentication", description = "User Login & Registration API")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    /**
     * Custom endpoint for user login.
     */
    @Operation(summary = "User Login", description = "Authenticates a user with email/password and returns a JWT.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login Successful", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalResponse.class))),
            @ApiResponse(responseCode = "401", description = "Authentication Failed (e.g., Bad Credentials)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalResponse.class)))
    })
    @PostMapping("/login")
    public GlobalResponse<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {
        return GlobalResponse.success(loginService.login(request));
    }
}

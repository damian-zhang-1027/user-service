package com.ecommerce.user.controller.registration;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.user.controller.registration.dto.RegistrationRequest;
import com.ecommerce.user.controller.registration.dto.UserResponse;
import com.ecommerce.user.framework.response.GlobalResponse;
import com.ecommerce.user.service.registration.RegistrationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Controller for the 'Registration' feature.
 * Handles API endpoints related to user registration.
 */
@Tag(name = "User Registration", description = "User Registration API")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @Operation(summary = "Register a new user", description = "Creates a new user account and automatically assigns BUYER and SELLER roles.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Invalid input (e.g., email format, password too short)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalResponse.class))),
            @ApiResponse(responseCode = "409", description = "Email already exists", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GlobalResponse.class)))
    })
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public GlobalResponse<UserResponse> registerUser(
            @Valid @RequestBody RegistrationRequest request) {
        UserResponse responseData = registrationService.registerUser(request);
        return GlobalResponse.success(responseData);
    }

}

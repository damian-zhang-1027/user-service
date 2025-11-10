package com.ecommerce.user.service.registration;

import com.ecommerce.user.controller.registration.dto.RegistrationRequest;
import com.ecommerce.user.controller.registration.dto.UserResponse;

public interface RegistrationService {

    /**
     * Registers a new user account.
     *
     * @param request The registration request DTO
     * @return The created user DTO
     */
    UserResponse registerUser(RegistrationRequest request);
}

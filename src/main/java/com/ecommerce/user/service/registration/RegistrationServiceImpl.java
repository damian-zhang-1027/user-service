package com.ecommerce.user.service.registration;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.user.constant.RoleName;
import com.ecommerce.user.controller.registration.dto.RegistrationRequest;
import com.ecommerce.user.controller.registration.dto.UserResponse;
import com.ecommerce.user.exception.EmailAlreadyExistsException;
import com.ecommerce.user.exception.RoleNotFoundException;
import com.ecommerce.user.model.db.entity.Role;
import com.ecommerce.user.model.db.entity.User;
import com.ecommerce.user.model.db.entity.UserRole;
import com.ecommerce.user.repository.db.RoleRepository;
import com.ecommerce.user.repository.db.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Registration Service Implementation.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder; // Injected from SecurityConfig

    /**
     * Implements the business logic for user registration.
     */
    @Override
    @Transactional // Ensures user and roles are saved in one transaction
    public UserResponse registerUser(RegistrationRequest request) {

        // 1. Check if email exists (HTTP 409)
        if (userRepository.existsByEmail(request.email())) {
            log.warn("Attempted to register with existing email: {}", request.email());
            throw new EmailAlreadyExistsException(request.email());
        }

        // 2. Fetch the required roles (BUYER and SELLER)
        Set<RoleName> requiredRoleNames = Set.of(RoleName.ROLE_BUYER_USER, RoleName.ROLE_SELLER_ADMIN);
        Set<Role> rolesToAssign = roleRepository.findByNameIn(requiredRoleNames);

        // 2a. Validate that the roles exist in the database (system integrity check)
        if (rolesToAssign.size() != requiredRoleNames.size()) {
            RoleName missingRole = requiredRoleNames.stream()
                    .filter(roleName -> rolesToAssign.stream().noneMatch(role -> role.getName().equals(roleName)))
                    .findFirst()
                    .orElse(RoleName.ROLE_BUYER_USER); // Fallback for error message

            log.error("System configuration error: Role '{}' not found in database.", missingRole);
            throw new RoleNotFoundException(missingRole);
        }

        // 3. Create new User entity and hash the password
        User user = new User(
                request.email(),
                passwordEncoder.encode(request.password()),
                request.displayName());

        // 4. We *can* save it all at once if the User entity manages the relationship.
        // We will *not* save User first. We let Cascade.ALL handle it.
        Set<UserRole> userRoles = rolesToAssign.stream()
                .map(role -> new UserRole(user, role)) // UserRole constructor sets up the association
                .collect(Collectors.toSet());
        user.setUserRoles(userRoles);

        // 5. Save the User. Cascade.ALL will save the UserRoles.
        User savedUser = userRepository.save(user);

        log.info("New user registered successfully. User ID: {}, Email: {}", savedUser.getId(), savedUser.getEmail());

        // 6. Map the entity to the response DTO
        return mapToUserResponse(savedUser);
    }

    /**
     * Helper method to map User entity to UserResponse DTO.
     */
    private UserResponse mapToUserResponse(User user) {
        // Fetch role names from the (EAGER-loaded) collection
        Set<String> roleNames = user.getUserRoles().stream()
                .map(userRole -> userRole.getRole().getName().name())
                .collect(Collectors.toSet());

        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getDisplayName(),
                roleNames);
    }
}

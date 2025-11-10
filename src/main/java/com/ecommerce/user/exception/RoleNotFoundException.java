package com.ecommerce.user.exception;

import com.ecommerce.user.constant.RoleName;

/**
 * Custom exception for HTTP 500 Internal Server Error.
 * Thrown if the system's required roles (e.g., BUYER) are not found in the DB.
 */
public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(RoleName roleName) {
        super("Required role not found in database: " + roleName.name());
    }
}

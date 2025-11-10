package com.ecommerce.user.exception;

/**
 * Custom exception for HTTP 409 Conflict.
 * Thrown when attempting to register an email that already exists.
 */
public class EmailAlreadyExistsException extends RuntimeException {
    public EmailAlreadyExistsException(String email) {
        super("Email already exists: " + email);
    }
}

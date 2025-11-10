package com.ecommerce.user.framework.response;

import java.util.Collections;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Standard HTTP Response Wrapper, as per company specification.
 *
 * @param ret_code Business status code (0 = success, -1 = failure)
 * @param data     The actual response payload
 * @param meta     Metadata (e.g., pagination, request ID)
 * @param msg      Error message (null on success)
 */
@Schema(description = "Standard API Response Format")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record GlobalResponse<T>(
        @Schema(description = "Business status code (0=success, -1=failure)", example = "0") int ret_code,
        @Schema(description = "Response payload") T data,
        @Schema(description = "Metadata (e.g., pagination, request ID)") Object meta,
        @Schema(description = "Error message (null on success)", example = "Email already exists") String msg) {

    /**
     * Creates a standard success response.
     */
    public static <T> GlobalResponse<T> success(T data) {
        return new GlobalResponse<>(0, data, null, null);
    }

    /**
     * Creates a standard success response with metadata.
     */
    public static <T> GlobalResponse<T> success(T data, Object meta) {
        return new GlobalResponse<>(0, data, meta, null);
    }

    /**
     * Creates a standard error response.
     * Per specification, data is an empty object {} on failure.
     */
    public static GlobalResponse<Object> error(String message) {
        return new GlobalResponse<>(-1, Collections.emptyMap(), null, message);
    }

    /**
     * Creates a standard error response with metadata.
     */
    public static GlobalResponse<Object> error(String message, Object meta) {
        return new GlobalResponse<>(-1, Collections.emptyMap(), meta, message);
    }
}

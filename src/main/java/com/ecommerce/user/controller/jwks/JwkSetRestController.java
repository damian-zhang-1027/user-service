package com.ecommerce.user.controller.jwks;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.jwk.JWKSet;

import lombok.RequiredArgsConstructor;

/**
 * Exposes the RSA public key in OIDC-compliant JWKS format
 * for API Gateway and other microservices to fetch.
 */
@RestController
@RequiredArgsConstructor
public class JwkSetRestController {

    // Inject the JWKSet Bean we created in JwtConfig
    private final JWKSet jwkSet;

    /**
     * This is the endpoint that API Gateway will call.
     * SecurityConfig has set this path to permitAll.
     */
    @GetMapping("/.well-known/jwks.json")
    public Map<String, Object> jwks() {
        // .toJSONObject() will automatically only include the public key,
        // and will never leak the private key.
        return this.jwkSet.toJSONObject();
    }
}

package com.ecommerce.user.config;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;

import com.ecommerce.user.model.db.entity.User;
import com.ecommerce.user.service.security.SecurityUser;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Configures the Spring Security OAuth2 Authorization Server.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(SecurityProperties.class)
public class AuthorizationServerConfig {

    private final SecurityProperties securityProperties;
    private final PasswordEncoder passwordEncoder;

    /**
     * Defines the "client" application that is allowed to request tokens from this
     * server.
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        log.info("Registering OAuth2 client: {}", securityProperties.clientId());

        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(securityProperties.clientId())
                // The client secret *must* be encoded, just like a user's password
                .clientSecret(passwordEncoder.encode(securityProperties.clientSecret()))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)

                // Enable the "Password" grant type
                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                // Enable the "Refresh Token" grant type
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)

                .build();

        return new InMemoryRegisteredClientRepository(registeredClient);
    }

    /**
     * Creates and stores the RSA key pair used to sign JWTs.
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = generateRsaKey();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    private static RSAKey generateRsaKey() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            return new RSAKey.Builder(publicKey)
                    .privateKey(privateKey)
                    .keyID(UUID.randomUUID().toString())
                    .build();
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to generate RSA key", ex);
        }
    }

    /**
     * Configures the Authorization Server's settings, like the issuer URL.
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer(securityProperties.issuerUrl())
                .build();
    }

    /**
     * Customizes the JWT claims.
     */
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer() {
        return (context) -> {
            // Check if the principal is our custom SecurityUser
            if (context.getPrincipal().getPrincipal() instanceof SecurityUser securityUser) {
                User user = securityUser.getUser();
                JwtClaimsSet.Builder claims = context.getClaims();

                // Set 'sub' claim to User ID
                claims.subject(user.getId().toString());

                // Add custom claims (optional but useful)
                claims.claim("userId", user.getId());
                claims.claim("displayName", user.getDisplayName());

                // Note: The 'authorities' claim is added automatically
                // because our SecurityUser returns them via getAuthorities().
            }
        };
    }

    /**
     * The main security filter chain for the Authorization Server endpoints
     * (e.g., /oauth2/token).
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        // Apply the default OAuth2 Authorization Server configuration
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                // Enable OIDC (OpenID Connect) if needed, for now, just OAuth2
                .oidc(configurer -> {
                });

        http
                // For the /oauth2/token endpoint, we use Basic Auth (client_id:client_secret)
                .httpBasic(config -> {
                })
                // We also need to configure the Resource Server part to decode JWTs
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);

        return http.build();
    }
}

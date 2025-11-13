package com.ecommerce.user.service.login;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.ecommerce.user.config.JwtProperties;
import com.ecommerce.user.controller.login.dto.LoginRequest;
import com.ecommerce.user.controller.login.dto.LoginResponse;
import com.ecommerce.user.service.security.SecurityUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implements the logic for 'POST /api/v1/users/login'.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    // Inject the AuthenticationManager bean from SecurityConfig
    private final AuthenticationManager authenticationManager;

    // Inject the JwtEncoder bean from JwtConfig
    private final JwtEncoder jwtEncoder;

    // Inject the JwtProperties bean from JwtConfig
    private final JwtProperties jwtProperties;

    @Override
    public LoginResponse login(LoginRequest request) {

        // Authenticate using UserDetailsServiceImpl and PasswordEncoder
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        log.info("User {} authenticated successfully", request.email());
        String token = generateJwtToken(authentication);
        return new LoginResponse(token);
    }

    /**
     * Creates a JWT using the JwtEncoder Bean.
     */
    private String generateJwtToken(Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();

        Instant now = Instant.now();
        long expiryInSeconds = jwtProperties.expirationSec();

        // Get authorities (roles) as a space-separated string
        List<String> authorities = securityUser.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String userId = securityUser.getUser().getId().toString();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                // Set 'iss' (Issuer) claim
                .issuer(jwtProperties.issuerUrl())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiryInSeconds))
                // Set 'sub' (Subject) claim to userId
                .subject(userId)
                // Set custom 'authorities' claim
                .claim("authorities", authorities)
                .claim("userId", userId)
                .build();

        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}

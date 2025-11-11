package com.ecommerce.user.service.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ecommerce.user.model.db.entity.User;

import lombok.Getter;

/**
 * A bridge between our application 'User' entity and Spring Security's
 * UserDetails.
 */
public class SecurityUser implements UserDetails {

    @Getter
    private final User user; // We store the original User entity
    private final Set<GrantedAuthority> authorities;

    public SecurityUser(User user) {
        this.user = user;

        // Convert UserRoles to GrantedAuthority (e.g., "ROLE_BUYER_USER")
        // This is crucial for the 'authorities' claim in the JWT.
        this.authorities = user.getUserRoles().stream()
                .map(userRole -> new SimpleGrantedAuthority(userRole.getRole().getName().name()))
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        // Delegate to our User entity
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        // Spring Security's 'username' is our 'email'
        return this.user.getEmail();
    }

    // --- Standard UserDetails methods ---
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

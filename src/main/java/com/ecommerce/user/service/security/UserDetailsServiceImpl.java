package com.ecommerce.user.service.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.user.repository.db.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * This service implements the logic for 'POST /oauth2/token'.
 * It finds the user by 'email' (which Spring Security calls 'username').
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Find the user by email
        return userRepository.findByEmail(email)
                .map(SecurityUser::new) // Convert our User to Spring's UserDetails
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }
}

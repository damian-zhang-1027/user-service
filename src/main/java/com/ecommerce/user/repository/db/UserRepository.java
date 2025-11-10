package com.ecommerce.user.repository.db;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.user.model.db.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their email.
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if an email already exists in the database.
     */
    boolean existsByEmail(String email);
}

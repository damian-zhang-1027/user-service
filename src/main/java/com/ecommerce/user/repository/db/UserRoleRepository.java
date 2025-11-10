package com.ecommerce.user.repository.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.user.model.db.entity.UserRole;
import com.ecommerce.user.model.db.entity.UserRoleId;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
    // This repository is used by JPA for cascading operations from the User entity.
    // Explicit methods are not required for the current registration feature.
}

package com.ecommerce.user.repository.db;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.user.constant.RoleName;
import com.ecommerce.user.model.db.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    /**
     * Finds a role by its Enum name.
     */
    Optional<Role> findByName(RoleName name);

    /**
     * Finds a set of roles by their Enum names.
     */
    Set<Role> findByNameIn(Set<RoleName> names);
}

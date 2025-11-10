package com.ecommerce.user.model.db.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Composite Primary Key class for the UserRole entity.
 * This maps to the composite PK (user_id, role_id) in user-service.md.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable // Mark as embeddable
public class UserRoleId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "role_id")
    private Integer roleId;
}

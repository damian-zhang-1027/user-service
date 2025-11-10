package com.ecommerce.user.model.db.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JPA Entity for the 'user_roles' join table.
 * This is the "Join Entity" that replaces @ManyToMany.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_roles")
public class UserRole {

    /**
     * Use the composite key.
     */
    @EmbeddedId
    private UserRoleId id;

    /**
     * Many-to-One mapping back to User.
     * 'MapsId' links the 'userId' field in UserRoleId to this association.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Many-to-One mapping back to Role.
     * 'MapsId' links the 'roleId' field in UserRoleId to this association.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roleId")
    @JoinColumn(name = "role_id")
    private Role role;

    public UserRole(User user, Role role) {
        this.user = user;
        this.role = role;
        // Create the composite key
        this.id = new UserRoleId(user.getId(), role.getId());
    }
}

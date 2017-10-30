package com.core.jpa.entity;

import com.common.dto.User;
import com.common.dto.SecurityRole;
import com.core.movie.UserMoviePermission;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Representation of the user.
 */
@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
public class UserEntity extends BaseEntity {

    private static final long serialVersionUID = -1588795025690250131L;

    @Id
    @Column(updatable = false)
    @GeneratedValue
    private Long id;

    @Column(length = 36, unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "activation_token")
    private String activationToken;

    @Column(name = "email_change_token")
    private String emailChangeToken;

    @Column(name = "new_email")
    private String newEmail;

    private boolean enabled;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name="users_authorities",
            joinColumns=@JoinColumn(name="user_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Set<SecurityRole> authorities;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name="users_movie_permissions",
            joinColumns=@JoinColumn(name="user_id")
    )
    @Enumerated(EnumType.STRING)
    private Set<UserMoviePermission> permissions;

    @CreatedDate
    @Column(name = "registration_date", updatable = false, nullable = false)
    private Date registrationDate;

    @LastModifiedDate
    @Column(name = "modified_date", nullable = false)
    private Date modifiedDate;



    /**
     * Get a DTO representing this user.
     *
     * @return The read-only DTO.
     */
    public User getDTO() {
        return User.builder()
                .id(this.id)
                .username(this.username)
                .email(this.email)
                .build();
    }
}

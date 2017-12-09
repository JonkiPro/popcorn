package com.core.jpa.entity;

import com.common.dto.User;
import com.common.dto.SecurityRole;
import com.core.jpa.entity.movie.MovieRateEntity;
import com.core.movie.UserMoviePermission;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Representation of the user.
 */
@Getter
@Setter
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class UserEntity extends BaseEntity {

    private static final long serialVersionUID = -1588795025690250131L;

    @Id
    @Basic(optional = false)
    @Column(unique = true, nullable = false, updatable = false)
    @GenericGenerator(
            name = "userSequenceGenerator", strategy = "enhanced-sequence",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "optimizer", value = "pooled-lo"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "5")
            }
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userSequenceGenerator")
    private Long id;

    @Basic(optional = false)
    @Column(length = 36, unique = true, nullable = false)
    private String username;

    @Basic(optional = false)
    @Column(unique = true, nullable = false)
    private String email;

    @Basic(optional = false)
    @Column(nullable = false)
    private String password;

    @Basic
    @Column(name = "activation_token")
    private String activationToken;

    @Basic
    @Column(name = "email_change_token")
    private String emailChangeToken;

    @Basic
    @Column(name = "new_email")
    private String newEmail;

    @Basic(optional = false)
    @Column(nullable = false)
    private boolean enabled;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "users_authorities",
            joinColumns = @JoinColumn(name = "user_id", nullable = false, updatable = false)
    )
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Set<SecurityRole> authorities;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "users_movie_permissions",
            joinColumns = @JoinColumn(name = "user_id", nullable = false, updatable = false)
    )
    @Enumerated(EnumType.STRING)
    private Set<UserMoviePermission> permissions;

    @OneToMany(mappedBy = "user")
    private Set<MovieRateEntity> ratings;

    @OneToMany(mappedBy = "user")
    private Set<ContributionEntity> contributions;

    @Basic(optional = false)
    @Column(name = "registration_date", updatable = false, nullable = false)
    @CreatedDate
    private Date registrationDate;

    @Basic
    @Column(name = "modified_date", nullable = false)
    @LastModifiedDate
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

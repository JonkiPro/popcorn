package com.core.jpa.entity;

import com.common.dto.SecurityRole;
import com.common.exception.ResourceConflictException;
import com.common.exception.ResourceNotFoundException;
import com.core.jpa.entity.movie.MovieRateEntity;
import com.common.dto.UserMoviePermission;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Representation of the user.
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

    private static final long serialVersionUID = -1588795025690250131L;

    @Basic(optional = false)
    @Column(name = "username", length = 36, unique = true, nullable = false)
    private String username;

    @Basic(optional = false)
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Basic(optional = false)
    @Column(name = "password", nullable = false)
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
    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "users_authorities",
            joinColumns = {
                    @JoinColumn(name = "user_id", nullable = false, updatable = false)
            }
    )
    @Column(name = "authority", nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private Set<SecurityRole> authorities;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MessageEntity> sentMessages;

    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MessageEntity> receivedMessages;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_friends",
            joinColumns = {
                    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, updatable = false)
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "user_friend_id", referencedColumnName = "id", nullable = false, updatable = false)
            }
    )
    private List<UserEntity> friends;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_sent_invitations",
            joinColumns = {
                    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, updatable = false)
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "user_invited_id", referencedColumnName = "id", nullable = false, updatable = false)
            }
    )
    private List<UserEntity> sentInvitations;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_received_invitations",
            joinColumns = {
                    @JoinColumn(name = "user_invited_id", referencedColumnName = "id", nullable = false, updatable = false)
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, updatable = false)
            }
    )
    private List<UserEntity> receivedInvitations;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "users_movie_permissions",
            joinColumns = {
                    @JoinColumn(name = "user_id", nullable = false, updatable = false)
            }
    )
    @Column(name = "permission", nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private Set<UserMoviePermission> permissions;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<MovieRateEntity> ratings;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<ContributionEntity> contributions;

    @Basic
    @Column(name = "modified_date", nullable = false)
    @LastModifiedDate
    private Date modifiedDate;

    /**
     * Default constructor.
     */
    public UserEntity() {
        super();
    }

    /**
     * Add a new user to these friends. Manages both sides of relationship.
     *
     * @param user The user to add. Not null.
     * @throws ResourceConflictException if the user is a duplicate of an existing friend or is an IDs conflict
     */
    public void addFriend(@NotNull final UserEntity user) throws ResourceConflictException {
        if (this.friends.contains(user)) {
            throw new ResourceConflictException("A user with id " + user.getId() + " is already added");
        } else if (this.getId().equals(user.getId())) {
            throw new ResourceConflictException("Conflict of IDs " + this.getId() + " : " + user.getId());
        }
        this.friends.add(user);
        user.getFriends().add(this);
    }

    /**
     * Remove a user from these friends. Manages both sides of relationship.
     *
     * @param user The user to remove. Not null.
     * @throws ResourceNotFoundException if the user is not added to friends
     */
    public void removeFriend(@NotNull final UserEntity user) throws ResourceNotFoundException {
        if (!this.friends.contains(user)) {
            throw new ResourceNotFoundException("A user with id " + user.getId() + " is not added");
        }
        this.friends.remove(user);
        user.getFriends().remove(this);
    }

    /**
     * Add a new user to these sent invitations. Manages both sides of relationship.
     *
     * @param user The user to add. Not null.
     * @throws ResourceConflictException if the user is a duplicate of an existing friend
     * or the user is added to friends or is an IDs conflict
     */
    public void addSentInvitation(@NotNull final UserEntity user) throws ResourceConflictException {
        if (this.sentInvitations.contains(user)) {
            throw new ResourceConflictException("A user with id " + user.getId() + " is already added");
        } else if (this.friends.contains(user)) {
            throw new ResourceConflictException("A user with id " + user.getId() + " is already added to friends");
        } else if (this.getId().equals(user.getId())) {
            throw new ResourceConflictException("Conflict of IDs " + this.getId() + " : " + user.getId());
        }
        this.sentInvitations.add(user);
        user.getReceivedInvitations().add(this);
    }

    /**
     * Remove a user from these sent invitations. Manages both sides of relationship.
     *
     * @param user The user to remove. Not null.
     * @throws ResourceNotFoundException if the user is not added to sent invitations
     */
    public void removeSentInvitation(@NotNull final UserEntity user) throws ResourceNotFoundException {
        if (!this.sentInvitations.contains(user)) {
            throw new ResourceNotFoundException("A user with id " + user.getId() + " is not added");
        }
        this.sentInvitations.remove(user);
        user.getReceivedInvitations().remove(this);
    }
}

package com.jonki.popcorn.core.jpa.entity;

import com.jonki.popcorn.common.dto.SecurityRole;
import com.jonki.popcorn.common.dto.StorageProvider;
import com.jonki.popcorn.common.dto.UserMoviePermission;
import com.jonki.popcorn.common.exception.ResourceConflictException;
import com.jonki.popcorn.common.exception.ResourceNotFoundException;
import com.jonki.popcorn.core.jpa.entity.movie.MovieRateEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.LastModifiedDate;

import javax.annotation.Nullable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Representation of the user.
 */
@Getter
@Setter
@ToString(
        callSuper = true,
        of = {
                "username",
                "email",
                "password",
                "activationToken",
                "emailChangeToken",
                "newEmail",
                "avatarId",
                "avatarProvider",
                "enabled",
                "modifiedDate"
        },
        doNotUseGetters = true
)
@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

    private static final long serialVersionUID = -7304519269103967289L;

    @Basic(optional = false)
    @Column(name = "username", length = 36, unique = true, nullable = false)
    @NotBlank
    @Size(min = 6, max = 36)
    private String username;

    @Basic(optional = false)
    @Column(name = "email", unique = true, nullable = false)
    @NotBlank
    @Email
    private String email;

    @Basic(optional = false)
    @Column(name = "password", nullable = false)
    @NotBlank
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

    @Basic
    @Column(name = "avatar_id_in_cloud")
    private String avatarId;

    @Basic
    @Column(name = "avatar_provider")
    @Enumerated(EnumType.STRING)
    private StorageProvider avatarProvider;

    @Basic(optional = false)
    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "users_authorities",
            joinColumns = {
                    @JoinColumn(name = "user_id", nullable = false, updatable = false)
            }
    )
    @Column(name = "authority", nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    @NotEmpty
    private Set<SecurityRole> authorities = new HashSet<>();

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MessageEntity> sentMessages = new ArrayList<>();

    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MessageEntity> receivedMessages = new ArrayList<>();

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
    private Set<UserEntity> friends = new HashSet<>();

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
    @OrderColumn(name = "invitation_order", nullable = false, updatable = false)
    private List<UserEntity> sentInvitations = new ArrayList<>();

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
    @OrderColumn(name = "invitation_order", nullable = false, updatable = false)
    private List<UserEntity> receivedInvitations = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "users_movie_permissions",
            joinColumns = {
                    @JoinColumn(name = "user_id", nullable = false, updatable = false)
            }
    )
    @Column(name = "permission", nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private Set<UserMoviePermission> permissions = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<MovieRateEntity> ratings = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<ContributionEntity> contributions = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_favorites_movies",
            joinColumns = {
                    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, updatable = false)
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "movie_id", referencedColumnName = "id", nullable = false, updatable = false)
            }
    )
    private Set<MovieEntity> favoritesMovies = new HashSet<>();

    @Basic
    @Column(name = "modified_date", nullable = false)
    @LastModifiedDate
    @Setter(AccessLevel.NONE)
    private Date modifiedDate;

    /**
     * Default constructor.
     */
    public UserEntity() {
        super();
    }

    /**
     * Get the user's avatar ID.
     *
     * @return The user's avatar ID
     */
    public Optional<String> getAvatarId() {
        return Optional.ofNullable(this.avatarId);
    }

    /**
     * Set all the authorities.
     *
     * @param authorities The dependency authorities to set
     */
    public void setAuthorities(@NotNull final Set<SecurityRole> authorities) {
        this.authorities.clear();
        this.authorities.addAll(authorities);
    }

    /**
     * Add a new user to these friends. Manages both sides of relationship.
     *
     * @param user The user to add. Not null.
     * @throws ResourceConflictException if the user is a duplicate of an existing friend or is an IDs conflict
     */
    public void addFriend(@NotNull final UserEntity user) throws ResourceConflictException {
        if (this.friends.contains(user)) {
            throw new ResourceConflictException("A user with id " + user.getUniqueId() + " is already added");
        } else if (this.getUniqueId().equals(user.getUniqueId())) {
            throw new ResourceConflictException("Conflict of IDs " + this.getUniqueId() + " : " + user.getUniqueId());
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
            throw new ResourceNotFoundException("A user with id " + user.getUniqueId() + " is not added");
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
            throw new ResourceConflictException("A user with id " + user.getUniqueId() + " is already added");
        } else if (this.friends.contains(user)) {
            throw new ResourceConflictException("A user with id " + user.getUniqueId() + " is already added to friends");
        } else if (this.getUniqueId().equals(user.getUniqueId())) {
            throw new ResourceConflictException("Conflict of IDs " + this.getUniqueId() + " : " + user.getUniqueId());
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
            throw new ResourceNotFoundException("A user with id " + user.getUniqueId() + " is not added");
        }
        this.sentInvitations.remove(user);
        user.getReceivedInvitations().remove(this);
    }

    /**
     * Set all the permissions.
     *
     * @param permissions The dependency permissions to set
     */
    public void setPermissions(@Nullable final Set<UserMoviePermission> permissions) {
        this.permissions.clear();
        if (permissions != null) {
            this.permissions.addAll(permissions);
        }
    }

    /**
     * Add a movie to your favourites list. Manages both sides of relationship.
     *
     * @param movie The movie to add. Not null.
     * @throws ResourceConflictException if the movie is added to favorites
     */
    public void addFavoriteMovie(@NotNull final MovieEntity movie) throws ResourceConflictException {
        if (this.favoritesMovies.contains(movie)) {
            throw new ResourceConflictException("A movie with id " + movie.getId() + " is already added to favorites");
        }
        this.favoritesMovies.add(movie);
        movie.setFavoriteCount(movie.getFavoriteCount()+1);
    }

    /**
     * Remove a movie from your favourites list. Manages both sides of relationship.
     *
     * @param movie The movie to remove. Not null.
     * @throws ResourceConflictException if the movie is not added to favorites
     */
    public void removeFavoriteMovie(@NotNull final MovieEntity movie) throws ResourceConflictException {
        if (!this.favoritesMovies.contains(movie)) {
            throw new ResourceConflictException("A movie with id " + movie.getId() + " is not added to favorites");
        }
        this.favoritesMovies.remove(movie);
        movie.setFavoriteCount(movie.getFavoriteCount()-1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object o) {
        return super.equals(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}

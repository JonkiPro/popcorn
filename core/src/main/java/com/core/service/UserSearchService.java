package com.core.service;

import com.common.dto.RelationshipStatus;
import com.common.dto.User;
import com.common.dto.search.UserSearchResult;
import com.common.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Nullable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * Interface for searching users.
 */
@Validated
public interface UserSearchService {

    /**
     * Search for users which match the given filter criteria. Null or empty parameters are ignored.
     *
     * @param username The user's name
     * @param page Page number
     * @return All the users matching the criteria
     */
    Page<UserSearchResult> findUsers(
            @Nullable final String username,
            @NotNull final Pageable page
    );

    /**
     * Get user by username.
     *
     * @param username The user's name
     * @return The user
     * @throws ResourceNotFoundException if no user found
     */
    User getUserByUsername(
            @NotBlank @Pattern(regexp = "[a-zA-Z0-9_-]{6,36}") final String username
    ) throws ResourceNotFoundException;

    /**
     * Get user by e-mail.
     *
     * @param email The user's e-mail
     * @return The user
     * @throws ResourceNotFoundException if no user found
     */
    User getUserByEmail(
            @NotBlank @Email final String email
    ) throws ResourceNotFoundException;

    /**
     * Check whether the user exists by username.
     *
     * @param username The user's name
     * @return True if the user exists
     */
    boolean existsUserByUsername(
            @NotBlank @Pattern(regexp = "[a-zA-Z0-9_-]{6,36}") final String username
    );

    /**
     * Check whether the user exists by e-mail.
     *
     * @param email The user's e-mail
     * @return True if the user exists
     */
    boolean existsUserByEmail(
            @NotBlank @Email final String email
    );

    /**
     * Get the user's password.
     *
     * @param username The user's name
     * @return The user's password
     * @throws ResourceNotFoundException if no user found
     */
    String getUserPassword(
            @NotBlank @Pattern(regexp = "[a-zA-Z0-9_-]{6,36}") final String username
    ) throws ResourceNotFoundException;

    /**
     * Get the user friends.
     *
     * @param id The user ID
     * @return List of friends
     * @throws ResourceNotFoundException if no user found
     */
    List<User> getFriends(
            @NotBlank final String id
    ) throws ResourceNotFoundException;

    /**
     * Get the user invitations.
     *
     * @param outgoing True, if invitations sent. False, if invitations received
     * @return List of invitations
     * @throws ResourceNotFoundException if no user found
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    List<User> getInvitations(
            @NotNull final Boolean outgoing
    ) throws ResourceNotFoundException;

    /**
     * Get the status of the relationship between users.
     *
     * @param id The user ID
     * @return Status between users
     * @throws ResourceNotFoundException if no user found
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    RelationshipStatus getUserFriendStatus(
            @NotBlank final String id
    ) throws ResourceNotFoundException;
}

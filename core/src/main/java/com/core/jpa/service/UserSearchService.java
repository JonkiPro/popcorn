package com.core.jpa.service;

import com.common.dto.User;
import com.common.exception.ResourceNotFoundException;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * Interface for searching users.
 */
@Validated
public interface UserSearchService {

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
     * Get all users by username, page number, number of items per page, sort.
     *
     * @param username  Search for a phrase
     * @param page  Page number
     * @param pageSize  Number of items per page
     * @param sort  Sort field
     * @return Users
     */
    List<User> getAllUsersByUsername(
            final String username,
            @Min(0) final int page,
            @Min(1) final int pageSize,
            @NotNull final Sort sort
    );

    /**
     * Get all users by page number, number of items per page, sort.
     *
     * @param page  Page number
     * @param pageSize  Number of items per page
     * @param sort  Sort field
     * @return Users
     */
    List<User> getAllUsers(
            @Min(0) final int page,
            @Min(1) final int pageSize,
            @NotNull final Sort sort
    );

    /**
     * Get the number of users by username.
     *
     * @param username The user's name
     * @return Number of users
     */
    Long getUserCountByUsername(
            final String username
    );

    /**
     * Check whether the user exists by username.
     *
     * @param username The user's name
     * @return True if the user exists
     */
    boolean getUserExistsByUsername(
            @NotBlank @Pattern(regexp = "[a-zA-Z0-9_-]{6,36}") final String username
    );

    /**
     * Check whether the user exists by e-mail.
     *
     * @param email The user's e-mail
     * @return True if the user exists
     */
    boolean getUserExistsByEmail(
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
}

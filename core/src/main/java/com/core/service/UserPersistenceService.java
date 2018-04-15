package com.core.service;

import com.common.dto.request.ChangeEmailDTO;
import com.common.dto.request.ChangePasswordDTO;
import com.common.dto.request.ForgotPasswordDTO;
import com.common.dto.request.RegisterDTO;
import com.common.exception.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;

/**
 * Interfaces for providing persistence functions for users other than search.
 */
@Validated
public interface UserPersistenceService {

    /**
     * Create user with DTO data.
     *
     * @param registerDTO DTO with user registration data
     * @return The id of the user created
     * @throws ResourceConflictException if username or e-mail exists
     */
    String createUser(
            @NotNull @Valid final RegisterDTO registerDTO
    ) throws ResourceConflictException;

    /**
     * Activate the user.
     *
     * @param token  The user activation token
     * @throws ResourceNotFoundException if no user found
     */
    void activationUser(
            @NotBlank final String token
    ) throws ResourceNotFoundException;

    /**
     * Reset the user password.
     *
     * @param forgotPasswordDTO DTO of the user whose password should be reset
     * @throws ResourceNotFoundException if no user found
     */
    void resetPassword(
            @NotNull @Valid final ForgotPasswordDTO forgotPasswordDTO
    ) throws ResourceNotFoundException;

    /**
     * Update the new e-mail.
     *
     * @param changeEmailDTO DTO user whose new e-mail should be set
     * @throws ResourceNotFoundException if no user found
     * @throws ResourceConflictException if e-mail exists
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    void updateNewEmail(
            @NotNull @Valid final ChangeEmailDTO changeEmailDTO
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Update the password.
     *
     * @param changePasswordDTO DTO user whose password should be changed
     * @throws ResourceBadRequestException if the password is incorrect
     * @throws ResourceNotFoundException if no user found
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    void updatePassword(
            @NotNull @Valid final ChangePasswordDTO changePasswordDTO
    ) throws ResourceBadRequestException, ResourceNotFoundException;

    /**
     * Update the avatar.
     *
     * @param file A new avatar file for the user
     * @throws ResourceBadRequestException if the password is incorrect
     * @throws ResourceNotFoundException if no user found
     * @throws ResourcePreconditionException if an I/O error occurs or incorrect content type
     * @throws ResourceServerException if an error occurred with the server
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    void updateAvatar(
            @NotNull final File file
    ) throws ResourceBadRequestException, ResourceNotFoundException, ResourcePreconditionException, ResourceServerException;

    /**
     * A new e-mail will be saved as an email.
     *
     * @param token New email activation token
     * @throws ResourceNotFoundException if no user found
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    void updateEmail(
            @NotBlank final String token
    ) throws ResourceNotFoundException;

    /**
     * Add a user to the list of friends.
     *
     * @param id The user ID
     * @throws ResourceNotFoundException if no user found
     * @throws ResourceConflictException if friendship exists or no invitation found or is an IDs conflict
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    void addFriend(
            @NotBlank final String id
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Remove a user from the list of friends.
     *
     * @param id The user ID
     * @throws ResourceNotFoundException if no user found or no friendship found
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    void removeFriend(
            @NotBlank final String id
    ) throws ResourceNotFoundException;

    /**
     * Add a user to the list of invitations.
     *
     * @param id The user ID
     * @throws ResourceNotFoundException if no user found
     * @throws ResourceConflictException if invitation exists or friendship exists or is an IDs conflict
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    void addInvitation(
            @NotBlank final String id
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Remove a user from the list of invitations.
     *
     * @param id The user ID
     * @throws ResourceNotFoundException if no user found or no invitation found
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    void removeInvitation(
            @NotBlank final String id
    ) throws ResourceNotFoundException;

    /**
     * Reject the invitation from the user.
     *
     * @param id The user ID
     * @throws ResourceNotFoundException if no user found or no invitation found
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    void rejectInvitation(
            @NotBlank final String id
    ) throws ResourceNotFoundException;

    /**
     * Create user admin with DTO data.
     *
     * @param registerDTO DTO with user registration data
     * @return The id of the user created
     * @throws ResourceConflictException if username or e-mail exists
     */
    String createAdmin(
            final RegisterDTO registerDTO
    ) throws ResourceConflictException;
}

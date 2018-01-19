package com.core.service;

import com.common.dto.request.ChangeEmailDTO;
import com.common.dto.request.ChangePasswordDTO;
import com.common.dto.request.ForgotPasswordDTO;
import com.common.dto.request.RegisterDTO;
import com.common.exception.*;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;

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
     * @param id The user ID
     * @param changeEmailDTO DTO user whose new e-mail should be set
     * @throws ResourceNotFoundException if no user found
     * @throws ResourceConflictException if e-mail exists
     */
    void updateNewEmail(
            @NotBlank final String id,
            @NotNull @Valid final ChangeEmailDTO changeEmailDTO
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Update the password.
     *
     * @param id The user ID
     * @param changePasswordDTO DTO user whose password should be changed
     * @throws ResourceBadRequestException if the password is incorrect
     * @throws ResourceNotFoundException if no user found
     */
    void updatePassword(
            @NotBlank final String id,
            @NotNull @Valid final ChangePasswordDTO changePasswordDTO
    ) throws ResourceBadRequestException, ResourceNotFoundException;

    /**
     * Update the avatar.
     *
     * @param id The user ID
     * @param file A new avatar file for the user
     * @throws ResourceBadRequestException if the password is incorrect
     * @throws ResourceNotFoundException if no user found
     * @throws ResourcePreconditionException if an I/O error occurs or incorrect content type
     * @throws ResourceServerException if an error occurred with the server
     */
    void updateAvatar(
            @NotBlank final String id,
            @NotNull final File file
    ) throws ResourceBadRequestException, ResourceNotFoundException, ResourcePreconditionException, ResourceServerException;

    /**
     * A new e-mail will be saved as an email.
     *
     * @param token New email activation token
     * @throws ResourceNotFoundException if no user found
     */
    void updateEmail(
            @NotBlank final String token
    ) throws ResourceNotFoundException;

    /**
     * Add a user to the list of friends.
     *
     * @param fromId From the user ID
     * @param toId To the user ID
     * @throws ResourceNotFoundException if no user found
     * @throws ResourceConflictException if friendship exists or no invitation found or is an IDs conflict
     */
    void addFriend(
            @NotBlank final String fromId,
            @NotBlank final String toId
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Remove a user from the list of friends.
     *
     * @param fromId From the user ID
     * @param toId To the user ID
     * @throws ResourceNotFoundException if no user found or no friendship found
     */
    void removeFriend(
            @NotBlank final String fromId,
            @NotBlank final String toId
    ) throws ResourceNotFoundException;

    /**
     * Add a user to the list of invitations.
     *
     * @param fromId From the user ID
     * @param toId To the user ID
     * @throws ResourceNotFoundException if no user found
     * @throws ResourceConflictException if invitation exists or friendship exists or is an IDs conflict
     */
    void addInvitation(
            @NotBlank final String fromId,
            @NotBlank final String toId
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Remove a user from the list of invitations.
     *
     * @param fromId From the user ID
     * @param toId To the user ID
     * @throws ResourceNotFoundException if no user found or no invitation found
     */
    void removeInvitation(
            @NotBlank final String fromId,
            @NotBlank final String toId
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

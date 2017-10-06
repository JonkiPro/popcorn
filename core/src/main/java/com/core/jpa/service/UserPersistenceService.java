package com.core.jpa.service;

import com.common.dto.request.ChangeEmailDTO;
import com.common.dto.request.ChangePasswordDTO;
import com.common.dto.request.ForgotPasswordDTO;
import com.common.dto.request.RegisterDTO;
import com.common.exception.ResourceBadRequestException;
import com.common.exception.ResourceConflictException;
import com.common.exception.ResourceNotFoundException;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

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
    Long createUser(
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
     * Change a new e-mail.
     *
     * @param id user ID
     * @param changeEmailDTO DTO user whose new e-mail should be set
     * @throws ResourceNotFoundException if no user found
     * @throws ResourceConflictException if e-mail exists
     */
    void updateNewEmail(
            @Min(1) final Long id,
            @NotNull @Valid final ChangeEmailDTO changeEmailDTO
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Change password.
     *
     * @param id The user ID
     * @param changePasswordDTO DTO user whose password should be changed
     * @throws ResourceNotFoundException if no user found
     * @throws ResourceBadRequestException if the password is incorrect
     */
    void updatePassword(
            @Min(1) final Long id,
            @NotNull @Valid final ChangePasswordDTO changePasswordDTO
    ) throws ResourceNotFoundException, ResourceBadRequestException;

    /**
     * A new e-mail will be saved as an email.
     *
     * @param token New email activation token
     * @throws ResourceNotFoundException if no user found
     */
    void updateEmail(
            @NotBlank final String token
    ) throws ResourceNotFoundException;
}

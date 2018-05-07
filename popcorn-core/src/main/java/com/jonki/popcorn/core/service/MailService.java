package com.jonki.popcorn.core.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * E-mail sending service.
 */
@Validated
public interface MailService {

    /**
     * This method sends an e-mail with an activation token.
     *
     * @param email E-mail address of the recipient
     * @param token Activation token
     */
    @Async
    void sendMailWithActivationToken(
            @NotBlank @Email final String email,
            @NotBlank final String token
    );

    /**
     * This method sends an e-mail with an email change token.
     *
     * @param email E-mail address of the recipient
     * @param token E-mail change token
     */
    @Async
    void sendMailWithEmailChangeToken(
            @NotBlank @Email final String email,
            @NotBlank final String token
    );

    /**
     * This method sends an e-mail with a new password.
     *
     * @param email E-mail address of the recipient
     * @param newPassword A new password
     */
    @Async
    void sendMailWithNewPassword(
            @NotBlank @Email final String email,
            @NotBlank final String newPassword
    );

    /**
     * This method sends an e-mail with username.
     *
     * @param email E-mail address of the recipient
     * @param username The user's name
     */
    @Async
    void sendMailWithUsername(
            @NotBlank @Email final String email,
            @NotBlank final String username
    );
}

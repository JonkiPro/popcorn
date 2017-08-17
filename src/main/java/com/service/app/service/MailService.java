package com.service.app.service;

/**
 * E-mail sending service.
 */
public interface MailService {

    /**
     * This method sends an e-mail with an activation token.
     * @param email E-mail address of the recipient.
     * @param token Activation token.
     */
    void sendMailWithActivationToken(String email, String token);

    /**
     * This method sends an e-mail with an email change token.
     * @param email E-mail address of the recipient.
     * @param token E-mail change token.
     */
    void sendMailWithEmailChangeToken(String email, String token);

    /**
     * This method sends an e-mail with a new password.
     * @param email E-mail address of the recipient.
     * @param newPassword A new password.
     */
    void sendMailWithNewPassword(String email, String newPassword);

    /**
     * This method sends an e-mail with username.
     * @param email E-mail address of the recipient.
     * @param username The user's name.
     */
    void sendMailWithUsername(String email, String username);
}

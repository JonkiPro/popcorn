package com.service.app.service;

public interface MailService {

    void sendMailWithActivationToken(String email, String token);

    void sendMailWithEmailChangeToken(String email, String token);

    void sendMailWithNewPassword(String email, String newPassword);

    void sendMailWithUsername(String email, String username);
}

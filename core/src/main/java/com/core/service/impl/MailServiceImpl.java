package com.core.service.impl;

import com.core.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.NotNull;

/**
 * Implementation of the Mail Service.
 */
@Service("mailService")
@Slf4j
@Validated
public class MailServiceImpl implements MailService{

    private final JavaMailSender javaMailSender;

    /**
     * Constructor.
     *
     * @param javaMailSender The mail sender to use
     */
    @Autowired
    public MailServiceImpl(
            @NotNull final JavaMailSender javaMailSender
    ) {
        this.javaMailSender = javaMailSender;
    }

    /**
     * Send an e-mail message.
     *
     * @param mailMessage Mail message
     */
    private void sendMail(
            @NotNull final MimeMessage mailMessage
    ) {
        javaMailSender.send(mailMessage);
    }

    /**
     * {@inheritDoc}
     */
    @Async
    @Override
    public void sendMailWithActivationToken(
            @NotBlank @Email final String email,
            @NotBlank final String token
    ) {
        log.info("Called with e-mail {}, token {}", email, token);

        try {
            final JavaMailSenderImpl sender = new JavaMailSenderImpl();

            final MimeMessage message = sender.createMimeMessage();

            final MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setTo(email);
            helper.setSubject("Complete registration");
            helper.setText("To activation your account, click the link below:<br />"
                    + "<a href='" + "https://localhost:8443" + "/register/thanks?token=" + token + "'>" +
                    "Click here to complete your registration" +
                    "</a>", true);

            sendMail(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Async
    @Override
    public void sendMailWithEmailChangeToken(
            @NotBlank @Email final String email,
            @NotBlank final String token
    ) {
        log.info("Called with e-mail {}, token {}", email, token);

        try {
            final JavaMailSenderImpl sender = new JavaMailSenderImpl();

            final MimeMessage message = sender.createMimeMessage();

            final MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setTo(email);
            helper.setSubject("Change e-mail");
            helper.setText("Change e-mail address, click the link below:<br />"
                    + "<a href='" + "https://localhost:8443" + "/settings/changeEmail/thanks?token=" + token + "'>" +
                    "Click here to complete the change of your e-mail" +
                    "</a>", true);

            sendMail(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Async
    @Override
    public void sendMailWithNewPassword(
            @NotBlank @Email final String email,
            @NotBlank final String newPassword
    ) {
        log.info("Called with e-mail {}, newPassword {}", email, newPassword);

        try {
            final JavaMailSenderImpl sender = new JavaMailSenderImpl();

            final MimeMessage message = sender.createMimeMessage();

            final MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setTo(email);
            helper.setSubject("Recover password");
            helper.setText("Your new password: " + "<b>" + newPassword + "</b>", true);

            sendMail(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Async
    @Override
    public void sendMailWithUsername(
            @NotBlank @Email final String email,
            @NotBlank final String username
    ) {
        log.info("Called with e-mail {}, username {}", email, username);

        try {
            final JavaMailSenderImpl sender = new JavaMailSenderImpl();

            final MimeMessage message = sender.createMimeMessage();

            final MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setTo(email);
            helper.setSubject("Recover username");
            helper.setText("Your username: " + "<b>" + username + "</b>", true);

            sendMail(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

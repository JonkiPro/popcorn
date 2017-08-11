package com.service.app.service.impl;

import com.service.app.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Repository;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Repository("mailService")
public class MailServiceImpl implements MailService{

    private JavaMailSender javaMailSender;

    @Autowired
    public MailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    private void sendMail(MimeMessage mailMessage) {
        javaMailSender.send(mailMessage);
    }

    @Override
    public void sendMailWithActivationToken(String email, String token) {
        try {
            JavaMailSenderImpl sender = new JavaMailSenderImpl();

            MimeMessage message = sender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setTo(email);
            helper.setSubject("Complete registration");
            helper.setText("To activation your account, click the link below:<br />"
                    + "<a href='" + "http://localhost:8080" + "/register/thanks?token=" + token + "'>" +
                    "localhost:8080" + "/register/thanks?token=" + token +
                    "</a>", true);

            sendMail(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMailWithEmailChangeToken(String email, String token) {
        try {
            JavaMailSenderImpl sender = new JavaMailSenderImpl();

            MimeMessage message = sender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setTo(email);
            helper.setSubject("Change e-mail");
            helper.setText("Change e-mail address, click the link below:<br />"
                    + "<a href='" + "http://localhost:8080" + "/settings/changeEmail/thanks?token=" + token + "'>" +
                    "localhost:8080" + "/settings/changeEmail/thanks?token=" + token +
                    "</a>", true);

            sendMail(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMailWithNewPassword(String email, String newPassword) {
        try {
            JavaMailSenderImpl sender = new JavaMailSenderImpl();

            MimeMessage message = sender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setTo(email);
            helper.setSubject("Recover password");
            helper.setText("Your new password: " + "<b>" + newPassword + "</b>", true);

            sendMail(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMailWithUsername(String email, String username) {
        try {
            JavaMailSenderImpl sender = new JavaMailSenderImpl();

            MimeMessage message = sender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setTo(email);
            helper.setSubject("Recover username");
            helper.setText("Your username: " + "<b>" + username + "</b>", true);

            sendMail(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

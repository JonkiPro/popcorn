package com.jonki.popcorn.core.service.impl;

import com.jonki.popcorn.core.service.MailService;
import com.jonki.popcorn.core.util.RandomUtils;
import com.jonki.popcorn.test.category.UnitTest;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.UUID;

/**
 * Tests for the MailServiceImpl class.
 */
@Category(UnitTest.class)
public class MailServiceImplUnitTests {

    private MailService mailService;
    private JavaMailSender mailSender;

    /**
     * Setup for the tests.
     */
    @Before
    public void setup() {
        this.mailSender = Mockito.mock(JavaMailSender.class);
        this.mailService = new MailServiceImpl(this.mailSender);
    }

    /**
     * Make sure we can successfully send an email with activation token.
     */
    @Test
    public void canSendEmailWithActivationToken() throws MessagingException, IOException {
        final ArgumentCaptor<MimeMessage> captor = ArgumentCaptor.forClass(MimeMessage.class);
        final String email = "someemail@someemail.com";
        final String token = RandomUtils.randomToken();

        final String subject = "Complete registration";
        final String text = "To activation your account, click the link below:<br />"
                + "<a href='" + "https://localhost:8443" + "/register/thanks?token=" + token + "'>" +
                "Click here to complete your registration" +
                "</a>";

        this.mailService.sendMailWithActivationToken(email, token);
        Mockito.verify(this.mailSender, Mockito.times(1)).send(captor.capture());
        Assert.assertThat(captor.getValue().getAllRecipients()[0].toString(), Matchers.is(email));
        Assert.assertThat(captor.getValue().getSubject(), Matchers.is(subject));
        Assert.assertThat(captor.getValue().getContent(), Matchers.is(text));
    }

    /**
     * Make sure we can successfully send an email with change token.
     */
    @Test
    public void canSendEmailWithChangeToken() throws MessagingException, IOException {
        final ArgumentCaptor<MimeMessage> captor = ArgumentCaptor.forClass(MimeMessage.class);
        final String email = "someemail@someemail.com";
        final String token = RandomUtils.randomToken();

        final String subject = "Change e-mail";
        final String text = "Change e-mail address, click the link below:<br />"
                + "<a href='" + "https://localhost:8443" + "/settings/changeEmail/thanks?token=" + token + "'>" +
                "Click here to complete the change of your e-mail" +
                "</a>";

        this.mailService.sendMailWithEmailChangeToken(email, token);
        Mockito.verify(this.mailSender, Mockito.times(1)).send(captor.capture());
        Assert.assertThat(captor.getValue().getAllRecipients()[0].toString(), Matchers.is(email));
        Assert.assertThat(captor.getValue().getSubject(), Matchers.is(subject));
        Assert.assertThat(captor.getValue().getContent(), Matchers.is(text));
    }

    /**
     * Make sure we can successfully send an email with new password.
     */
    @Test
    public void canSendEmailWithNewPassword() throws MessagingException, IOException {
        final ArgumentCaptor<MimeMessage> captor = ArgumentCaptor.forClass(MimeMessage.class);
        final String email = "someemail@someemail.com";
        final String password = RandomUtils.randomPassword();

        final String subject = "Recover password";
        final String text = "Your new password: " + "<b>" + password + "</b>";

        this.mailService.sendMailWithNewPassword(email, password);
        Mockito.verify(this.mailSender, Mockito.times(1)).send(captor.capture());
        Assert.assertThat(captor.getValue().getAllRecipients()[0].toString(), Matchers.is(email));
        Assert.assertThat(captor.getValue().getSubject(), Matchers.is(subject));
        Assert.assertThat(captor.getValue().getContent(), Matchers.is(text));
    }

    /**
     * Make sure we can successfully send an email with username.
     */
    @Test
    public void canSendEmailWithUsername() throws MessagingException, IOException {
        final ArgumentCaptor<MimeMessage> captor = ArgumentCaptor.forClass(MimeMessage.class);
        final String email = "someemail@someemail.com";
        final String username = UUID.randomUUID().toString();

        final String subject = "Recover username";
        final String text = "Your username: " + "<b>" + username + "</b>";

        this.mailService.sendMailWithUsername(email, username);
        Mockito.verify(this.mailSender, Mockito.times(1)).send(captor.capture());
        Assert.assertThat(captor.getValue().getAllRecipients()[0].toString(), Matchers.is(email));
        Assert.assertThat(captor.getValue().getSubject(), Matchers.is(subject));
        Assert.assertThat(captor.getValue().getContent(), Matchers.is(text));
    }
}

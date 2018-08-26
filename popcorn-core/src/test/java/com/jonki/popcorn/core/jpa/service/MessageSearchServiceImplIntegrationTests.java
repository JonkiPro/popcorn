package com.jonki.popcorn.core.jpa.service;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.jonki.popcorn.common.dto.MessageReceived;
import com.jonki.popcorn.common.dto.MessageSent;
import com.jonki.popcorn.common.exception.ResourceException;
import com.jonki.popcorn.core.security.model.CustomUserDetails;
import com.jonki.popcorn.core.service.MessageSearchService;
import com.jonki.popcorn.test.category.IntegrationTest;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

/**
 * Integration tests for MessageSearchServiceImpl.
 */
@Category(IntegrationTest.class)
@DatabaseSetup("MessageSearchServiceImplIntegrationTests/init.xml")
@DatabaseTearDown("cleanup.xml")
public class MessageSearchServiceImplIntegrationTests extends DBIntegrationTestBase {

    private static final String USER_1_ID = "user1";
    private static final String USER_1_USERNAME = "jonki1";
    private static final String USER_1_PASSWORD = "$2a$12$O02MQJS6AnCJiRw1nF6XsO7udBCza591oUKUbRC4/XrUmPXZaSjVK";

    private static final String MESS_1_ID = "mess1";
    private static final String MESS_1_SUBJECT = "Message1";

    private static final String MESS_2_ID = "mess2";
    private static final String MESS_2_SUBJECT = "Message2";

    @Autowired
    private MessageSearchService messageSearchService;

    /**
     * Setup.
     */
    @Before
    public void setup() {
        Assert.assertThat(this.messageRepository.count(), Matchers.is(4L));

        final Authentication authentication = new UsernamePasswordAuthenticationToken(
                new CustomUserDetails(
                        USER_1_USERNAME,
                        USER_1_PASSWORD,
                        AuthorityUtils.createAuthorityList("ROLE_USER"),
                        USER_1_ID
                ),
                null,
                AuthorityUtils.createAuthorityList("ROLE_USER"));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * Test the getMessageSent method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetMessageSent() throws ResourceException {
        Assert.assertThat(
                this.messageSearchService.getMessageSent(MESS_1_ID).getId(),
                Matchers.is(MESS_1_ID)
        );
    }

    /**
     * Test the getMessagesSent method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void cantGetMessagesSent() throws ResourceException {
        List<MessageSent> messagesSent = this.messageSearchService.getMessagesSent(null);
        Assert.assertThat(messagesSent.size(), Matchers.is(2));

        messagesSent = this.messageSearchService.getMessagesSent("mess");
        Assert.assertThat(messagesSent.size(), Matchers.is(2));
        Assert.assertThat(
                messagesSent
                        .stream()
                        .filter(message -> message.getSubject().contains(MESS_1_SUBJECT))
                        .count(),
                Matchers.is(1L)
        );

        messagesSent = this.messageSearchService.getMessagesSent("message1");
        Assert.assertThat(messagesSent.size(), Matchers.is(1));
        Assert.assertThat(
                messagesSent
                        .stream()
                        .filter(message -> message.getSubject().contains(MESS_1_SUBJECT))
                        .count(),
                Matchers.is(1L)
        );

        messagesSent = this.messageSearchService.getMessagesSent("qwerty");
        Assert.assertThat(messagesSent.size(), Matchers.is(0));
        Assert.assertTrue(messagesSent.isEmpty());
    }

    /**
     * Test the getMessageReceived  method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetMessageReceived() throws ResourceException {
        Assert.assertThat(
                this.messageSearchService.getMessageReceived(MESS_2_ID).getId(),
                Matchers.is(MESS_2_ID)
        );
    }

    /**
     * Test the getMessagesReceived method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void cantGetMessagesReceived() throws ResourceException {
        List<MessageReceived> messagesReceived = this.messageSearchService.getMessagesReceived(null);
        Assert.assertThat(messagesReceived.size(), Matchers.is(2));

        messagesReceived = this.messageSearchService.getMessagesReceived("mess");
        Assert.assertThat(messagesReceived.size(), Matchers.is(2));
        Assert.assertThat(
                messagesReceived
                        .stream()
                        .filter(message -> message.getSubject().contains(MESS_2_SUBJECT))
                        .count(),
                Matchers.is(1L)
        );

        messagesReceived = this.messageSearchService.getMessagesReceived("message2");
        Assert.assertThat(messagesReceived.size(), Matchers.is(1));
        Assert.assertThat(
                messagesReceived
                        .stream()
                        .filter(message -> message.getSubject().contains(MESS_2_SUBJECT))
                        .count(),
                Matchers.is(1L)
        );

        messagesReceived = this.messageSearchService.getMessagesReceived("qwerty");
        Assert.assertThat(messagesReceived.size(), Matchers.is(0));
        Assert.assertTrue(messagesReceived.isEmpty());
    }

    /**
     * Test the existsMessageSent method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void existsMessageSent() throws ResourceException {
        Assert.assertTrue(this.messageSearchService.existsMessageSent(MESS_1_ID));
    }

    /**
     * Test the existsMessageReceived method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void existsMessageReceived() throws ResourceException {
        Assert.assertTrue(this.messageSearchService.existsMessageReceived(MESS_2_ID));
    }
}

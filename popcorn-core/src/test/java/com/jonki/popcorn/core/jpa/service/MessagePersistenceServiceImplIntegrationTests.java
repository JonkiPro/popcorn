package com.jonki.popcorn.core.jpa.service;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.jonki.popcorn.common.dto.MessageSent;
import com.jonki.popcorn.common.dto.request.MessageRequest;
import com.jonki.popcorn.common.exception.ResourceException;
import com.jonki.popcorn.core.security.model.CustomUserDetails;
import com.jonki.popcorn.core.service.MessagePersistenceService;
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

import java.util.UUID;

/**
 * Integration tests for MessagePersistenceServiceImpl.
 */
@Category(IntegrationTest.class)
@DatabaseSetup("MessagePersistenceServiceImplIntegrationTests/init.xml")
@DatabaseTearDown("cleanup.xml")
public class MessagePersistenceServiceImplIntegrationTests extends DBIntegrationTestBase {

    private static final String USER_1_ID = "user1";
    private static final String USER_1_USERNAME = "jonki1";
    private static final String USER_1_PASSWORD = "$2a$12$O02MQJS6AnCJiRw1nF6XsO7udBCza591oUKUbRC4/XrUmPXZaSjVK";

    private static final String USER_2_USERNAME = "jonki2";

    private static final String MESS_1_ID = "mess1";
    private static final String MESS_1_SUBJECT = "Message1";

    private static final String MESS_2_ID = "mess2";
    private static final String MESS_2_SUBJECT = "Message2";

    @Autowired
    private MessagePersistenceService messagePersistenceService;

    @Autowired
    private MessageSearchService messageSearchService;

    /**
     * Setup.
     */
    @Before
    public void setup() {
        Assert.assertThat(this.messageRepository.count(), Matchers.is(2L));

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
     * Test the createMessage method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateMessage() throws ResourceException {
        final String subject = UUID.randomUUID().toString();
        final String text = UUID.randomUUID().toString();
        final MessageRequest messageRequest = new MessageRequest.Builder(
                USER_2_USERNAME,
                subject,
                text
        ).build();

        Assert.assertThat(this.messageRepository.count(), Matchers.is(2L));
        final String id = this.messagePersistenceService.createMessage(messageRequest);
        Assert.assertThat(this.messageRepository.count(), Matchers.is(3L));

        final MessageSent created = this.messageSearchService.getMessageSent(id);
        Assert.assertNotNull(created);
        Assert.assertThat(created.getRecipient().getUsername(), Matchers.is(USER_2_USERNAME));
        Assert.assertThat(created.getSubject(), Matchers.is(subject));
        Assert.assertThat(created.getText(), Matchers.is(text));
    }

    /**
     * Test the deleteMessageSent method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canDeleteMessageSent() throws ResourceException {
        Assert.assertEquals(1, this.messageSearchService.getMessagesSent(MESS_1_SUBJECT).size());
        this.messagePersistenceService.deleteMessageSent(MESS_1_ID);
        Assert.assertEquals(0, this.messageSearchService.getMessagesSent(MESS_1_SUBJECT).size());
    }

    /**
     * Test the deleteMessageReceived  method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canDeleteMessageReceived() throws ResourceException {
        Assert.assertEquals(1, this.messageSearchService.getMessagesReceived(MESS_2_SUBJECT).size());
        this.messagePersistenceService.deleteMessageReceived(MESS_2_ID);
        Assert.assertEquals(0, this.messageSearchService.getMessagesReceived(MESS_2_SUBJECT).size());
    }
}

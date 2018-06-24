package com.jonki.popcorn.core.jpa.service;

import com.jonki.popcorn.common.exception.ResourceException;
import com.jonki.popcorn.common.exception.ResourceNotFoundException;
import com.jonki.popcorn.core.jpa.entity.MessageEntity;
import com.jonki.popcorn.core.jpa.entity.UserEntity;
import com.jonki.popcorn.core.jpa.repository.MessageRepository;
import com.jonki.popcorn.core.jpa.repository.UserRepository;
import com.jonki.popcorn.core.service.AuthorizationService;
import com.jonki.popcorn.test.category.UnitTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

/**
 * Unit tests for MessageSearchServiceImpl.
 */
@Category(UnitTest.class)
public class MessageSearchServiceImplUnitTests {

    private MessageRepository messageRepository;
    private UserRepository userRepository;
    private AuthorizationService authorizationService;
    private MessageSearchServiceImpl messageSearchService;

    /**
     * Setup for the tests.
     */
    @Before
    public void setup() {
        this.messageRepository = Mockito.mock(MessageRepository.class);
        this.userRepository = Mockito.mock(UserRepository.class);
        this.authorizationService = Mockito.mock(AuthorizationService.class);
        this.messageSearchService = new MessageSearchServiceImpl(
                this.messageRepository,
                this.userRepository,
                this.authorizationService
        );
    }

    /**
     * Test the getMessageSent method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetMessageSent() throws ResourceException {
        final MessageEntity messageEntity = new MessageEntity();
        final UserEntity sender = new UserEntity();
        final UserEntity recipient = new UserEntity();
        recipient.setUsername(UUID.randomUUID().toString());
        messageEntity.setRecipient(recipient);
        final String userId = UUID.randomUUID().toString();
        final String messageId = UUID.randomUUID().toString();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(sender));
        Mockito
                .when(this.messageRepository.findByUniqueIdAndSenderAndIsVisibleForSenderTrue(messageId, sender))
                .thenReturn(Optional.of(messageEntity));
        this.messageSearchService.getMessageSent(messageId);
    }

    /**
     * Test the getMessageSent method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantGetMessageSentIfMessageDoesNotExist() throws ResourceException {
        final UserEntity sender = new UserEntity();
        final String userId = UUID.randomUUID().toString();
        final String messageId = UUID.randomUUID().toString();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(sender));
        Mockito
                .when(this.messageRepository.findByUniqueIdAndSenderAndIsVisibleForSenderTrue(messageId, sender))
                .thenReturn(Optional.empty());
        this.messageSearchService.getMessageSent(messageId);
    }

    /**
     * Test the getMessagesSent method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantGetMessagesSentIfUserDoesNotExist() throws ResourceException {
        final String id = UUID.randomUUID().toString();
        final String content = UUID.randomUUID().toString();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(id);
        this.messageSearchService.getMessagesSent(content);
    }

    /**
     * Test the getMessageReceived method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetMessageReceived() throws ResourceException {
        final MessageEntity messageEntity = new MessageEntity();
        final UserEntity recipient = new UserEntity();
        final UserEntity sender = new UserEntity();
        sender.setUsername(UUID.randomUUID().toString());
        messageEntity.setSender(sender);
        final String userId = UUID.randomUUID().toString();
        final String messageId = UUID.randomUUID().toString();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(recipient));
        Mockito
                .when(this.messageRepository.findByUniqueIdAndRecipientAndIsVisibleForRecipientTrue(messageId, recipient))
                .thenReturn(Optional.of(messageEntity));
        this.messageSearchService.getMessageReceived(messageId);
    }

    /**
     * Test the getMessageReceived method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantGetMessageReceivedIfMessageDoesNotExist() throws ResourceException {
        final UserEntity recipient = new UserEntity();
        final String userId = UUID.randomUUID().toString();
        final String messageId = UUID.randomUUID().toString();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(recipient));
        Mockito
                .when(this.messageRepository.findByUniqueIdAndRecipientAndIsVisibleForRecipientTrue(messageId, recipient))
                .thenReturn(Optional.empty());
        this.messageSearchService.getMessageReceived(messageId);
    }

    /**
     * Test the getMessagesReceived method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantGetMessagesReceivedIfUserDoesNotExist() throws ResourceException {
        final String id = UUID.randomUUID().toString();
        final String content = UUID.randomUUID().toString();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(id);
        this.messageSearchService.getMessagesReceived(content);
    }

    /**
     * Test the existsMessageSent method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void existsMessageSent() throws ResourceException {
        final UserEntity sender = new UserEntity();
        final String userId = UUID.randomUUID().toString();
        final String messageId = UUID.randomUUID().toString();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(sender));
        Mockito
                .when(this.messageRepository.existsByUniqueIdAndSenderAndIsVisibleForSenderTrue(messageId, sender))
                .thenReturn(true);
        Assert.assertTrue(this.messageSearchService.existsMessageSent(messageId));
    }

    /**
     * Test the existsMessageSent method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void notExistsMessageSent() throws ResourceException {
        final UserEntity sender = new UserEntity();
        final String userId = UUID.randomUUID().toString();
        final String messageId = UUID.randomUUID().toString();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(sender));
        Mockito
                .when(this.messageRepository.existsByUniqueIdAndSenderAndIsVisibleForSenderTrue(messageId, sender))
                .thenReturn(false);
        Assert.assertFalse(this.messageSearchService.existsMessageSent(messageId));
    }

    /**
     * Test the existsMessageReceived method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void existsMessageReceived() throws ResourceException {
        final UserEntity recipient = new UserEntity();
        final String userId = UUID.randomUUID().toString();
        final String messageId = UUID.randomUUID().toString();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(recipient));
        Mockito
                .when(this.messageRepository.existsByUniqueIdAndRecipientAndIsVisibleForRecipientTrue(messageId, recipient))
                .thenReturn(true);
        Assert.assertTrue(this.messageSearchService.existsMessageReceived(messageId));
    }

    /**
     * Test the existsMessageReceived method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void notExistsMessageReceived() throws ResourceException {
        final UserEntity recipient = new UserEntity();
        final String userId = UUID.randomUUID().toString();
        final String messageId = UUID.randomUUID().toString();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(recipient));
        Mockito
                .when(this.messageRepository.existsByUniqueIdAndRecipientAndIsVisibleForRecipientTrue(messageId, recipient))
                .thenReturn(false);
        Assert.assertFalse(this.messageSearchService.existsMessageReceived(messageId));
    }
}

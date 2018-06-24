package com.jonki.popcorn.core.jpa.service;

import com.jonki.popcorn.common.dto.request.MessageRequest;
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
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

/**
 * Unit tests for MessagePersistenceServiceImpl.
 */
@Category(UnitTest.class)
public class MessagePersistenceServiceImplUnitTests {

    private static final String USER_1_ID = UUID.randomUUID().toString();

    private MessageRepository messageRepository;
    private UserRepository userRepository;
    private AuthorizationService authorizationService;
    private MessagePersistenceServiceImpl messagePersistenceService;

    /**
     * Setup for the tests.
     */
    @Before
    public void setup() {
        this.messageRepository = Mockito.mock(MessageRepository.class);
        this.userRepository = Mockito.mock(UserRepository.class);
        this.authorizationService = Mockito.mock(AuthorizationService.class);
        this.messagePersistenceService = new MessagePersistenceServiceImpl(
                this.messageRepository,
                this.userRepository,
                this.authorizationService
        );
    }

    /**
     * Test the createMessage method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void testCreateMessage() throws ResourceException {
        final UserEntity sender = new UserEntity();
        sender.setUniqueId(USER_1_ID);
        final UserEntity recipient = new UserEntity();
        recipient.setUniqueId(UUID.randomUUID().toString());
        final MessageRequest messageRequest = new MessageRequest.Builder(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString()
        ).build();

        Mockito.when(this.authorizationService.getUserId()).thenReturn(USER_1_ID);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(USER_1_ID))
                .thenReturn(Optional.of(sender));
        Mockito
                .when(this.userRepository.findOneByUsernameIgnoreCaseAndEnabledTrue(messageRequest.getTo()))
                .thenReturn(recipient);
        this.messagePersistenceService.createMessage(messageRequest);
    }

    /**
     * Test the createMessage method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void testCreateMessageIfRecipientDoesNotExists() throws ResourceException {
        final UserEntity sender = Mockito.mock(UserEntity.class);
        final MessageRequest messageRequest = new MessageRequest.Builder(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString()
        ).build();

        Mockito.when(this.authorizationService.getUserId()).thenReturn(USER_1_ID);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(USER_1_ID))
                .thenReturn(Optional.of(sender));
        Mockito
                .when(this.userRepository.findOneByUsernameIgnoreCaseAndEnabledTrue(messageRequest.getTo()))
                .thenReturn(null);
        this.messagePersistenceService.createMessage(messageRequest);
    }

    /**
     * Test the deleteMessageSent method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canDeleteMessageSent() throws ResourceException {
        final UserEntity sender = Mockito.mock(UserEntity.class);
        final MessageEntity messageEntity = new MessageEntity();
        final String messageId = UUID.randomUUID().toString();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(USER_1_ID);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(USER_1_ID))
                .thenReturn(Optional.of(sender));
        Mockito
                .when(this.messageRepository.findByUniqueIdAndSenderAndIsVisibleForSenderTrue(messageId, sender))
                .thenReturn(Optional.of(messageEntity));

        final ArgumentCaptor<MessageEntity> argument = ArgumentCaptor.forClass(MessageEntity.class);
        this.messagePersistenceService.deleteMessageSent(messageId);
        Mockito.verify(this.messageRepository).save(argument.capture());

        Assert.assertFalse(messageEntity.isVisibleForSender());
    }

    /**
     * Test the deleteMessageSent method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantDeleteMessageSentIfDoesNotExists() throws ResourceException {
        final UserEntity sender = Mockito.mock(UserEntity.class);
        final String messageId = UUID.randomUUID().toString();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(USER_1_ID);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(USER_1_ID))
                .thenReturn(Optional.of(sender));
        Mockito
                .when(this.messageRepository.findByUniqueIdAndSenderAndIsVisibleForSenderTrue(messageId, sender))
                .thenReturn(Optional.empty());
        this.messagePersistenceService.deleteMessageSent(messageId);
    }

    /**
     * Test the deleteMessageReceived method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canDeleteMessageReceived() throws ResourceException {
        final UserEntity recipient = Mockito.mock(UserEntity.class);
        final MessageEntity messageEntity = new MessageEntity();
        final String messageId = UUID.randomUUID().toString();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(USER_1_ID);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(USER_1_ID))
                .thenReturn(Optional.of(recipient));
        Mockito
                .when(this.messageRepository.findByUniqueIdAndRecipientAndIsVisibleForRecipientTrue(messageId, recipient))
                .thenReturn(Optional.of(messageEntity));

        final ArgumentCaptor<MessageEntity> argument = ArgumentCaptor.forClass(MessageEntity.class);
        this.messagePersistenceService.deleteMessageReceived(messageId);
        Mockito.verify(this.messageRepository).save(argument.capture());

        Assert.assertFalse(messageEntity.isVisibleForRecipient());
    }

    /**
     * Test the deleteMessageReceived method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantDeleteMessageReceivedIfDoesNotExists() throws ResourceException {
        final UserEntity recipient = Mockito.mock(UserEntity.class);
        final String messageId = UUID.randomUUID().toString();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(USER_1_ID);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(USER_1_ID))
                .thenReturn(Optional.of(recipient));
        Mockito
                .when(this.messageRepository.findByUniqueIdAndRecipientAndIsVisibleForRecipientTrue(messageId, recipient))
                .thenReturn(Optional.empty());
        this.messagePersistenceService.deleteMessageReceived(messageId);
    }
}

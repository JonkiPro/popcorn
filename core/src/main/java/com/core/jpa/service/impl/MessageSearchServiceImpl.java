package com.core.jpa.service.impl;

import com.common.dto.MessageReceived;
import com.common.dto.MessageSent;
import com.common.exception.ResourceNotFoundException;
import com.core.jpa.entity.MessageEntity;
import com.core.jpa.entity.UserEntity;
import com.core.jpa.repository.MessageRepository;
import com.core.jpa.repository.UserRepository;
import com.core.jpa.service.MessageSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * JPA implementation of the Message Search Service.
 */
@Service("messageSearchService")
@Slf4j
@Validated
public class MessageSearchServiceImpl implements MessageSearchService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    /**
     * Constructor.
     *
     * @param messageRepository The message repository to use
     * @param userRepository The user repository to use
     */
    @Autowired
    public MessageSearchServiceImpl(
            @NotNull final MessageRepository messageRepository,
            @NotNull final UserRepository userRepository
    ) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MessageSent getMessageSent(
            @Min(1) final Long messageId,
            @Min(1) final Long userId
    ) throws ResourceNotFoundException {
        log.info("Called with messageId {}, userId {}", messageId, userId);

        Optional<UserEntity> user = this.userRepository.findByIdAndEnabledTrue(userId);
        user.orElseThrow(() -> new ResourceNotFoundException("No user found with id " + userId));

        return this.messageRepository.findByIdAndSenderAndIsVisibleForSenderTrue(messageId, user.get())
                .map(MessageEntity::getSentDTO)
                .orElseThrow(() -> new ResourceNotFoundException("No message found with id " + messageId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MessageSent> getMessagesSent(
            @Min(1) final Long userId
    ) throws ResourceNotFoundException {
        log.info("Called with userId {}", userId);

        Optional<UserEntity> user = this.userRepository.findByIdAndEnabledTrue(userId);
        user.orElseThrow(() -> new ResourceNotFoundException("No user found with id " + userId));

        return this.messageRepository.findBySenderAndIsVisibleForSenderTrueOrderByIdDesc(user.get())
                .stream()
                .map(MessageEntity::getSentDTO)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MessageSent> getMessagesSent(
            @Min(1) final Long userId,
            final String content
    ) throws ResourceNotFoundException {
        log.info("Called with userId {}", userId);

        Optional<UserEntity> user = this.userRepository.findByIdAndEnabledTrue(userId);
        user.orElseThrow(() -> new ResourceNotFoundException("No user found with id " + userId));

        return this.messageRepository.findSentMessagesByContaining(user.get(), "%"+ content.trim()+"%", "%"+ content.trim()+"%")
                .stream()
                .map(MessageEntity::getSentDTO)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MessageReceived getMessageReceived(
            @Min(1) final Long messageId,
            @Min(1) final Long userId
    ) throws ResourceNotFoundException {
        log.info("Called with messageId {}, userId {}", messageId, userId);

        Optional<UserEntity> user = this.userRepository.findByIdAndEnabledTrue(userId);
        user.orElseThrow(() -> new ResourceNotFoundException("No user found with id " + userId));

        return this.messageRepository.findByIdAndRecipientAndIsVisibleForRecipientTrue(messageId, user.get())
                .map(MessageEntity::getReceivedDTO)
                .orElseThrow(() -> new ResourceNotFoundException("No message found with id " + messageId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MessageReceived> getMessagesReceived(
            @Min(1) final Long userId
    ) throws ResourceNotFoundException {
        log.info("Called with userId {}", userId);

        Optional<UserEntity> user = this.userRepository.findByIdAndEnabledTrue(userId);
        user.orElseThrow(() -> new ResourceNotFoundException("No user found with id " + userId));

        return this.messageRepository.findByRecipientAndIsVisibleForRecipientTrueOrderByIdDesc(user.get())
                .stream()
                .map(MessageEntity::getReceivedDTO)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MessageReceived> getMessagesReceived(
            @Min(1) final Long userId,
            final String content
    ) throws ResourceNotFoundException {
        log.info("Called with userId {}", userId);

        Optional<UserEntity> user = this.userRepository.findByIdAndEnabledTrue(userId);
        user.orElseThrow(() -> new ResourceNotFoundException("No user found with id " + userId));

        return this.messageRepository.findReceivedMessagesByContaining(user.get(), "%"+ content.trim()+"%", "%"+ content.trim()+"%")
                .stream()
                .map(MessageEntity::getReceivedDTO)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getMessageSentExists(
            @Min(1) final Long messageId,
            @Min(1) final Long userId
    ) throws ResourceNotFoundException {
        log.info("Called with messageId {}, userId {}", messageId, userId);

        Optional<UserEntity> user = this.userRepository.findByIdAndEnabledTrue(userId);
        user.orElseThrow(() -> new ResourceNotFoundException("No user found with id " + userId));

        return this.messageRepository.existsByIdAndSenderAndIsVisibleForSenderTrue(messageId, user.get());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getMessageReceivedExists(
            @Min(1) final Long messageId,
            @Min(1) final Long userId
    ) throws ResourceNotFoundException {
        log.info("Called with messageId {}, userId {}", messageId, userId);

        Optional<UserEntity> user = this.userRepository.findByIdAndEnabledTrue(userId);
        user.orElseThrow(() -> new ResourceNotFoundException("No user found with id " + userId));

        return this.messageRepository.existsByIdAndRecipientAndIsVisibleForRecipientTrue(messageId, user.get());
    }
}

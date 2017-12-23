package com.core.jpa.service;

import com.common.dto.Message;
import com.common.dto.MessageReceived;
import com.common.dto.MessageSent;
import com.common.exception.ResourceNotFoundException;
import com.core.jpa.entity.MessageEntity;
import com.core.jpa.entity.UserEntity;
import com.core.jpa.repository.MessageRepository;
import com.core.jpa.repository.UserRepository;
import com.core.service.MessageSearchService;
import com.core.jpa.specifications.MessageSpecs;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JPA implementation of the Message Search Service.
 */
@Service("messageSearchService")
@Slf4j
@Transactional(readOnly = true)
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
            @NotBlank final String messageId,
            @NotBlank final String userId
    ) throws ResourceNotFoundException {
        log.info("Called with messageId {}, userId {}", messageId, userId);

        final UserEntity user = this.findUser(userId);

        return this.messageRepository.findByUniqueIdAndSenderAndIsVisibleForSenderTrue(messageId, user)
                .map(ServiceUtils::toMessageSentDto)
                .orElseThrow(() -> new ResourceNotFoundException("No message found with id " + messageId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MessageSent> getMessagesSent(
            @NotBlank final String userId,
            @Nullable final String content
    ) throws ResourceNotFoundException {
        log.info("Called with userId {}, content {}", userId, content);

        @SuppressWarnings("unchecked") final List<MessageEntity> messageEntities = this.messageRepository.findAll(
                MessageSpecs.findSentMessagesForUser(
                        this.findUser(userId),
                        content,
                        content)
        );

        final List<MessageSent> collect = messageEntities
                .stream()
                .map(ServiceUtils::toMessageSentDto)
                .sorted(Comparator.comparing(Message::getDate))
                .collect(Collectors.toList());

        Collections.reverse(collect);

        return collect;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = false)
    public MessageReceived getMessageReceived(
            @NotBlank final String messageId,
            @NotBlank final String userId
    ) throws ResourceNotFoundException {
        log.info("Called with messageId {}, userId {}", messageId, userId);

        final UserEntity user = this.findUser(userId);

        final MessageEntity message
                = this.messageRepository.findByUniqueIdAndRecipientAndIsVisibleForRecipientTrue(messageId, user)
                .orElseThrow(() -> new ResourceNotFoundException("No message found with id " + messageId));

        if(message.getDateOfRead() == null) {
            message.setDateOfRead(new Date());
        }

        return ServiceUtils.toMessageReceivedDto(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MessageReceived> getMessagesReceived(
            @NotBlank final String userId,
            @Nullable final String content
    ) throws ResourceNotFoundException {
        log.info("Called with userId {}, content {}", userId, content);

        @SuppressWarnings("unchecked") final List<MessageEntity> messageEntities = this.messageRepository.findAll(
                MessageSpecs.findReceivedMessagesForUser(
                        this.findUser(userId),
                        content,
                        content)
        );

        final List<MessageReceived> collect = messageEntities
                .stream()
                .map(ServiceUtils::toMessageReceivedDto)
                .sorted(Comparator.comparing(Message::getDate))
                .collect(Collectors.toList());

        Collections.reverse(collect);

        return collect;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsMessageSent(
            @NotBlank final String messageId,
            @NotBlank final String userId
    ) throws ResourceNotFoundException {
        log.info("Called with messageId {}, userId {}", messageId, userId);

        final UserEntity user = this.findUser(userId);

        return this.messageRepository.existsByUniqueIdAndSenderAndIsVisibleForSenderTrue(messageId, user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsMessageReceived(
            @NotBlank final String messageId,
            @NotBlank final String userId
    ) throws ResourceNotFoundException {
        log.info("Called with messageId {}, userId {}", messageId, userId);

        final UserEntity user = this.findUser(userId);

        return this.messageRepository.existsByUniqueIdAndRecipientAndIsVisibleForRecipientTrue(messageId, user);
    }

    /**
     * Helper method to find the user entity.
     *
     * @param id The user ID
     * @return The user
     * @throws ResourceNotFoundException if no user found
     */
    private UserEntity findUser(final String id) throws ResourceNotFoundException {
        return this.userRepository
                .findByUniqueIdAndEnabledTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with id " + id));
    }
}

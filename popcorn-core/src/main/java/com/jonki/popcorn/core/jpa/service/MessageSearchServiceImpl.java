package com.jonki.popcorn.core.jpa.service;

import com.jonki.popcorn.common.dto.Message;
import com.jonki.popcorn.common.dto.MessageReceived;
import com.jonki.popcorn.common.dto.MessageSent;
import com.jonki.popcorn.common.exception.ResourceNotFoundException;
import com.jonki.popcorn.core.jpa.entity.MessageEntity;
import com.jonki.popcorn.core.jpa.entity.UserEntity;
import com.jonki.popcorn.core.jpa.repository.MessageRepository;
import com.jonki.popcorn.core.jpa.repository.UserRepository;
import com.jonki.popcorn.core.service.AuthorizationService;
import com.jonki.popcorn.core.service.MessageSearchService;
import com.jonki.popcorn.core.jpa.specification.MessageSpecs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
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
    private final AuthorizationService authorizationService;

    /**
     * Constructor.
     *
     * @param messageRepository The message repository to use
     * @param userRepository The user repository to use
     * @param authorizationService The authorization service to use
     */
    @Autowired
    public MessageSearchServiceImpl(
            @NotNull final MessageRepository messageRepository,
            @NotNull final UserRepository userRepository,
            @NotNull final AuthorizationService authorizationService
    ) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.authorizationService = authorizationService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MessageSent getMessageSent(
            @NotBlank final String id
    ) throws ResourceNotFoundException {
        log.info("Called with id {}", id);

        final UserEntity user = this.findUser(this.authorizationService.getUserId());

        return this.messageRepository.findByUniqueIdAndSenderAndIsVisibleForSenderTrue(id, user)
                .map(ServiceUtils::toMessageSentDto)
                .orElseThrow(() -> new ResourceNotFoundException("No message found with id " + id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MessageSent> getMessagesSent(
            @Nullable final String content
    ) throws ResourceNotFoundException {
        log.info("Called with content {}", content);

        @SuppressWarnings("unchecked") final List<MessageEntity> messageEntities = this.messageRepository.findAll(
                MessageSpecs.findSentMessagesForUser(
                        this.findUser(this.authorizationService.getUserId()),
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
            @NotBlank final String id
    ) throws ResourceNotFoundException {
        log.info("Called with id {}", id);

        final UserEntity user = this.findUser(this.authorizationService.getUserId());

        final MessageEntity message
                = this.messageRepository.findByUniqueIdAndRecipientAndIsVisibleForRecipientTrue(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("No message found with id " + id));

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
            @Nullable final String content
    ) throws ResourceNotFoundException {
        log.info("Called with content {}", content);

        @SuppressWarnings("unchecked") final List<MessageEntity> messageEntities = this.messageRepository.findAll(
                MessageSpecs.findReceivedMessagesForUser(
                        this.findUser(this.authorizationService.getUserId()),
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
            @NotBlank final String id
    ) throws ResourceNotFoundException {
        log.info("Called with id {}", id);

        final UserEntity user = this.findUser(this.authorizationService.getUserId());

        return this.messageRepository.existsByUniqueIdAndSenderAndIsVisibleForSenderTrue(id, user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsMessageReceived(
            @NotBlank final String id
    ) throws ResourceNotFoundException {
        log.info("Called with id {}", id);

        final UserEntity user = this.findUser(this.authorizationService.getUserId());

        return this.messageRepository.existsByUniqueIdAndRecipientAndIsVisibleForRecipientTrue(id, user);
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

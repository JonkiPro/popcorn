package com.jonki.popcorn.core.jpa.service;

import com.google.common.collect.Iterables;
import com.jonki.popcorn.common.dto.request.SendMessageDTO;
import com.jonki.popcorn.common.exception.ResourceConflictException;
import com.jonki.popcorn.common.exception.ResourceNotFoundException;
import com.jonki.popcorn.core.jpa.entity.MessageEntity;
import com.jonki.popcorn.core.jpa.entity.UserEntity;
import com.jonki.popcorn.core.jpa.repository.MessageRepository;
import com.jonki.popcorn.core.jpa.repository.UserRepository;
import com.jonki.popcorn.core.service.AuthorizationService;
import com.jonki.popcorn.core.service.MessagePersistenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * JPA implementation of the Message Persistence Service.
 */
@Service("messagePersistenceService")
@Slf4j
@Transactional(
        rollbackFor = {
                ResourceNotFoundException.class,
                ResourceConflictException.class,
                ConstraintViolationException.class
        }
)
@Validated
public class MessagePersistenceServiceImpl implements MessagePersistenceService {

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
    public MessagePersistenceServiceImpl(
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
    public String createMessage(
            @NotNull @Valid final SendMessageDTO sendMessageDTO
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with sendMessageDTO {}", sendMessageDTO);

        final UserEntity user = this.findUser(this.authorizationService.getUserId());
        final MessageEntity message = this.sendMessageDtoToMessageEntity(sendMessageDTO);

        message.setSender(user);

        if(message.getRecipient().getUniqueId().equals(user.getUniqueId())) {
            throw new ResourceConflictException("The recipient's ID can't be the same as the sender's ID");
        }

        user.getSentMessages().add(message);

        this.userRepository.save(user);
        return Iterables.getLast(user.getSentMessages()).getUniqueId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteMessageSent(
            @NotBlank final String id
    ) throws ResourceNotFoundException {
        log.info("Called with id {}", id);

        final UserEntity user = this.findUser(this.authorizationService.getUserId());
        final MessageEntity message
                = this.messageRepository.findByUniqueIdAndSenderAndIsVisibleForSenderTrue(id, user)
                         .orElseThrow(() -> new ResourceNotFoundException("No message sent found with id " + id));

        message.setVisibleForSender(false);

        this.messageRepository.save(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteMessageReceived(
            @NotBlank final String id
    ) throws ResourceNotFoundException {
        log.info("Called with id {}", id);

        final UserEntity user = this.findUser(this.authorizationService.getUserId());
        final MessageEntity message
                = this.messageRepository.findByUniqueIdAndRecipientAndIsVisibleForRecipientTrue(id, user)
                         .orElseThrow(() -> new ResourceNotFoundException("No message received found with id " + id));

        message.setVisibleForRecipient(false);

        this.messageRepository.save(message);
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

    /**
     * Converter SendMessageDTO to MessageEntity.
     *
     * @param sendMessageDTO SendMessageDTO object
     * @return The message entity
     */
    private MessageEntity sendMessageDtoToMessageEntity(final SendMessageDTO sendMessageDTO) {
        final UserEntity user = this.userRepository.findOneByUsernameIgnoreCaseAndEnabledTrue(sendMessageDTO.getTo());
        if(user == null) throw new ResourceNotFoundException("No user found with username " + sendMessageDTO.getTo());

        final MessageEntity message = new MessageEntity();
        message.setRecipient(user);
        message.setSubject(sendMessageDTO.getSubject());
        message.setText(sendMessageDTO.getText());
        message.setVisibleForSender(true);
        message.setVisibleForRecipient(true);

        return message;
    }
}

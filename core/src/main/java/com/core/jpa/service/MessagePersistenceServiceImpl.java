package com.core.jpa.service;

import com.common.dto.request.SendMessageDTO;
import com.common.exception.ResourceConflictException;
import com.common.exception.ResourceNotFoundException;
import com.core.jpa.entity.MessageEntity;
import com.core.jpa.entity.UserEntity;
import com.core.jpa.repository.MessageRepository;
import com.core.jpa.repository.UserRepository;
import com.core.service.MessagePersistenceService;
import com.google.common.collect.Iterables;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
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

    /**
     * Constructor.
     *
     * @param messageRepository The message repository to use
     * @param userRepository The user repository to use
     */
    @Autowired
    public MessagePersistenceServiceImpl(
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
    public String createMessage(
            @NotBlank final String senderId,
            @NotNull @Valid SendMessageDTO sendMessageDTO
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with senderId {}, sendMessageDTO {}", senderId, sendMessageDTO);

        final UserEntity user = this.findUser(senderId);
        final MessageEntity message = this.sendMessageDtoToMessageEntity(sendMessageDTO);

        message.setSender(user);

        if(message.getRecipient().getUniqueId().equals(senderId)) {
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
            @NotBlank final String messageId,
            @NotBlank final String userId
    ) throws ResourceNotFoundException {
        log.info("Called with messageId {}, userId {}", messageId, userId);

        final UserEntity user = this.findUser(userId);
        final MessageEntity message
                = this.messageRepository.findByUniqueIdAndSenderAndIsVisibleForSenderTrue(messageId, user)
                         .orElseThrow(() -> new ResourceNotFoundException("No message sent found with id " + messageId));

        message.setVisibleForSender(false);

        this.messageRepository.save(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteMessageReceived(
            @NotBlank final String messageId,
            @NotBlank final String userId
    ) throws ResourceNotFoundException {
        log.info("Called with messageId {}, userId {}", messageId, userId);

        final UserEntity user = this.findUser(userId);
        final MessageEntity message
                = this.messageRepository.findByUniqueIdAndRecipientAndIsVisibleForRecipientTrue(messageId, user)
                         .orElseThrow(() -> new ResourceNotFoundException("No message received found with id " + messageId));

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

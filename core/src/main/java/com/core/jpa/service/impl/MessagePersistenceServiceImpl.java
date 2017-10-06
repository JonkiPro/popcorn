package com.core.jpa.service.impl;

import com.common.dto.request.SendMessageDTO;
import com.common.exception.ResourceConflictException;
import com.common.exception.ResourceNotFoundException;
import com.core.jpa.entity.MessageEntity;
import com.core.jpa.entity.UserEntity;
import com.core.jpa.repository.MessageRepository;
import com.core.jpa.repository.UserRepository;
import com.core.jpa.service.MessagePersistenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Min;
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
     */
    @Autowired
    public MessagePersistenceServiceImpl(
            @NotNull final MessageRepository messageRepository,
            @NotNull final UserRepository userRepository
    ) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Long createMessage(
            @Min(1) final Long senderId,
            @NotNull @Valid SendMessageDTO sendMessageDTO
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with senderId {}, {}", senderId, sendMessageDTO);

        final MessageEntity message = this.sendMessageDtoToMessageEntity(sendMessageDTO);

        message.setSender(this.userRepository.findByIdAndEnabledTrue(senderId)
                                .orElseThrow(() -> new ResourceNotFoundException("No user found with id " + senderId)));

        if(message.getRecipient().getId().equals(senderId)) {
            throw new ResourceConflictException("The recipient's ID can't be the same as the sender's ID");
        }

        return this.messageRepository.save(message).getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteMessageSent(
            @Min(1) final Long id,
            @Min(1) final Long userId
    ) throws ResourceNotFoundException {
        log.info("Called with id {}, userId {}", id, userId);

        final UserEntity user
                = this.userRepository.findByIdAndEnabledTrue(userId)
                         .orElseThrow(() -> new ResourceNotFoundException("No user found with id " + userId));

        final MessageEntity message
                = this.messageRepository.findByIdAndSenderAndIsVisibleForSenderTrue(id, user)
                         .orElseThrow(() -> new ResourceNotFoundException("No message sent found with id " + id));

        message.setVisibleForSender(false);

        this.messageRepository.save(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteMessageReceived(
            @Min(1) final Long id,
            @Min(1) final Long userId
    ) throws ResourceNotFoundException {
        log.info("Called with id {}, userId {}", id, userId);

        final UserEntity user
                = this.userRepository.findByIdAndEnabledTrue(userId)
                         .orElseThrow(() -> new ResourceNotFoundException("No user found with id " + userId));

        final MessageEntity message
                = this.messageRepository.findByIdAndRecipientAndIsVisibleForRecipientTrue(id, user)
                         .orElseThrow(() -> new ResourceNotFoundException("No message received found with id " + id));

        message.setVisibleForRecipient(false);

        this.messageRepository.save(message);
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

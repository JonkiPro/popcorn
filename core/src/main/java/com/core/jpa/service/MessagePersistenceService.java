package com.core.jpa.service;

import com.common.dto.request.SendMessageDTO;
import com.common.exception.ResourceConflictException;
import com.common.exception.ResourceNotFoundException;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Interfaces for providing persistence functions for messages other than search.
 */
@Validated
public interface MessagePersistenceService {

    /**
     * Create message with DTO data.
     *
     * @param sendMessageDTO DTO with message data
     * @return The id of the message created
     * @throws ResourceNotFoundException if no user found
     * @throws ResourceConflictException if any data conflict
     */
    Long createMessage(
            @Min(1) final Long senderId,
            @NotNull @Valid final SendMessageDTO sendMessageDTO
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Remove message sent.
     *
     * @param id The message ID
     * @param userId The user ID
     * @throws ResourceNotFoundException if no message found
     */
    void deleteMessageSent(
            @Min(1) final Long id,
            @Min(1) final Long userId
    ) throws ResourceNotFoundException;

    /**
     * Remove message received.
     *
     * @param id The message ID
     * @param userId The user ID
     * @throws ResourceNotFoundException if no message found
     */
    void deleteMessageReceived(
            @Min(1) final Long id,
            @Min(1) final Long userId
    ) throws ResourceNotFoundException;
}

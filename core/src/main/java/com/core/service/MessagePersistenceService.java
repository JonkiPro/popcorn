package com.core.service;

import com.common.dto.request.SendMessageDTO;
import com.common.exception.ResourceConflictException;
import com.common.exception.ResourceNotFoundException;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Interfaces for providing persistence functions for messages other than search.
 */
@Validated
public interface MessagePersistenceService {

    /**
     * Create message with DTO data.
     *
     * @param senderId The user ID
     * @param sendMessageDTO DTO with message data
     * @return The id of the message created
     * @throws ResourceNotFoundException if no user found
     * @throws ResourceConflictException if any data conflict
     */
    String createMessage(
            @NotBlank final String senderId,
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
            @NotBlank final String id,
            @NotBlank final String userId
    ) throws ResourceNotFoundException;

    /**
     * Remove message received.
     *
     * @param id The message ID
     * @param userId The user ID
     * @throws ResourceNotFoundException if no message found
     */
    void deleteMessageReceived(
            @NotBlank final String id,
            @NotBlank final String userId
    ) throws ResourceNotFoundException;
}

package com.jonki.popcorn.core.service;

import com.jonki.popcorn.common.dto.request.MessageRequest;
import com.jonki.popcorn.common.exception.ResourceConflictException;
import com.jonki.popcorn.common.exception.ResourceNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Interfaces for providing persistence functions for messages other than search.
 */
@PreAuthorize("hasRole('ROLE_USER')")
@Validated
public interface MessagePersistenceService {

    /**
     * Create message with DTO data.
     *
     * @param messageRequest DTO with message data
     * @return The id of the message created
     * @throws ResourceNotFoundException if no user found
     * @throws ResourceConflictException if any data conflict
     */
    String createMessage(
            @NotNull @Valid final MessageRequest messageRequest
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Remove message sent.
     *
     * @param id The message ID
     * @throws ResourceNotFoundException if no message found
     */
    void deleteMessageSent(
            @NotBlank final String id
    ) throws ResourceNotFoundException;

    /**
     * Remove message received.
     *
     * @param id The message ID
     * @throws ResourceNotFoundException if no message found
     */
    void deleteMessageReceived(
            @NotBlank final String id
    ) throws ResourceNotFoundException;
}

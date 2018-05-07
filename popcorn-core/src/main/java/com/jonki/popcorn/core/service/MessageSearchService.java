package com.jonki.popcorn.core.service;

import com.jonki.popcorn.common.dto.MessageReceived;
import com.jonki.popcorn.common.dto.MessageSent;
import com.jonki.popcorn.common.exception.ResourceNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Nullable;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Interface for searching messages.
 */
@PreAuthorize("hasRole('ROLE_USER')")
@Validated
public interface MessageSearchService {

    /**
     * Get message sent by message ID.
     *
     * @param id The message ID
     * @return The message sent
     * @throws ResourceNotFoundException if no user found or no message found
     */
    MessageSent getMessageSent(
            @NotBlank final String id
    ) throws ResourceNotFoundException;

    /**
     * Get messages sent by content.
     *
     * @param content The message content
     * @return The messages sent
     * @throws ResourceNotFoundException if no user found
     */
    List<MessageSent> getMessagesSent(
            @Nullable final String content
    ) throws ResourceNotFoundException;

    /**
     * Get message received by message ID.
     *
     * @param id The message ID
     * @return The message received
     * @throws ResourceNotFoundException if no user found or no message found
     */
    MessageReceived getMessageReceived(
            @NotBlank final String id
    ) throws ResourceNotFoundException;

    /**
     * Get messages received by content.
     *
     * @param content The message content
     * @return The messages received
     * @throws ResourceNotFoundException if no user found
     */
    List<MessageReceived> getMessagesReceived(
            @Nullable final String content
    ) throws ResourceNotFoundException;

    /**
     * Check whether the message sent exists.
     *
     * @param id The message ID
     * @return True if the message exists
     * @throws ResourceNotFoundException if no user found
     */
    boolean existsMessageSent(
            @NotBlank final String id
    ) throws ResourceNotFoundException;

    /**
     * Check whether the message received exists.
     *
     * @param id The message ID
     * @return True if the message exists
     * @throws ResourceNotFoundException if no user found
     */
    boolean existsMessageReceived(
            @NotBlank final String id
    ) throws ResourceNotFoundException;
}

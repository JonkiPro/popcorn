package com.core.service;

import com.common.dto.MessageReceived;
import com.common.dto.MessageSent;
import com.common.exception.ResourceNotFoundException;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Interface for searching messages.
 */
@Validated
public interface MessageSearchService {

    /**
     * Get message sent by message ID and user ID.
     *
     * @param messageId The message ID
     * @param userId The user ID
     * @return The message sent
     * @throws ResourceNotFoundException if no user found or no message found
     */
    MessageSent getMessageSent(
            @NotBlank final String messageId,
            @NotBlank final String userId
    ) throws ResourceNotFoundException;

    /**
     * Get messages sent by user ID and content.
     *
     * @param userId The user ID
     * @param content The message content
     * @return The messages sent
     * @throws ResourceNotFoundException if no user found
     */
    List<MessageSent> getMessagesSent(
            @NotBlank final String userId,
            @Nullable final String content
    ) throws ResourceNotFoundException;

    /**
     * Get message received by message ID an user ID.
     *
     * @param messageId The message ID
     * @param userId The user ID
     * @return The message received
     * @throws ResourceNotFoundException if no user found or no message found
     */
    MessageReceived getMessageReceived(
            @NotBlank final String messageId,
            @NotBlank final String userId
    ) throws ResourceNotFoundException;

    /**
     * Get messages received by user ID and content.
     *
     * @param userId The user ID
     * @param content The message content
     * @return The messages received
     * @throws ResourceNotFoundException if no user found
     */
    List<MessageReceived> getMessagesReceived(
            @NotBlank final String userId,
            @Nullable final String content
    ) throws ResourceNotFoundException;

    /**
     * Check whether the message sent exists.
     *
     * @param messageId The message ID
     * @param userId The user ID
     * @return True if the message exists
     * @throws ResourceNotFoundException if no user found
     */
    boolean existsMessageSent(
            @NotBlank final String messageId,
            @NotBlank final String userId
    ) throws ResourceNotFoundException;

    /**
     * Check whether the message received exists.
     *
     * @param messageId The message ID
     * @param userId The user ID
     * @return True if the message exists
     * @throws ResourceNotFoundException if no user found
     */
    boolean existsMessageReceived(
            @NotBlank final String messageId,
            @NotBlank final String userId
    ) throws ResourceNotFoundException;
}

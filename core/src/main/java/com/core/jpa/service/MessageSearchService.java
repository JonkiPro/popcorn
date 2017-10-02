package com.core.jpa.service;

import com.common.dto.MessageReceived;
import com.common.dto.MessageSent;
import com.common.exception.ResourceNotFoundException;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import java.util.List;

/**
 * Interface for searching messages.
 */
@Validated
public interface MessageSearchService {

    /**
     * Get message sent by message ID an user ID.
     *
     * @param messageId The message ID
     * @param userId The user ID
     * @return The message sent
     * @throws ResourceNotFoundException if no user found or no message found
     */
    MessageSent getMessageSent(
            @Min(1) final Long messageId,
            @Min(1) final Long userId
    ) throws ResourceNotFoundException;

    /**
     * Get messages sent by user ID.
     *
     * @param userId The user ID
     * @return The messages sent
     * @throws ResourceNotFoundException if no user found
     */
    List<MessageSent> getMessagesSent(
            @Min(1) final Long userId
    ) throws ResourceNotFoundException;

    /**
     * Get message sent by user ID and content.
     *
     * @param userId The user ID
     * @param content The message content
     * @return The messages sent
     * @throws ResourceNotFoundException if no user found
     */
    List<MessageSent> getMessagesSent(
            @Min(1) final Long userId,
            final String content
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
            @Min(1) final Long messageId,
            @Min(1) final Long userId
    ) throws ResourceNotFoundException;

    /**
     * Get messages received by user ID.
     *
     * @param userId The user ID
     * @return The messages received
     * @throws ResourceNotFoundException if no user found
     */
    List<MessageReceived> getMessagesReceived(
            @Min(1) final Long userId
    ) throws ResourceNotFoundException;

    /**
     * Get message received by user ID and content.
     *
     * @param userId The user ID
     * @param content The message content
     * @return The messages received
     * @throws ResourceNotFoundException if no user found
     */
    List<MessageReceived> getMessagesReceived(
            @Min(1) final Long userId,
            final String content
    ) throws ResourceNotFoundException;

    /**
     * Check whether the message sent exists.
     *
     * @param messageId The message ID
     * @param userId The user ID
     * @return True if the message exists
     * @throws ResourceNotFoundException if no user found
     */
    boolean getMessageSentExists(
            @Min(1) final Long messageId,
            @Min(1) final Long userId
    );

    /**
     * Check whether the message received exists.
     *
     * @param messageId The message ID
     * @param userId The user ID
     * @return True if the message exists
     * @throws ResourceNotFoundException if no user found
     */
    boolean getMessageReceivedExists(
            @Min(1) final Long messageId,
            @Min(1) final Long userId
    );
}

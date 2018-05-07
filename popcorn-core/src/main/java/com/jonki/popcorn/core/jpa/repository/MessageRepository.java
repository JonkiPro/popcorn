package com.jonki.popcorn.core.jpa.repository;

import com.jonki.popcorn.core.jpa.entity.MessageEntity;
import com.jonki.popcorn.core.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * Message repository.
 */
public interface MessageRepository extends JpaRepository<MessageEntity, Long>, JpaSpecificationExecutor {

    /**
     * Find the message by ID and recipient.
     *
     * @param id The message ID
     * @param recipient Recipient user
     * @return The message
     */
    Optional<MessageEntity> findByUniqueIdAndRecipientAndIsVisibleForRecipientTrue(final String id, final UserEntity recipient);

    /**
     * Find the message by ID and sender.
     *
     * @param id The message ID
     * @param sender Sender user
     * @return The message
     */
    Optional<MessageEntity> findByUniqueIdAndSenderAndIsVisibleForSenderTrue(final String id, final UserEntity sender);

    /**
     * Check whether the message sent exists.
     *
     * @param id The message ID
     * @param recipient Recipient user
     * @return True if the message exists
     */
    boolean existsByUniqueIdAndRecipientAndIsVisibleForRecipientTrue(final String id, final UserEntity recipient);

    /**
     * Check whether the message received exists.
     *
     * @param id The message ID
     * @param sender Sender user
     * @return True if the message exists
     */
    boolean existsByUniqueIdAndSenderAndIsVisibleForSenderTrue(final String id, final UserEntity sender);
}

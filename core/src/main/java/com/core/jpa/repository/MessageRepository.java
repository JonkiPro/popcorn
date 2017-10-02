package com.core.jpa.repository;

import com.core.jpa.entity.MessageEntity;
import com.core.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Message repository.
 */
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    /**
     * Find the message by recipient, subject and text
     *
     * @param recipient Recipient user
     * @param subject Message subject
     * @param text Message text
     * @return Messages
     */
    @Modifying
    @Query("SELECT e FROM MessageEntity e WHERE e.recipient = :recipient AND (UPPER(e.subject) LIKE UPPER(:subject) OR UPPER(e.text) LIKE UPPER(:text)) AND e.isVisibleForRecipient = true ORDER BY id DESC")
    List<MessageEntity> findReceivedMessagesByContaining(@Param("recipient") UserEntity recipient, @Param("subject") String subject, @Param("text") String text);

    /**
     * Find the message by sender, subject and text
     *
     * @param sender Sender user
     * @param subject Message subject
     * @param text Message text
     * @return Messages
     */
    @Modifying
    @Query("SELECT e FROM MessageEntity e WHERE e.sender = :sender AND (UPPER(e.subject) LIKE UPPER(:subject) OR UPPER(e.text) LIKE UPPER(:text)) AND e.isVisibleForSender = true ORDER BY id DESC")
    List<MessageEntity> findSentMessagesByContaining(@Param("sender") UserEntity sender, @Param("subject") String subject, @Param("text") String text);

    /**
     * Find the received messages.
     *
     * @param recipient Recipient user
     * @return Messages
     */
    List<MessageEntity> findByRecipientAndIsVisibleForRecipientTrueOrderByIdDesc(UserEntity recipient);

    /**
     * Find the sent messages.
     *
     * @param sender Sender user
     * @return Messages
     */
    List<MessageEntity> findBySenderAndIsVisibleForSenderTrueOrderByIdDesc(UserEntity sender);

    /**
     * Find the message by ID and recipient.
     *
     * @param id The message ID
     * @param recipient Recipient user
     * @return The message
     */
    Optional<MessageEntity> findByIdAndRecipientAndIsVisibleForRecipientTrue(Long id, UserEntity recipient);

    /**
     * Find the message by ID and sender.
     *
     * @param id The message ID
     * @param sender Sender user
     * @return The message
     */
    Optional<MessageEntity> findByIdAndSenderAndIsVisibleForSenderTrue(Long id, UserEntity sender);

    /**
     * Check whether the message sent exists.
     *
     * @param id The message ID
     * @param recipient Recipient user
     * @return True if the message exists
     */
    boolean existsByIdAndRecipientAndIsVisibleForRecipientTrue(Long id, UserEntity recipient);

    /**
     * Check whether the message received exists.
     *
     * @param id The message ID
     * @param sender Sender user
     * @return True if the message exists
     */
    boolean existsByIdAndSenderAndIsVisibleForSenderTrue(Long id, UserEntity sender);
}

package com.service.app.repository;

import com.service.app.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByRecipientAndIsVisibleForRecipientTrueOrderByIdDesc(Long recipient);
    List<Message> findBySenderAndIsVisibleForSenderTrueOrderByIdDesc(Long sender);

    Optional<Message> findById(Long id);
    Optional<Message> findByIdAndRecipientAndIsVisibleForRecipientTrue(Long id, Long recipient);
    Optional<Message> findByIdAndSenderAndIsVisibleForSenderTrue(Long id, Long sender);

    @Query("SELECT e FROM Message e WHERE e.recipient = :recipient AND (UPPER(e.subject) LIKE UPPER(:subject) OR UPPER(e.text) LIKE UPPER(:text)) AND e.isVisibleForRecipient = true ORDER BY id DESC")
    List<Message> findReceivedMessagesByContaining(@Param("recipient") Long recipient, @Param("subject") String subject, @Param("text") String text);

    @Query("SELECT e FROM Message e WHERE e.sender = :sender AND (UPPER(e.subject) LIKE UPPER(:subject) OR UPPER(e.text) LIKE UPPER(:text)) AND e.isVisibleForSender = true ORDER BY id DESC")
    List<Message> findSentMessagesByContaining(@Param("sender") Long sender, @Param("subject") String subject, @Param("text") String text);
}

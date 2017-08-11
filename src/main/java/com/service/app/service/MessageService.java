package com.service.app.service;

import com.service.app.entity.Message;

import java.util.List;
import java.util.Optional;

public interface MessageService {

    void saveMessage(Message message);

    List<Message> findByRecipient(Long recipient);
    List<Message> findBySender(Long sender);

    boolean removeReceivedMessage(Long id);
    boolean removeSentMessage(Long id);

    Optional<Message> findById(Long id);
    Optional<Message> findByIdAndRecipient(Long id, Long recipient);
    Optional<Message> findByIdAndSender(Long id, Long sender);

    List<Message> findReceivedMessagesByContaining(Long recipient, String containing);
    List<Message> findSentMessagesByContaining(Long sender, String containing);
}

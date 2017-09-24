package com.service.app.service;

import com.service.app.entity.Message;
import com.service.app.entity.User;

import java.util.List;
import java.util.Optional;

public interface MessageService {

    void saveMessage(Message message);

    List<Message> findByRecipient(User recipient);
    List<Message> findBySender(User sender);

    boolean removeReceivedMessage(Long id);
    boolean removeSentMessage(Long id);

    Optional<Message> findById(Long id);
    Optional<Message> findByIdAndRecipient(Long id, User recipient);
    Optional<Message> findByIdAndSender(Long id, User sender);

    List<Message> findReceivedMessagesByContaining(User recipient, String containing);
    List<Message> findSentMessagesByContaining(User sender, String containing);
}

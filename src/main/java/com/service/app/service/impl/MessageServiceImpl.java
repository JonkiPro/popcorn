package com.service.app.service.impl;

import com.service.app.entity.Message;
import com.service.app.entity.User;
import com.service.app.repository.MessageRepository;
import com.service.app.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service("messageService")
public class MessageServiceImpl implements MessageService {

    private MessageRepository messageRepository;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void saveMessage(Message message) {
        messageRepository.save(message);
    }

    @Override
    public List<Message> findByRecipient(User recipient) {
        return messageRepository.findByRecipientAndIsVisibleForRecipientTrueOrderByIdDesc(recipient);
    }

    @Override
    public List<Message> findBySender(User sender) {
        return messageRepository.findBySenderAndIsVisibleForSenderTrueOrderByIdDesc(sender);
    }

    @Override
    public boolean removeReceivedMessage(Long id) {
        return this.findById(id)
                .map(message -> {
                    message.setVisibleForRecipient(false);

                    this.saveMessage(message);

                    return true;
                }).orElse(false);
    }

    @Override
    public boolean removeSentMessage(Long id) {
        return this.findById(id)
                .map(message -> {
                    message.setVisibleForSender(false);

                    this.saveMessage(message);

                    return true;
                }).orElse(false);
    }

    @Override
    public Optional<Message> findById(Long id) {
        return messageRepository.findById(id);
    }

    @Override
    public Optional<Message> findByIdAndRecipient(Long id, User recipient) {
        Optional<Message> messageOptional
                =  messageRepository.findByIdAndRecipientAndIsVisibleForRecipientTrue(id, recipient);

        messageOptional.ifPresent(message -> {
            if(message.getDateOfRead() == null) {
                message.setDateOfRead(new Date());

                this.saveMessage(message);
            }
        });

        return messageOptional;
    }

    @Override
    public Optional<Message> findByIdAndSender(Long id, User sender) {
        return messageRepository.findByIdAndSenderAndIsVisibleForSenderTrue(id, sender);
    }

    @Override
    public List<Message> findReceivedMessagesByContaining(User recipient, String containing) {
        return messageRepository.findReceivedMessagesByContaining(recipient, "%"+containing.trim()+"%", "%"+containing.trim()+"%");
    }

    @Override
    public List<Message> findSentMessagesByContaining(User sender, String containing) {
        return messageRepository.findSentMessagesByContaining(sender, "%"+containing.trim()+"%", "%"+containing.trim()+"%");
    }
}

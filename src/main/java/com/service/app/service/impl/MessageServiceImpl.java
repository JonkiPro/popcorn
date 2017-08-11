package com.service.app.service.impl;

import com.service.app.entity.Message;
import com.service.app.repository.MessageRepository;
import com.service.app.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<Message> findByRecipient(Long recipient) {
        return messageRepository.findByRecipientAndIsVisibleForRecipientTrueOrderByIdDesc(recipient);
    }

    @Override
    public List<Message> findBySender(Long sender) {
        return messageRepository.findBySenderAndIsVisibleForSenderTrueOrderByIdDesc(sender);
    }

    @Override
    public boolean removeReceivedMessage(Long id) {
        Optional<Message> messageOptional = this.findById(id);

        if(messageOptional.isPresent()) {
            Message message = messageOptional.get();
            message.setVisibleForRecipient(false);

            this.saveMessage(message);

            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean removeSentMessage(Long id) {
        Optional<Message> messageOptional = this.findById(id);

        if(messageOptional.isPresent()) {
            Message message = messageOptional.get();
            message.setVisibleForSender(false);

            this.saveMessage(message);

            return true;
        } else {
            return false;
        }
    }

    @Override
    public Optional<Message> findById(Long id) {
        return messageRepository.findById(id);
    }

    @Override
    public Optional<Message> findByIdAndRecipient(Long id, Long recipient) {
        return messageRepository.findByIdAndRecipientAndIsVisibleForRecipientTrue(id, recipient);
    }

    @Override
    public Optional<Message> findByIdAndSender(Long id, Long sender) {
        return messageRepository.findByIdAndSenderAndIsVisibleForSenderTrue(id, sender);
    }

    @Override
    public List<Message> findReceivedMessagesByContaining(Long recipient, String containing) {
        return messageRepository.findReceivedMessagesByContaining(recipient, "%"+containing+"%", "%"+containing+"%");
    }

    @Override
    public List<Message> findSentMessagesByContaining(Long sender, String containing) {
        return messageRepository.findSentMessagesByContaining(sender, "%"+containing+"%", "%"+containing+"%");
    }
}

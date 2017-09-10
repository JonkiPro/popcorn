package com.service.app.converter.impl;

import com.service.app.converter.UnidirectionalConverter;
import com.service.app.rest.response.SentMessageDTO;
import com.service.app.entity.Message;
import com.service.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageToSentMessageDTO implements UnidirectionalConverter<Message, SentMessageDTO> {

    @Autowired
    private UserService userService;

    @Override
    @SuppressWarnings("ConstantConditions")
    public SentMessageDTO convert(Message message) {
        SentMessageDTO sentMessageDTO = new SentMessageDTO();
        sentMessageDTO.setId(message.getId());
        sentMessageDTO.setRecipient(userService.findById(message.getRecipient()).get().getUsername());
        sentMessageDTO.setSubject(message.getSubject());
        sentMessageDTO.setText(message.getText());

        return sentMessageDTO;
    }
}

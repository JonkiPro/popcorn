package com.service.app.converter.impl;

import com.service.app.converter.UnidirectionalConverter;
import com.service.app.rest.response.SentMessageDTO;
import com.service.app.entity.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageToSentMessageDTO implements UnidirectionalConverter<Message, SentMessageDTO> {

    @Override
    public SentMessageDTO convert(Message message) {
        SentMessageDTO sentMessageDTO = new SentMessageDTO();
        sentMessageDTO.setId(message.getId());
        sentMessageDTO.setRecipient(message.getRecipient().getUsername());
        sentMessageDTO.setSubject(message.getSubject());
        sentMessageDTO.setText(message.getText());

        return sentMessageDTO;
    }
}

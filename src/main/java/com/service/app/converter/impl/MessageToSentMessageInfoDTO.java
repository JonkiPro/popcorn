package com.service.app.converter.impl;

import com.service.app.converter.UnidirectionalConverter;
import com.service.app.rest.response.SentMessageInfoDTO;
import com.service.app.entity.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageToSentMessageInfoDTO implements UnidirectionalConverter<Message, SentMessageInfoDTO> {

    @Override
    public SentMessageInfoDTO convert(Message message) {
        SentMessageInfoDTO sentMessageInfoDTO = new SentMessageInfoDTO();
        sentMessageInfoDTO.setId(message.getId());
        sentMessageInfoDTO.setRecipient(message.getRecipient().getUsername());
        sentMessageInfoDTO.setSubject(message.getSubject());
        sentMessageInfoDTO.setText(message.getText());
        sentMessageInfoDTO.setDate(message.getDate());

        return sentMessageInfoDTO;
    }
}

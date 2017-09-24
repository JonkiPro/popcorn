package com.service.app.converter.impl;

import com.service.app.converter.UnidirectionalConverter;
import com.service.app.rest.response.ReceivedMessageInfoDTO;
import com.service.app.entity.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageToReceivedMessageInfoDTO implements UnidirectionalConverter<Message, ReceivedMessageInfoDTO> {

    @Override
    public ReceivedMessageInfoDTO convert(Message message) {
        ReceivedMessageInfoDTO receivedMessageInfoDTO = new ReceivedMessageInfoDTO();
        receivedMessageInfoDTO.setId(message.getId());
        receivedMessageInfoDTO.setSender(message.getSender().getUsername());
        receivedMessageInfoDTO.setSubject(message.getSubject());
        receivedMessageInfoDTO.setText(message.getText());
        receivedMessageInfoDTO.setDate(message.getDate());

        return receivedMessageInfoDTO;
    }
}

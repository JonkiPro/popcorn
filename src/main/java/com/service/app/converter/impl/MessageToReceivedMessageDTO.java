package com.service.app.converter.impl;

import com.service.app.converter.UnidirectionalConverter;
import com.service.app.rest.response.ReceivedMessageDTO;
import com.service.app.entity.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageToReceivedMessageDTO implements UnidirectionalConverter<Message, ReceivedMessageDTO> {

    @Override
    public ReceivedMessageDTO convert(Message message) {
        ReceivedMessageDTO receivedMessageDTO = new ReceivedMessageDTO();
        receivedMessageDTO.setId(message.getId());
        receivedMessageDTO.setSender(message.getSender().getUsername());
        receivedMessageDTO.setSubject(message.getSubject());
        receivedMessageDTO.setText(message.getText());
        receivedMessageDTO.setDateOfRead(message.getDateOfRead());

        return receivedMessageDTO;
    }
}

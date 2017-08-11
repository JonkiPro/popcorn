package com.service.app.converter.impl;

import com.service.app.converter.UnidirectionalConverter;
import com.service.app.dto.out.ReceivedMessageDTO;
import com.service.app.entity.Message;
import com.service.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageToReceivedMessageDTO implements UnidirectionalConverter<Message, ReceivedMessageDTO> {

    @Autowired
    private UserService userService;

    @Override
    @SuppressWarnings("ConstantConditions")
    public ReceivedMessageDTO convert(Message message) {
        ReceivedMessageDTO receivedMessageDTO = new ReceivedMessageDTO();
        receivedMessageDTO.setId(message.getId());
        receivedMessageDTO.setSender(userService.findById(message.getSender()).get().getUsername());
        receivedMessageDTO.setSubject(message.getSubject());
        receivedMessageDTO.setText(message.getText());
        receivedMessageDTO.setDateOfRead(message.getDateOfRead());

        return receivedMessageDTO;
    }
}

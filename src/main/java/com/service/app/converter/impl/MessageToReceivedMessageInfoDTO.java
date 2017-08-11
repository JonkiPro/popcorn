package com.service.app.converter.impl;

import com.service.app.converter.UnidirectionalConverter;
import com.service.app.dto.out.ReceivedMessageInfoDTO;
import com.service.app.entity.Message;
import com.service.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageToReceivedMessageInfoDTO implements UnidirectionalConverter<Message, ReceivedMessageInfoDTO> {

    @Autowired
    private UserService userService;

    @Override
    @SuppressWarnings("ConstantConditions")
    public ReceivedMessageInfoDTO convert(Message message) {
        ReceivedMessageInfoDTO receivedMessageInfoDTO = new ReceivedMessageInfoDTO();
        receivedMessageInfoDTO.setId(message.getId());
        receivedMessageInfoDTO.setSender(userService.findById(message.getSender()).get().getUsername());
        receivedMessageInfoDTO.setSubject(message.getSubject());
        receivedMessageInfoDTO.setText(message.getText());
        receivedMessageInfoDTO.setDate(message.getDate());

        return receivedMessageInfoDTO;
    }
}

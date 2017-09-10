package com.service.app.converter.impl;

import com.service.app.converter.UnidirectionalConverter;
import com.service.app.rest.response.SentMessageInfoDTO;
import com.service.app.entity.Message;
import com.service.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageToSentMessageInfoDTO implements UnidirectionalConverter<Message, SentMessageInfoDTO> {

    @Autowired
    private UserService userService;

    @Override
    @SuppressWarnings("ConstantConditions")
    public SentMessageInfoDTO convert(Message message) {
        SentMessageInfoDTO sentMessageInfoDTO = new SentMessageInfoDTO();
        sentMessageInfoDTO.setId(message.getId());
        sentMessageInfoDTO.setRecipient(userService.findById(message.getRecipient()).get().getUsername());
        sentMessageInfoDTO.setSubject(message.getSubject());
        sentMessageInfoDTO.setText(message.getText());
        sentMessageInfoDTO.setDate(message.getDate());

        return sentMessageInfoDTO;
    }
}

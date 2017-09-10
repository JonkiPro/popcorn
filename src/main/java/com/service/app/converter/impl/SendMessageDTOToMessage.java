package com.service.app.converter.impl;

import com.service.app.converter.UnidirectionalConverter;
import com.service.app.rest.request.SendMessageDTO;
import com.service.app.entity.Message;
import com.service.app.service.AuthorizationService;
import com.service.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SendMessageDTOToMessage implements UnidirectionalConverter<SendMessageDTO, Message> {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthorizationService authorizationService;

    final private boolean VISIBLE_FOR_SENDER = true;
    final private boolean VISIBLE_FOR_RECIPIENT = true;

    @Override
    @SuppressWarnings("ConstantConditions")
    public Message convert(SendMessageDTO sendMessageDTO) {
        Message message = new Message();
        message.setSender(authorizationService.getUserId());
        message.setRecipient(userService.findByUsername(sendMessageDTO.getTo()).get().getId());
        message.setSubject(sendMessageDTO.getSubject());
        message.setText(sendMessageDTO.getText());
        message.setVisibleForSender(VISIBLE_FOR_SENDER);
        message.setVisibleForRecipient(VISIBLE_FOR_RECIPIENT);

        return message;
    }
}
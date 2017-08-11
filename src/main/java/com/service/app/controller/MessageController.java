package com.service.app.controller;

import com.service.app.entity.Message;
import com.service.app.exception.AccessToMessageForbiddenException;
import com.service.app.service.AuthorizationService;
import com.service.app.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Objects;

@Controller
@PreAuthorize("hasRole('ROLE_USER')")
public class MessageController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private AuthorizationService authorizationService;

    @GetMapping("/messages")
    public ModelAndView messages() {
        return new ModelAndView("messages");
    }

    @GetMapping("/message/new")
    public ModelAndView newMessage(
            @RequestParam(required = false) String username,
            ModelMap modelMap
    ) {
        try {
            Objects.requireNonNull(username);

            modelMap.addAttribute("username", username);
            return new ModelAndView("/newQuickMessage", modelMap);
        } catch (NullPointerException e) {
            return new ModelAndView("/newMessage");
        }
    }

    @GetMapping("/message/{id}")
    public ModelAndView showMessage(
            @PathVariable Long id,
            ModelMap modelMap
    ) {
        this.validAccessToMessage(id);

        modelMap.addAttribute("messageId", id);

        if(messageService.findByRecipient(authorizationService.getUserId()).stream().anyMatch(message -> message.getId().equals(id))) {
            return new ModelAndView("showReceivedMessage", modelMap);
        } else {
            return new ModelAndView("showSentMessage", modelMap);
        }
    }

    private void validAccessToMessage(Long messageId) {
        List<Message> messageList = messageService.findBySender(authorizationService.getUserId());
        messageList.addAll(messageService.findByRecipient(authorizationService.getUserId()));

        messageList.stream()
                .filter(v -> messageId.equals(v.getId()))
                .findAny()
                .orElseThrow(AccessToMessageForbiddenException::new);
    }
}

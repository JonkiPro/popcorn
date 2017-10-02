package com.web.web.view;

import com.core.jpa.service.MessageSearchService;
import com.web.web.security.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;

@Controller
@PreAuthorize("hasRole('ROLE_USER')")
@RequestMapping(value = "/messages")
public class MessageController {

    private final MessageSearchService messageSearchService;
    private final AuthorizationService authorizationService;

    /**
     * Constructor.
     *
     * @param messageSearchService The message search service to use
     * @param authorizationService The authorization service to use
     */
    @Autowired
    public MessageController(
            final MessageSearchService messageSearchService,
            final AuthorizationService authorizationService
    ) {
        this.messageSearchService = messageSearchService;
        this.authorizationService = authorizationService;
    }

    /**
     * @return The ModelAndView for the list of messages
     */
    @GetMapping
    public ModelAndView messages() {
        return new ModelAndView("messages");
    }

    /**
     * @param username The user's name
     * @param modelMap {@link ModelMap}
     * @return The ModelAndView to create new messages
     */
    @GetMapping(value = "/new")
    public ModelAndView newMessage(
            @RequestParam(required = false) final String username,
            final ModelMap modelMap
    ) {
        try {
            Objects.requireNonNull(username);

            modelMap.addAttribute("username", username);
            return new ModelAndView("/newQuickMessage", modelMap);
        } catch (NullPointerException e) {
            return new ModelAndView("/newMessage");
        }
    }

    /**
     * @param id The message ID
     * @param modelMap {@link ModelMap}
     * @return The ModelAndView for the selected message
     */
    @GetMapping(value = "/{id}")
    public ModelAndView showMessage(
            @PathVariable final Long id,
            final ModelMap modelMap
    ) {
        final boolean messageReceivedExists = this.messageSearchService.getMessageReceivedExists(id, authorizationService.getUserId());
        if(messageReceivedExists) {
            modelMap.addAttribute("messageId", id);
            return new ModelAndView("showReceivedMessage", modelMap);
        }

        final boolean messageSentExists = this.messageSearchService.getMessageSentExists(id, authorizationService.getUserId());
        if(messageSentExists) {
            modelMap.addAttribute("messageId", id);
            return new ModelAndView("showSentMessage", modelMap);
        }

        return new ModelAndView("redirect:/messages");
    }
}

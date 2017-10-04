package com.web.web.hateoas.assembler;

import com.common.dto.MessageSent;
import com.web.web.controller.MessageRestController;
import com.web.web.hateoas.resource.MessageSentResource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;

/**
 * Assembles Message resources out of MessageSent DTOs.
 */
@Component
public class MessageSentResourceAssembler implements ResourceAssembler<MessageSent, MessageSentResource> {

    /**
     * {@inheritDoc}
     */
    @Override
    public MessageSentResource toResource(final MessageSent message) {
        final MessageSentResource messageResource = new MessageSentResource(message);

        messageResource.add(
                ControllerLinkBuilder.linkTo(
                        ControllerLinkBuilder
                                .methodOn(MessageRestController.class)
                                .getMessageSent(message.getId())
                ).withSelfRel()
        );

        return messageResource;
    }
}

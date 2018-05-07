package com.jonki.popcorn.web.hateoas.assembler;

import com.jonki.popcorn.common.dto.MessageSent;
import com.jonki.popcorn.web.controller.MessageRestController;
import com.jonki.popcorn.web.hateoas.resource.MessageSentResource;
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

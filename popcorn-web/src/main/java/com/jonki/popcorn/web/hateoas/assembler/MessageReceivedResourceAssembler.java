package com.jonki.popcorn.web.hateoas.assembler;

import com.jonki.popcorn.common.dto.MessageReceived;
import com.jonki.popcorn.web.controller.MessageRestController;
import com.jonki.popcorn.web.hateoas.resource.MessageReceivedResource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;

/**
 * Assembles Message resources out of MessageReceived DTOs.
 */
@Component
public class MessageReceivedResourceAssembler implements ResourceAssembler<MessageReceived, MessageReceivedResource> {

    /**
     * {@inheritDoc}
     */
    @Override
    public MessageReceivedResource toResource(final MessageReceived message) {
        final MessageReceivedResource messageResource = new MessageReceivedResource(message);

        messageResource.add(
                ControllerLinkBuilder.linkTo(
                        ControllerLinkBuilder
                                .methodOn(MessageRestController.class)
                                .getMessageReceived(message.getId())
                ).withSelfRel()
        );

        return messageResource;
    }
}

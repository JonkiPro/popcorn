package com.web.web.hateoas.assembler;

import com.common.dto.MessageReceived;
import com.web.web.controller.MessageRestController;
import com.web.web.hateoas.resource.MessageReceivedResource;
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

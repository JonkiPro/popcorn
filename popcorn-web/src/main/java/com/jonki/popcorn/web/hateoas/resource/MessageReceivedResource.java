package com.jonki.popcorn.web.hateoas.resource;

import com.jonki.popcorn.common.dto.MessageReceived;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.hateoas.Resource;

/**
 * HATEOAS resource representation of an MessageReceived.
 */
public class MessageReceivedResource extends Resource<MessageReceived> {

    /**
     * Constructor.
     *
     * @param message The message this resource is wrapping
     */
    @JsonCreator
    public MessageReceivedResource(final MessageReceived message) {
        super(message);
    }
}

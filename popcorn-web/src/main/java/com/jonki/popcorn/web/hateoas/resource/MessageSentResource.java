package com.jonki.popcorn.web.hateoas.resource;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.jonki.popcorn.common.dto.MessageSent;
import org.springframework.hateoas.Resource;

/**
 * HATEOAS resource representation of an MessageSent.
 */
public class MessageSentResource extends Resource<MessageSent> {

    /**
     * Constructor.
     *
     * @param message The message this resource is wrapping
     */
    @JsonCreator
    public MessageSentResource(final MessageSent message) {
        super(message);
    }
}

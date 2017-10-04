package com.web.web.hateoas.resource;

import com.common.dto.MessageSent;
import com.fasterxml.jackson.annotation.JsonCreator;
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

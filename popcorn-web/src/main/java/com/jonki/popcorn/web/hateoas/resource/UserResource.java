package com.jonki.popcorn.web.hateoas.resource;

import com.jonki.popcorn.common.dto.User;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.hateoas.Resource;

/**
 * HATEOAS resource representation of an User.
 */
public class UserResource extends Resource<User> {

    /**
     * Constructor.
     *
     * @param user The user this resource is wrapping
     */
    @JsonCreator
    public UserResource(final User user) {
        super(user);
    }
}

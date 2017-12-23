package com.web.web.hateoas.assembler;

import com.common.dto.User;
import com.web.web.controller.UserRestController;
import com.web.web.hateoas.resource.UserResource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;

/**
 * Assembles User resources out of User DTOs.
 */
@Component
public class UserResourceAssembler implements ResourceAssembler<User, UserResource> {

    /**
     * {@inheritDoc}
     */
    @Override
    public UserResource toResource(final User user) {
        final UserResource userResource = new UserResource(user);

        userResource.add(
                ControllerLinkBuilder.linkTo(
                        ControllerLinkBuilder
                                .methodOn(UserRestController.class)
                                .getProfile(user.getUsername())
                ).withSelfRel()
        );

        userResource.add(
                ControllerLinkBuilder.linkTo(
                        ControllerLinkBuilder
                                .methodOn(UserRestController.class)
                                .getFriends(user.getUsername())
                ).withRel("friends")
        );

        return userResource;
    }
}

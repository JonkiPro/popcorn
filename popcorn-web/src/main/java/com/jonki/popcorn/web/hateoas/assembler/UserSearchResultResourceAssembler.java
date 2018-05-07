package com.jonki.popcorn.web.hateoas.assembler;

import com.jonki.popcorn.common.dto.search.UserSearchResult;
import com.jonki.popcorn.web.hateoas.resource.UserSearchResultResource;
import com.jonki.popcorn.web.controller.UserRestController;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;

/**
 * Assembles User resources out of user search result DTOs.
 */
@Component
public class UserSearchResultResourceAssembler implements ResourceAssembler<UserSearchResult, UserSearchResultResource> {

    /**
     * {@inheritDoc}
     */
    @Override
    public UserSearchResultResource toResource(final UserSearchResult user) {
        final UserSearchResultResource userResource = new UserSearchResultResource(user);

        userResource.add(
                ControllerLinkBuilder.linkTo(
                        ControllerLinkBuilder
                                .methodOn(UserRestController.class)
                                .getProfile(user.getUsername())
                ).withSelfRel()
        );

        return userResource;
    }
}

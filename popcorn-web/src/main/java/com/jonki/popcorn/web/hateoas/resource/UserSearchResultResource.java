package com.jonki.popcorn.web.hateoas.resource;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.jonki.popcorn.common.dto.search.UserSearchResult;
import org.springframework.hateoas.Resource;

/**
 * HATEOAS resource representation of an User.
 */
public class UserSearchResultResource extends Resource<UserSearchResult> {

    /**
     * Constructor.
     *
     * @param user The user search result this resource is wrapping
     */
    @JsonCreator
    public UserSearchResultResource(final UserSearchResult user) {
        super(user);
    }
}

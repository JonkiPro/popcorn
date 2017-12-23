package com.web.web.hateoas.resource;

import com.common.dto.search.UserSearchResult;
import com.fasterxml.jackson.annotation.JsonCreator;
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

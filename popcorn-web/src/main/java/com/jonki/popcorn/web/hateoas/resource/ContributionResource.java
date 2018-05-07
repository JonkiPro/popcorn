package com.jonki.popcorn.web.hateoas.resource;

import com.jonki.popcorn.common.dto.Contribution;
import com.jonki.popcorn.common.dto.movie.MovieInfoDTO;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

/**
 * HATEOAS resource representation of an Contribution.
 */
public class ContributionResource<T  extends MovieInfoDTO> extends Resource<Contribution<? extends MovieInfoDTO>> {

    /**
     * Constructor.
     *
     * @param content The content this resource is wrapping
     * @param links Links (URLs)
     */
    @JsonCreator
    public ContributionResource(final Contribution<? extends MovieInfoDTO> content, final Link... links) {
        super(content, links);
    }
}

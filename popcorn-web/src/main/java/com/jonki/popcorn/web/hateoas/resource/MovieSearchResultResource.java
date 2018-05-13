package com.jonki.popcorn.web.hateoas.resource;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.jonki.popcorn.common.dto.search.MovieSearchResult;
import org.springframework.hateoas.Resource;

/**
 * HATEOAS resource representation of an Movie.
 */
public class MovieSearchResultResource extends Resource<MovieSearchResult> {

    /**
     * Constructor.
     *
     * @param movie The movie search result this resource is wrapping
     */
    @JsonCreator
    public MovieSearchResultResource(final MovieSearchResult movie) {
        super(movie);
    }
}

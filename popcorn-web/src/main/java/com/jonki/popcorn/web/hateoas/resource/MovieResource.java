package com.jonki.popcorn.web.hateoas.resource;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.jonki.popcorn.common.dto.Movie;
import org.springframework.hateoas.Resource;

/**
 * HATEOAS resource representation of an Movie.
 */
public class MovieResource extends Resource<Movie> {

    /**
     * Constructor.
     *
     * @param movie The movie this resource is wrapping
     */
    @JsonCreator
    public MovieResource(final Movie movie) {
        super(movie);
    }
}

package com.web.web.hateoas.resource;

import com.common.dto.Movie;
import com.fasterxml.jackson.annotation.JsonCreator;
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

package com.web.web.hateoas.assembler;

import com.common.dto.Movie;
import com.web.web.controller.MovieRestController;
import com.web.web.hateoas.resource.MovieResource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;

/**
 * Assembles Movie resources out of Movie DTOs.
 */
@Component
public class MovieResourceAssembler implements ResourceAssembler<Movie, MovieResource> {

    /**
     * {@inheritDoc}
     */
    @Override
    public MovieResource toResource(final Movie movie) {
        final MovieResource userResource = new MovieResource(movie);

        userResource.add(
                ControllerLinkBuilder.linkTo(
                        ControllerLinkBuilder
                                .methodOn(MovieRestController.class)
                                .getMovie(movie.getId())
                ).withSelfRel()
        );

        return userResource;
    }
}

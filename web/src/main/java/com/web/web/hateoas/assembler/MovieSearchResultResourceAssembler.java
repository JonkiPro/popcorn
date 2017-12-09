package com.web.web.hateoas.assembler;

import com.common.dto.search.MovieSearchResult;
import com.web.web.controller.MovieRestController;
import com.web.web.hateoas.resource.MovieSearchResultResource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;

/**
 * Assembles Movie resources out of movie search result DTOs.
 */
@Component
public class MovieSearchResultResourceAssembler implements ResourceAssembler<MovieSearchResult, MovieSearchResultResource> {

    /**
     * {@inheritDoc}
     */
    @Override
    public MovieSearchResultResource toResource(final MovieSearchResult movie) {
        final MovieSearchResultResource movieResource = new MovieSearchResultResource(movie);

        movieResource.add(
                ControllerLinkBuilder.linkTo(
                        ControllerLinkBuilder
                                .methodOn(MovieRestController.class)
                                .getMovie(movie.getId())
                ).withSelfRel()
        );

        return movieResource;
    }
}

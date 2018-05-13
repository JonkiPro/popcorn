package com.jonki.popcorn.web.hateoas.assembler;

import com.jonki.popcorn.common.dto.Movie;
import com.jonki.popcorn.web.controller.MovieContributionRestController;
import com.jonki.popcorn.web.controller.MovieRestController;
import com.jonki.popcorn.web.hateoas.resource.MovieResource;
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
        final MovieResource movieResource = new MovieResource(movie);
        final Long id = Long.valueOf(movie.getId());

        movieResource.add(
                ControllerLinkBuilder.linkTo(
                        ControllerLinkBuilder
                                .methodOn(MovieRestController.class)
                                .getMovie(id)
                ).withSelfRel()
        );

        movieResource.add(
                ControllerLinkBuilder.linkTo(
                        ControllerLinkBuilder
                                .methodOn(MovieRestController.class)
                                .getTitles(id)
                ).withRel("titles")
        );

        movieResource.add(
                ControllerLinkBuilder.linkTo(
                        ControllerLinkBuilder
                                .methodOn(MovieRestController.class)
                                .getReleaseDates(id)
                ).withRel("releaseDates")
        );

        movieResource.add(
                ControllerLinkBuilder.linkTo(
                        ControllerLinkBuilder
                                .methodOn(MovieRestController.class)
                                .getOutlines(id)
                ).withRel("outlines")
        );

        movieResource.add(
                ControllerLinkBuilder.linkTo(
                        ControllerLinkBuilder
                                .methodOn(MovieRestController.class)
                                .getSummaries(id)
                ).withRel("summaries")
        );

        movieResource.add(
                ControllerLinkBuilder.linkTo(
                        ControllerLinkBuilder
                                .methodOn(MovieRestController.class)
                                .getSynopses(id)
                ).withRel("synopses")
        );

        movieResource.add(
                ControllerLinkBuilder.linkTo(
                        ControllerLinkBuilder
                                .methodOn(MovieRestController.class)
                                .getBoxOffices(id)
                ).withRel("boxOffices")
        );

        movieResource.add(
                ControllerLinkBuilder.linkTo(
                        ControllerLinkBuilder
                                .methodOn(MovieRestController.class)
                                .getSites(id)
                ).withRel("sites")
        );

        movieResource.add(
                ControllerLinkBuilder.linkTo(
                        ControllerLinkBuilder
                                .methodOn(MovieRestController.class)
                                .getCountries(id)
                ).withRel("countries")
        );

        movieResource.add(
                ControllerLinkBuilder.linkTo(
                        ControllerLinkBuilder
                                .methodOn(MovieRestController.class)
                                .getLanguages(id)
                ).withRel("languages")
        );

        movieResource.add(
                ControllerLinkBuilder.linkTo(
                        ControllerLinkBuilder
                                .methodOn(MovieRestController.class)
                                .getGenres(id)
                ).withRel("genres")
        );

        movieResource.add(
                ControllerLinkBuilder.linkTo(
                        ControllerLinkBuilder
                                .methodOn(MovieRestController.class)
                                .getReviews(id)
                ).withRel("reviews")
        );

        movieResource.add(
                ControllerLinkBuilder.linkTo(
                        ControllerLinkBuilder
                                .methodOn(MovieRestController.class)
                                .getPhotos(id)
                ).withRel("photos")
        );

        movieResource.add(
                ControllerLinkBuilder.linkTo(
                        ControllerLinkBuilder
                                .methodOn(MovieRestController.class)
                                .getPosters(id)
                ).withRel("posters")
        );

        movieResource.add(
                ControllerLinkBuilder.linkTo(
                        ControllerLinkBuilder
                                .methodOn(MovieRestController.class)
                                .getRatings(id)
                ).withRel("ratings")
        );

        movieResource.add(
                ControllerLinkBuilder.linkTo(
                        ControllerLinkBuilder
                                .methodOn(MovieContributionRestController.class)
                                .findContributions(id,
                                        null, null, null, null, null, null)
                ).withRel("contributions")
        );

        return movieResource;
    }
}

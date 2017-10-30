package com.web.web.controller;

import com.common.dto.Movie;
import com.common.dto.movie.CountryType;
import com.common.dto.movie.GenreType;
import com.common.dto.movie.LanguageType;
import com.common.dto.movie.MovieType;
import com.common.dto.request.MovieDTO;
import com.core.jpa.service.MoviePersistenceService;
import com.core.jpa.service.MovieSearchService;
import com.web.web.hateoas.assembler.MovieResourceAssembler;
import com.web.web.hateoas.resource.MovieResource;
import com.web.web.security.service.AuthorizationService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@PreAuthorize("permitAll()")
@RequestMapping(value = "/api/v1.0/movies")
@Slf4j
@Api(value = "Movie Data API", description = "Provides a list of methods that manage movies")
public class MovieRestController {

    private final MoviePersistenceService moviePersistenceService;
    private final MovieSearchService movieSearchService;
    private final AuthorizationService authorizationService;
    private final MovieResourceAssembler movieResourceAssembler;

    /**
     * Constructor.
     *
     * @param moviePersistenceService The movie persistence service to use
     * @param movieSearchService The movie search service to use
     * @param authorizationService The authorization service to use
     * @param movieResourceAssembler Assemble movie resources out of movies
     */
    @Autowired
    public MovieRestController(
            final MoviePersistenceService moviePersistenceService,
            final MovieSearchService movieSearchService,
            final AuthorizationService authorizationService,
            final MovieResourceAssembler movieResourceAssembler
    ) {
        this.moviePersistenceService = moviePersistenceService;
        this.movieSearchService = movieSearchService;
        this.authorizationService = authorizationService;
        this.movieResourceAssembler = movieResourceAssembler;
    }

    @ApiOperation(value = "Create a new movie")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No user found") })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public
    ResponseEntity<Void> createMovie(
            @ApiParam(value = "Form of the new movie", required = true) @RequestBody @Valid MovieDTO movieDTO
    ) {
        log.info("Called with movieDTO {}", movieDTO);

        this.moviePersistenceService.createMovie(movieDTO, this.authorizationService.getUserId());

        final HttpHeaders httpHeaders = new HttpHeaders();

        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Accept movie")
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "No permissions"),
            @ApiResponse(code = 404, message = "No movie found or no user found")
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/editions/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public
    void acceptMovie(
            @ApiParam(value = "The movie ID", required = true) @PathVariable Long id
    ) {
        log.info("Called with id {}", id);

        this.moviePersistenceService.acceptMovie(id, authorizationService.getUserId());
    }

    @ApiOperation(value = "Accept contribution with movie info")
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "No permissions"),
            @ApiResponse(code = 404, message = "No contribution found or no user found")
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/contributions/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public
    void acceptContribution(
            @ApiParam(value = "The contribution ID", required = true) @PathVariable Long id
    ) {
        log.info("Called with id {}", id);

        this.moviePersistenceService.acceptContribution(id, authorizationService.getUserId());
    }

    @ApiOperation(value = "Reject movie")
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "No permissions"),
            @ApiResponse(code = 404, message = "No movie found or no user found")
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping(value = "/editions/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public
    void rejectMovie(
            @ApiParam(value = "The movie ID", required = true) @PathVariable Long id
    ) {
        log.info("Called with id {}", id);

        this.moviePersistenceService.rejectMovie(id, authorizationService.getUserId());
    }

    @ApiOperation(value = "Reject contribution with movie info")
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "No permissions"),
            @ApiResponse(code = 404, message = "No contribution found or no user found")
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping(value = "/contributions/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public
    void rejectContribution(
            @ApiParam(value = "The contribution ID", required = true) @PathVariable Long id
    ) {
        log.info("Called with id {}", id);

        this.moviePersistenceService.rejectContribution(id, authorizationService.getUserId());
    }

    @ApiOperation(value = "Get list of movies")
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public
    PagedResources<MovieResource> getMovies(
            @RequestParam(required = false) final String title,
            @RequestParam(required = false) final MovieType type,
            @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") final Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") final Date toDate,
            @RequestParam(required = false) final List<CountryType> countries,
            @RequestParam(required = false) final List<LanguageType> languages,
            @RequestParam(required = false) final List<GenreType> genres,
            @PageableDefault(sort = {"title"}, direction = Sort.Direction.DESC) final Pageable page,
            final PagedResourcesAssembler<Movie> assembler
    ) {
        log.info("Called with" + " title {}, type {}," +
                "fromDate {}, toDate {}, countries {}," +
                "languages {}, genres {}, page {},",
                title, type, fromDate, toDate, countries, languages, genres, page);

        // Build the self link which will be used for the next, previous, etc links
        final Link self = ControllerLinkBuilder
                .linkTo(
                        ControllerLinkBuilder
                                .methodOn(MovieRestController.class)
                                .getMovies(
                                        title,
                                        type,
                                        fromDate,
                                        toDate,
                                        countries,
                                        languages,
                                        genres,
                                        page,
                                        assembler
                                )
                ).withSelfRel();

        return assembler.toResource(this.movieSearchService.getAllMovies(
                title, type, fromDate, toDate, countries, languages, genres, page
        ), this.movieResourceAssembler, self);
    }

    @ApiOperation(value = "Get movie")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No movie found") })
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public
    MovieResource getMovie(
            @ApiParam(value = "The movie ID", required = true) @PathVariable final Long id
    ) {
        log.info("Called with id {}", id);

        return this.movieResourceAssembler.toResource(this.movieSearchService.getMovie(id));
    }
}

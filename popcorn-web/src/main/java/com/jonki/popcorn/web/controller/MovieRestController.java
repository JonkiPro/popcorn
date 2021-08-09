package com.jonki.popcorn.web.controller;

import com.jonki.popcorn.common.dto.VerificationStatus;
import com.jonki.popcorn.common.dto.movie.BoxOffice;
import com.jonki.popcorn.common.dto.movie.Country;
import com.jonki.popcorn.common.dto.movie.Genre;
import com.jonki.popcorn.common.dto.movie.Language;
import com.jonki.popcorn.common.dto.movie.OtherTitle;
import com.jonki.popcorn.common.dto.movie.Outline;
import com.jonki.popcorn.common.dto.movie.ReleaseDate;
import com.jonki.popcorn.common.dto.movie.Review;
import com.jonki.popcorn.common.dto.movie.Site;
import com.jonki.popcorn.common.dto.movie.Summary;
import com.jonki.popcorn.common.dto.movie.Synopsis;
import com.jonki.popcorn.common.dto.movie.request.RateRequest;
import com.jonki.popcorn.common.dto.movie.response.ImageResponse;
import com.jonki.popcorn.common.dto.movie.response.RateResponse;
import com.jonki.popcorn.common.dto.movie.type.CountryType;
import com.jonki.popcorn.common.dto.movie.type.GenreType;
import com.jonki.popcorn.common.dto.movie.type.LanguageType;
import com.jonki.popcorn.common.dto.movie.type.MovieType;
import com.jonki.popcorn.common.dto.request.MovieRequest;
import com.jonki.popcorn.common.dto.search.MovieSearchResult;
import com.jonki.popcorn.core.service.AuthorizationService;
import com.jonki.popcorn.core.service.MoviePersistenceService;
import com.jonki.popcorn.core.service.MovieSearchService;
import com.jonki.popcorn.web.hateoas.assembler.MovieResourceAssembler;
import com.jonki.popcorn.web.hateoas.assembler.MovieSearchResultResourceAssembler;
import com.jonki.popcorn.web.hateoas.resource.MovieResource;
import com.jonki.popcorn.web.hateoas.resource.MovieSearchResultResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * REST end-point for supporting Movies.
 */
@RestController
@RequestMapping(value = "/api/v1.0/movies")
@Slf4j
@Api(value = "Movie Data API", description = "Provides a list of methods that manage movies")
public class MovieRestController {

    private final MoviePersistenceService moviePersistenceService;
    private final MovieSearchService movieSearchService;
    private final AuthorizationService authorizationService;
    private final MovieResourceAssembler movieResourceAssembler;
    private final MovieSearchResultResourceAssembler movieSearchResultResourceAssembler;

    /**
     * Constructor.
     *
     * @param moviePersistenceService The movie persistence service to use
     * @param movieSearchService The movie search service to use
     * @param authorizationService The authorization service to use
     * @param movieResourceAssembler Assemble movie resources out of movies
     * @param movieSearchResultResourceAssembler Assemble movie search resources out of movies
     */
    @Autowired
    public MovieRestController(
            final MoviePersistenceService moviePersistenceService,
            final MovieSearchService movieSearchService,
            final AuthorizationService authorizationService,
            final MovieResourceAssembler movieResourceAssembler,
            final MovieSearchResultResourceAssembler movieSearchResultResourceAssembler
    ) {
        this.moviePersistenceService = moviePersistenceService;
        this.movieSearchService = movieSearchService;
        this.authorizationService = authorizationService;
        this.movieResourceAssembler = movieResourceAssembler;
        this.movieSearchResultResourceAssembler = movieSearchResultResourceAssembler;
    }

    /**
     * Create an Movie.
     *
     * @param movieRequest The movie to create
     * @return The created movie
     */
    @ApiOperation(value = "Create a new movie")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No user found") })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public
    ResponseEntity<Void> createMovie(
            @ApiParam(value = "New movie", required = true)
            @RequestBody @Valid MovieRequest movieRequest
    ) {
        log.info("Called with movieRequest {}", movieRequest);

        this.moviePersistenceService.createMovie(movieRequest);

        final HttpHeaders httpHeaders = new HttpHeaders();

        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

    /**
     * Update the movie's status.
     *
     * @param id The movie ID
     * @param status Verification status for the movie
     */
    @ApiOperation(value = "Update the movie status")
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "No permissions"),
            @ApiResponse(code = 404, message = "No movie found or no user found")
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(value = "/{id}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public
    void updateMovieStatus(
            @ApiParam(value = "The movie ID", required = true)
            @PathVariable("id") final Long id,
            @ApiParam(value = "Status for the movie", required = true)
            @RequestParam("status") final VerificationStatus status
    ) {
        log.info("Called with id {}, status {}", id, status);

        this.moviePersistenceService.updateMovieStatus(id, status);
    }

    /**
     * Get movies for given filter criteria.
     *
     * @param title The title of the movie (optional)
     * @param type The type of the movie (optional)
     * @param fromDate Release date range "from" (optional)
     * @param toDate Release date range "to" (optional)
     * @param countries List of countries (optional)
     * @param languages List of languages (optional)
     * @param genres List of genres (optional)
     * @param minRating Min. movie rating (optional)
     * @param maxRating Max. movie rating (optional)
     * @param page The page to get
     * @param assembler The paged resources assembler to use
     * @return All movies matching the criteria
     */
    @ApiOperation(value = "Find movies")
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public
    PagedResources<MovieSearchResultResource> findMovies(
            @ApiParam(value = "The title of the movie")
            @RequestParam(value = "title", required = false) final String title,
            @ApiParam(value = "The type of the movie")
            @RequestParam(value = "type", required = false) final MovieType type,
            @ApiParam(value = "Release date range \"from\"")
            @RequestParam(value = "fromDate", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") final Date fromDate,
            @ApiParam(value = "Release date range \"to\"")
            @RequestParam(value = "toDate", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") final Date toDate,
            @ApiParam(value = "List of countries")
            @RequestParam(value = "country", required = false) final List<CountryType> countries,
            @ApiParam(value = "List of languages")
            @RequestParam(value = "language", required = false) final List<LanguageType> languages,
            @ApiParam(value = "List of genres")
            @RequestParam(value = "genre", required = false) final List<GenreType> genres,
            @ApiParam(value = "Min. rating")
            @RequestParam(value = "minRating", required = false) @Size(max = 10) final Integer minRating,
            @ApiParam(value = "Max. rating")
            @RequestParam(value = "maxRating", required = false) @Size(max = 10) final Integer maxRating,
            @PageableDefault(sort = {"title"}, direction = Sort.Direction.DESC) final Pageable page,
            final PagedResourcesAssembler<MovieSearchResult> assembler
    ) {
        log.info("Called with" + " title {}, type {}," +
                "fromDate {}, toDate {}, countries {}," +
                "languages {}, genres {}, minRating {}," +
                        "maxRating {},  page {},",
                title, type, fromDate, toDate, countries, languages, genres, minRating, maxRating, page);

        // Build the self link which will be used for the next, previous, etc links
        final Link self = ControllerLinkBuilder
                .linkTo(
                        ControllerLinkBuilder
                                .methodOn(MovieRestController.class)
                                .findMovies(
                                        title,
                                        type,
                                        fromDate,
                                        toDate,
                                        countries,
                                        languages,
                                        genres,
                                        minRating,
                                        maxRating,
                                        page,
                                        assembler
                                )
                ).withSelfRel();

        return assembler.toResource(this.movieSearchService.findMovies(
                title, type, fromDate, toDate, countries, languages, genres, minRating, maxRating, page
        ), this.movieSearchResultResourceAssembler, self);
    }

    /**
     * Get Movie for given id.
     *
     * @param id The movie ID
     * @return The movie
     */
    @ApiOperation(value = "Get movie")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No movie found or no user found") })
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public MovieResource getMovie(
            @ApiParam(value = "The movie ID", required = true)
            @PathVariable("id") final Long id
    ) {
        log.info("Called with id {}", id);

        if(this.authorizationService.isLogged()) {
            return this.movieResourceAssembler.toResource(this.movieSearchService.getUserMovie(id));
        } else {
            return this.movieResourceAssembler.toResource(this.movieSearchService.getMovie(id));
        }
    }

    /**
     * Get all the titles for a given movie.
     *
     * @param id The movie ID
     * @return The list of titles
     */
    @ApiOperation(value = "Get movie titles")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No movie found") })
    @GetMapping(value = "/{id}/titles")
    @ResponseStatus(HttpStatus.OK)
    public
    List<OtherTitle> getTitles(
            @ApiParam(value = "The movie ID", required = true)
            @PathVariable("id") final Long id
    ) {
        log.info("Called with id {}", id);

        return this.movieSearchService.getTitles(id);
    }

    /**
     * Get all the release dates for a given movie.
     *
     * @param id The movie ID
     * @return The list of release dates
     */
    @ApiOperation(value = "Get movie release dates")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No movie found") })
    @GetMapping(value = "/{id}/releaseDates")
    @ResponseStatus(HttpStatus.OK)
    public
    List<ReleaseDate> getReleaseDates(
            @ApiParam(value = "The movie ID", required = true)
            @PathVariable("id") final Long id
    ) {
        log.info("Called with id {}", id);

        return this.movieSearchService.getReleaseDates(id);
    }

    /**
     * Get all the outlines for a given movie.
     *
     * @param id The movie ID
     * @return The list of outlines
     */
    @ApiOperation(value = "Get movie outlines")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No movie found") })
    @GetMapping(value = "/{id}/outlines")
    @ResponseStatus(HttpStatus.OK)
    public
    List<Outline> getOutlines(
            @ApiParam(value = "The movie ID", required = true)
            @PathVariable("id") final Long id
    ) {
        log.info("Called with id {}", id);

        return this.movieSearchService.getOutlines(id);
    }

    /**
     * Get all the summaries for a given movie.
     *
     * @param id The movie ID
     * @return The list of summaries
     */
    @ApiOperation(value = "Get movie summaries")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No movie found") })
    @GetMapping(value = "/{id}/summaries")
    @ResponseStatus(HttpStatus.OK)
    public
    List<Summary> getSummaries(
            @ApiParam(value = "The movie ID", required = true)
            @PathVariable("id") final Long id
    ) {
        log.info("Called with id {}", id);

        return this.movieSearchService.getSummaries(id);
    }

    /**
     * Get all the synopses for a given movie.
     *
     * @param id The movie ID
     * @return The list of synopses
     */
    @ApiOperation(value = "Get movie synopses")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No movie found") })
    @GetMapping(value = "/{id}/synopses")
    @ResponseStatus(HttpStatus.OK)
    public
    List<Synopsis> getSynopses(
            @ApiParam(value = "The movie ID", required = true)
            @PathVariable("id") final Long id
    ) {
        log.info("Called with id {}", id);

        return this.movieSearchService.getSynopses(id);
    }

    /**
     * Get all the box offices for a given movie.
     *
     * @param id The movie ID
     * @return The list of box offices
     */
    @ApiOperation(value = "Get movie box offices")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No movie found") })
    @GetMapping(value = "/{id}/boxOffices")
    @ResponseStatus(HttpStatus.OK)
    public
    List<BoxOffice> getBoxOffices(
            @ApiParam(value = "The movie ID", required = true)
            @PathVariable("id") final Long id
    ) {
        log.info("Called with id {}", id);

        return this.movieSearchService.getBoxOffices(id);
    }

    /**
     * Get all the sites for a given movie.
     *
     * @param id The movie ID
     * @return The list of sites
     */
    @ApiOperation(value = "Get movie sites")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No movie found") })
    @GetMapping(value = "/{id}/sites")
    @ResponseStatus(HttpStatus.OK)
    public
    List<Site> getSites(
            @ApiParam(value = "The movie ID", required = true)
            @PathVariable("id") final Long id
    ) {
        log.info("Called with id {}", id);

        return this.movieSearchService.getSites(id);
    }

    /**
     * Get all the countries for a given movie.
     *
     * @param id The movie ID
     * @return The list of countries
     */
    @ApiOperation(value = "Get movie countries")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No movie found") })
    @GetMapping(value = "/{id}/countries")
    @ResponseStatus(HttpStatus.OK)
    public
    List<Country> getCountries(
            @ApiParam(value = "The movie ID", required = true)
            @PathVariable("id") final Long id
    ) {
        log.info("Called with id {}", id);

        return this.movieSearchService.getCountries(id);
    }

    /**
     * Get all the languages for a given movie.
     *
     * @param id The movie ID
     * @return The list of languages
     */
    @ApiOperation(value = "Get movie languages")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No movie found") })
    @GetMapping(value = "/{id}/languages")
    @ResponseStatus(HttpStatus.OK)
    public
    List<Language> getLanguages(
            @ApiParam(value = "The movie ID", required = true)
            @PathVariable("id") final Long id
    ) {
        log.info("Called with id {}", id);

        return this.movieSearchService.getLanguages(id);
    }

    /**
     * Get all the genres for a given movie.
     *
     * @param id The movie ID
     * @return The list of genres
     */
    @ApiOperation(value = "Get movie genres")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No movie found") })
    @GetMapping(value = "/{id}/genres")
    @ResponseStatus(HttpStatus.OK)
    public
    List<Genre> getGenres(
            @ApiParam(value = "The movie ID", required = true)
            @PathVariable("id") final Long id
    ) {
        log.info("Called with id {}", id);

        return this.movieSearchService.getGenres(id);
    }

    /**
     * Get all the reviews for a given movie.
     *
     * @param id The movie ID
     * @return The list of reviews
     */
    @ApiOperation(value = "Get movie reviews")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No movie found") })
    @GetMapping(value = "/{id}/reviews")
    @ResponseStatus(HttpStatus.OK)
    public
    List<Review> getReviews(
            @ApiParam(value = "The movie ID", required = true)
            @PathVariable("id") final Long id
    ) {
        log.info("Called with id {}", id);

        return this.movieSearchService.getReviews(id);
    }

    /**
     * Get all the photos for a given movie.
     *
     * @param id The movie ID
     * @return The list of photos
     */
    @ApiOperation(value = "Get movie photos")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No movie found") })
    @GetMapping(value = "/{id}/photos")
    @ResponseStatus(HttpStatus.OK)
    public
    List<ImageResponse> getPhotos(
            @ApiParam(value = "The movie ID", required = true)
            @PathVariable("id") final Long id
    ) {
        log.info("Called with id {}", id);

        return this.movieSearchService.getPhotos(id);
    }

    /**
     * Get all the posters for a given movie.
     *
     * @param id The movie ID
     * @return The list of posters
     */
    @ApiOperation(value = "Get movie posters")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No movie found") })
    @GetMapping(value = "/{id}/posters")
    @ResponseStatus(HttpStatus.OK)
    public
    List<ImageResponse> getPosters(
            @ApiParam(value = "The movie ID", required = true)
            @PathVariable("id") final Long id
    ) {
        log.info("Called with id {}", id);

        return this.movieSearchService.getPosters(id);
    }

    /**
     * Create a rating for the movie.
     *
     * @param id The movie ID
     * @param rateRequest Rating for the movie
     * @return The created rating
     */
    @ApiOperation(value = "RateRequest the movie")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "No movie found"),
            @ApiResponse(code = 409, message = "Today's date is earlier than the release date of the movie")
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/{id}/ratings", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public
    ResponseEntity<Void> createRating(
            @ApiParam(value = "The movie ID", required = true)
            @PathVariable("id") final Long id,
            @ApiParam(value = "Rating for the movie", required = true)
            @RequestBody @Valid final RateRequest rateRequest
    ) {
        log.info("Called with id {}, rateRequest {}", id, rateRequest);

        this.moviePersistenceService.saveRating(id, rateRequest);

        final HttpHeaders httpHeaders = new HttpHeaders();

        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

    /**
     * Get all the ratings for a given movie.
     *
     * @param id The movie ID
     * @return The list of ratings
     */
    @ApiOperation(value = "Get ratings")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No movie found") })
    @GetMapping(value = "/{id}/ratings")
    @ResponseStatus(HttpStatus.OK)
    public
    List<RateResponse> getRatings(
            @ApiParam(value = "The movie ID", required = true)
            @PathVariable("id") final Long id
    ) {
        log.info("Called with id {}", id);

        return this.movieSearchService.getRatings(id);
    }

    /**
     * Add a movie to favorites.
     *
     * @param id The movie ID you want to add to favorites. Not null/empty/blank
     */
    @ApiOperation(value = "Add a movie to your favourites list")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "No movie found"),
            @ApiResponse(code = 409, message = "The movie is added to favorites")
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/{id}/favorite")
    @ResponseStatus(HttpStatus.CREATED)
    public
    ResponseEntity<Void> favoriteMovie(
            @ApiParam(value = "The movie ID", required = true)
            @PathVariable("id") final Long id
    ) {
        log.info("Called with id {}", id);

        this.moviePersistenceService.setFavoriteMovie(id);

        final HttpHeaders httpHeaders = new HttpHeaders();

        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

    /**
     * Delete a movie from favorites.
     *
     * @param id The movie ID you want to delete from favorites list. Not null/empty/blank
     */
    @ApiOperation(value = "Remove a movie from your favourites list")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "No movie found"),
            @ApiResponse(code = 409, message = "The movie is not added to favorites")
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping(value = "/{id}/favorite/undo")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public
    void undoFavoriteMovie(
            @ApiParam(value = "The movie ID", required = true)
            @PathVariable("id") final Long id
    ) {
        log.info("Called with id {}", id);

        this.moviePersistenceService.undoFavoriteMovie(id);
    }
}

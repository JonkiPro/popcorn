package com.web.web.controller;

import com.common.dto.Contribution;
import com.common.dto.movie.*;
import com.common.dto.movie.request.*;
import com.common.dto.search.ContributionSearchResult;
import com.core.jpa.service.MovieContributionPersistenceService;
import com.core.jpa.service.MovieContributionSearchService;
import com.core.movie.DataStatus;
import com.core.movie.MovieField;
import com.core.movie.VerificationStatus;
import com.web.web.hateoas.assembler.ContributionSearchResultResourceAssembler;
import com.web.web.hateoas.resource.ContributionResource;
import com.web.web.hateoas.resource.ContributionSearchResultResource;
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
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.validation.Valid;
import java.util.Date;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

@RestController
@RequestMapping(value = "/api/v1.0/movies")
@Slf4j
@Api(value = "Movie Contribution Data API", description = "Provides a list of methods that manage movie contributions")
public class MovieContributionRestController {

    private final MovieContributionPersistenceService movieContributionPersistenceService;
    private final MovieContributionSearchService movieContributionSearchService;
    private final AuthorizationService authorizationService;
    private final ContributionSearchResultResourceAssembler contributionSearchResultResourceAssembler;

    /**
     *  Constructor.
     *
     * @param movieContributionPersistenceService The contribution persistence service to use
     * @param movieContributionSearchService The contribution search service to use
     * @param authorizationService The authorization service to use
     * @param contributionSearchResultResourceAssembler Assemble contribution search resources out of contributions
     */
    @Autowired
    public MovieContributionRestController(
            final MovieContributionPersistenceService movieContributionPersistenceService,
            final MovieContributionSearchService movieContributionSearchService,
            final AuthorizationService authorizationService,
            final ContributionSearchResultResourceAssembler contributionSearchResultResourceAssembler
    ) {
        this.movieContributionPersistenceService = movieContributionPersistenceService;
        this.movieContributionSearchService = movieContributionSearchService;
        this.authorizationService = authorizationService;
        this.contributionSearchResultResourceAssembler = contributionSearchResultResourceAssembler;
    }

    @ApiOperation(value = "Update the contribution status")
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "No permissions"),
            @ApiResponse(code = 404, message = "No contribution found or no user found")
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(value = "/contributions/{id}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public
    void updateContributionStatus(
            @ApiParam(value = "The contribution ID", required = true) @PathVariable final Long id,
            @ApiParam(value = "Status for the contribution", required = true) @RequestParam final VerificationStatus status,
            @ApiParam(value = "Comment for verification") @RequestParam(required = false) final String comment
    ) {
        log.info("Called with id {}, status {}, comment {}", id, status, comment);

        this.movieContributionPersistenceService.updateContributionStatus(id, authorizationService.getUserId(), status, comment);
    }

    @ApiOperation(value = "Get contributions")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No movie found") })
    @GetMapping(value = "/{id}/contributions", produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public
    PagedResources<ContributionSearchResultResource> getContributions(
            @ApiParam(value = "The movie ID", required = true) @PathVariable final Long id,
            @RequestParam(required = false) final MovieField field,
            @RequestParam(required = false) final DataStatus status,
            @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") final Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") final Date toDate,
            @PageableDefault(sort = {"creationDate"}, direction = Sort.Direction.DESC) final Pageable page,
            final PagedResourcesAssembler<ContributionSearchResult> assembler
    ) {
        log.info("Called with id {}, field {}, status {}, fromDate {}, toDate {}, page {}",
                id, field, status, fromDate, toDate, page);

        // Build the self link which will be used for the next, previous, etc links
        final Link self = ControllerLinkBuilder
                .linkTo(
                        ControllerLinkBuilder
                                .methodOn(MovieContributionRestController.class)
                                .getContributions(
                                        id,
                                        field,
                                        status,
                                        fromDate,
                                        toDate,
                                        page,
                                        assembler
                                )
                ).withSelfRel();

        return assembler.toResource(this.movieContributionSearchService.getContributions(
                id, field, status, fromDate, toDate, page
        ), this.contributionSearchResultResourceAssembler, self);
    }

    @ApiOperation(value = "Get the contribution of titles")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No contribution found") })
    @GetMapping(value = "/contributions/{id}/othertitles", produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public
    ContributionResource<OtherTitle> getOtherTitleContribution(
            @ApiParam(value = "The contribution ID", required = true) @PathVariable final Long id
    ) {
        log.info("Called with id {}", id);

        final Contribution<OtherTitle> otherTitleContribution = this.movieContributionSearchService.getOtherTitleContribution(id);

        final Link self = linkTo(
                methodOn(MovieContributionRestController.class).getOtherTitleContribution(id)
        ).withSelfRel();

        return new ContributionResource<>(otherTitleContribution, self);
    }

    @ApiOperation(value = "Create the contribution of titles")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Incorrect data in the DTO"),
            @ApiResponse(code = 404, message = "No movie found or no user found"),
            @ApiResponse(code = 404, message = "An ID conflict or element exists"),
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/{id}/contributions/othertitles", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public
    ResponseEntity<Void> createOtherTitleContribution(
            @ApiParam(value = "The movie ID", required = true) @PathVariable final Long id,
            @RequestBody @Valid final ContributionNew<OtherTitle> contribution
    ) {
        log.info("Called with id {}, contribution {}", id, contribution);

        final Long cId = this.movieContributionPersistenceService.createOtherTitleContribution(contribution, id, this.authorizationService.getUserId());

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(
                MvcUriComponentsBuilder
                        .fromMethodName(MovieContributionRestController.class, "getOtherTitleContribution", cId)
                        .buildAndExpand(cId)
                        .toUri()
        );

        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update the contribution of titles")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Incorrect data in the DTO"),
            @ApiResponse(code = 404, message = "No movie found or no user found"),
            @ApiResponse(code = 404, message = "An ID conflict or element exists"),
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(value = "/contributions/othertitles/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public
    void updateOtherTitleContribution(
            @ApiParam(value = "The contribution ID", required = true) @PathVariable final Long id,
            @RequestBody @Valid final ContributionUpdate<OtherTitle> contribution
    ) {
        log.info("Called with id {}, contribution {}", id, contribution);

        this.movieContributionPersistenceService.updateOtherTitleContribution(contribution, id, this.authorizationService.getUserId());
    }

    @ApiOperation(value = "Get the contribution of release dates")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No contribution found") })
    @GetMapping(value = "/contributions/{id}/releasedates", produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public
    ContributionResource<ReleaseDate> getReleaseDateContribution(
            @ApiParam(value = "The contribution ID", required = true) @PathVariable final Long id
    ) {
        log.info("Called with id {}", id);

        final Contribution<ReleaseDate> releaseDateContribution = this.movieContributionSearchService.getReleaseDateContribution(id);

        final Link self = linkTo(
                methodOn(MovieContributionRestController.class).getReleaseDateContribution(id)
        ).withSelfRel();

        return new ContributionResource<>(releaseDateContribution, self);
    }

    @ApiOperation(value = "Create the contribution of release dates")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Incorrect data in the DTO"),
            @ApiResponse(code = 404, message = "No movie found or no user found"),
            @ApiResponse(code = 404, message = "An ID conflict or element exists"),
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/{id}/contributions/releasedates", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public
    ResponseEntity<Void> createReleaseDateContribution(
            @ApiParam(value = "The movie ID", required = true) @PathVariable final Long id,
            @RequestBody @Valid final ContributionNew<ReleaseDate> contribution
    ) {
        log.info("Called with id {}, contribution {}", id, contribution);

        final Long cId = this.movieContributionPersistenceService.createReleaseDateContribution(contribution, id, this.authorizationService.getUserId());

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(
                MvcUriComponentsBuilder
                        .fromMethodName(MovieContributionRestController.class, "getReleaseDateContribution", cId)
                        .buildAndExpand(cId)
                        .toUri()
        );

        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update the contribution of release dates")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Incorrect data in the DTO"),
            @ApiResponse(code = 404, message = "No movie found or no user found"),
            @ApiResponse(code = 404, message = "An ID conflict or element exists"),
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(value = "/contributions/releasedates/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public
    void updateReleaseDateContribution(
            @ApiParam(value = "The contribution ID", required = true) @PathVariable final Long id,
            @RequestBody @Valid final ContributionUpdate<ReleaseDate> contribution
    ) {
        log.info("Called with id {}, contribution {}", id, contribution);

        this.movieContributionPersistenceService.updateReleaseDateContribution(contribution, id, this.authorizationService.getUserId());
    }

    @ApiOperation(value = "Get the contribution of storylines")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No contribution found") })
    @GetMapping(value = "/contributions/{id}/storylines", produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public
    ContributionResource<Storyline> getStorylineContribution(
            @ApiParam(value = "The contribution ID", required = true) @PathVariable final Long id
    ) {
        log.info("Called with id {}", id);

        final Contribution<Storyline> storylineContribution = this.movieContributionSearchService.getStorylineContribution(id);

        final Link self = linkTo(
                methodOn(MovieContributionRestController.class).getStorylineContribution(id)
        ).withSelfRel();

        return new ContributionResource<>(storylineContribution, self);
    }

    @ApiOperation(value = "Create the contribution of storylines")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Incorrect data in the DTO"),
            @ApiResponse(code = 404, message = "No movie found or no user found"),
            @ApiResponse(code = 404, message = "An ID conflict or element exists"),
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/{id}/contributions/storylines", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public
    ResponseEntity<Void> createStorylineContribution(
            @ApiParam(value = "The movie ID", required = true) @PathVariable final Long id,
            @RequestBody @Valid final ContributionNew<Storyline> contribution
    ) {
        log.info("Called with id {}, contribution {}", id, contribution);

        final Long cId = this.movieContributionPersistenceService.createStorylineContribution(contribution, id, this.authorizationService.getUserId());

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(
                MvcUriComponentsBuilder
                        .fromMethodName(MovieContributionRestController.class, "getStorylineContribution", cId)
                        .buildAndExpand(cId)
                        .toUri()
        );

        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update the contribution of storylines")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Incorrect data in the DTO"),
            @ApiResponse(code = 404, message = "No movie found or no user found"),
            @ApiResponse(code = 404, message = "An ID conflict or element exists"),
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(value = "/contributions/storylines/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public
    void updateStorylineContribution(
            @ApiParam(value = "The contribution ID", required = true) @PathVariable final Long id,
            @RequestBody @Valid final ContributionUpdate<Storyline> contribution
    ) {
        log.info("Called with id {}, contribution {}", id, contribution);

        this.movieContributionPersistenceService.updateStorylineContribution(contribution, id, this.authorizationService.getUserId());
    }

    @ApiOperation(value = "Get the contribution of box offices")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No contribution found") })
    @GetMapping(value = "/contributions/{id}/boxoffices", produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public
    ContributionResource<BoxOffice> getBoxOfficeContribution(
            @ApiParam(value = "The contribution ID", required = true) @PathVariable final Long id
    ) {
        log.info("Called with id {}", id);

        final Contribution<BoxOffice> boxOfficeContribution = this.movieContributionSearchService.getBoxOfficeContribution(id);

        final Link self = linkTo(
                methodOn(MovieContributionRestController.class).getBoxOfficeContribution(id)
        ).withSelfRel();

        return new ContributionResource<>(boxOfficeContribution, self);
    }

    @ApiOperation(value = "Create the contribution of box offices")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Incorrect data in the DTO"),
            @ApiResponse(code = 404, message = "No movie found or no user found"),
            @ApiResponse(code = 404, message = "An ID conflict or element exists"),
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/{id}/contributions/boxoffices", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public
    ResponseEntity<Void> createBoxOfficeContribution(
            @ApiParam(value = "The movie ID", required = true) @PathVariable final Long id,
            @RequestBody @Valid final ContributionNew<BoxOffice> contribution
    ) {
        log.info("Called with id {}, contribution {}", id, contribution);

        final Long cId = this.movieContributionPersistenceService.createBoxOfficeContribution(contribution, id, this.authorizationService.getUserId());

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(
                MvcUriComponentsBuilder
                        .fromMethodName(MovieContributionRestController.class, "getBoxOfficeContribution", cId)
                        .buildAndExpand(cId)
                        .toUri()
        );

        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update the contribution of box offices")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Incorrect data in the DTO"),
            @ApiResponse(code = 404, message = "No movie found or no user found"),
            @ApiResponse(code = 404, message = "An ID conflict or element exists"),
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(value = "/contributions/boxoffices/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public
    void updateBoxOfficeContribution(
            @ApiParam(value = "The contribution ID", required = true) @PathVariable final Long id,
            @RequestBody @Valid final ContributionUpdate<BoxOffice> contribution
    ) {
        log.info("Called with id {}, contribution {}", id, contribution);

        this.movieContributionPersistenceService.updateBoxOfficeContribution(contribution, id, this.authorizationService.getUserId());
    }

    @ApiOperation(value = "Get the contribution of sites")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No contribution found") })
    @GetMapping(value = "/contributions/{id}/sites", produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public
    ContributionResource<Site> getSiteContribution(
            @ApiParam(value = "The contribution ID", required = true) @PathVariable final Long id
    ) {
        log.info("Called with id {}", id);

        final Contribution<Site> siteContribution = this.movieContributionSearchService.getSiteContribution(id);

        final Link self = linkTo(
                methodOn(MovieContributionRestController.class).getSiteContribution(id)
        ).withSelfRel();

        return new ContributionResource<>(siteContribution, self);
    }

    @ApiOperation(value = "Create the contribution of sites")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Incorrect data in the DTO"),
            @ApiResponse(code = 404, message = "No movie found or no user found"),
            @ApiResponse(code = 404, message = "An ID conflict or element exists"),
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/{id}/contributions/sites", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public
    ResponseEntity<Void> createSiteContribution(
            @ApiParam(value = "The movie ID", required = true) @PathVariable final Long id,
            @RequestBody @Valid final ContributionNew<Site> contribution
    ) {
        log.info("Called with id {}, contribution {}", id, contribution);

        final Long cId = this.movieContributionPersistenceService.createSiteContribution(contribution, id, this.authorizationService.getUserId());

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(
                MvcUriComponentsBuilder
                        .fromMethodName(MovieContributionRestController.class, "getSiteContribution", cId)
                        .buildAndExpand(cId)
                        .toUri()
        );

        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update the contribution of sites")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Incorrect data in the DTO"),
            @ApiResponse(code = 404, message = "No movie found or no user found"),
            @ApiResponse(code = 404, message = "An ID conflict or element exists"),
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(value = "/contributions/sites/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public
    void updateSiteContribution(
            @ApiParam(value = "The contribution ID", required = true) @PathVariable final Long id,
            @RequestBody @Valid final ContributionUpdate<Site> contribution
    ) {
        log.info("Called with id {}, contribution {}", id, contribution);

        this.movieContributionPersistenceService.updateSiteContribution(contribution, id, this.authorizationService.getUserId());
    }

    @ApiOperation(value = "Get the contribution of countries")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No contribution found") })
    @GetMapping(value = "/contributions/{id}/countries", produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public
    ContributionResource<Country> getCountryContribution(
            @ApiParam(value = "The contribution ID", required = true) @PathVariable final Long id
    ) {
        log.info("Called with id {}", id);

        final Contribution<Country> countryContribution = this.movieContributionSearchService.getCountryContribution(id);

        final Link self = linkTo(
                methodOn(MovieContributionRestController.class).getCountryContribution(id)
        ).withSelfRel();

        return new ContributionResource<>(countryContribution, self);
    }

    @ApiOperation(value = "Create the contribution of countries")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Incorrect data in the DTO"),
            @ApiResponse(code = 404, message = "No movie found or no user found"),
            @ApiResponse(code = 404, message = "An ID conflict or element exists"),
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/{id}/contributions/countries", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public
    ResponseEntity<Void> createCountryContribution(
            @ApiParam(value = "The movie ID", required = true) @PathVariable final Long id,
            @RequestBody @Valid final ContributionNew<Country> contribution
    ) {
        log.info("Called with id {}, contribution {}", id, contribution);

        final Long cId = this.movieContributionPersistenceService.createCountryContribution(contribution, id, this.authorizationService.getUserId());

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(
                MvcUriComponentsBuilder
                        .fromMethodName(MovieContributionRestController.class, "getCountryContribution", cId)
                        .buildAndExpand(cId)
                        .toUri()
        );

        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update the contribution of countries")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Incorrect data in the DTO"),
            @ApiResponse(code = 404, message = "No movie found or no user found"),
            @ApiResponse(code = 404, message = "An ID conflict or element exists"),
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(value = "/contributions/countries/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public
    void updateCountryContribution(
            @ApiParam(value = "The contribution ID", required = true) @PathVariable final Long id,
            @RequestBody @Valid final ContributionUpdate<Country> contribution
    ) {
        log.info("Called with id {}, contribution {}", id, contribution);

        this.movieContributionPersistenceService.updateCountryContribution(contribution, id, this.authorizationService.getUserId());
    }

    @ApiOperation(value = "Get the contribution of languages")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No contribution found") })
    @GetMapping(value = "/contributions/{id}/languages", produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public
    ContributionResource<Language> getLanguageContribution(
            @ApiParam(value = "The contribution ID", required = true) @PathVariable final Long id
    ) {
        log.info("Called with id {}", id);

        final Contribution<Language> languageContribution = this.movieContributionSearchService.getLanguageContribution(id);

        final Link self = linkTo(
                methodOn(MovieContributionRestController.class).getLanguageContribution(id)
        ).withSelfRel();

        return new ContributionResource<>(languageContribution, self);
    }

    @ApiOperation(value = "Create the contribution of languages")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Incorrect data in the DTO"),
            @ApiResponse(code = 404, message = "No movie found or no user found"),
            @ApiResponse(code = 404, message = "An ID conflict or element exists"),
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/{id}/contributions/languages", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public
    ResponseEntity<Void> createLanguageContribution(
            @ApiParam(value = "The movie ID", required = true) @PathVariable final Long id,
            @RequestBody @Valid final ContributionNew<Language> contribution
    ) {
        log.info("Called with id {}, contribution {}", id, contribution);

        final Long cId = this.movieContributionPersistenceService.createLanguageContribution(contribution, id, this.authorizationService.getUserId());

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(
                MvcUriComponentsBuilder
                        .fromMethodName(MovieContributionRestController.class, "getLanguageContribution", cId)
                        .buildAndExpand(cId)
                        .toUri()
        );

        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update the contribution of languages")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Incorrect data in the DTO"),
            @ApiResponse(code = 404, message = "No movie found or no user found"),
            @ApiResponse(code = 404, message = "An ID conflict or element exists"),
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(value = "/contributions/languages/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public
    void updateLanguageContribution(
            @ApiParam(value = "The contribution ID", required = true) @PathVariable final Long id,
            @RequestBody @Valid final ContributionUpdate<Language> contribution
    ) {
        log.info("Called with id {}, contribution {}", id, contribution);

        this.movieContributionPersistenceService.updateLanguageContribution(contribution, id, this.authorizationService.getUserId());
    }

    @ApiOperation(value = "Get the contribution of genres")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No contribution found") })
    @GetMapping(value = "/contributions/{id}/genres", produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public
    ContributionResource<Genre> getGenreContribution(
            @ApiParam(value = "The contribution ID", required = true) @PathVariable final Long id
    ) {
        log.info("Called with id {}", id);

        final Contribution<Genre> genreContribution = this.movieContributionSearchService.getGenreContribution(id);

        final Link self = linkTo(
                methodOn(MovieContributionRestController.class).getGenreContribution(id)
        ).withSelfRel();

        return new ContributionResource<>(genreContribution, self);
    }

    @ApiOperation(value = "Create the contribution of genres")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Incorrect data in the DTO"),
            @ApiResponse(code = 404, message = "No movie found or no user found"),
            @ApiResponse(code = 404, message = "An ID conflict or element exists"),
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/{id}/contributions/genres", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public
    ResponseEntity<Void> createGenreContribution(
            @ApiParam(value = "The movie ID", required = true) @PathVariable final Long id,
            @RequestBody @Valid final ContributionNew<Genre> contribution
    ) {
        log.info("Called with id {}, contribution {}", id, contribution);

        final Long cId = this.movieContributionPersistenceService.createGenreContribution(contribution, id, this.authorizationService.getUserId());

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(
                MvcUriComponentsBuilder
                        .fromMethodName(MovieContributionRestController.class, "getGenreContribution", cId)
                        .buildAndExpand(cId)
                        .toUri()
        );

        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update the contribution of genres")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Incorrect data in the DTO"),
            @ApiResponse(code = 404, message = "No movie found or no user found"),
            @ApiResponse(code = 404, message = "An ID conflict or element exists"),
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(value = "/contributions/genres/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public
    void updateGenreContribution(
            @ApiParam(value = "The contribution ID", required = true) @PathVariable final Long id,
            @RequestBody @Valid final ContributionUpdate<Genre> contribution
    ) {
        log.info("Called with id {}, contribution {}", id, contribution);

        this.movieContributionPersistenceService.updateGenreContribution(contribution, id, this.authorizationService.getUserId());
    }

    @ApiOperation(value = "Get the contribution of reviews")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No contribution found") })
    @GetMapping(value = "/contributions/{id}/reviews", produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public
    ContributionResource<Review> getReviewContribution(
            @ApiParam(value = "The contribution ID", required = true) @PathVariable final Long id
    ) {
        log.info("Called with id {}", id);

        final Contribution<Review> reviewContribution = this.movieContributionSearchService.getReviewContribution(id);

        final Link self = linkTo(
                methodOn(MovieContributionRestController.class).getReviewContribution(id)
        ).withSelfRel();

        return new ContributionResource<>(reviewContribution, self);
    }

    @ApiOperation(value = "Create the contribution of reviews")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Incorrect data in the DTO"),
            @ApiResponse(code = 404, message = "No movie found or no user found"),
            @ApiResponse(code = 404, message = "An ID conflict or element exists"),
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/{id}/contributions/reviews", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public
    ResponseEntity<Void> createReviewContribution(
            @ApiParam(value = "The movie ID", required = true) @PathVariable final Long id,
            @RequestBody @Valid final ContributionNew<Review> contribution
    ) {
        log.info("Called with id {}, contribution {}", id, contribution);

        final Long cId = this.movieContributionPersistenceService.createReviewContribution(contribution, id, this.authorizationService.getUserId());

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(
                MvcUriComponentsBuilder
                        .fromMethodName(MovieContributionRestController.class, "getReviewContribution", cId)
                        .buildAndExpand(cId)
                        .toUri()
        );

        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update the contribution of reviews")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Incorrect data in the DTO"),
            @ApiResponse(code = 404, message = "No movie found or no user found"),
            @ApiResponse(code = 404, message = "An ID conflict or element exists"),
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(value = "/contributions/reviews/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public
    void updateReviewContribution(
            @ApiParam(value = "The contribution ID", required = true) @PathVariable final Long id,
            @RequestBody @Valid final ContributionUpdate<Review> contribution
    ) {
        log.info("Called with id {}, contribution {}", id, contribution);

        this.movieContributionPersistenceService.updateReviewContribution(contribution, id, this.authorizationService.getUserId());
    }
}

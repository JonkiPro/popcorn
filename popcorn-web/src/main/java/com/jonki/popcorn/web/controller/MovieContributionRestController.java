package com.jonki.popcorn.web.controller;

import com.jonki.popcorn.common.dto.Contribution;
import com.jonki.popcorn.common.dto.DataStatus;
import com.jonki.popcorn.common.dto.MovieField;
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
import com.jonki.popcorn.common.dto.movie.request.ImageRequest;
import com.jonki.popcorn.common.dto.movie.response.ImageResponse;
import com.jonki.popcorn.common.dto.request.ContributionNewRequest;
import com.jonki.popcorn.common.dto.request.ContributionUpdateRequest;
import com.jonki.popcorn.common.dto.search.ContributionSearchResult;
import com.jonki.popcorn.core.service.MovieContributionPersistenceService;
import com.jonki.popcorn.core.service.MovieContributionSearchService;
import com.jonki.popcorn.web.hateoas.assembler.ContributionSearchResultResourceAssembler;
import com.jonki.popcorn.web.hateoas.resource.ContributionResource;
import com.jonki.popcorn.web.hateoas.resource.ContributionSearchResultResource;
import com.jonki.popcorn.web.util.MapUtils;
import com.jonki.popcorn.web.util.MultipartFileUtils;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * REST end-point for supporting Movie Contributions.
 */
@RestController
@RequestMapping(value = "/api/v1.0/movies")
@Slf4j
@Api(value = "Movie Contribution Data API", description = "Provides a list of methods that manage movie contributions")
public class MovieContributionRestController {

    private final MovieContributionPersistenceService movieContributionPersistenceService;
    private final MovieContributionSearchService movieContributionSearchService;
    private final ContributionSearchResultResourceAssembler contributionSearchResultResourceAssembler;

    /**
     *  Constructor.
     *
     * @param movieContributionPersistenceService The contribution persistence service to use
     * @param movieContributionSearchService The contribution search service to use
     * @param contributionSearchResultResourceAssembler Assemble contribution search resources out of contributions
     */
    @Autowired
    public MovieContributionRestController(
            final MovieContributionPersistenceService movieContributionPersistenceService,
            final MovieContributionSearchService movieContributionSearchService,
            final ContributionSearchResultResourceAssembler contributionSearchResultResourceAssembler
    ) {
        this.movieContributionPersistenceService = movieContributionPersistenceService;
        this.movieContributionSearchService = movieContributionSearchService;
        this.contributionSearchResultResourceAssembler = contributionSearchResultResourceAssembler;
    }

    /**
     * Update the contribution's status.
     *
     * @param id The contribution ID
     * @param status Verification status for the contribution
     * @param comment Comment on the contribution (optional)
     */
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
            @ApiParam(value = "The contribution ID", required = true)
            @PathVariable("id") final Long id,
            @ApiParam(value = "Status for the contribution", required = true)
            @RequestParam("status") final VerificationStatus status,
            @ApiParam(value = "Comment for verification")
            @RequestParam(value = "comment", required = false) final String comment
    ) {
        log.info("Called with id {}, status {}, comment {}", id, status, comment);

        this.movieContributionPersistenceService.updateContributionStatus(id, status, comment);
    }

    /**
     * Get contributions for given filter criteria.
     *
     * @param id The movie ID
     * @param field The movie field (optional)
     * @param status The contribution status (optional)
     * @param fromDate Creation date range "from" (optional)
     * @param toDate Creation date range "to" (optional)
     * @param page The page to get
     * @param assembler The paged resources assembler to use
     * @return All contributions matching the criteria
     */
    @ApiOperation(value = "Find contributions")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No movie found") })
    @GetMapping(value = "/{id}/contributions", produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public
    PagedResources<ContributionSearchResultResource> findContributions(
            @ApiParam(value = "The movie ID", required = true)
            @PathVariable("id") final Long id,
            @ApiParam(value = "The movie field")
            @RequestParam(value = "field", required = false) final MovieField field,
            @ApiParam(value = "The contribution status")
            @RequestParam(value = "status", required = false) final DataStatus status,
            @ApiParam(value = "Creation date range \"from\"")
            @RequestParam(value = "fromDate", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") final Date fromDate,
            @ApiParam(value = "Creation date range \"to\"")
            @RequestParam(value = "toDate", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") final Date toDate,
            @PageableDefault(sort = {"created"}, direction = Sort.Direction.DESC) final Pageable page,
            final PagedResourcesAssembler<ContributionSearchResult> assembler
    ) {
        log.info("Called with id {}, field {}, status {}, fromDate {}, toDate {}, page {}",
                id, field, status, fromDate, toDate, page);

        // Build the self link which will be used for the next, previous, etc links
        final Link self = ControllerLinkBuilder
                .linkTo(
                        ControllerLinkBuilder
                                .methodOn(MovieContributionRestController.class)
                                .findContributions(
                                        id,
                                        field,
                                        status,
                                        fromDate,
                                        toDate,
                                        page,
                                        assembler
                                )
                ).withSelfRel();

        return assembler.toResource(this.movieContributionSearchService.findContributions(
                id, field, status, fromDate, toDate, page
        ), this.contributionSearchResultResourceAssembler, self);
    }

    /**
     * Get Movie Contribution for given id.
     *
     * @param id The contribution ID
     * @return The contribution
     */
    @ApiOperation(value = "Get the contribution of titles")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No contribution found") })
    @GetMapping(value = "/contributions/{id}/othertitles", produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public
    ContributionResource<OtherTitle> getOtherTitleContribution(
            @ApiParam(value = "The contribution ID", required = true)
            @PathVariable("id") final Long id
    ) {
        log.info("Called with id {}", id);

        final Contribution<OtherTitle> otherTitleContribution = this.movieContributionSearchService.getOtherTitleContribution(id);

        final Link self = linkTo(
                methodOn(MovieContributionRestController.class).getOtherTitleContribution(id)
        ).withSelfRel();

        return new ContributionResource<>(otherTitleContribution, self);
    }

    /**
     * Create an Contribution with titles.
     *
     * @param id The movie ID for which you want to add a contribution
     * @param contribution Contribution with titles for the movie
     * @return The created contribution
     */
    @ApiOperation(value = "Create the contribution of titles")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Incorrect data in the DTO"),
            @ApiResponse(code = 404, message = "No movie found or no user found"),
            @ApiResponse(code = 409, message = "An ID conflict or element exists"),
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/{id}/contributions/othertitles", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public
    ResponseEntity<Void> createOtherTitleContribution(
            @ApiParam(value = "The movie ID", required = true)
            @PathVariable("id") final Long id,
            @ApiParam(value = "The contribution", required = true)
            @RequestBody @Valid final ContributionNewRequest<OtherTitle> contribution
    ) {
        log.info("Called with id {}, contribution {}", id, contribution);

        final Long cId = this.movieContributionPersistenceService.createOtherTitleContribution(id, contribution);

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
            @ApiResponse(code = 409, message = "An ID conflict or element exists"),
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(value = "/contributions/{id}/othertitles", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public
    void updateOtherTitleContribution(
            @ApiParam(value = "The contribution ID", required = true)
            @PathVariable("id") final Long id,
            @ApiParam(value = "The contribution", required = true)
            @RequestBody @Valid final ContributionUpdateRequest<OtherTitle> contribution
    ) {
        log.info("Called with id {}, contribution {}", id, contribution);

        this.movieContributionPersistenceService.updateOtherTitleContribution(id, contribution);
    }

    @ApiOperation(value = "Get the contribution of release dates")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No contribution found") })
    @GetMapping(value = "/contributions/{id}/releasedates", produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public
    ContributionResource<ReleaseDate> getReleaseDateContribution(
            @ApiParam(value = "The contribution ID", required = true) @PathVariable("id") final Long id
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
            @ApiResponse(code = 409, message = "An ID conflict or element exists"),
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/{id}/contributions/releasedates", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public
    ResponseEntity<Void> createReleaseDateContribution(
            @ApiParam(value = "The movie ID", required = true)
            @PathVariable("id") final Long id,
            @ApiParam(value = "The contribution", required = true)
            @RequestBody @Valid final ContributionNewRequest<ReleaseDate> contribution
    ) {
        log.info("Called with id {}, contribution {}", id, contribution);

        final Long cId = this.movieContributionPersistenceService.createReleaseDateContribution(id, contribution);

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
            @ApiResponse(code = 409, message = "An ID conflict or element exists"),
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(value = "/contributions/{id}/releasedates", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public
    void updateReleaseDateContribution(
            @ApiParam(value = "The contribution ID", required = true)
            @PathVariable("id") final Long id,
            @ApiParam(value = "The contribution", required = true)
            @RequestBody @Valid final ContributionUpdateRequest<ReleaseDate> contribution
    ) {
        log.info("Called with id {}, contribution {}", id, contribution);

        this.movieContributionPersistenceService.updateReleaseDateContribution(id, contribution);
    }

    @ApiOperation(value = "Get the contribution of outlines")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No contribution found") })
    @GetMapping(value = "/contributions/{id}/outlines", produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public
    ContributionResource<Outline> getOutlineContribution(
            @ApiParam(value = "The contribution ID", required = true)
            @PathVariable("id") final Long id
    ) {
        log.info("Called with id {}", id);

        final Contribution<Outline> outlineContribution = this.movieContributionSearchService.getOutlineContribution(id);

        final Link self = linkTo(
                methodOn(MovieContributionRestController.class).getOutlineContribution(id)
        ).withSelfRel();

        return new ContributionResource<>(outlineContribution, self);
    }

    @ApiOperation(value = "Create the contribution of outlines")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Incorrect data in the DTO"),
            @ApiResponse(code = 404, message = "No movie found or no user found"),
            @ApiResponse(code = 409, message = "An ID conflict or element exists"),
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/{id}/contributions/outlines", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public
    ResponseEntity<Void> createOutlineContribution(
            @ApiParam(value = "The movie ID", required = true)
            @PathVariable("id") final Long id,
            @ApiParam(value = "The contribution", required = true)
            @RequestBody @Valid final ContributionNewRequest<Outline> contribution
    ) {
        log.info("Called with id {}, contribution {}", id, contribution);

        final Long cId = this.movieContributionPersistenceService.createOutlineContribution(id, contribution);

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(
                MvcUriComponentsBuilder
                        .fromMethodName(MovieContributionRestController.class, "getOutlineContribution", cId)
                        .buildAndExpand(cId)
                        .toUri()
        );

        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update the contribution of outlines")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Incorrect data in the DTO"),
            @ApiResponse(code = 404, message = "No movie found or no user found"),
            @ApiResponse(code = 409, message = "An ID conflict or element exists"),
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(value = "/contributions/{id}/outlines", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public
    void updateOutlineContribution(
            @ApiParam(value = "The contribution ID", required = true)
            @PathVariable("id") final Long id,
            @ApiParam(value = "The contribution", required = true)
            @RequestBody @Valid final ContributionUpdateRequest<Outline> contribution
    ) {
        log.info("Called with id {}, contribution {}", id, contribution);

        this.movieContributionPersistenceService.updateOutlineContribution(id, contribution);
    }

    @ApiOperation(value = "Get the contribution of summaries")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No contribution found") })
    @GetMapping(value = "/contributions/{id}/summaries", produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public
    ContributionResource<Summary> getSummaryContribution(
            @ApiParam(value = "The contribution ID", required = true)
            @PathVariable("id") final Long id
    ) {
        log.info("Called with id {}", id);

        final Contribution<Summary> summaryContribution = this.movieContributionSearchService.getSummaryContribution(id);

        final Link self = linkTo(
                methodOn(MovieContributionRestController.class).getSummaryContribution(id)
        ).withSelfRel();

        return new ContributionResource<>(summaryContribution, self);
    }

    @ApiOperation(value = "Create the contribution of summaries")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Incorrect data in the DTO"),
            @ApiResponse(code = 404, message = "No movie found or no user found"),
            @ApiResponse(code = 409, message = "An ID conflict or element exists"),
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/{id}/contributions/summaries", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public
    ResponseEntity<Void> createSummaryContribution(
            @ApiParam(value = "The movie ID", required = true)
            @PathVariable("id") final Long id,
            @ApiParam(value = "The contribution", required = true)
            @RequestBody @Valid final ContributionNewRequest<Summary> contribution
    ) {
        log.info("Called with id {}, contribution {}", id, contribution);

        final Long cId = this.movieContributionPersistenceService.createSummaryContribution(id, contribution);

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(
                MvcUriComponentsBuilder
                        .fromMethodName(MovieContributionRestController.class, "getSummaryContribution", cId)
                        .buildAndExpand(cId)
                        .toUri()
        );

        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update the contribution of summaries")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Incorrect data in the DTO"),
            @ApiResponse(code = 404, message = "No movie found or no user found"),
            @ApiResponse(code = 409, message = "An ID conflict or element exists"),
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(value = "/contributions/{id}/summaries", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public
    void updateSummaryContribution(
            @ApiParam(value = "The contribution ID", required = true)
            @PathVariable("id") final Long id,
            @ApiParam(value = "The contribution", required = true)
            @RequestBody @Valid final ContributionUpdateRequest<Summary> contribution
    ) {
        log.info("Called with id {}, contribution {}", id, contribution);

        this.movieContributionPersistenceService.updateSummaryContribution(id, contribution);
    }

    @ApiOperation(value = "Get the contribution of synopses")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No contribution found") })
    @GetMapping(value = "/contributions/{id}/synopses", produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public
    ContributionResource<Synopsis> getSynopsisContribution(
            @ApiParam(value = "The contribution ID", required = true)
            @PathVariable("id") final Long id
    ) {
        log.info("Called with id {}", id);

        final Contribution<Synopsis> synopsisContribution = this.movieContributionSearchService.getSynopsisContribution(id);

        final Link self = linkTo(
                methodOn(MovieContributionRestController.class).getSynopsisContribution(id)
        ).withSelfRel();

        return new ContributionResource<>(synopsisContribution, self);
    }

    @ApiOperation(value = "Create the contribution of synopses")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Incorrect data in the DTO"),
            @ApiResponse(code = 404, message = "No movie found or no user found"),
            @ApiResponse(code = 409, message = "An ID conflict or element exists"),
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/{id}/contributions/synopses", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public
    ResponseEntity<Void> createSynopsisContribution(
            @ApiParam(value = "The movie ID", required = true)
            @PathVariable("id") final Long id,
            @ApiParam(value = "The contribution", required = true)
            @RequestBody @Valid final ContributionNewRequest<Synopsis> contribution
    ) {
        log.info("Called with id {}, contribution {}", id, contribution);

        final Long cId = this.movieContributionPersistenceService.createSynopsisContribution(id, contribution);

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(
                MvcUriComponentsBuilder
                        .fromMethodName(MovieContributionRestController.class, "getSynopsisContribution", cId)
                        .buildAndExpand(cId)
                        .toUri()
        );

        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update the contribution of synopses")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Incorrect data in the DTO"),
            @ApiResponse(code = 404, message = "No movie found or no user found"),
            @ApiResponse(code = 409, message = "An ID conflict or element exists"),
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(value = "/contributions/{id}/synopses", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public
    void updateSynopsisContribution(
            @ApiParam(value = "The contribution ID", required = true)
            @PathVariable("id") final Long id,
            @ApiParam(value = "The contribution", required = true)
            @RequestBody @Valid final ContributionUpdateRequest<Synopsis> contribution
    ) {
        log.info("Called with id {}, contribution {}", id, contribution);

        this.movieContributionPersistenceService.updateSynopsisContribution(id, contribution);
    }

    @ApiOperation(value = "Get the contribution of box offices")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No contribution found") })
    @GetMapping(value = "/contributions/{id}/boxoffices", produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public
    ContributionResource<BoxOffice> getBoxOfficeContribution(
            @ApiParam(value = "The contribution ID", required = true)
            @PathVariable("id") final Long id
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
            @ApiResponse(code = 409, message = "An ID conflict or element exists"),
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/{id}/contributions/boxoffices", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public
    ResponseEntity<Void> createBoxOfficeContribution(
            @ApiParam(value = "The movie ID", required = true)
            @PathVariable("id") final Long id,
            @ApiParam(value = "The contribution", required = true)
            @RequestBody @Valid final ContributionNewRequest<BoxOffice> contribution
    ) {
        log.info("Called with id {}, contribution {}", id, contribution);

        final Long cId = this.movieContributionPersistenceService.createBoxOfficeContribution(id, contribution);

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
            @ApiResponse(code = 409, message = "An ID conflict or element exists"),
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(value = "/contributions/{id}/boxoffices", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public
    void updateBoxOfficeContribution(
            @ApiParam(value = "The contribution ID", required = true)
            @PathVariable("id") final Long id,
            @ApiParam(value = "The contribution", required = true)
            @RequestBody @Valid final ContributionUpdateRequest<BoxOffice> contribution
    ) {
        log.info("Called with id {}, contribution {}", id, contribution);

        this.movieContributionPersistenceService.updateBoxOfficeContribution(id, contribution);
    }

    @ApiOperation(value = "Get the contribution of sites")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No contribution found") })
    @GetMapping(value = "/contributions/{id}/sites", produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public
    ContributionResource<Site> getSiteContribution(
            @ApiParam(value = "The contribution ID", required = true)
            @PathVariable("id") final Long id
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
            @ApiResponse(code = 409, message = "An ID conflict or element exists"),
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/{id}/contributions/sites", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public
    ResponseEntity<Void> createSiteContribution(
            @ApiParam(value = "The movie ID", required = true)
            @PathVariable("id") final Long id,
            @ApiParam(value = "The contribution", required = true)
            @RequestBody @Valid final ContributionNewRequest<Site> contribution
    ) {
        log.info("Called with id {}, contribution {}", id, contribution);

        final Long cId = this.movieContributionPersistenceService.createSiteContribution(id, contribution);

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
            @ApiResponse(code = 409, message = "An ID conflict or element exists"),
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(value = "/contributions/{id}/sites", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public
    void updateSiteContribution(
            @ApiParam(value = "The contribution ID", required = true)
            @PathVariable("id") final Long id,
            @ApiParam(value = "The contribution", required = true)
            @RequestBody @Valid final ContributionUpdateRequest<Site> contribution
    ) {
        log.info("Called with id {}, contribution {}", id, contribution);

        this.movieContributionPersistenceService.updateSiteContribution(id, contribution);
    }

    @ApiOperation(value = "Get the contribution of countries")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No contribution found") })
    @GetMapping(value = "/contributions/{id}/countries", produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public
    ContributionResource<Country> getCountryContribution(
            @ApiParam(value = "The contribution ID", required = true)
            @PathVariable("id") final Long id
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
            @ApiResponse(code = 409, message = "An ID conflict or element exists"),
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/{id}/contributions/countries", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public
    ResponseEntity<Void> createCountryContribution(
            @ApiParam(value = "The movie ID", required = true)
            @PathVariable("id") final Long id,
            @ApiParam(value = "The contribution", required = true)
            @RequestBody @Valid final ContributionNewRequest<Country> contribution
    ) {
        log.info("Called with id {}, contribution {}", id, contribution);

        final Long cId = this.movieContributionPersistenceService.createCountryContribution(id, contribution);

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
            @ApiResponse(code = 409, message = "An ID conflict or element exists"),
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(value = "/contributions/{id}/countries", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public
    void updateCountryContribution(
            @ApiParam(value = "The contribution ID", required = true)
            @PathVariable("id") final Long id,
            @ApiParam(value = "The contribution", required = true)
            @RequestBody @Valid final ContributionUpdateRequest<Country> contribution
    ) {
        log.info("Called with id {}, contribution {}", id, contribution);

        this.movieContributionPersistenceService.updateCountryContribution(id, contribution);
    }

    @ApiOperation(value = "Get the contribution of languages")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No contribution found") })
    @GetMapping(value = "/contributions/{id}/languages", produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public
    ContributionResource<Language> getLanguageContribution(
            @ApiParam(value = "The contribution ID", required = true)
            @PathVariable("id") final Long id
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
            @ApiResponse(code = 409, message = "An ID conflict or element exists"),
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/{id}/contributions/languages", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public
    ResponseEntity<Void> createLanguageContribution(
            @ApiParam(value = "The movie ID", required = true)
            @PathVariable("id") final Long id,
            @ApiParam(value = "The contribution", required = true)
            @RequestBody @Valid final ContributionNewRequest<Language> contribution
    ) {
        log.info("Called with id {}, contribution {}", id, contribution);

        final Long cId = this.movieContributionPersistenceService.createLanguageContribution(id, contribution);

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
            @ApiResponse(code = 409, message = "An ID conflict or element exists"),
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(value = "/contributions/{id}/languages", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public
    void updateLanguageContribution(
            @ApiParam(value = "The contribution ID", required = true)
            @PathVariable("id") final Long id,
            @ApiParam(value = "The contribution", required = true)
            @RequestBody @Valid final ContributionUpdateRequest<Language> contribution
    ) {
        log.info("Called with id {}, contribution {}", id, contribution);

        this.movieContributionPersistenceService.updateLanguageContribution(id, contribution);
    }

    @ApiOperation(value = "Get the contribution of genres")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No contribution found") })
    @GetMapping(value = "/contributions/{id}/genres", produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public
    ContributionResource<Genre> getGenreContribution(
            @ApiParam(value = "The contribution ID", required = true)
            @PathVariable("id") final Long id
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
            @ApiResponse(code = 409, message = "An ID conflict or element exists"),
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/{id}/contributions/genres", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public
    ResponseEntity<Void> createGenreContribution(
            @ApiParam(value = "The movie ID", required = true)
            @PathVariable("id") final Long id,
            @ApiParam(value = "The contribution", required = true)
            @RequestBody @Valid final ContributionNewRequest<Genre> contribution
    ) {
        log.info("Called with id {}, contribution {}", id, contribution);

        final Long cId = this.movieContributionPersistenceService.createGenreContribution(id, contribution);

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
            @ApiResponse(code = 409, message = "An ID conflict or element exists"),
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(value = "/contributions/{id}/genres", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public
    void updateGenreContribution(
            @ApiParam(value = "The contribution ID", required = true)
            @PathVariable("id") final Long id,
            @ApiParam(value = "The contribution", required = true)
            @RequestBody @Valid final ContributionUpdateRequest<Genre> contribution
    ) {
        log.info("Called with id {}, contribution {}", id, contribution);

        this.movieContributionPersistenceService.updateGenreContribution(id, contribution);
    }

    @ApiOperation(value = "Get the contribution of reviews")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No contribution found") })
    @GetMapping(value = "/contributions/{id}/reviews", produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public
    ContributionResource<Review> getReviewContribution(
            @ApiParam(value = "The contribution ID", required = true)
            @PathVariable("id") final Long id
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
            @ApiResponse(code = 409, message = "An ID conflict or element exists"),
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/{id}/contributions/reviews", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public
    ResponseEntity<Void> createReviewContribution(
            @ApiParam(value = "The movie ID", required = true)
            @PathVariable("id") final Long id,
            @ApiParam(value = "The contribution", required = true)
            @RequestBody @Valid final ContributionNewRequest<Review> contribution
    ) {
        log.info("Called with id {}, contribution {}", id, contribution);

        final Long cId = this.movieContributionPersistenceService.createReviewContribution(id, contribution);

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
            @ApiResponse(code = 409, message = "An ID conflict or element exists"),
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(value = "/contributions/{id}/reviews", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public
    void updateReviewContribution(
            @ApiParam(value = "The contribution ID", required = true)
            @PathVariable("id") final Long id,
            @ApiParam(value = "The contribution", required = true)
            @RequestBody @Valid final ContributionUpdateRequest<Review> contribution
    ) {
        log.info("Called with id {}, contribution {}", id, contribution);

        this.movieContributionPersistenceService.updateReviewContribution(id, contribution);
    }

    @ApiOperation(value = "Get the contribution of photos")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No contribution found") })
    @GetMapping(value = "/contributions/{id}/photos", produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public
    ContributionResource<ImageResponse> getPhotoContribution(
            @ApiParam(value = "The contribution ID", required = true)
            @PathVariable("id") final Long id
    ) {
        log.info("Called with id {}", id);

        final Contribution<ImageResponse> photoContribution = this.movieContributionSearchService.getPhotoContribution(id);

        final Link self = linkTo(
                methodOn(MovieContributionRestController.class).getPhotoContribution(id)
        ).withSelfRel();

        return new ContributionResource<>(photoContribution, self);
    }

    @ApiOperation(value = "Create the contribution of photos")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Incorrect data"),
            @ApiResponse(code = 404, message = "No movie found or no user found"),
            @ApiResponse(code = 409, message = "An ID conflict or element exists"),
            @ApiResponse(code = 412, message = "An I/O error occurs or incorrect content type"),
            @ApiResponse(code = 500, message = "An error occurred with the server")
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/{id}/contributions/photos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @SuppressWarnings("unchecked")
    public
    ResponseEntity<Void> createPhotoContribution(
            @ApiParam(value = "The movie ID", required = true)
            @PathVariable("id") final Long id,
            @ApiParam(value = "Elements to be added")
            @RequestPart(required = false) List<MultipartFile> elementsToAdd,
            @ApiParam(value = "Element IDs to be updated")
            @RequestParam(defaultValue = "", required = false) Set<Long> idsToUpdate,
            @ApiParam(value = "Element to be updated")
            @RequestPart(required = false) List<MultipartFile> elementsToUpdate,
            @ApiParam(value = "Element IDs to be deleted")
            @RequestParam(defaultValue = "", required = false) Set<Long> idsToDelete,
            @ApiParam(value = "Sources of information(elements)", required = true)
            @RequestParam final Set<String> sources,
            @ApiParam(value = "Comment from the user")
            @RequestParam(required = false) final String comment
    ) {
        log.info("Called with id {}, elementsToAdd {}, idsToUpdate {}, elementsToUpdate{}, idsToDelete {}," +
                        " sources {}, comment {}",
                id, elementsToAdd, idsToUpdate, elementsToUpdate, idsToDelete, sources, comment);

        final ContributionNewRequest.Builder<ImageRequest> builder = new ContributionNewRequest.Builder<>(
                sources
        );

        final List<ImageRequest> listPhotosToAdd = new ArrayList<>();
        for(final MultipartFile multipartFile : elementsToAdd) {
            final ImageRequest.Builder bir = new ImageRequest.Builder().withFile(MultipartFileUtils.convert(multipartFile));
            listPhotosToAdd.add(bir.build());
        }
        builder.withElementsToAdd(listPhotosToAdd);

        final HashMap<Long, ImageRequest> mapPhotosToUpdate = MapUtils.merge(idsToUpdate, elementsToUpdate);
        builder.withElementsToUpdate(mapPhotosToUpdate);

        builder.withIdsToDelete(idsToDelete);
        builder.withComment(comment);

        final Long cId = this.movieContributionPersistenceService.createPhotoContribution(id, builder.build());

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(
                MvcUriComponentsBuilder
                        .fromMethodName(MovieContributionRestController.class, "getPhotoContribution", cId)
                        .buildAndExpand(cId)
                        .toUri()
        );

        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update the contribution of photos")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Incorrect data"),
            @ApiResponse(code = 404, message = "No movie found or no user found"),
            @ApiResponse(code = 409, message = "An ID conflict or element exists"),
            @ApiResponse(code = 412, message = "An I/O error occurs or incorrect content type"),
            @ApiResponse(code = 500, message = "An error occurred with the server")
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(value = "/contributions/{id}/photos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SuppressWarnings("unchecked")
    public
    void updatePhotoContribution(
            @ApiParam(value = "The movie ID", required = true)
            @PathVariable("id") final Long id,
            @ApiParam(value = "New elements to be added")
            @RequestPart(required = false) List<MultipartFile> newElementsToAdd,
            @ApiParam(value = "Element IDs to be added")
            @RequestParam(defaultValue = "", required = false) Set<Long> idsToAdd,
            @ApiParam(value = "Element to be updated")
            @RequestPart(required = false) List<MultipartFile> elementsToAdd,
            @ApiParam(value = "Element IDs to be updated")
            @RequestParam(defaultValue = "", required = false) Set<Long> idsToUpdate,
            @ApiParam(value = "Element to be updated")
            @RequestPart(required = false) List<MultipartFile> elementsToUpdate,
            @ApiParam(value = "Element IDs to be deleted")
            @RequestParam(defaultValue = "", required = false) Set<Long> idsToDelete,
            @ApiParam(value = "Sources of information(elements)", required = true)
            @RequestParam final Set<String> sources,
            @ApiParam(value = "Comment from the user")
            @RequestParam(required = false) final String comment
    ) {
        log.info("Called with id {}, idsToAdd {}, elementsToAdd{}, newElementsToAdd {}, idsToUpdate {}," +
                "elementsToUpdate {}, idsToDelete {}, sources {}, comment {}", id, idsToAdd,
                elementsToAdd, newElementsToAdd, idsToUpdate, elementsToUpdate, idsToDelete, sources, comment);

        final ContributionUpdateRequest.Builder<ImageRequest> builder = new ContributionUpdateRequest.Builder<>(
                sources
        );

        final HashMap<Long, ImageRequest> mapPhotosToAdd = MapUtils.merge(idsToAdd, elementsToAdd);
        builder.withElementsToAdd(mapPhotosToAdd);

        final List<ImageRequest> listNewPhotosToAdd = new ArrayList<>();
        for(final MultipartFile multipartFile : newElementsToAdd) {
            final ImageRequest.Builder bir = new ImageRequest.Builder().withFile(MultipartFileUtils.convert(multipartFile));
            listNewPhotosToAdd.add(bir.build());
        }
        builder.withNewElementsToAdd(listNewPhotosToAdd);

        final HashMap<Long, ImageRequest> mapPhotosToUpdate = MapUtils.merge(idsToUpdate, elementsToUpdate);
        builder.withElementsToUpdate(mapPhotosToUpdate);

        builder.withIdsToDelete(idsToDelete);
        builder.withComment(comment);

        this.movieContributionPersistenceService.updatePhotoContribution(id, builder.build());
    }

    @ApiOperation(value = "Get the contribution of posters")
    @ApiResponses(value = { @ApiResponse(code = 404, message = "No contribution found") })
    @GetMapping(value = "/contributions/{id}/posters", produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public
    ContributionResource<ImageResponse> getPosterContribution(
            @ApiParam(value = "The contribution ID", required = true)
            @PathVariable("id") final Long id
    ) {
        log.info("Called with id {}", id);

        final Contribution<ImageResponse> posterContribution = this.movieContributionSearchService.getPosterContribution(id);

        final Link self = linkTo(
                methodOn(MovieContributionRestController.class).getPosterContribution(id)
        ).withSelfRel();

        return new ContributionResource<>(posterContribution, self);
    }

    @ApiOperation(value = "Create the contribution of posters")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Incorrect data"),
            @ApiResponse(code = 404, message = "No movie found or no user found"),
            @ApiResponse(code = 409, message = "An ID conflict or element exists"),
            @ApiResponse(code = 412, message = "An I/O error occurs or incorrect content type"),
            @ApiResponse(code = 500, message = "An error occurred with the server")
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/{id}/contributions/posters", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @SuppressWarnings("unchecked")
    public
    ResponseEntity<Void> createPosterContribution(
            @ApiParam(value = "The movie ID", required = true)
            @PathVariable("id") final Long id,
            @ApiParam(value = "Elements to be added")
            @RequestPart(required = false) List<MultipartFile> elementsToAdd,
            @ApiParam(value = "Element IDs to be updated")
            @RequestParam(defaultValue = "", required = false) Set<Long> idsToUpdate,
            @ApiParam(value = "Element to be updated")
            @RequestPart(required = false) List<MultipartFile> elementsToUpdate,
            @ApiParam(value = "Element IDs to be deleted")
            @RequestParam(defaultValue = "", required = false) Set<Long> idsToDelete,
            @ApiParam(value = "Sources of information(elements)", required = true)
            @RequestParam final Set<String> sources,
            @ApiParam(value = "Comment from the user")
            @RequestParam(required = false) final String comment
    ) {
        log.info("Called with id {}, elementsToAdd {}, idsToUpdate {}, elementsToUpdate{}, idsToDelete {}," +
                        " sources {}, comment {}",
                id, elementsToAdd, idsToUpdate, elementsToUpdate, idsToDelete, sources, comment);

        final ContributionNewRequest.Builder<ImageRequest> builder = new ContributionNewRequest.Builder<>(
                sources
        );

        final List<ImageRequest> listPhotosToAdd = new ArrayList<>();
        for(final MultipartFile multipartFile : elementsToAdd) {
            final ImageRequest.Builder bir = new ImageRequest.Builder().withFile(MultipartFileUtils.convert(multipartFile));
            listPhotosToAdd.add(bir.build());
        }
        builder.withElementsToAdd(listPhotosToAdd);

        final HashMap<Long, ImageRequest> mapPhotosToUpdate = MapUtils.merge(idsToUpdate, elementsToUpdate);
        builder.withElementsToUpdate(mapPhotosToUpdate);

        builder.withIdsToDelete(idsToDelete);
        builder.withComment(comment);

        final Long cId = this.movieContributionPersistenceService.createPosterContribution(id, builder.build());

        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(
                MvcUriComponentsBuilder
                        .fromMethodName(MovieContributionRestController.class, "getPosterContribution", cId)
                        .buildAndExpand(cId)
                        .toUri()
        );

        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update the contribution of posters")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Incorrect data"),
            @ApiResponse(code = 404, message = "No movie found or no user found"),
            @ApiResponse(code = 409, message = "An ID conflict or element exists"),
            @ApiResponse(code = 412, message = "An I/O error occurs or incorrect content type"),
            @ApiResponse(code = 500, message = "An error occurred with the server")
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping(value = "/contributions/{id}/posters", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SuppressWarnings("unchecked")
    public
    void updatePosterContribution(
            @ApiParam(value = "The movie ID", required = true)
            @PathVariable("id") final Long id,
            @ApiParam(value = "New elements to be added")
            @RequestPart(required = false) List<MultipartFile> newElementsToAdd,
            @ApiParam(value = "Element IDs to be added")
            @RequestParam(defaultValue = "", required = false) Set<Long> idsToAdd,
            @ApiParam(value = "Element to be updated")
            @RequestPart(required = false) List<MultipartFile> elementsToAdd,
            @ApiParam(value = "Element IDs to be updated")
            @RequestParam(defaultValue = "", required = false) Set<Long> idsToUpdate,
            @ApiParam(value = "Element to be updated")
            @RequestPart(required = false) List<MultipartFile> elementsToUpdate,
            @ApiParam(value = "Element IDs to be deleted")
            @RequestParam(defaultValue = "", required = false) Set<Long> idsToDelete,
            @ApiParam(value = "Sources of information(elements)", required = true)
            @RequestParam final Set<String> sources,
            @ApiParam(value = "Comment from the user")
            @RequestParam(required = false) final String comment
    ) {
        log.info("Called with id {}, idsToAdd {}, elementsToAdd{}, newElementsToAdd {}, idsToUpdate {}," +
                        "elementsToUpdate {}, idsToDelete {}, sources {}, comment {}", id, idsToAdd,
                elementsToAdd, newElementsToAdd, idsToUpdate, elementsToUpdate, idsToDelete, sources, comment);

        final ContributionUpdateRequest.Builder<ImageRequest> builder = new ContributionUpdateRequest.Builder<>(
                sources
        );

        final HashMap<Long, ImageRequest> mapPhotosToAdd = MapUtils.merge(idsToAdd, elementsToAdd);
        builder.withElementsToAdd(mapPhotosToAdd);

        final List<ImageRequest> listNewPhotosToAdd = new ArrayList<>();
        for(final MultipartFile multipartFile : newElementsToAdd) {
            final ImageRequest.Builder bir = new ImageRequest.Builder().withFile(MultipartFileUtils.convert(multipartFile));
            listNewPhotosToAdd.add(bir.build());
        }
        builder.withNewElementsToAdd(listNewPhotosToAdd);

        final HashMap<Long, ImageRequest> mapPhotosToUpdate = MapUtils.merge(idsToUpdate, elementsToUpdate);
        builder.withElementsToUpdate(mapPhotosToUpdate);

        builder.withIdsToDelete(idsToDelete);
        builder.withComment(comment);

        this.movieContributionPersistenceService.updatePosterContribution(id, builder.build());
    }
}

package com.jonki.popcorn.core.service;

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
import com.jonki.popcorn.common.dto.request.ContributionNewRequest;
import com.jonki.popcorn.common.dto.request.ContributionUpdateRequest;
import com.jonki.popcorn.common.exception.ResourceConflictException;
import com.jonki.popcorn.common.exception.ResourceForbiddenException;
import com.jonki.popcorn.common.exception.ResourceNotFoundException;
import com.jonki.popcorn.common.exception.ResourcePreconditionException;
import com.jonki.popcorn.common.exception.ResourceServerException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Interfaces for providing persistence functions for contributions other than search.
 */
@PreAuthorize("hasRole('ROLE_USER')")
@Validated
public interface MovieContributionPersistenceService {

    /**
     * Update the contribution status.
     *
     * @param id The contribution ID
     * @param status Status for the contribution
     * @param comment Comment for verification
     * @throws ResourceForbiddenException if no permissions
     * @throws ResourceNotFoundException if no contribution found or no user found
     */
    void updateContributionStatus(
            @Min(1) final Long id,
            @NotNull final VerificationStatus status,
            final String comment
    ) throws ResourceForbiddenException, ResourceNotFoundException;

    /**
     * Save the contribution of the titles for the movie.
     *
     * @param movieId The movie ID
     * @param contribution The contribution of titles
     * @return The ID of the contribution created
     * @throws ResourceNotFoundException if no movie found or no user found
     * @throws ResourceConflictException if there is an IDs conflict or an element exists
     */
    Long createOtherTitleContribution(
            @Min(1) final Long movieId,
            @NotNull @Valid final ContributionNewRequest<OtherTitle> contribution
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Update the contribution of the titles for the movie.
     *
     * @param contributionId The contribution ID
     * @param contribution The contribution of titles
     * @throws ResourceNotFoundException if no contribution found or no user found
     * @throws ResourceConflictException if the element exists
     */
    void updateOtherTitleContribution(
            @Min(1) final Long contributionId,
            @NotNull @Valid final ContributionUpdateRequest<OtherTitle> contribution
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Save the contribution of the release dates for the movie.
     *
     * @param movieId The movie ID
     * @param contribution The contribution of release dates
     * @return The ID of the contribution created
     * @throws ResourceNotFoundException if no movie found or no user found
     * @throws ResourceConflictException if there is an IDs conflict or an element exists
     */
    Long createReleaseDateContribution(
            @Min(1) final Long movieId,
            @NotNull @Valid final ContributionNewRequest<ReleaseDate> contribution
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Update the contribution of the release dates for the movie.
     *
     * @param contributionId The contribution ID
     * @param contribution The contribution of release dates
     * @throws ResourceNotFoundException if no contribution found or no user found
     * @throws ResourceConflictException if the element exists
     */
    void updateReleaseDateContribution(
            @Min(1) final Long contributionId,
            @NotNull @Valid final ContributionUpdateRequest<ReleaseDate> contribution
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Save the contribution of the outlines for the movie.
     *
     * @param movieId The movie ID
     * @param contribution The contribution of outlines
     * @return The ID of the contribution created
     * @throws ResourceNotFoundException if no movie found or no user found
     * @throws ResourceConflictException if there is an IDs conflict or an element exists
     */
    Long createOutlineContribution(
            @Min(1) final Long movieId,
            @NotNull @Valid final ContributionNewRequest<Outline> contribution
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Update the contribution of the outlines for the movie.
     *
     * @param contributionId The contribution ID
     * @param contribution The contribution of outlines
     * @throws ResourceNotFoundException if no contribution found or no user found
     * @throws ResourceConflictException if the element exists
     */
    void updateOutlineContribution(
            @Min(1) final Long contributionId,
            @NotNull @Valid final ContributionUpdateRequest<Outline> contribution
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Save the contribution of the summaries for the movie.
     *
     * @param movieId The movie ID
     * @param contribution The contribution of summaries
     * @return The ID of the contribution created
     * @throws ResourceNotFoundException if no movie found or no user found
     * @throws ResourceConflictException if there is an IDs conflict or an element exists
     */
    Long createSummaryContribution(
            @Min(1) final Long movieId,
            @NotNull @Valid final ContributionNewRequest<Summary> contribution
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Update the contribution of the summaries for the movie.
     *
     * @param contributionId The contribution ID
     * @param contribution The contribution of summaries
     * @throws ResourceNotFoundException if no contribution found or no user found
     * @throws ResourceConflictException if the element exists
     */
    void updateSummaryContribution(
            @Min(1) final Long contributionId,
            @NotNull @Valid final ContributionUpdateRequest<Summary> contribution
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Save the contribution of the synopses for the movie.
     *
     * @param movieId The movie ID
     * @param contribution The contribution of synopses
     * @return The ID of the contribution created
     * @throws ResourceNotFoundException if no movie found or no user found
     * @throws ResourceConflictException if there is an IDs conflict or an element exists
     */
    Long createSynopsisContribution(
            @Min(1) final Long movieId,
            @NotNull @Valid final ContributionNewRequest<Synopsis> contribution
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Update the contribution of the synopses for the movie.
     *
     * @param contributionId The contribution ID
     * @param contribution The contribution of synopses
     * @throws ResourceNotFoundException if no contribution found or no user found
     * @throws ResourceConflictException if the element exists
     */
    void updateSynopsisContribution(
            @Min(1) final Long contributionId,
            @NotNull @Valid final ContributionUpdateRequest<Synopsis> contribution
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Save the contribution of the box offices for the movie.
     *
     * @param movieId The movie ID
     * @param contribution The contribution of box offices
     * @return The ID of the contribution created
     * @throws ResourceNotFoundException if no movie found or no user found
     * @throws ResourceConflictException if there is an IDs conflict or an element exists
     */
    Long createBoxOfficeContribution(
            @Min(1) final Long movieId,
            @NotNull @Valid final ContributionNewRequest<BoxOffice> contribution
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Update the contribution of the box offices for the movie.
     *
     * @param contributionId The contribution ID
     * @param contribution The contribution of box offices
     * @throws ResourceNotFoundException if no contribution found or no user found
     * @throws ResourceConflictException if the element exists
     */
    void updateBoxOfficeContribution(
            @Min(1) final Long contributionId,
            @NotNull @Valid final ContributionUpdateRequest<BoxOffice> contribution
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Save the contribution of the sites for the movie.
     *
     * @param movieId The movie ID
     * @param contribution The contribution of sites
     * @return The ID of the contribution created
     * @throws ResourceNotFoundException if no movie found or no user found
     * @throws ResourceConflictException if there is an IDs conflict or an element exists
     */
    Long createSiteContribution(
            @Min(1) final Long movieId,
            @NotNull @Valid final ContributionNewRequest<Site> contribution
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Update the contribution of the sites for the movie.
     *
     * @param contributionId The contribution ID
     * @param contribution The contribution of sites
     * @throws ResourceNotFoundException if no contribution found or no user found
     * @throws ResourceConflictException if the element exists
     */
    void updateSiteContribution(
            @Min(1) final Long contributionId,
            @NotNull @Valid final ContributionUpdateRequest<Site> contribution
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Save the contribution of the countries for the movie.
     *
     * @param movieId The movie ID
     * @param contribution The contribution of countries
     * @return The ID of the contribution created
     * @throws ResourceNotFoundException if no movie found or no user found
     * @throws ResourceConflictException if there is an IDs conflict or an element exists
     */
    Long createCountryContribution(
            @Min(1) final Long movieId,
            @NotNull @Valid final ContributionNewRequest<Country> contribution
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Update the contribution of the countries for the movie.
     *
     * @param contributionId The contribution ID
     * @param contribution The contribution of countries
     * @throws ResourceNotFoundException if no contribution found or no user found
     * @throws ResourceConflictException if the element exists
     */
    void updateCountryContribution(
            @Min(1) final Long contributionId,
            @NotNull @Valid final ContributionUpdateRequest<Country> contribution
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Save the contribution of the languages for the movie.
     *
     * @param movieId The movie ID
     * @param contribution The contribution of languages
     * @return The ID of the contribution created
     * @throws ResourceNotFoundException if no movie found or no user found
     * @throws ResourceConflictException if there is an IDs conflict or an element exists
     */
    Long createLanguageContribution(
            @Min(1) final Long movieId,
            @NotNull @Valid final ContributionNewRequest<Language> contribution
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Update the contribution of the languages for the movie.
     *
     * @param contributionId The contribution ID
     * @param contribution The contribution of languages
     * @throws ResourceNotFoundException if no contribution found or no user found
     * @throws ResourceConflictException if the element exists
     */
    void updateLanguageContribution(
            @Min(1) final Long contributionId,
            @NotNull @Valid final ContributionUpdateRequest<Language> contribution
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Save the contribution of the genres for the movie.
     *
     * @param movieId The movie ID
     * @param contribution The contribution of genres
     * @return The ID of the contribution created
     * @throws ResourceNotFoundException if no movie found or no user found
     * @throws ResourceConflictException if there is an IDs conflict or an element exists
     */
    Long createGenreContribution(
            @Min(1) final Long movieId,
            @NotNull @Valid final ContributionNewRequest<Genre> contribution
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Update the contribution of the genres for the movie.
     *
     * @param contributionId The contribution ID
     * @param contribution The contribution of genres
     * @throws ResourceNotFoundException if no contribution found or no user found
     * @throws ResourceConflictException if the element exists
     */
    void updateGenreContribution(
            @Min(1) final Long contributionId,
            @NotNull @Valid final ContributionUpdateRequest<Genre> contribution
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Save the contribution of the reviews for the movie.
     *
     * @param movieId The movie ID
     * @param contribution The contribution of reviews
     * @return The ID of the contribution created
     * @throws ResourceNotFoundException if no movie found or no user found
     * @throws ResourceConflictException if there is an IDs conflict or an element exists
     */
    Long createReviewContribution(
            @Min(1) final Long movieId,
            @NotNull @Valid final ContributionNewRequest<Review> contribution
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Update the contribution of the reviews for the movie.
     *
     * @param contributionId The contribution ID
     * @param contribution The contribution of reviews
     * @throws ResourceNotFoundException if no contribution found or no user found
     * @throws ResourceConflictException if the element exists
     */
    void updateReviewContribution(
            @Min(1) final Long contributionId,
            @NotNull @Valid final ContributionUpdateRequest<Review> contribution
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Save the contribution of the photos for the movie.
     *
     * @param movieId The movie ID
     * @param contribution The contribution of photos
     * @return The ID of the contribution created
     * @throws ResourceNotFoundException if no movie found or no user found
     * @throws ResourceConflictException if there is an IDs conflict or an element exists
     * @throws ResourcePreconditionException if an I/O error occurs or incorrect content type
     * @throws ResourceServerException if an error occurred with the server
     */
    Long createPhotoContribution(
            @Min(1) final Long movieId,
            @NotNull @Valid final ContributionNewRequest<ImageRequest> contribution
    ) throws ResourceNotFoundException, ResourceConflictException, ResourcePreconditionException, ResourceServerException;

    /**
     * Update the contribution of the photos for the movie.
     *
     * @param contributionId The contribution ID
     * @param contribution The contribution of photos
     * @throws ResourceNotFoundException if no contribution found or no user found
     * @throws ResourceConflictException if the element exists
     * @throws ResourcePreconditionException if an I/O error occurs or incorrect content type
     * @throws ResourceServerException if an error occurred with the server
     */
    void updatePhotoContribution(
            @Min(1) final Long contributionId,
            @NotNull @Valid final ContributionUpdateRequest<ImageRequest> contribution
    ) throws ResourceNotFoundException, ResourceConflictException, ResourcePreconditionException, ResourceServerException;

    /**
     * Save the contribution of the posters for the movie.
     *
     * @param movieId The movie ID
     * @param contribution The contribution of posters
     * @return The ID of the contribution created
     * @throws ResourceNotFoundException if no movie found or no user found
     * @throws ResourceConflictException if there is an IDs conflict or an element exists
     * @throws ResourcePreconditionException if an I/O error occurs or incorrect content type
     * @throws ResourceServerException if an error occurred with the server
     */
    Long createPosterContribution(
            @Min(1) final Long movieId,
            @NotNull @Valid final ContributionNewRequest<ImageRequest> contribution
    ) throws ResourceNotFoundException, ResourceConflictException, ResourcePreconditionException, ResourceServerException;

    /**
     * Update the contribution of the posters for the movie.
     *
     * @param contributionId The contribution ID
     * @param contribution The contribution of posters
     * @throws ResourceNotFoundException if no contribution found or no user found
     * @throws ResourceConflictException if the element exists
     * @throws ResourcePreconditionException if an I/O error occurs or incorrect content type
     * @throws ResourceServerException if an error occurred with the server
     */
    void updatePosterContribution(
            @Min(1) final Long contributionId,
            @NotNull @Valid final ContributionUpdateRequest<ImageRequest> contribution
    ) throws ResourceNotFoundException, ResourceConflictException, ResourcePreconditionException, ResourceServerException;
}

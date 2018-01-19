package com.core.service;

import com.common.dto.VerificationStatus;
import com.common.dto.movie.*;
import com.common.dto.movie.request.ContributionNew;
import com.common.dto.movie.request.ContributionUpdate;
import com.common.dto.movie.request.ImageRequest;
import com.common.exception.*;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * Interfaces for providing persistence functions for contributions other than search.
 */
@Validated
public interface MovieContributionPersistenceService {

    /**
     * Update the contribution status.
     *
     * @param contributionId The contribution ID
     * @param userId The user ID
     * @param status Status for the contribution
     * @param comment Comment for verification
     * @throws ResourceForbiddenException if no permissions
     * @throws ResourceNotFoundException if no contribution found or no user found
     */
    void updateContributionStatus(
            @Min(1) final Long contributionId,
            @NotBlank final String userId,
            @NotNull final VerificationStatus status,
            final String comment
    ) throws ResourceForbiddenException, ResourceNotFoundException;

    /**
     * Save the contribution of the titles for the movie.
     *
     * @param contribution The contribution of titles
     * @param movieId The movie ID
     * @param userId The user ID
     * @return The ID of the contribution created
     * @throws ResourceNotFoundException if no movie found or no user found
     * @throws ResourceConflictException if there is an IDs conflict or an element exists
     */
    Long createOtherTitleContribution(
            @NotNull @Valid final ContributionNew<OtherTitle> contribution,
            @Min(1) final Long movieId,
            @NotBlank final String userId
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Update the contribution of the titles for the movie.
     *
     * @param contribution The contribution of titles
     * @param contributionId The contribution ID
     * @param userId The user ID
     * @throws ResourceNotFoundException if no contribution found or no user found
     * @throws ResourceConflictException if the element exists
     */
    void updateOtherTitleContribution(
            @NotNull @Valid final ContributionUpdate<OtherTitle> contribution,
            @Min(1) final Long contributionId,
            @NotBlank final String userId
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Save the contribution of the release dates for the movie.
     *
     * @param contribution The contribution of release dates
     * @param movieId The movie ID
     * @param userId The user ID
     * @return The ID of the contribution created
     * @throws ResourceNotFoundException if no movie found or no user found
     * @throws ResourceConflictException if there is an IDs conflict or an element exists
     */
    Long createReleaseDateContribution(
            @NotNull @Valid final ContributionNew<ReleaseDate> contribution,
            @Min(1) final Long movieId,
            @NotBlank final String userId
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Update the contribution of the release dates for the movie.
     *
     * @param contribution The contribution of release dates
     * @param contributionId The contribution ID
     * @param userId The user ID
     * @throws ResourceNotFoundException if no contribution found or no user found
     * @throws ResourceConflictException if the element exists
     */
    void updateReleaseDateContribution(
            @NotNull @Valid final ContributionUpdate<ReleaseDate> contribution,
            @Min(1) final Long contributionId,
            @NotBlank final String userId
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Save the contribution of the storylines for the movie.
     *
     * @param contribution The contribution of storylines
     * @param movieId The movie ID
     * @param userId The user ID
     * @return The ID of the contribution created
     * @throws ResourceNotFoundException if no movie found or no user found
     * @throws ResourceConflictException if there is an IDs conflict or an element exists
     */
    Long createStorylineContribution(
            @NotNull @Valid final ContributionNew<Storyline> contribution,
            @Min(1) final Long movieId,
            @NotBlank final String userId
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Update the contribution of the storylines for the movie.
     *
     * @param contribution The contribution of storylines
     * @param contributionId The contribution ID
     * @param userId The user ID
     * @throws ResourceNotFoundException if no contribution found or no user found
     * @throws ResourceConflictException if the element exists
     */
    void updateStorylineContribution(
            @NotNull @Valid final ContributionUpdate<Storyline> contribution,
            @Min(1) final Long contributionId,
            @NotBlank final String userId
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Save the contribution of the box offices for the movie.
     *
     * @param contribution The contribution of box offices
     * @param movieId The movie ID
     * @param userId The user ID
     * @return The ID of the contribution created
     * @throws ResourceNotFoundException if no movie found or no user found
     * @throws ResourceConflictException if there is an IDs conflict or an element exists
     */
    Long createBoxOfficeContribution(
            @NotNull @Valid final ContributionNew<BoxOffice> contribution,
            @Min(1) final Long movieId,
            @NotBlank final String userId
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Update the contribution of the box offices for the movie.
     *
     * @param contribution The contribution of box offices
     * @param contributionId The contribution ID
     * @param userId The user ID
     * @throws ResourceNotFoundException if no contribution found or no user found
     * @throws ResourceConflictException if the element exists
     */
    void updateBoxOfficeContribution(
            @NotNull @Valid final ContributionUpdate<BoxOffice> contribution,
            @Min(1) final Long contributionId,
            @NotBlank final String userId
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Save the contribution of the sites for the movie.
     *
     * @param contribution The contribution of sites
     * @param movieId The movie ID
     * @param userId The user ID
     * @return The ID of the contribution created
     * @throws ResourceNotFoundException if no movie found or no user found
     * @throws ResourceConflictException if there is an IDs conflict or an element exists
     */
    Long createSiteContribution(
            @NotNull @Valid final ContributionNew<Site> contribution,
            @Min(1) final Long movieId,
            @NotBlank final String userId
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Update the contribution of the sites for the movie.
     *
     * @param contribution The contribution of sites
     * @param contributionId The contribution ID
     * @param userId The user ID
     * @throws ResourceNotFoundException if no contribution found or no user found
     * @throws ResourceConflictException if the element exists
     */
    void updateSiteContribution(
            @NotNull @Valid final ContributionUpdate<Site> contribution,
            @Min(1) final Long contributionId,
            @NotBlank final String userId
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Save the contribution of the countries for the movie.
     *
     * @param contribution The contribution of countries
     * @param movieId The movie ID
     * @param userId The user ID
     * @return The ID of the contribution created
     * @throws ResourceNotFoundException if no movie found or no user found
     * @throws ResourceConflictException if there is an IDs conflict or an element exists
     */
    Long createCountryContribution(
            @NotNull @Valid final ContributionNew<Country> contribution,
            @Min(1) final Long movieId,
            @NotBlank final String userId
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Update the contribution of the countries for the movie.
     *
     * @param contribution The contribution of countries
     * @param contributionId The contribution ID
     * @param userId The user ID
     * @throws ResourceNotFoundException if no contribution found or no user found
     * @throws ResourceConflictException if the element exists
     */
    void updateCountryContribution(
            @NotNull @Valid final ContributionUpdate<Country> contribution,
            @Min(1) final Long contributionId,
            @NotBlank final String userId
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Save the contribution of the languages for the movie.
     *
     * @param contribution The contribution of languages
     * @param movieId The movie ID
     * @param userId The user ID
     * @return The ID of the contribution created
     * @throws ResourceNotFoundException if no movie found or no user found
     * @throws ResourceConflictException if there is an IDs conflict or an element exists
     */
    Long createLanguageContribution(
            @NotNull @Valid final ContributionNew<Language> contribution,
            @Min(1) final Long movieId,
            @NotBlank final String userId
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Update the contribution of the languages for the movie.
     *
     * @param contribution The contribution of languages
     * @param contributionId The contribution ID
     * @param userId The user ID
     * @throws ResourceNotFoundException if no contribution found or no user found
     * @throws ResourceConflictException if the element exists
     */
    void updateLanguageContribution(
            @NotNull @Valid final ContributionUpdate<Language> contribution,
            @Min(1) final Long contributionId,
            @NotBlank final String userId
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Save the contribution of the genres for the movie.
     *
     * @param contribution The contribution of genres
     * @param movieId The movie ID
     * @param userId The user ID
     * @return The ID of the contribution created
     * @throws ResourceNotFoundException if no movie found or no user found
     * @throws ResourceConflictException if there is an IDs conflict or an element exists
     */
    Long createGenreContribution(
            @NotNull @Valid final ContributionNew<Genre> contribution,
            @Min(1) final Long movieId,
            @NotBlank final String userId
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Update the contribution of the genres for the movie.
     *
     * @param contribution The contribution of genres
     * @param contributionId The contribution ID
     * @param userId The user ID
     * @throws ResourceNotFoundException if no contribution found or no user found
     * @throws ResourceConflictException if the element exists
     */
    void updateGenreContribution(
            @NotNull @Valid final ContributionUpdate<Genre> contribution,
            @Min(1) final Long contributionId,
            @NotBlank final String userId
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Save the contribution of the reviews for the movie.
     *
     * @param contribution The contribution of reviews
     * @param movieId The movie ID
     * @param userId The user ID
     * @return The ID of the contribution created
     * @throws ResourceNotFoundException if no movie found or no user found
     * @throws ResourceConflictException if there is an IDs conflict or an element exists
     */
    Long createReviewContribution(
            @NotNull @Valid final ContributionNew<Review> contribution,
            @Min(1) final Long movieId,
            @NotBlank final String userId
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Update the contribution of the reviews for the movie.
     *
     * @param contribution The contribution of reviews
     * @param contributionId The contribution ID
     * @param userId The user ID
     * @throws ResourceNotFoundException if no contribution found or no user found
     * @throws ResourceConflictException if the element exists
     */
    void updateReviewContribution(
            @NotNull @Valid final ContributionUpdate<Review> contribution,
            @Min(1) final Long contributionId,
            @NotBlank final String userId
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Save the contribution of the photos for the movie.
     *
     * @param contribution The contribution of photos
     * @param movieId The movie ID
     * @param userId The user ID
     * @return The ID of the contribution created
     * @throws ResourceNotFoundException if no movie found or no user found
     * @throws ResourceConflictException if there is an IDs conflict or an element exists
     * @throws ResourcePreconditionException if an I/O error occurs or incorrect content type
     * @throws ResourceServerException if an error occurred with the server
     */
    Long createPhotoContribution(
            @NotNull @Valid final ContributionNew<ImageRequest> contribution,
            @Min(1) final Long movieId,
            @NotBlank final String userId
    ) throws ResourceNotFoundException, ResourceConflictException, ResourcePreconditionException, ResourceServerException;

    /**
     * Update the contribution of the photos for the movie.
     *
     * @param contribution The contribution of photos
     * @param contributionId The contribution ID
     * @param userId The user ID
     * @throws ResourceNotFoundException if no contribution found or no user found
     * @throws ResourceConflictException if the element exists
     * @throws ResourcePreconditionException if an I/O error occurs or incorrect content type
     * @throws ResourceServerException if an error occurred with the server
     */
    void updatePhotoContribution(
            @NotNull @Valid final ContributionUpdate<ImageRequest> contribution,
            @Min(1) final Long contributionId,
            @NotBlank final String userId
    ) throws ResourceNotFoundException, ResourceConflictException, ResourcePreconditionException, ResourceServerException;

    /**
     * Save the contribution of the posters for the movie.
     *
     * @param contribution The contribution of posters
     * @param movieId The movie ID
     * @param userId The user ID
     * @return The ID of the contribution created
     * @throws ResourceNotFoundException if no movie found or no user found
     * @throws ResourceConflictException if there is an IDs conflict or an element exists
     * @throws ResourcePreconditionException if an I/O error occurs or incorrect content type
     * @throws ResourceServerException if an error occurred with the server
     */
    Long createPosterContribution(
            @NotNull @Valid final ContributionNew<ImageRequest> contribution,
            @Min(1) final Long movieId,
            @NotBlank final String userId
    ) throws ResourceNotFoundException, ResourceConflictException, ResourcePreconditionException, ResourceServerException;

    /**
     * Update the contribution of the posters for the movie.
     *
     * @param contribution The contribution of posters
     * @param contributionId The contribution ID
     * @param userId The user ID
     * @throws ResourceNotFoundException if no contribution found or no user found
     * @throws ResourceConflictException if the element exists
     * @throws ResourcePreconditionException if an I/O error occurs or incorrect content type
     * @throws ResourceServerException if an error occurred with the server
     */
    void updatePosterContribution(
            @NotNull @Valid final ContributionUpdate<ImageRequest> contribution,
            @Min(1) final Long contributionId,
            @NotBlank final String userId
    ) throws ResourceNotFoundException, ResourceConflictException, ResourcePreconditionException, ResourceServerException;
}

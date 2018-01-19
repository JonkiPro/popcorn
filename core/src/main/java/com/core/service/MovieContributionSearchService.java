package com.core.service;

import com.common.dto.Contribution;
import com.common.dto.DataStatus;
import com.common.dto.MovieField;
import com.common.dto.movie.*;
import com.common.dto.movie.response.ImageResponse;
import com.common.dto.search.ContributionSearchResult;
import com.common.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Nullable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Interface for searching contributions.
 */
@Validated
public interface MovieContributionSearchService {

    /**
     * Search for contributions which match the given filter criteria. Null or empty parameters are ignored.
     *
     * @param id  The movie ID
     * @param field  The movie's field
     * @param status  The contribution's status
     * @param fromDate  Min date of the contribution
     * @param toDate  Max date of the contribution
     * @param page  The page to get
     * @return All the contributions matching the criteria
     * @throws ResourceNotFoundException if no movie found
     */
    Page<ContributionSearchResult> findContributions(
            @Nullable @Min(1) final Long id,
            @Nullable final MovieField field,
            @Nullable final DataStatus status,
            @Nullable final Date fromDate,
            @Nullable final Date toDate,
            @NotNull final Pageable page
    ) throws ResourceNotFoundException;

    /**
     * Get a contribution of titles.
     *
     * @param contributionId The contribution ID
     * @return The contribution of titles
     * @throws ResourceNotFoundException if no contribution found
     */
    Contribution<OtherTitle> getOtherTitleContribution(
            @Min(1) final Long contributionId
    ) throws ResourceNotFoundException;

    /**
     * Get a contribution of release dates.
     *
     * @param contributionId The contribution ID
     * @return The contribution of release dates
     * @throws ResourceNotFoundException if no contribution found
     */
    Contribution<ReleaseDate> getReleaseDateContribution(
            @Min(1) final Long contributionId
    ) throws ResourceNotFoundException;

    /**
     * Get a contribution of storylines.
     *
     * @param contributionId The contribution ID
     * @return The contribution of storylines
     * @throws ResourceNotFoundException if no contribution found
     */
    Contribution<Storyline> getStorylineContribution(
            @Min(1) final Long contributionId
    ) throws ResourceNotFoundException;

    /**
     * Get a contribution of box offices.
     *
     * @param contributionId The contribution ID
     * @return The contribution of box offices
     * @throws ResourceNotFoundException if no contribution found
     */
    Contribution<BoxOffice> getBoxOfficeContribution(
            @Min(1) final Long contributionId
    ) throws ResourceNotFoundException;

    /**
     * Get a contribution of sites.
     *
     * @param contributionId The contribution ID
     * @return The contribution of sites
     * @throws ResourceNotFoundException if no contribution found
     */
    Contribution<Site> getSiteContribution(
            @Min(1) final Long contributionId
    ) throws ResourceNotFoundException;

    /**
     * Get a contribution of countries.
     *
     * @param contributionId The contribution ID
     * @return The contribution of countries
     * @throws ResourceNotFoundException if no contribution found
     */
    Contribution<Country> getCountryContribution(
            @Min(1) final Long contributionId
    ) throws ResourceNotFoundException;

    /**
     * Get a contribution of languages.
     *
     * @param contributionId The contribution ID
     * @return The contribution of languages
     * @throws ResourceNotFoundException if no contribution found
     */
    Contribution<Language> getLanguageContribution(
            @Min(1) final Long contributionId
    ) throws ResourceNotFoundException;

    /**
     * Get a contribution of genres.
     *
     * @param contributionId The contribution ID
     * @return The contribution of genres
     * @throws ResourceNotFoundException if no contribution found
     */
    Contribution<Genre> getGenreContribution(
            @Min(1) final Long contributionId
    ) throws ResourceNotFoundException;

    /**
     * Get a contribution of reviews.
     *
     * @param contributionId The contribution ID
     * @return The contribution of reviews
     * @throws ResourceNotFoundException if no contribution found
     */
    Contribution<Review> getReviewContribution(
            @Min(1) final Long contributionId
    ) throws ResourceNotFoundException;

    /**
     * Get a contribution of photos.
     *
     * @param contributionId The contribution ID
     * @return The contribution of photos
     * @throws ResourceNotFoundException if no contribution found
     */
    Contribution<ImageResponse> getPhotoContribution(
            @Min(1) final Long contributionId
    ) throws ResourceNotFoundException;

    /**
     * Get a contribution of posters.
     *
     * @param contributionId The contribution ID
     * @return The contribution of posters
     * @throws ResourceNotFoundException if no contribution found
     */
    Contribution<ImageResponse> getPosterContribution(
            @Min(1) final Long contributionId
    ) throws ResourceNotFoundException;
}

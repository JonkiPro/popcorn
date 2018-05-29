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
import com.jonki.popcorn.common.dto.movie.request.RateRequest;
import com.jonki.popcorn.common.dto.request.MovieRequest;
import com.jonki.popcorn.common.exception.ResourceConflictException;
import com.jonki.popcorn.common.exception.ResourceForbiddenException;
import com.jonki.popcorn.common.exception.ResourceNotFoundException;
import com.jonki.popcorn.core.jpa.entity.MovieEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Interfaces for providing persistence functions for movies other than search.
 */
@PreAuthorize("hasRole('ROLE_USER')")
@Validated
public interface MoviePersistenceService {

    /**
     * Create movie with DTO data.
     *
     * @param movieRequest DTO with movie data
     * @throws ResourceNotFoundException if no user found
     */
    void createMovie(
            @NotNull @Valid final MovieRequest movieRequest
    ) throws ResourceNotFoundException;

    /**
     * Update the movie status.
     *
     * @param id The movie ID
     * @param status Status for the movie
     * @throws ResourceForbiddenException if no permissions
     * @throws ResourceNotFoundException if no movie found or no user found
     */
    void updateMovieStatus(
            @Min(1) final Long id,
            @NotNull final VerificationStatus status
    ) throws ResourceForbiddenException, ResourceNotFoundException;

    /**
     * Save the other title for the movie.
     *
     * @param otherTitle The OtherTitle object to create and save
     * @param movie The object of the movie to which the other title will be added and check the uniqueness
     * @return The ID of the created element
     * @throws ResourceConflictException if the element exists
     */
    Long createOtherTitle(
            @NotNull @Valid final OtherTitle otherTitle,
            @NotNull final MovieEntity movie
    ) throws ResourceConflictException;

    /**
     * Update the title of the movie.
     *
     * @param otherTitle  The OtherTitle object to update
     * @param otherTitleId The ID of the other title
     * @param movie A movie object to check the existence of the same title
     * @throws ResourceConflictException if the element exists
     */
    void updateOtherTitle(
            @NotNull @Valid final OtherTitle otherTitle,
            @Min(1) final Long otherTitleId,
            @NotNull MovieEntity movie
    ) throws ResourceConflictException;

    /**
     * Save the release date for the movie.
     *
     * @param releaseDate The ReleaseDate object to create and save
     * @param movie The object of the movie to which the release date will be added and check the uniqueness
     * @return The ID of the created element
     * @throws ResourceConflictException if the element exists
     */
    Long createReleaseDate(
            @NotNull @Valid final ReleaseDate releaseDate,
            @NotNull final MovieEntity movie
    ) throws ResourceConflictException;

    /**
     * Update the release date of the movie.
     *
     * @param releaseDate The ReleaseDate object to update
     * @param releaseDateId The ID of the release date
     * @param movie A movie object to check the existence of the same release date
     * @throws ResourceConflictException if the element exists
     */
    void updateReleaseDate(
            @NotNull @Valid final ReleaseDate releaseDate,
            @Min(1) final Long releaseDateId,
            @NotNull MovieEntity movie
    ) throws ResourceConflictException;

    /**
     * Save the outline for the movie.
     *
     * @param outline The Outline object to create and save
     * @param movie The object of the movie to which the outline will be added and check the uniqueness
     * @return The ID of the created element
     * @throws ResourceConflictException if the element exists
     */
    Long createOutline(
            @NotNull @Valid final Outline outline,
            @NotNull final MovieEntity movie
    ) throws ResourceConflictException;

    /**
     * Update the outline of the movie.
     *
     * @param outline The Outline object to update
     * @param outlineId The ID of the outline
     * @param movie A movie object to check the existence of the same outline
     * @throws ResourceConflictException if the element exists
     */
    void updateOutline(
            @NotNull @Valid final Outline outline,
            @Min(1) final Long outlineId,
            @NotNull MovieEntity movie
    ) throws ResourceConflictException;

    /**
     * Save the summary for the movie.
     *
     * @param summary The Summary object to create and save
     * @param movie The object of the movie to which the summary will be added and check the uniqueness
     * @return The ID of the created element
     * @throws ResourceConflictException if the element exists
     */
    Long createSummary(
            @NotNull @Valid final Summary summary,
            @NotNull final MovieEntity movie
    ) throws ResourceConflictException;

    /**
     * Update the summary of the movie.
     *
     * @param summary The Summary object to update
     * @param summaryId The ID of the summary
     * @param movie A movie object to check the existence of the same summary
     * @throws ResourceConflictException if the element exists
     */
    void updateSummary(
            @NotNull @Valid final Summary summary,
            @Min(1) final Long summaryId,
            @NotNull MovieEntity movie
    ) throws ResourceConflictException;

    /**
     * Save the synopsis for the movie.
     *
     * @param synopsis The Synopsis object to create and save
     * @param movie The object of the movie to which the synopsis will be added and check the uniqueness
     * @return The ID of the created element
     * @throws ResourceConflictException if the element exists
     */
    Long createSynopsis(
            @NotNull @Valid final Synopsis synopsis,
            @NotNull final MovieEntity movie
    ) throws ResourceConflictException;

    /**
     * Update the synopsis of the movie.
     *
     * @param synopsis The Synopsis object to update
     * @param synopsisId The ID of the synopsis
     * @param movie A movie object to check the existence of the same synopsis
     * @throws ResourceConflictException if the element exists
     */
    void updateSynopsis(
            @NotNull @Valid final Synopsis synopsis,
            @Min(1) final Long synopsisId,
            @NotNull MovieEntity movie
    ) throws ResourceConflictException;

    /**
     * Save the box office for the movie.
     *
     * @param boxOffice The BoxOffice object to create and save
     * @param movie The object of the movie to which the box office will be added and check the uniqueness
     * @return The ID of the created element
     * @throws ResourceConflictException if the element exists
     */
    Long createBoxOffice(
            @NotNull @Valid final BoxOffice boxOffice,
            @NotNull final MovieEntity movie
    ) throws ResourceConflictException;

    /**
     * Update the box office of the movie.
     *
     * @param boxOffice The BoxOffice object to update
     * @param boxOfficeId The ID of the box office
     * @param movie A movie object to check the existence of the same box office
     * @throws ResourceConflictException if the element exists
     */
    void updateBoxOffice(
            @NotNull @Valid final BoxOffice boxOffice,
            @Min(1) final Long boxOfficeId,
            @NotNull MovieEntity movie
    ) throws ResourceConflictException;

    /**
     * Save the site for the movie.
     *
     * @param site The Site object to create and save
     * @param movie The object of the movie to which the site will be added and check the uniqueness
     * @return The ID of the created element
     * @throws ResourceConflictException if the element exists
     */
    Long createSite(
            @NotNull @Valid final Site site,
            @NotNull final MovieEntity movie
    ) throws ResourceConflictException;

    /**
     * Update the site of the movie.
     *
     * @param site The Site object to update
     * @param siteId The ID of the site
     * @param movie A movie object to check the existence of the same site
     * @throws ResourceConflictException if the element exists
     */
    void updateSite(
            @NotNull @Valid final Site site,
            @Min(1) final Long siteId,
            @NotNull MovieEntity movie
    ) throws ResourceConflictException;

    /**
     * Save the country for the movie.
     *
     * @param country The Country object to create and save
     * @param movie The object of the movie to which the country will be added and check the uniqueness
     * @return The ID of the created element
     * @throws ResourceConflictException if the element exists
     */
    Long createCountry(
            @NotNull @Valid final Country country,
            @NotNull final MovieEntity movie
    ) throws ResourceConflictException;

    /**
     * Update the country of the movie.
     *
     * @param country The Country object to update
     * @param countryId The ID of the country
     * @param movie A movie object to check the existence of the same country
     * @throws ResourceConflictException if the element exists
     */
    void updateCountry(
            @NotNull @Valid final Country country,
            @Min(1) final Long countryId,
            @NotNull MovieEntity movie
    ) throws ResourceConflictException;

    /**
     * Save the language for the movie.
     *
     * @param language The Language object to create and save
     * @param movie The object of the movie to which the language will be added and check the uniqueness
     * @return The ID of the created element
     * @throws ResourceConflictException if the element exists
     */
    Long createLanguage(
            @NotNull @Valid final Language language,
            @NotNull final MovieEntity movie
    ) throws ResourceConflictException;

    /**
     * Update the language of the movie.
     *
     * @param language The Language object to update
     * @param languageId The ID of the language
     * @param movie A movie object to check the existence of the same language
     * @throws ResourceConflictException if the element exists
     */
    void updateLanguage(
            @NotNull @Valid final Language language,
            @Min(1) final Long languageId,
            @NotNull MovieEntity movie
    ) throws ResourceConflictException;

    /**
     * Save the genre for the movie.
     *
     * @param genre The Genre object to create and save
     * @param movie The object of the movie to which the genre will be added and check the uniqueness
     * @return The ID of the created element
     * @throws ResourceConflictException if the element exists
     */
    Long createGenre(
            @NotNull @Valid final Genre genre,
            @NotNull final MovieEntity movie
    ) throws ResourceConflictException;

    /**
     * Update the genre of the movie.
     *
     * @param genre The Genre object to update
     * @param genreId The ID of the genre
     * @param movie A movie object to check the existence of the same genre
     * @throws ResourceConflictException if the element exists
     */
    void updateGenre(
            @NotNull @Valid final Genre genre,
            @Min(1) final Long genreId,
            @NotNull MovieEntity movie
    ) throws ResourceConflictException;

    /**
     * Save the review for the movie.
     *
     * @param review The Review object to create and save
     * @param movie The object of the movie to which the review will be added and check the uniqueness
     * @return The ID of the created element
     * @throws ResourceConflictException if the element exists
     */
    Long createReview(
            @NotNull @Valid final Review review,
            @NotNull final MovieEntity movie
    ) throws ResourceConflictException;

    /**
     * Update the review of the movie.
     *
     * @param review The Review object to update
     * @param reviewId The ID of the review
     * @param movie A movie object to check the existence of the same review
     * @throws ResourceConflictException if the element exists
     */
    void updateReview(
            @NotNull @Valid final Review review,
            @Min(1) final Long reviewId,
            @NotNull MovieEntity movie
    ) throws ResourceConflictException;

    /**
     * Save the photo for the movie.
     *
     * @param photo The Photo object to create and save
     * @param movie The object of the movie to which the photo will be added and check the uniqueness
     * @return The ID of the created element
     */
    Long createPhoto(
            @NotNull @Valid final ImageRequest photo,
            @NotNull final MovieEntity movie
    );

    /**
     * Update the photo of the movie.
     *
     * @param photo The Photo object to update
     * @param photoId The ID of the photo
     * @param movie A movie object to check the existence of the same photo
     */
    void updatePhoto(
            @NotNull @Valid final ImageRequest photo,
            @Min(1) final Long photoId,
            @NotNull MovieEntity movie
    );

    /**
     * Save the poster for the movie.
     *
     * @param photo The Poster object to create and save
     * @param movie The object of the movie to which the poster will be added and check the uniqueness
     * @return The ID of the created element
     */
    Long createPoster(
            @NotNull @Valid final ImageRequest photo,
            @NotNull final MovieEntity movie
    );

    /**
     * Update the poster of the movie.
     *
     * @param photo The Poster object to update
     * @param photoId The ID of the poster
     * @param movie A movie object to check the existence of the same poster
     */
    void updatePoster(
            @NotNull @Valid final ImageRequest photo,
            @Min(1) final Long photoId,
            @NotNull MovieEntity movie
    );

    /**
     * Save the rating for the movie.
     *
     * @param id The movie ID
     * @param rateRequest Rating for the movie
     * @throws ResourceNotFoundException if no movie found or no user found
     * @throws ResourceConflictException if today's date is earlier than the release date of the movie
     */
    void saveRating(
            @Min(1) final Long id,
            @NotNull @Valid RateRequest rateRequest
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Add a movie to your favourites list.
     *
     * @param id The movie ID
     * @throws ResourceNotFoundException if no movie found or no user found
     * @throws ResourceConflictException if the movie is added to favorites
     */
    void setFavoriteQuestion(
            @Min(1) final Long id
    ) throws ResourceNotFoundException, ResourceConflictException;

    /**
     * Remove a movie from your favourites list.
     *
     * @param id The movie ID
     * @throws ResourceNotFoundException if no movie found or no user found
     * @throws ResourceConflictException if the movie is not added to favorites
     */
    void undoFavoriteQuestion(
            @Min(1) final Long id
    ) throws ResourceNotFoundException, ResourceConflictException;
}

package com.core.service;

import com.common.dto.VerificationStatus;
import com.common.dto.movie.*;
import com.common.dto.movie.request.Rate;
import com.common.dto.request.MovieDTO;
import com.common.exception.ResourceConflictException;
import com.common.exception.ResourceForbiddenException;
import com.common.exception.ResourceNotFoundException;
import com.core.jpa.entity.MovieEntity;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Interfaces for providing persistence functions for movies other than search.
 */
@Validated
public interface MoviePersistenceService {

    /**
     * Create movie with DTO data.
     *
     * @param movieDTO DTO with movie data
     * @param userId The user ID
     * @throws ResourceNotFoundException if no user found
     */
    void createMovie(
            @NotNull @Valid final MovieDTO movieDTO,
            @NotBlank final String userId
    ) throws ResourceNotFoundException;

    /**
     * Update the movie status.
     *
     * @param movieId The movie ID
     * @param userId The user ID
     * @param status Status for the movie
     * @throws ResourceForbiddenException if no permissions
     * @throws ResourceNotFoundException if no movie found or no user found
     */
    void updateMovieStatus(
            @Min(1) final Long movieId,
            @NotBlank final String userId,
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
     * Save the storyline for the movie.
     *
     * @param storyline The Storyline object to create and save
     * @param movie The object of the movie to which the storyline will be added and check the uniqueness
     * @return The ID of the created element
     * @throws ResourceConflictException if the element exists
     */
    Long createStoryline(
            @NotNull @Valid final Storyline storyline,
            @NotNull final MovieEntity movie
    ) throws ResourceConflictException;

    /**
     * Update the storyline of the movie.
     *
     * @param storyline The Storyline object to update
     * @param storylineId The ID of the storyline
     * @param movie A movie object to check the existence of the same storyline
     * @throws ResourceConflictException if the element exists
     */
    void updateStoryline(
            @NotNull @Valid final Storyline storyline,
            @Min(1) final Long storylineId,
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
     * Save the rating for the movie.
     *
     * @param rate Rating for the movie
     * @param movieId The movie ID
     * @param userId The user ID
     * @throws ResourceNotFoundException if no movie found or no user found
     * @throws ResourceConflictException if today's date is earlier than the release date of the movie
     */
    void saveRating(
            @NotNull @Valid Rate rate,
            @Min(1) final Long movieId,
            @NotBlank final String userId
    ) throws ResourceNotFoundException, ResourceConflictException;
}

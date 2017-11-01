package com.core.jpa.service;

import com.common.dto.movie.CountryType;
import com.common.dto.movie.GenreType;
import com.common.dto.movie.LanguageType;
import com.common.dto.request.MovieDTO;
import com.common.dto.request.movie.*;
import com.common.exception.ResourceConflictException;
import com.common.exception.ResourceForbiddenException;
import com.common.exception.ResourceNotFoundException;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Set;

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
            @Min(1) final Long userId
    ) throws ResourceNotFoundException;

    /**
     * Accept the movie.
     *
     * @param movieId The movie ID
     * @param userId The user ID
     * @throws ResourceForbiddenException if no permissions
     * @throws ResourceNotFoundException if no movie found or no user found
     */
    void acceptMovie(
            @Min(1) final Long movieId,
            @Min(1) final Long userId
    ) throws ResourceForbiddenException, ResourceNotFoundException;

    /**
     * Accept the contribution with movie info ids.
     *
     * @param contributionId The contribution ID
     * @param userId The user ID
     * @param comment Comment for verification
     * @throws ResourceForbiddenException if no permissions
     * @throws ResourceNotFoundException if no contribution found or no user found
     */
    void acceptContribution(
            @Min(1) final Long contributionId,
            @Min(1) final Long userId,
            final String comment
    ) throws ResourceForbiddenException, ResourceNotFoundException;

    /**
     * Reject the movie.
     *
     * @param movieId The movie ID
     * @param userId The user ID
     * @throws ResourceForbiddenException if no permissions
     * @throws ResourceNotFoundException if no movie found or no user found
     */
    void rejectMovie(
            @Min(1) final Long movieId,
            @Min(1) final Long userId
    ) throws ResourceForbiddenException, ResourceNotFoundException;

    /**
     * Reject the contribution with movie info ids.
     *
     * @param contributionId The contribution ID
     * @param userId The user ID
     * @param comment Comment for verification
     * @throws ResourceForbiddenException if no permissions
     * @throws ResourceNotFoundException if no contribution found or no user found
     */
    void rejectContribution(
            @Min(1) final Long contributionId,
            @Min(1) final Long userId,
            final String comment
    ) throws ResourceForbiddenException, ResourceNotFoundException;

    /**
     * Save movie description.
     *
     * @param description The movie description
     * @param movieId The movie ID
     * @param userId The user ID
     * @throws ResourceNotFoundException if no movie found or no user found
     */
    void saveDescription(
            @NotBlank final String description,
            @Min(1) final Long movieId,
            @Min(1) final Long userId
    ) throws ResourceNotFoundException;

    /**
     * Save other movie titles.
     *
     * @param otherTitles List of other titles
     * @param movieId The movie ID
     * @param userId The user ID
     * @throws ResourceNotFoundException if no movie found or no user found
     */
    void saveOtherTitles(
            @NotEmpty final Set<OtherTitle> otherTitles,
            @Min(1) final Long movieId,
            @Min(1) final Long userId
    ) throws ResourceNotFoundException;

    /**
     * Save movie box offices.
     *
     * @param boxOffices List of box offices
     * @param movieId The movie ID
     * @param userId The user ID
     * @throws ResourceNotFoundException if no movie found or no user found
     */
    void saveBoxOffices(
            @NotEmpty final Set<BoxOffice> boxOffices,
            @Min(1) final Long movieId,
            @Min(1) final Long userId
    ) throws ResourceNotFoundException;

    /**
     * Save movie sites.
     *
     * @param sites List of sites
     * @param movieId The movie ID
     * @param userId The user ID
     * @throws ResourceNotFoundException if no movie found or no user found
     */
    void saveSites(
            @NotEmpty final Set<Site> sites,
            @Min(1) final Long movieId,
            @Min(1) final Long userId
    ) throws ResourceNotFoundException;

    /**
     * Save movie release dates.
     *
     * @param releaseDates List of release dates
     * @param movieId The movie ID
     * @param userId The user ID
     * @throws ResourceNotFoundException if no movie found or no user found
     */
    void saveReleaseDates(
            @NotEmpty final Set<ReleaseDate> releaseDates,
            @Min(1) final Long movieId,
            @Min(1) final Long userId
    ) throws ResourceNotFoundException;

    /**
     * Save movie storyline.
     *
     * @param storyline The movie storyline
     * @param movieId The movie ID
     * @param userId The user ID
     * @throws ResourceNotFoundException if no movie found or no user found
     */
    void saveStoryline(
            @NotBlank final String storyline,
            @Min(1) final Long movieId,
            @Min(1) final Long userId
    ) throws ResourceNotFoundException;

    /**
     * Save movie countries.
     *
     * @param countries List of countries
     * @param movieId The movie ID
     * @param userId The user ID
     * @throws ResourceNotFoundException if no movie found or no user found
     */
    void saveCountries(
            @NotEmpty final Set<CountryType> countries,
            @Min(1) final Long movieId,
            @Min(1) final Long userId
    ) throws ResourceNotFoundException;

    /**
     * Save movie languages.
     *
     * @param languages List of languages
     * @param movieId The movie ID
     * @param userId The user ID
     * @throws ResourceNotFoundException if no movie found or no user found
     */
    void saveLanguages(
            @NotEmpty final Set<LanguageType> languages,
            @Min(1) final Long movieId,
            @Min(1) final Long userId
    ) throws ResourceNotFoundException;

    /**
     * Save movie genres.
     *
     * @param genres List of genres
     * @param movieId The movie ID
     * @param userId The user ID
     * @throws ResourceNotFoundException if no movie found or no user found
     */
    void saveGenres(
            @NotEmpty final Set<GenreType> genres,
            @Min(1) final Long movieId,
            @Min(1) final Long userId
    ) throws ResourceNotFoundException;

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
            @Min(1) final Long userId
    ) throws ResourceNotFoundException, ResourceConflictException;
}

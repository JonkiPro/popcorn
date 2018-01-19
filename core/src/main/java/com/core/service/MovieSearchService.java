package com.core.service;

import com.common.dto.Movie;
import com.common.dto.movie.*;
import com.common.dto.movie.response.ImageResponse;
import com.common.dto.movie.response.RateResponse;
import com.common.dto.movie.type.CountryType;
import com.common.dto.movie.type.GenreType;
import com.common.dto.movie.type.LanguageType;
import com.common.dto.movie.type.MovieType;
import com.common.dto.search.MovieSearchResult;
import com.common.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Nullable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Interface for searching movies.
 */
@Validated
public interface MovieSearchService {

    /**
     * Search for movies which match the given filter criteria. Null or empty parameters are ignored.
     *
     * @param title  The movie's title
     * @param type  The movie's type
     * @param fromDate  Min time when the movie was premiered
     * @param toDate  Max time when the movie was premiered
     * @param countries  List of countries
     * @param languages  List of languages
     * @param genres  List of genres
     * @param minRating  Minimal rating
     * @param maxRating  Maximum rating
     * @param page  The page to get
     * @return All the movies matching the criteria
     */
    Page<MovieSearchResult> findMovies(
            @Nullable final String title,
            @Nullable final MovieType type,
            @Nullable final Date fromDate,
            @Nullable final Date toDate,
            @Nullable final List<CountryType> countries,
            @Nullable final List<LanguageType> languages,
            @Nullable final List<GenreType> genres,
            @Nullable final Integer minRating,
            @Nullable final Integer maxRating,
            @NotNull final Pageable page
    );

    /**
     * Get movie by ID.
     *
     * @param id The movie ID
     * @return The movie
     * @throws ResourceNotFoundException if no movie found
     */
    Movie getMovie(
            @Min(1) final Long id
    ) throws ResourceNotFoundException;

    /**
     * Get user ratings for the movie by ID.
     *
     * @param id The movie ID
     * @return User ratings
     * @throws ResourceNotFoundException if no movie found
     */
    Set<RateResponse> getRatings(
            @Min(1) final Long id
    ) throws ResourceNotFoundException;

    /**
     * Get titles for the movie by ID.
     *
     * @param id The movie ID
     * @return Movie titles
     * @throws ResourceNotFoundException if no movie found
     */
    Set<OtherTitle> getTitles(
            @Min(1) final Long id
    ) throws ResourceNotFoundException;

    /**
     * Get release dates for the movie by ID.
     *
     * @param id The movie ID
     * @return Movie release dates
     * @throws ResourceNotFoundException if no movie found
     */
    Set<ReleaseDate> getReleaseDates(
            @Min(1) final Long id
    ) throws ResourceNotFoundException;

    /**
     * Get storylines for the movie by ID.
     *
     * @param id The movie ID
     * @return Movie storylines
     * @throws ResourceNotFoundException if no movie found
     */
    Set<Storyline> getStorylines(
            @Min(1) final Long id
    ) throws ResourceNotFoundException;

    /**
     * Get box offices for the movie by ID.
     *
     * @param id The movie ID
     * @return Movie titles
     * @throws ResourceNotFoundException if no movie found
     */
    Set<BoxOffice> getBoxOffices(
            @Min(1) final Long id
    ) throws ResourceNotFoundException;

    /**
     * Get sites for the movie by ID.
     *
     * @param id The movie ID
     * @return Movie sites
     * @throws ResourceNotFoundException if no movie found
     */
    Set<Site> getSites(
            @Min(1) final Long id
    ) throws ResourceNotFoundException;

    /**
     * Get countries for the movie by ID.
     *
     * @param id The movie ID
     * @return Movie countries
     * @throws ResourceNotFoundException if no movie found
     */
    Set<Country> getCountries(
            @Min(1) final Long id
    ) throws ResourceNotFoundException;

    /**
     * Get languages for the movie by ID.
     *
     * @param id The movie ID
     * @return Movie languages
     * @throws ResourceNotFoundException if no movie found
     */
    Set<Language> getLanguages(
            @Min(1) final Long id
    ) throws ResourceNotFoundException;

    /**
     * Get genres for the movie by ID.
     *
     * @param id The movie ID
     * @return Movie genres
     * @throws ResourceNotFoundException if no movie found
     */
    Set<Genre> getGenres(
            @Min(1) final Long id
    ) throws ResourceNotFoundException;

    /**
     * Get reviews for the movie by ID.
     *
     * @param id The movie ID
     * @return Movie reviews
     * @throws ResourceNotFoundException if no movie found
     */
    Set<Review> getReviews(
            @Min(1) final Long id
    ) throws ResourceNotFoundException;

    /**
     * Get photos for the movie by ID.
     *
     * @param id The movie ID
     * @return Movie photos
     * @throws ResourceNotFoundException if no movie found
     */
    Set<ImageResponse> getPhotos(
            @Min(1) final Long id
    ) throws ResourceNotFoundException;

    /**
     * Get posters for the movie by ID.
     *
     * @param id The movie ID
     * @return Movie posters
     * @throws ResourceNotFoundException if no movie found
     */
    Set<ImageResponse> getPosters(
            @Min(1) final Long id
    ) throws ResourceNotFoundException;
}

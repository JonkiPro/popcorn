package com.core.jpa.service;

import com.common.dto.Movie;
import com.common.dto.User;
import com.common.dto.movie.CountryType;
import com.common.dto.movie.GenreType;
import com.common.dto.movie.LanguageType;
import com.common.dto.movie.MovieType;
import com.common.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * Interface for searching movies.
 */
@Validated
public interface MovieSearchService {

    /**
     * Get movie info for various parameters. Null or empty parameters are ignored.
     *
     * @param title  The movie's title
     * @param type  The movie's type
     * @param fromDate  Min time when the movie was premiered
     * @param toDate  Max time when the movie was premiered
     * @param countries  List of countries
     * @param languages  List of languages
     * @param genres  List of genres
     * @param page  The page to get
     * @return All the movies matching the criteria
     */
    Page<Movie> getAllMovies(
            final String title,
            final MovieType type,
            final Date fromDate,
            final Date toDate,
            final List<CountryType> countries,
            final List<LanguageType> languages,
            final List<GenreType> genres,
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
}

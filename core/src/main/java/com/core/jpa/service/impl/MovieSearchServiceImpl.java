package com.core.jpa.service.impl;

import com.common.dto.Movie;
import com.common.dto.movie.CountryType;
import com.common.dto.movie.GenreType;
import com.common.dto.movie.LanguageType;
import com.common.dto.movie.MovieType;
import com.common.exception.ResourceNotFoundException;
import com.core.jpa.entity.MovieEntity;
import com.core.jpa.repository.MovieRepository;
import com.core.jpa.service.MovieSearchService;
import com.core.jpa.specifications.MovieSpecs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * JPA implementation of the Movie Search Service.
 */
@Service("movieSearchService")
@Slf4j
@Transactional(readOnly = true)
@Validated
public class MovieSearchServiceImpl implements MovieSearchService {

    private final MovieRepository movieRepository;

    /**
     * Constructor.
     *
     * @param movieRepository The movie repository to use
     */
    @Autowired
    public MovieSearchServiceImpl(
            @NotNull final MovieRepository movieRepository
    ) {
        this.movieRepository = movieRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Movie> getAllMovies(
            final String title,
            final MovieType type,
            final Date fromDate,
            final Date toDate,
            final List<CountryType> countries,
            final List<LanguageType> languages,
            final List<GenreType> genres,
            @NotNull final Pageable page
    ) {
        log.info("Called with title {}, type {}, fromYear {}, toYear {}, countries {}," +
                " languages {}, genres {}, page {}", title, type, fromDate, toDate, countries,
                languages, genres, page);

        @SuppressWarnings("unchecked")
        final Page<MovieEntity> movieEntities = this.movieRepository.findAll(
                MovieSpecs.getFindPredicate(
                        title,
                        type,
                        fromDate,
                        toDate,
                        countries,
                        languages,
                        genres),
                page);

        return movieEntities.map(MovieEntity::getDTO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Movie getMovie(
            @Min(1) Long id
    ) throws ResourceNotFoundException {
        log.info("Called with id {}", id);

        return this.movieRepository.findById(id)
                .map(MovieEntity::getDTO)
                .orElseThrow(() -> new ResourceNotFoundException("No movie found with id " + id));
    }
}

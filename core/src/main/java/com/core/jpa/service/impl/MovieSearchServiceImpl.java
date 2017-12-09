package com.core.jpa.service.impl;

import com.common.dto.Movie;
import com.common.dto.movie.*;
import com.common.dto.movie.response.RateResponse;
import com.common.dto.movie.type.CountryType;
import com.common.dto.movie.type.GenreType;
import com.common.dto.movie.type.LanguageType;
import com.common.dto.movie.type.MovieType;
import com.common.dto.search.MovieSearchResult;
import com.common.exception.ResourceNotFoundException;
import com.core.jpa.entity.MovieEntity;
import com.core.jpa.entity.movie.*;
import com.core.jpa.repository.MovieRepository;
import com.core.jpa.service.MovieSearchService;
import com.core.jpa.specifications.MovieSpecs;
import com.core.movie.DataStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Nullable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public Page<MovieSearchResult> getAllMovies(
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
    ) {
        log.info("Called with title {}, type {}, fromYear {}, toYear {}, countries {}," +
                " languages {}, genres {}, minRating {}, maxRating {}, page {}", title, type, fromDate, toDate,
                countries, languages, genres, minRating, maxRating, page);

        @SuppressWarnings("unchecked")
        final Page<MovieEntity> movieEntities = this.movieRepository.findAll(
                MovieSpecs.getFindPredicate(
                        title,
                        type,
                        fromDate,
                        toDate,
                        countries,
                        languages,
                        genres,
                        minRating,
                        maxRating),
                page);

        return movieEntities.map(MovieEntity::getSearchResultDTO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Movie getMovie(
            @Min(1) final Long id
    ) throws ResourceNotFoundException {
        log.info("Called with id {}", id);

        return this.findMovie(id).getDTO();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<RateResponse> getRatings(
            @Min(1) final Long id
    ) throws ResourceNotFoundException {
        log.info("Called with id {}", id);

        final MovieEntity movie = this.findMovie(id);

        return movie.getRatings()
                .stream()
                .map(MovieRateEntity::getDTO)
                .collect(Collectors.toSet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<OtherTitle> getTitles(
            @Min(1) final Long id
    ) throws ResourceNotFoundException {
        log.info("Called with id {}", id);

        final MovieEntity movie = this.findMovie(id);

        return movie.getOtherTitles()
                .stream()
                .filter(title -> title.getStatus() == DataStatus.ACCEPTED)
                .map(MovieOtherTitleEntity::getDTO)
                .collect(Collectors.toSet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Storyline> getStorylines(
            @Min(1) final Long id
    ) throws ResourceNotFoundException {
        log.info("Called with id {}", id);

        final MovieEntity movie = this.findMovie(id);

        return movie.getStorylines()
                .stream()
                .filter(storyline -> storyline.getStatus() == DataStatus.ACCEPTED)
                .map(MovieStorylineEntity::getDTO)
                .collect(Collectors.toSet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<BoxOffice> getBoxOffices(
            @Min(1) final Long id
    ) throws ResourceNotFoundException {
        log.info("Called with id {}", id);

        final MovieEntity movie = this.findMovie(id);

        return movie.getBoxOffices()
                .stream()
                .filter(boxOffice -> boxOffice.getStatus() == DataStatus.ACCEPTED)
                .map(MovieBoxOfficeEntity::getDTO)
                .collect(Collectors.toSet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<ReleaseDate> getReleaseDates(
            @Min(1) final Long id
    ) throws ResourceNotFoundException {
        log.info("Called with id {}", id);

        final MovieEntity movie = this.findMovie(id);

        return movie.getReleaseDates()
                .stream()
                .filter(releaseDate -> releaseDate.getStatus() == DataStatus.ACCEPTED)
                .map(MovieReleaseDateEntity::getDTO)
                .collect(Collectors.toSet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Site> getSites(
            @Min(1) final Long id
    ) throws ResourceNotFoundException {
        log.info("Called with id {}", id);

        final MovieEntity movie = this.findMovie(id);

        return movie.getSites()
                .stream()
                .filter(site -> site.getStatus() == DataStatus.ACCEPTED)
                .map(MovieSiteEntity::getDTO)
                .collect(Collectors.toSet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Review> getReviews(
            @Min(1) final Long id
    ) throws ResourceNotFoundException {
        log.info("Called with id {}", id);

        final MovieEntity movie = this.findMovie(id);

        return movie.getReviews()
                .stream()
                .filter(review -> review.getStatus() == DataStatus.ACCEPTED)
                .map(MovieReviewEntity::getDTO)
                .collect(Collectors.toSet());
    }

    /**
     * Helper method to find the movie entity.
     *
     * @param id The movie ID
     * @return The movie
     */
    private MovieEntity findMovie(final Long id) {
        return this.movieRepository
                .findByIdAndStatus(id, DataStatus.ACCEPTED)
                .orElseThrow(() -> new ResourceNotFoundException("No movie found with id " + id));
    }
}

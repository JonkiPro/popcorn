package com.core.jpa.service;

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
import com.core.jpa.entity.MovieEntity_;
import com.core.jpa.repository.MovieRepository;
import com.core.jpa.specifications.MovieSpecs;
import com.common.dto.DataStatus;
import com.core.service.MovieSearchService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
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

    @PersistenceContext
    private EntityManager entityManager;

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
    public Page<MovieSearchResult> findMovies(
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

        final CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        final Root<MovieEntity> root = countQuery.from(MovieEntity.class);

        final Predicate whereClause = MovieSpecs
                .getFindPredicate(
                        root,
                        cb,
                        title,
                        type,
                        fromDate,
                        toDate,
                        countries,
                        languages,
                        genres,
                        minRating,
                        maxRating
                );

        countQuery.select(cb.count(root)).where(whereClause);

        final Long count = this.entityManager.createQuery(countQuery).getSingleResult();

        if (count > 0) {
            final CriteriaQuery<MovieSearchResult> contentQuery = cb.createQuery(MovieSearchResult.class);
            contentQuery.from(MovieEntity.class);

            contentQuery.multiselect(
                    root.get(MovieEntity_.id),
                    root.get(MovieEntity_.title),
                    root.get(MovieEntity_.type),
                    root.get(MovieEntity_.rating)
            );

            contentQuery.where(whereClause);

            final Sort sort = page.getSort();
            final List<Order> orders = new ArrayList<>();
            sort.iterator().forEachRemaining(
                    order -> {
                        if (order.isAscending()) {
                            orders.add(cb.asc(root.get(order.getProperty())));
                        } else {
                            orders.add(cb.desc(root.get(order.getProperty())));
                        }
                    }
            );

            contentQuery.orderBy(orders);

            final List<MovieSearchResult> results = this.entityManager
                    .createQuery(contentQuery)
                    .setFirstResult(page.getOffset())
                    .setMaxResults(page.getPageSize())
                    .getResultList();

            return new PageImpl<>(results, page, count);
        } else {
            return new PageImpl<>(Lists.newArrayList(), page, count);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Movie getMovie(
            @Min(1) final Long id
    ) throws ResourceNotFoundException {
        log.info("Called with id {}", id);

        return ServiceUtils.toMovieDto(this.findMovie(id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<RateResponse> getRatings(
            @Min(1) final Long id
    ) throws ResourceNotFoundException {
        log.info("Called with id {}", id);

        return this.findMovie(id).getRatings()
                .stream()
                .map(ServiceUtils::toRateResponseDto)
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

        return this.findMovie(id).getOtherTitles()
                .stream()
                .filter(title -> title.getStatus() == DataStatus.ACCEPTED)
                .map(ServiceUtils::toOtherTitleDto)
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

        return this.findMovie(id).getReleaseDates()
                .stream()
                .filter(releaseDate -> releaseDate.getStatus() == DataStatus.ACCEPTED)
                .map(ServiceUtils::toReleaseDateDto)
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

        return this.findMovie(id).getStorylines()
                .stream()
                .filter(storyline -> storyline.getStatus() == DataStatus.ACCEPTED)
                .map(ServiceUtils::toStorylineDto)
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

        return this.findMovie(id).getBoxOffices()
                .stream()
                .filter(boxOffice -> boxOffice.getStatus() == DataStatus.ACCEPTED)
                .map(ServiceUtils::toBoxOfficeDto)
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

        return this.findMovie(id).getSites()
                .stream()
                .filter(site -> site.getStatus() == DataStatus.ACCEPTED)
                .map(ServiceUtils::toSiteDto)
                .collect(Collectors.toSet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Country> getCountries(
            @Min(1) final Long id
    ) throws ResourceNotFoundException {
        log.info("Called with id {}", id);

        return this.findMovie(id).getCountries()
                .stream()
                .filter(review -> review.getStatus() == DataStatus.ACCEPTED)
                .map(ServiceUtils::toCountryDto)
                .collect(Collectors.toSet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Language> getLanguages(
            @Min(1) final Long id
    ) throws ResourceNotFoundException {
        log.info("Called with id {}", id);

        return this.findMovie(id).getLanguages()
                .stream()
                .filter(review -> review.getStatus() == DataStatus.ACCEPTED)
                .map(ServiceUtils::toLanguageDto)
                .collect(Collectors.toSet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Genre> getGenres(
            @Min(1) final Long id
    ) throws ResourceNotFoundException {
        log.info("Called with id {}", id);

        return this.findMovie(id).getGenres()
                .stream()
                .filter(review -> review.getStatus() == DataStatus.ACCEPTED)
                .map(ServiceUtils::toGenreDto)
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

        return this.findMovie(id).getReviews()
                .stream()
                .filter(review -> review.getStatus() == DataStatus.ACCEPTED)
                .map(ServiceUtils::toReviewDto)
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

package com.jonki.popcorn.core.jpa.service;

import com.google.common.collect.Lists;
import com.jonki.popcorn.common.dto.DataStatus;
import com.jonki.popcorn.common.dto.Movie;
import com.jonki.popcorn.common.dto.UserMovie;
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
import com.jonki.popcorn.common.dto.movie.response.ImageResponse;
import com.jonki.popcorn.common.dto.movie.response.RateResponse;
import com.jonki.popcorn.common.dto.movie.type.CountryType;
import com.jonki.popcorn.common.dto.movie.type.GenreType;
import com.jonki.popcorn.common.dto.movie.type.LanguageType;
import com.jonki.popcorn.common.dto.movie.type.MovieType;
import com.jonki.popcorn.common.dto.search.MovieSearchResult;
import com.jonki.popcorn.common.exception.ResourceNotFoundException;
import com.jonki.popcorn.core.jpa.entity.MovieEntity;
import com.jonki.popcorn.core.jpa.entity.MovieEntity_;
import com.jonki.popcorn.core.jpa.entity.UserEntity;
import com.jonki.popcorn.core.jpa.repository.MovieRepository;
import com.jonki.popcorn.core.jpa.repository.UserRepository;
import com.jonki.popcorn.core.jpa.specification.MovieSpecs;
import com.jonki.popcorn.core.service.AuthorizationService;
import com.jonki.popcorn.core.service.MovieSearchService;
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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    private final UserRepository userRepository;
    private final AuthorizationService authorizationService;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Constructor.
     *
     * @param movieRepository The movie repository to use
     * @param userRepository The user repository to use
     * @param authorizationService The authorization service to use
     */
    @Autowired
    public MovieSearchServiceImpl(
            @NotNull final MovieRepository movieRepository,
            @NotNull final UserRepository userRepository,
            @NotNull final AuthorizationService authorizationService
    ) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.authorizationService = authorizationService;
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
                    .setFirstResult(((Long) page.getOffset()).intValue())
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
    public UserMovie getUserMovie(
            @Min(1) final Long id
    ) throws ResourceNotFoundException {
        log.info("Called with id {}", id);

        return ServiceUtils.toUserMovieDto(this.findMovie(id), this.findUser(this.authorizationService.getUserId()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RateResponse> getRatings(
            @Min(1) final Long id
    ) throws ResourceNotFoundException {
        log.info("Called with id {}", id);

        return this.findMovie(id).getRatings()
                .stream()
                .map(ServiceUtils::toRateResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OtherTitle> getTitles(
            @Min(1) final Long id
    ) throws ResourceNotFoundException {
        log.info("Called with id {}", id);

        return this.findMovie(id).getOtherTitles()
                .stream()
                .filter(title -> title.getStatus() == DataStatus.ACCEPTED)
                .map(ServiceUtils::toOtherTitleDto)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ReleaseDate> getReleaseDates(
            @Min(1) final Long id
    ) throws ResourceNotFoundException {
        log.info("Called with id {}", id);

        return this.findMovie(id).getReleaseDates()
                .stream()
                .filter(releaseDate -> releaseDate.getStatus() == DataStatus.ACCEPTED)
                .map(ServiceUtils::toReleaseDateDto)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Outline> getOutlines(
            @Min(1) final Long id
    ) throws ResourceNotFoundException {
        log.info("Called with id {}", id);

        return this.findMovie(id).getOutlines()
                .stream()
                .filter(outline -> outline.getStatus() == DataStatus.ACCEPTED)
                .map(ServiceUtils::toOutlineDto)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Summary> getSummaries(
            @Min(1) final Long id
    ) throws ResourceNotFoundException {
        log.info("Called with id {}", id);

        return this.findMovie(id).getSummaries()
                .stream()
                .filter(summary -> summary.getStatus() == DataStatus.ACCEPTED)
                .map(ServiceUtils::toSummaryDto)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Synopsis> getSynopses(
            @Min(1) final Long id
    ) throws ResourceNotFoundException {
        log.info("Called with id {}", id);

        return this.findMovie(id).getSynopses()
                .stream()
                .filter(synopsis -> synopsis.getStatus() == DataStatus.ACCEPTED)
                .map(ServiceUtils::toSynopsisDto)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<BoxOffice> getBoxOffices(
            @Min(1) final Long id
    ) throws ResourceNotFoundException {
        log.info("Called with id {}", id);

        return this.findMovie(id).getBoxOffices()
                .stream()
                .filter(boxOffice -> boxOffice.getStatus() == DataStatus.ACCEPTED)
                .map(ServiceUtils::toBoxOfficeDto)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Site> getSites(
            @Min(1) final Long id
    ) throws ResourceNotFoundException {
        log.info("Called with id {}", id);

        return this.findMovie(id).getSites()
                .stream()
                .filter(site -> site.getStatus() == DataStatus.ACCEPTED)
                .map(ServiceUtils::toSiteDto)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Country> getCountries(
            @Min(1) final Long id
    ) throws ResourceNotFoundException {
        log.info("Called with id {}", id);

        return this.findMovie(id).getCountries()
                .stream()
                .filter(review -> review.getStatus() == DataStatus.ACCEPTED)
                .map(ServiceUtils::toCountryDto)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Language> getLanguages(
            @Min(1) final Long id
    ) throws ResourceNotFoundException {
        log.info("Called with id {}", id);

        return this.findMovie(id).getLanguages()
                .stream()
                .filter(review -> review.getStatus() == DataStatus.ACCEPTED)
                .map(ServiceUtils::toLanguageDto)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Genre> getGenres(
            @Min(1) final Long id
    ) throws ResourceNotFoundException {
        log.info("Called with id {}", id);

        return this.findMovie(id).getGenres()
                .stream()
                .filter(review -> review.getStatus() == DataStatus.ACCEPTED)
                .map(ServiceUtils::toGenreDto)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Review> getReviews(
            @Min(1) final Long id
    ) throws ResourceNotFoundException {
        log.info("Called with id {}", id);

        return this.findMovie(id).getReviews()
                .stream()
                .filter(review -> review.getStatus() == DataStatus.ACCEPTED)
                .map(ServiceUtils::toReviewDto)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ImageResponse> getPhotos(
            @Min(1) final Long id
    ) throws ResourceNotFoundException {
        log.info("Called with id {}", id);

        return this.findMovie(id).getPhotos()
                .stream()
                .filter(photo -> photo.getStatus() == DataStatus.ACCEPTED)
                .map(ServiceUtils::toImageResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ImageResponse> getPosters(
            @Min(1) final Long id
    ) throws ResourceNotFoundException {
        log.info("Called with id {}", id);

        return this.findMovie(id).getPosters()
                .stream()
                .filter(poster -> poster.getStatus() == DataStatus.ACCEPTED)
                .map(ServiceUtils::toImageResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Helper method to find the user entity.
     *
     * @param id The user ID
     * @return The user
     * @throws ResourceNotFoundException if no user found
     */
    private UserEntity findUser(final String id) throws ResourceNotFoundException {
        return this.userRepository
                .findByUniqueIdAndEnabledTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with id " + id));
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

package com.core.jpa.service.impl;

import com.common.dto.movie.*;
import com.common.dto.movie.request.*;
import com.common.dto.request.MovieDTO;
import com.common.exception.ResourceConflictException;
import com.common.exception.ResourceForbiddenException;
import com.common.exception.ResourceNotFoundException;
import com.core.jpa.entity.MovieEntity;
import com.core.jpa.entity.UserEntity;
import com.core.jpa.entity.movie.*;
import com.core.jpa.repository.*;
import com.core.jpa.service.MoviePersistenceService;
import com.core.movie.DataStatus;
import com.core.movie.UserMoviePermission;
import com.core.movie.VerificationStatus;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

/**
 * JPA implementation of the Movie Persistence Service.
 */
@Service("moviePersistenceService")
@Slf4j
@Transactional(
        rollbackFor = {
                ResourceNotFoundException.class,
                ResourceConflictException.class,
                ConstraintViolationException.class
        }
)
@Validated
public class MoviePersistenceServiceImpl implements MoviePersistenceService {

    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Constructor.
     *
     * @param movieRepository The movie repository to use
     * @param userRepository The user repository to use
     */
    @Autowired
    public MoviePersistenceServiceImpl(
            @NotNull final MovieRepository movieRepository,
            @NotNull final UserRepository userRepository
    ) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createMovie(
            @NotNull @Valid final MovieDTO movieDTO,
            @Min(1) final Long userId
    ) throws ResourceNotFoundException {
        log.info("Called with {}, userId {}", movieDTO, userId);

        final UserEntity user = this.findUser(userId);

        final MovieEntity movie = new MovieEntity();

        movie.setTitle(movieDTO.getTitle());
        movie.setType(movieDTO.getType());

        this.movieRepository.save(movie);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateMovieStatus(
            @Min(1) final Long movieId,
            @Min(1) final Long userId,
            @NotNull final VerificationStatus status
    ) throws ResourceForbiddenException, ResourceNotFoundException {
        log.info("Called with movieId {}, userId {}, status {}", movieId, userId, status);

        final UserEntity user = this.findUser(userId);
        final MovieEntity movie = this.findMovie(movieId, DataStatus.WAITING);

        if(!user.getPermissions().contains(UserMoviePermission.ALL)
                && !user.getPermissions().contains(UserMoviePermission.NEW_MOVIE)) {
            throw new ResourceForbiddenException("No permissions");
        }

        movie.setStatus(status.getDataStatus());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long createOtherTitle(
            @NotNull @Valid final OtherTitle otherTitle,
            @NotNull final MovieEntity movie,
            @NotNull final UserEntity user
    ) throws ResourceConflictException {
        log.info("Called with otherTitle {}, movie {}, user {}", otherTitle, movie, user);

        this.existsOtherTile(movie.getOtherTitles()
                .stream()
                .filter(ot -> ot.getStatus() == DataStatus.ACCEPTED)
                .collect(Collectors.toList()), otherTitle);

        final MovieOtherTitleEntity movieOtherTitle = new MovieOtherTitleEntity(otherTitle.getTitle(), otherTitle.getCountry());
        movieOtherTitle.setMovie(movie);
        movieOtherTitle.setUser(user);

        movie.getOtherTitles().add(movieOtherTitle);

        this.movieRepository.save(movie);

        return Iterables.getLast(movie.getOtherTitles()).getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateOtherTitle(
            @NotNull @Valid final OtherTitle otherTitle,
            @Min(1) final Long otherTitleId,
            @NotNull final MovieEntity movie
    ) throws ResourceConflictException {
        log.info("Called with otherTitle {}, otherTitleId {}, movie {}", otherTitle, otherTitleId, movie);

        this.existsOtherTile(movie.getOtherTitles()
                .stream()
                .filter(ot -> ot.getStatus() == DataStatus.ACCEPTED)
                .collect(Collectors.toList()), otherTitle);

        final MovieOtherTitleEntity movieOtherTitle = this.entityManager.find(MovieOtherTitleEntity.class, otherTitleId);
        movieOtherTitle.setTitle(otherTitle.getTitle());
        movieOtherTitle.setCountry(otherTitle.getCountry());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long createReleaseDate(
            @NotNull @Valid final ReleaseDate releaseDate,
            @NotNull final MovieEntity movie,
            @NotNull final UserEntity user
    ) throws ResourceConflictException {
        log.info("Called with releaseDate {}, movie {}, user {}", releaseDate, movie, user);

        this.existsReleaseDate(movie.getReleaseDates()
                .stream()
                .filter(rd -> rd.getStatus() == DataStatus.ACCEPTED)
                .collect(Collectors.toList()), releaseDate);

        final MovieReleaseDateEntity movieReleaseDate = new MovieReleaseDateEntity(releaseDate.getDate(), releaseDate.getCountry());
        movieReleaseDate.setMovie(movie);
        movieReleaseDate.setUser(user);

        movie.getReleaseDates().add(movieReleaseDate);

        this.movieRepository.save(movie);

        return Iterables.getLast(movie.getReleaseDates()).getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateReleaseDate(
            @NotNull @Valid final ReleaseDate releaseDate,
            @Min(1) final Long releaseDateId,
            @NotNull final MovieEntity movie
    ) throws ResourceConflictException {
        log.info("Called with releaseDate {}, releaseDateId {}, movie {}", releaseDate, releaseDateId, movie);

        this.existsReleaseDate(movie.getReleaseDates()
                .stream()
                .filter(rd -> rd.getStatus() == DataStatus.ACCEPTED)
                .collect(Collectors.toList()), releaseDate);

        final MovieReleaseDateEntity movieReleaseDate = this.entityManager.find(MovieReleaseDateEntity.class, releaseDateId);
        movieReleaseDate.setDate(releaseDate.getDate());
        movieReleaseDate.setCountry(releaseDate.getCountry());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long createStoryline(
            @NotNull @Valid final Storyline storyline,
            @NotNull final MovieEntity movie,
            @NotNull final UserEntity user
    ) throws ResourceConflictException {
        log.info("Called with storyline {}, movie {}, user {}", storyline, movie, user);

        this.existsStoryline(movie.getStorylines()
                .stream()
                .filter(s -> s.getStatus() == DataStatus.ACCEPTED)
                .collect(Collectors.toList()), storyline);

        final MovieStorylineEntity movieStoryline = new MovieStorylineEntity(storyline.getStoryline());
        movieStoryline.setMovie(movie);
        movieStoryline.setUser(user);

        movie.getStorylines().add(movieStoryline);

        this.movieRepository.save(movie);

        return Iterables.getLast(movie.getStorylines()).getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateStoryline(
            @NotNull @Valid final Storyline storyline,
            @Min(1) final Long storylineId,
            @NotNull final MovieEntity movie
    ) throws ResourceConflictException {
        log.info("Called with storyline {}, storylineId {}, movie {}", storyline, storylineId, movie);

        this.existsStoryline(movie.getStorylines()
                .stream()
                .filter(s -> s.getStatus() == DataStatus.ACCEPTED)
                .collect(Collectors.toList()), storyline);

        final MovieStorylineEntity movieStoryline = this.entityManager.find(MovieStorylineEntity.class, storylineId);
        movieStoryline.setStoryline(storyline.getStoryline());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long createBoxOffice(
            @NotNull @Valid final BoxOffice boxOffice,
            @NotNull final MovieEntity movie,
            @NotNull final UserEntity user
    ) throws ResourceConflictException {
        log.info("Called with boxOffice {}, movie {}, user {}", boxOffice, movie, user);

        this.existsBoxOffice(movie.getBoxOffices()
                .stream()
                .filter(bo -> bo.getStatus() == DataStatus.ACCEPTED)
                .collect(Collectors.toList()), boxOffice);

        final MovieBoxOfficeEntity movieBoxOffice = new MovieBoxOfficeEntity(boxOffice.getBoxOffice(), boxOffice.getCountry());
        movieBoxOffice.setMovie(movie);
        movieBoxOffice.setUser(user);

        movie.getBoxOffices().add(movieBoxOffice);

        this.movieRepository.save(movie);

        return Iterables.getLast(movie.getBoxOffices()).getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateBoxOffice(
            @NotNull @Valid final BoxOffice boxOffice,
            @Min(1) final Long boxOfficeId,
            @NotNull final MovieEntity movie
    ) throws ResourceConflictException {
        log.info("Called with boxOffice {}, boxOfficeId {}, movie {}", boxOffice, boxOfficeId, movie);

        this.existsBoxOffice(movie.getBoxOffices()
                .stream()
                .filter(bo -> bo.getStatus() == DataStatus.ACCEPTED)
                .collect(Collectors.toList()), boxOffice);

        final MovieBoxOfficeEntity movieBoxOffice = this.entityManager.find(MovieBoxOfficeEntity.class, boxOfficeId);
        movieBoxOffice.setBoxOffice(boxOffice.getBoxOffice());
        movieBoxOffice.setCountry(boxOffice.getCountry());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long createSite(
            @NotNull @Valid final Site site,
            @NotNull final MovieEntity movie,
            @NotNull final UserEntity user
    ) throws ResourceConflictException {
        log.info("Called with site {}, movie {}, user {}", site, movie, user);

        this.existsSite(movie.getSites()
                .stream()
                .filter(s -> s.getStatus() == DataStatus.ACCEPTED)
                .collect(Collectors.toList()), site);

        final MovieSiteEntity movieSite = new MovieSiteEntity(site.getSite(), site.getOfficial());
        movieSite.setMovie(movie);
        movieSite.setUser(user);

        movie.getSites().add(movieSite);

        this.movieRepository.save(movie);

        return Iterables.getLast(movie.getSites()).getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateSite(
            @NotNull @Valid final Site site,
            @Min(1) final Long siteId,
            @NotNull final MovieEntity movie
    ) throws ResourceConflictException {
        log.info("Called with site {}, siteId {}, movie {}", site, siteId, movie);

        this.existsSite(movie.getSites()
                .stream()
                .filter(s -> s.getStatus() == DataStatus.ACCEPTED)
                .collect(Collectors.toList()), site);

        final MovieSiteEntity movieSite = this.entityManager.find(MovieSiteEntity.class, siteId);
        movieSite.setSite(site.getSite());
        movieSite.setOfficial(site.getOfficial());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long createCountry(
            @NotNull @Valid final Country country,
            @NotNull final MovieEntity movie,
            @NotNull final UserEntity user
    ) throws ResourceConflictException {
        log.info("Called with country {}, movie {}, user {}", country, movie, user);

        this.existsCountry(movie.getCountries()
                .stream()
                .filter(c -> c.getStatus() == DataStatus.ACCEPTED)
                .collect(Collectors.toList()), country);

        final MovieCountryEntity movieCountry = new MovieCountryEntity(country.getCountry());
        movieCountry.setMovie(movie);
        movieCountry.setUser(user);

        movie.getCountries().add(movieCountry);

        this.movieRepository.save(movie);

        return Iterables.getLast(movie.getCountries()).getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateCountry(
            @NotNull @Valid final Country country,
            @Min(1) final Long countryId,
            @NotNull final MovieEntity movie
    ) throws ResourceConflictException {
        log.info("Called with country {}, countryId {}, movie {}", country, countryId, movie);

        this.existsCountry(movie.getCountries()
                .stream()
                .filter(c -> c.getStatus() == DataStatus.ACCEPTED)
                .collect(Collectors.toList()), country);

        final MovieCountryEntity movieCountry = this.entityManager.find(MovieCountryEntity.class, countryId);
        movieCountry.setCountry(country.getCountry());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long createLanguage(
            @NotNull @Valid final Language language,
            @NotNull final MovieEntity movie,
            @NotNull final UserEntity user
    ) throws ResourceConflictException {
        log.info("Called with language {}, movie {}, user {}", language, movie, user);

        this.existsLanguage(movie.getLanguages()
                .stream()
                .filter(l -> l.getStatus() == DataStatus.ACCEPTED)
                .collect(Collectors.toList()), language);

        final MovieLanguageEntity movieLanguage = new MovieLanguageEntity(language.getLanguage());
        movieLanguage.setMovie(movie);
        movieLanguage.setUser(user);

        movie.getLanguages().add(movieLanguage);

        this.movieRepository.save(movie);

        return Iterables.getLast(movie.getLanguages()).getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateLanguage(
            @NotNull @Valid final Language language,
            @Min(1) final Long languageId,
            @NotNull final MovieEntity movie
    ) throws ResourceConflictException {
        log.info("Called with language {}, languageId {}, movie {}", language, languageId, movie);

        this.existsLanguage(movie.getLanguages()
                .stream()
                .filter(l -> l.getStatus() == DataStatus.ACCEPTED)
                .collect(Collectors.toList()), language);

        final MovieLanguageEntity movieLanguage = this.entityManager.find(MovieLanguageEntity.class, languageId);
        movieLanguage.setLanguage(language.getLanguage());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long createGenre(
            @NotNull @Valid final Genre genre,
            @NotNull final MovieEntity movie,
            @NotNull final UserEntity user
    ) throws ResourceConflictException {
        log.info("Called with genre {}, movie {}, user {}", genre, movie, user);

        this.existsGenre(movie.getGenres()
                .stream()
                .filter(g -> g.getStatus() == DataStatus.ACCEPTED)
                .collect(Collectors.toList()), genre);

        final MovieGenreEntity movieGenre = new MovieGenreEntity(genre.getGenre());
        movieGenre.setMovie(movie);
        movieGenre.setUser(user);

        movie.getGenres().add(movieGenre);

        this.movieRepository.save(movie);

        return Iterables.getLast(movie.getGenres()).getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateGenre(
            @NotNull @Valid final Genre genre,
            @Min(1) final Long genreId,
            @NotNull final MovieEntity movie
    ) throws ResourceConflictException {
        log.info("Called with genre {}, genreId {}, movie {}", genre, genreId, movie);

        this.existsGenre(movie.getGenres()
                .stream()
                .filter(g -> g.getStatus() == DataStatus.ACCEPTED)
                .collect(Collectors.toList()), genre);

        final MovieGenreEntity movieGenre = this.entityManager.find(MovieGenreEntity.class, genreId);
        movieGenre.setGenre(genre.getGenre());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long createReview(
            @NotNull @Valid final Review review,
            @NotNull final MovieEntity movie,
            @NotNull final UserEntity user
    ) throws ResourceConflictException {
        log.info("Called with review {}, movie {}, user {}", review, movie, user);

        this.existsReview(movie.getReviews()
                .stream()
                .filter(r -> r.getStatus() == DataStatus.ACCEPTED)
                .collect(Collectors.toList()), review);

        final MovieReviewEntity movieReview = new MovieReviewEntity(review.getTitle(), review.getReview());
        movieReview.setMovie(movie);
        movieReview.setUser(user);

        movie.getReviews().add(movieReview);

        this.movieRepository.save(movie);

        return Iterables.getLast(movie.getReviews()).getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateReview(
            @NotNull @Valid final Review review,
            @Min(1) final Long reviewId,
            @NotNull final MovieEntity movie
    ) throws ResourceConflictException {
        log.info("Called with review {}, reviewId {}, movie {}", review, reviewId, movie);

        this.existsReview(movie.getReviews()
                .stream()
                .filter(r -> r.getStatus() == DataStatus.ACCEPTED)
                .collect(Collectors.toList()), review);

        final MovieReviewEntity movieReview = this.entityManager.find(MovieReviewEntity.class, reviewId);
        movieReview.setTitle(review.getTitle());
        movieReview.setReview(review.getReview());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveRating(
            @NotNull @Valid Rate rate,
            @Min(1) Long movieId,
            @Min(1) Long userId
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with rate {}, movieId {}, userId {}", rate, movieId, userId);

        final UserEntity user = this.findUser(userId);
        final MovieEntity movie = this.findMovie(movieId, DataStatus.ACCEPTED);

        final List<MovieReleaseDateEntity> releaseDates = movie.getReleaseDates();

        Lists.newArrayList(releaseDates).sort(Comparator.comparing(MovieReleaseDateEntity::getDate));

        if(!releaseDates.isEmpty()
                && new Date().before(releaseDates.iterator().next().getDate())) {
            throw new ResourceConflictException("The movie with id " + movieId + " had no premiere");
        }

        boolean rated = false;

        final List<MovieRateEntity> ratings = movie.getRatings();
        for(final MovieRateEntity mRate : ratings) {
            if(mRate.getUser().getId().equals(userId)) {
                mRate.setRate(rate.getRate());

                movie.setRating(this.calculationRating(ratings));

                rated = true;

                break;
            }
        }

        if(!rated) {
            final MovieRateEntity movieRate = new MovieRateEntity();
            movieRate.setRate(rate.getRate());
            movieRate.setMovie(movie);
            movieRate.setUser(user);

            movie.getRatings().add(movieRate);

            movie.setRating(this.calculationRating(movie.getRatings()));
        }
    }

    /**
     * Helper method to find the user entity.
     *
     * @param id The user ID
     * @return The user
     * @throws ResourceNotFoundException if no user found
     */
    private UserEntity findUser(final Long id) throws ResourceNotFoundException {
        return this.userRepository
                .findByIdAndEnabledTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with id " + id));
    }

    /**
     * Helper method to find the movie entity.
     *
     * @param id The movie ID
     * @param status The movie status
     * @return The movie
     * @throws ResourceNotFoundException if no movie found
     */
    private MovieEntity findMovie(final Long id, final DataStatus status) throws ResourceNotFoundException {
        return this.movieRepository
                .findByIdAndStatus(id, status)
                .orElseThrow(() -> new ResourceNotFoundException("No movie found with id " + id));
    }


    /**
     * Calculates the average rating for a movie.
     *
     * @param movieRates Rating list
     * @return Average rating
     */
    private Float calculationRating(final List<MovieRateEntity> movieRates) {
        Float averageRating = 0F;

        for(final MovieRateEntity movieRate : movieRates) {
            averageRating += movieRate.getRate();
        }

        averageRating = averageRating / movieRates.size();

        return averageRating;
    }

    /**
     * Helper method for checking the uniqueness of the title.
     *
     * @param otherTitles List of titles
     * @param otherTitle The OtherTitle object to be checked
     * @throws ResourceConflictException if the element exists
     */
    private void existsOtherTile(final List<MovieOtherTitleEntity> otherTitles, final OtherTitle otherTitle)
            throws  ResourceConflictException {
        otherTitles.forEach(ot -> {
            if(ot.getTitle().equalsIgnoreCase(otherTitle.getTitle())
                    && ot.getCountry() == otherTitle.getCountry()) {
                throw new ResourceConflictException(
                        "The element with the title " + otherTitle.getTitle() +
                                " and the country of the " + otherTitle.getCountry() +
                                " exists");
            }
        });
    }

    /**
     * Helper method for checking the uniqueness of the release date.
     *
     * @param releaseDates List of release dates
     * @param releaseDate The ReleaseDate to be checked
     * @throws ResourceConflictException if the element exists
     */
    private void existsReleaseDate(final List<MovieReleaseDateEntity> releaseDates, final ReleaseDate releaseDate)
            throws  ResourceConflictException {
        releaseDates.forEach(rd -> {
            if(rd.getDate().compareTo(releaseDate.getDate()) == 0
                    && rd.getCountry() == releaseDate.getCountry()) {
                throw new ResourceConflictException(
                        "The element with the date " + releaseDate.getDate() +
                                " and the country of the " + releaseDate.getCountry() +
                                " exists");
            }
        });
    }

    /**
     * Helper method for checking the uniqueness of the storyline.
     *
     * @param storylines List of storylines
     * @param storyline The Storyline object to be checked
     * @throws ResourceConflictException if the element exists
     */
    private void existsStoryline(final List<MovieStorylineEntity> storylines, final Storyline storyline)
            throws  ResourceConflictException {
        storylines.forEach(s -> {
            if(s.getStoryline().equals(storyline.getStoryline())) {
                throw new ResourceConflictException(
                        "The element with the storyline " + storyline.getStoryline() +
                                " exists");
            }
        });
    }

    /**
     * Helper method for checking the uniqueness of the box office.
     *
     * @param boxOffices List of box offices
     * @param boxOffice The BoxOffice object to be checked
     * @throws ResourceConflictException if the element exists
     */
    private void existsBoxOffice(final List<MovieBoxOfficeEntity> boxOffices, final BoxOffice boxOffice)
            throws  ResourceConflictException {
        boxOffices.forEach(bo -> {
            if(bo.getBoxOffice().equals(boxOffice.getBoxOffice())
                    && bo.getCountry() == boxOffice.getCountry()) {
                throw new ResourceConflictException(
                        "The element with the box office " + boxOffice.getBoxOffice() +
                                " and the country of the " + boxOffice.getCountry() +
                                " exists");
            }
        });
    }

    /**
     * Helper method for checking the uniqueness of the site.
     *
     * @param sites List of sites
     * @param site The Site object to be checked
     * @throws ResourceConflictException if the element exists
     */
    private void existsSite(final List<MovieSiteEntity> sites, final Site site)
            throws  ResourceConflictException {
        sites.forEach(s -> {
            if(s.getSite().equals(site.getSite())
                    && s.getOfficial() == site.getOfficial()) {
                throw new ResourceConflictException(
                        "The element with the site " + site.getSite() +
                                " and the status of the " + site.getOfficial() +
                                " exists");
            }
        });
    }

    /**
     * Helper method for checking the uniqueness of the country.
     *
     * @param countries List of countries
     * @param country The Country object to be checked
     * @throws ResourceConflictException if the element exists
     */
    private void existsCountry(final List<MovieCountryEntity> countries, final Country country)
            throws  ResourceConflictException {
        countries.forEach(c -> {
            if(c.getCountry() == country.getCountry()) {
                throw new ResourceConflictException(
                        "The element with the country " + country.getCountry() +
                                " exists");
            }
        });
    }

    /**
     * Helper method for checking the uniqueness of the language.
     *
     * @param languages List of languages
     * @param language The Language object to be checked
     * @throws ResourceConflictException if the element exists
     */
    private void existsLanguage(final List<MovieLanguageEntity> languages, final Language language)
            throws  ResourceConflictException {
        languages.forEach(l -> {
            if(l.getLanguage() == language.getLanguage()) {
                throw new ResourceConflictException(
                        "The element with the language " + language.getLanguage() +
                                " exists");
            }
        });
    }

    /***
     * Helper method for checking the uniqueness of the genre.
     *
     * @param genres List of genres
     * @param genre The Genre object to be checked
     * @throws ResourceConflictException if the element exists
     */
    private void existsGenre(final List<MovieGenreEntity> genres, final Genre genre)
            throws  ResourceConflictException {
        genres.forEach(g -> {
            if(g.getGenre() == genre.getGenre()) {
                throw new ResourceConflictException(
                        "The element with the genre " + genre.getGenre() +
                                " exists");
            }
        });
    }

    /**
     * Helper method for checking the uniqueness of the review.
     *
     * @param reviews List of reviews
     * @param review The Review object to be checked
     * @throws ResourceConflictException if the element exists
     */
    private void existsReview(final List<MovieReviewEntity> reviews, final Review review)
            throws  ResourceConflictException {
        reviews.forEach(r -> {
            if(r.getTitle().equals(review.getTitle())
                    && r.getReview().equals(review.getReview())) {
                throw new ResourceConflictException(
                        "The element with the title " + review.getTitle() +
                                " and the review of the " + review.getReview() +
                                " exists");
            }
        });
    }
}

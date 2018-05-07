package com.jonki.popcorn.core.jpa.service;

import com.jonki.popcorn.common.dto.TitleAttribute;
import com.jonki.popcorn.common.dto.movie.*;
import com.jonki.popcorn.common.dto.movie.request.ImageRequest;
import com.jonki.popcorn.common.dto.movie.request.Rate;
import com.jonki.popcorn.common.dto.request.MovieDTO;
import com.jonki.popcorn.common.exception.ResourceConflictException;
import com.jonki.popcorn.common.exception.ResourceForbiddenException;
import com.jonki.popcorn.common.exception.ResourceNotFoundException;
import com.jonki.popcorn.core.jpa.entity.MovieEntity;
import com.jonki.popcorn.core.jpa.entity.UserEntity;
import com.jonki.popcorn.core.jpa.repository.MovieRepository;
import com.jonki.popcorn.core.jpa.repository.UserRepository;
import com.jonki.popcorn.core.service.AuthorizationService;
import com.jonki.popcorn.core.service.MoviePersistenceService;
import com.jonki.popcorn.common.dto.DataStatus;
import com.jonki.popcorn.common.dto.UserMoviePermission;
import com.jonki.popcorn.common.dto.VerificationStatus;
import com.jonki.popcorn.core.util.CollectorUtils;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.jonki.popcorn.core.jpa.entity.movie.*;
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
    public MoviePersistenceServiceImpl(
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
    public void createMovie(
            @NotNull @Valid final MovieDTO movieDTO
    ) throws ResourceNotFoundException {
        log.info("Called with movieDTO {}", movieDTO);

        final UserEntity user = this.findUser(this.authorizationService.getUserId());

        final MovieEntity movie = new MovieEntity();

        movie.setTitle(movieDTO.getTitle());
        movie.setType(movieDTO.getType());

        this.movieRepository.save(movie);

        final MovieOtherTitleEntity movieOtherTitleEntity = new MovieOtherTitleEntity(movieDTO.getTitle(), null, TitleAttribute.ORIGINAL_TITLE);
        movieOtherTitleEntity.setMovie(movie);
        movie.getOtherTitles().add(movieOtherTitleEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateMovieStatus(
            @Min(1) final Long id,
            @NotNull final VerificationStatus status
    ) throws ResourceForbiddenException, ResourceNotFoundException {
        log.info("Called with id {}, status {}", id, status);

        final UserEntity user = this.findUser(this.authorizationService.getUserId());
        final MovieEntity movie = this.findMovie(id, DataStatus.WAITING);

        if(!user.getPermissions().contains(UserMoviePermission.ALL)
                && !user.getPermissions().contains(UserMoviePermission.NEW_MOVIE)) {
            throw new ResourceForbiddenException("No permissions");
        }

        movie.setStatus(status.getDataStatus());
        movie.getOtherTitles().stream().findFirst().ifPresent(title -> title.setStatus(status.getDataStatus()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long createOtherTitle(
            @NotNull @Valid final OtherTitle otherTitle,
            @NotNull final MovieEntity movie
    ) throws ResourceConflictException {
        log.info("Called with otherTitle {}, movie {}", otherTitle, movie);

        this.existsOtherTile(movie.getOtherTitles()
                .stream()
                .filter(ot -> ot.getStatus() == DataStatus.ACCEPTED)
                .collect(Collectors.toList()), otherTitle);

        final MovieOtherTitleEntity movieOtherTitle = new MovieOtherTitleEntity(otherTitle.getTitle(), otherTitle.getCountry(), otherTitle.getAttribute());
        movieOtherTitle.setMovie(movie);

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
        this.checkOriginalTitle(movie, otherTitleId, otherTitle);

        final MovieOtherTitleEntity movieOtherTitle = this.entityManager.find(MovieOtherTitleEntity.class, otherTitleId);
        movieOtherTitle.setTitle(otherTitle.getTitle());
        movieOtherTitle.setCountry(otherTitle.getCountry());
        movieOtherTitle.setAttribute(otherTitle.getAttribute());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long createReleaseDate(
            @NotNull @Valid final ReleaseDate releaseDate,
            @NotNull final MovieEntity movie
    ) throws ResourceConflictException {
        log.info("Called with releaseDate {}, movie {}", releaseDate, movie);

        this.existsReleaseDate(movie.getReleaseDates()
                .stream()
                .filter(rd -> rd.getStatus() == DataStatus.ACCEPTED)
                .collect(Collectors.toList()), releaseDate);

        final MovieReleaseDateEntity movieReleaseDate = new MovieReleaseDateEntity(releaseDate.getDate(), releaseDate.getCountry());
        movieReleaseDate.setMovie(movie);

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
    public Long createOutline(
            @NotNull @Valid final Outline outline,
            @NotNull final MovieEntity movie
    ) throws ResourceConflictException {
        log.info("Called with outline {}, movie {}", outline, movie);

        this.existsOutline(movie.getOutlines()
                .stream()
                .filter(o -> o.getStatus() == DataStatus.ACCEPTED)
                .collect(Collectors.toList()), outline);

        final MovieOutlineEntity movieOutline = new MovieOutlineEntity(outline.getOutline());
        movieOutline.setMovie(movie);

        movie.getOutlines().add(movieOutline);

        this.movieRepository.save(movie);

        return Iterables.getLast(movie.getOutlines()).getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateOutline(
            @NotNull @Valid final Outline outline,
            @Min(1) final Long outlineId,
            @NotNull final MovieEntity movie
    ) throws ResourceConflictException {
        log.info("Called with outline {}, outlineId {}, movie {}", outline, outlineId, movie);

        this.existsOutline(movie.getOutlines()
                .stream()
                .filter(o -> o.getStatus() == DataStatus.ACCEPTED)
                .collect(Collectors.toList()), outline);

        final MovieOutlineEntity movieOutline = this.entityManager.find(MovieOutlineEntity.class, outlineId);
        movieOutline.setOutline(outline.getOutline());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long createSummary(
            @NotNull @Valid final Summary summary,
            @NotNull final MovieEntity movie
    ) throws ResourceConflictException {
        log.info("Called with summary {}, movie {}", summary, movie);

        this.existsSummary(movie.getSummaries()
                .stream()
                .filter(s -> s.getStatus() == DataStatus.ACCEPTED)
                .collect(Collectors.toList()), summary);

        final MovieSummaryEntity movieSummary = new MovieSummaryEntity(summary.getSummary());
        movieSummary.setMovie(movie);

        movie.getSummaries().add(movieSummary);

        this.movieRepository.save(movie);

        return Iterables.getLast(movie.getSummaries()).getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateSummary(
            @NotNull @Valid final Summary summary,
            @Min(1) final Long summaryId,
            @NotNull final MovieEntity movie
    ) throws ResourceConflictException {
        log.info("Called with summary {}, summaryId {}, movie {}", summary, summaryId, movie);

        this.existsSummary(movie.getSummaries()
                .stream()
                .filter(s -> s.getStatus() == DataStatus.ACCEPTED)
                .collect(Collectors.toList()), summary);

        final MovieSummaryEntity movieSummary = this.entityManager.find(MovieSummaryEntity.class, summaryId);
        movieSummary.setSummary(summary.getSummary());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long createSynopsis(
            @NotNull @Valid final Synopsis synopsis,
            @NotNull final MovieEntity movie
    ) throws ResourceConflictException {
        log.info("Called with synopsis {}, movie {}", synopsis, movie);

        this.existsSynopsis(movie.getSynopses()
                .stream()
                .filter(s -> s.getStatus() == DataStatus.ACCEPTED)
                .collect(Collectors.toList()), synopsis);

        final MovieSynopsisEntity movieSynopsis = new MovieSynopsisEntity(synopsis.getSynopsis());
        movieSynopsis.setMovie(movie);

        movie.getSynopses().add(movieSynopsis);

        this.movieRepository.save(movie);

        return Iterables.getLast(movie.getSynopses()).getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateSynopsis(
            @NotNull @Valid final Synopsis synopsis,
            @Min(1) final Long synopsisId,
            @NotNull final MovieEntity movie
    ) throws ResourceConflictException {
        log.info("Called with synopsis {}, synopsisId {}, movie {}", synopsis, synopsisId, movie);

        this.existsSynopsis(movie.getSynopses()
                .stream()
                .filter(s -> s.getStatus() == DataStatus.ACCEPTED)
                .collect(Collectors.toList()), synopsis);

        final MovieSynopsisEntity movieSynopsis = this.entityManager.find(MovieSynopsisEntity.class, synopsisId);
        movieSynopsis.setSynopsis(synopsis.getSynopsis());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long createBoxOffice(
            @NotNull @Valid final BoxOffice boxOffice,
            @NotNull final MovieEntity movie
    ) throws ResourceConflictException {
        log.info("Called with boxOffice {}, movie {}", boxOffice, movie);

        this.existsBoxOffice(movie.getBoxOffices()
                .stream()
                .filter(bo -> bo.getStatus() == DataStatus.ACCEPTED)
                .collect(Collectors.toList()), boxOffice);

        final MovieBoxOfficeEntity movieBoxOffice = new MovieBoxOfficeEntity(boxOffice.getBoxOffice(), boxOffice.getCountry());
        movieBoxOffice.setMovie(movie);

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
            @NotNull final MovieEntity movie
    ) throws ResourceConflictException {
        log.info("Called with site {}, movie {}", site, movie);

        this.existsSite(movie.getSites()
                .stream()
                .filter(s -> s.getStatus() == DataStatus.ACCEPTED)
                .collect(Collectors.toList()), site);

        final MovieSiteEntity movieSite = new MovieSiteEntity(site.getSite(), site.getOfficial());
        movieSite.setMovie(movie);

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
            @NotNull final MovieEntity movie
    ) throws ResourceConflictException {
        log.info("Called with country {}, movie {}", country, movie);

        this.existsCountry(movie.getCountries()
                .stream()
                .filter(c -> c.getStatus() == DataStatus.ACCEPTED)
                .collect(Collectors.toList()), country);

        final MovieCountryEntity movieCountry = new MovieCountryEntity(country.getCountry());
        movieCountry.setMovie(movie);

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
            @NotNull final MovieEntity movie
    ) throws ResourceConflictException {
        log.info("Called with language {}, movie {}", language, movie);

        this.existsLanguage(movie.getLanguages()
                .stream()
                .filter(l -> l.getStatus() == DataStatus.ACCEPTED)
                .collect(Collectors.toList()), language);

        final MovieLanguageEntity movieLanguage = new MovieLanguageEntity(language.getLanguage());
        movieLanguage.setMovie(movie);

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
            @NotNull final MovieEntity movie
    ) throws ResourceConflictException {
        log.info("Called with genre {}, movie {}", genre, movie);

        this.existsGenre(movie.getGenres()
                .stream()
                .filter(g -> g.getStatus() == DataStatus.ACCEPTED)
                .collect(Collectors.toList()), genre);

        final MovieGenreEntity movieGenre = new MovieGenreEntity(genre.getGenre());
        movieGenre.setMovie(movie);

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
            @NotNull final MovieEntity movie
    ) throws ResourceConflictException {
        log.info("Called with review {}, movie {}", review, movie);

        this.existsReview(movie.getReviews()
                .stream()
                .filter(r -> r.getStatus() == DataStatus.ACCEPTED)
                .collect(Collectors.toList()), review);

        final MovieReviewEntity movieReview = new MovieReviewEntity(review.getTitle(), review.getReview(), review.isSpoiler());
        movieReview.setMovie(movie);

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
        movieReview.setSpoiler(review.isSpoiler());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long createPhoto(
            @NotNull @Valid final ImageRequest photo,
            @NotNull final MovieEntity movie
    ) {
        log.info("Called with photo {}, movie {}", photo, movie);

        final MoviePhotoEntity moviePhoto = new MoviePhotoEntity();
        moviePhoto.setIdInCloud(photo.getIdInCloud());
        moviePhoto.setMovie(movie);

        movie.getPhotos().add(moviePhoto);

        this.movieRepository.save(movie);

        return Iterables.getLast(movie.getPhotos()).getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePhoto(
            @NotNull @Valid final ImageRequest photo,
            @Min(1) final Long photoId,
            @NotNull final MovieEntity movie
    ) {
        log.info("Called with photo {}, photoId {}, movie {}", photo, photoId, movie);

        final MoviePhotoEntity moviePhoto = this.entityManager.find(MoviePhotoEntity.class, photoId);
        moviePhoto.setIdInCloud(photo.getIdInCloud());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long createPoster(
            @NotNull @Valid final ImageRequest poster,
            @NotNull final MovieEntity movie
    ) {
        log.info("Called with poster {}, movie {}", poster, movie);

        final MoviePosterEntity moviePoster = new MoviePosterEntity();
        moviePoster.setIdInCloud(poster.getIdInCloud());
        moviePoster.setMovie(movie);

        movie.getPosters().add(moviePoster);

        this.movieRepository.save(movie);

        return Iterables.getLast(movie.getPosters()).getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePoster(
            @NotNull @Valid final ImageRequest poster,
            @Min(1) final Long posterId,
            @NotNull final MovieEntity movie
    ) {
        log.info("Called with poster {}, posterId {}, movie {}", poster, posterId, movie);

        final MoviePosterEntity moviePoster = this.entityManager.find(MoviePosterEntity.class, posterId);
        moviePoster.setIdInCloud(poster.getIdInCloud());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveRating(
            @Min(1) final Long id,
            @NotNull @Valid final Rate rate
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with id {}, rate {}", id, rate);

        final UserEntity user = this.findUser(this.authorizationService.getUserId());
        final MovieEntity movie = this.findMovie(id, DataStatus.ACCEPTED);

        final List<MovieReleaseDateEntity> releaseDates = movie.getReleaseDates();

        Lists.newArrayList(releaseDates).sort(Comparator.comparing(MovieReleaseDateEntity::getDate));

        if(!releaseDates.isEmpty()
                && new Date().before(releaseDates.iterator().next().getDate())) {
            throw new ResourceConflictException("The movie with id " + id + " had no premiere");
        }

        boolean rated = false;

        final List<MovieRateEntity> ratings = movie.getRatings();
        for(final MovieRateEntity mRate : ratings) {
            if(mRate.getUser().getUniqueId().equals(user.getUniqueId())) {
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
     * {@inheritDoc}
     */
    @Override
    public void setFavoriteQuestion(
            @Min(1) final Long id
    ) throws ResourceNotFoundException, ResourceConflictException {
        this.findUser(this.authorizationService.getUserId()).addFavoriteMovie(this.findMovie(id, DataStatus.ACCEPTED));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void undoFavoriteQuestion(
            @Min(1) final Long id
    ) throws ResourceNotFoundException, ResourceConflictException {
        this.findUser(this.authorizationService.getUserId()).removeFavoriteMovie(this.findMovie(id, DataStatus.ACCEPTED));
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
     *
     *
     * @param movie
     * @param otherTitleId
     * @param otherTitle
     */
    private void checkOriginalTitle(MovieEntity movie, Long otherTitleId, OtherTitle otherTitle) {
        final MovieOtherTitleEntity originalTitle = movie.getOtherTitles().stream()
                .filter(title -> title.getAttribute() == TitleAttribute.ORIGINAL_TITLE)
                .collect(CollectorUtils.singletonCollector());

        if(otherTitle.getAttribute() == TitleAttribute.ORIGINAL_TITLE
                && !originalTitle.getId().equals(otherTitleId)) {
            throw new ResourceConflictException("The original title already exists");
        }
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
     * Helper method for checking the uniqueness of the outline.
     *
     * @param outlines List of outlines
     * @param outline The Outline object to be checked
     * @throws ResourceConflictException if the element exists
     */
    private void existsOutline(final List<MovieOutlineEntity> outlines, final Outline outline)
            throws  ResourceConflictException {
        outlines.forEach(o -> {
            if(o.getOutline().equals(outline.getOutline())) {
                throw new ResourceConflictException(
                        "The element with the outline " + outline.getOutline() +
                                " exists");
            }
        });
    }

    /**
     * Helper method for checking the uniqueness of the summary.
     *
     * @param summaries List of summaries
     * @param summary The Summary object to be checked
     * @throws ResourceConflictException if the element exists
     */
    private void existsSummary(final List<MovieSummaryEntity> summaries, final Summary summary)
            throws  ResourceConflictException {
        summaries.forEach(s -> {
            if(s.getSummary().equals(summary.getSummary())) {
                throw new ResourceConflictException(
                        "The element with the summary " + summary.getSummary() +
                                " exists");
            }
        });
    }

    /**
     * Helper method for checking the uniqueness of the synopsis.
     *
     * @param synopses List of synopses
     * @param synopsis The Synopsis object to be checked
     * @throws ResourceConflictException if the element exists
     */
    private void existsSynopsis(final List<MovieSynopsisEntity> synopses, final Synopsis synopsis)
            throws  ResourceConflictException {
        synopses.forEach(s -> {
            if(s.getSynopsis().equals(synopsis.getSynopsis())) {
                throw new ResourceConflictException(
                        "The element with the synopsis " + synopsis.getSynopsis() +
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

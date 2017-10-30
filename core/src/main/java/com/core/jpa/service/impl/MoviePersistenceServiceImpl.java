package com.core.jpa.service.impl;

import com.common.dto.movie.*;
import com.common.dto.request.MovieDTO;
import com.common.dto.request.movie.BoxOffice;
import com.common.dto.request.movie.OtherTitle;
import com.common.dto.request.movie.ReleaseDate;
import com.common.dto.request.movie.Site;
import com.common.exception.ResourceForbiddenException;
import com.common.exception.ResourceNotFoundException;
import com.core.jpa.entity.ContributionEntity;
import com.core.jpa.entity.MovieEntity;
import com.core.jpa.entity.UserEntity;
import com.core.jpa.entity.movie.*;
import com.core.jpa.repository.*;
import com.core.jpa.service.MoviePersistenceService;
import com.core.movie.EditStatus;
import com.core.movie.MovieField;
import com.core.movie.UserMoviePermission;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
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
import java.util.HashSet;
import java.util.Set;

/**
 * JPA implementation of the Movie Persistence Service.
 */
@Service("moviePersistenceService")
@Slf4j
@Transactional(
        rollbackFor = {
                ResourceForbiddenException.class,
                ResourceNotFoundException.class,
                ConstraintViolationException.class
        }
)
@Validated
public class MoviePersistenceServiceImpl implements MoviePersistenceService {

    private final MovieRepository movieRepository;
    private final MovieInfoRepository movieInfoRepository;
    private final ContributionRepository contributionRepository;
    private final UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Constructor.
     *
     * @param movieRepository The movie repository to use
     * @param movieInfoRepository The movie info repository to use
     * @param contributionRepository The contribution repository to use
     * @param userRepository The user repository to use
     */
    @Autowired
    public MoviePersistenceServiceImpl(
            @NotNull final MovieRepository movieRepository,
            @NotNull final MovieInfoRepository movieInfoRepository,
            @NotNull final ContributionRepository contributionRepository,
            @NotNull final UserRepository userRepository
    ) {
        this.movieRepository = movieRepository;
        this.movieInfoRepository = movieInfoRepository;
        this.contributionRepository = contributionRepository;
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
        movieDTO.getBudget().ifPresent(movie::setBudget);

        this.movieRepository.save(movie);

        this.createNewMovieContributions(movieDTO, movie, user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void acceptMovie(
            @Min(1) final Long movieId,
            @Min(1) final Long userId
    ) throws ResourceForbiddenException, ResourceNotFoundException {
        log.info("Called with movieId {}, userId {}", movieId, userId);

        final UserEntity user = this.findUser(userId);
        final MovieEntity movie = this.findMovie(movieId, EditStatus.WAITING);

        if(!user.getPermissions().contains(UserMoviePermission.ALL)
                || !user.getPermissions().contains(UserMoviePermission.NEW_MOVIE)) {
            throw new ResourceForbiddenException("No permissions");
        }

        movie.setStatus(EditStatus.ACCEPTED);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void acceptContribution(
            @Min(1) final Long contributionId,
            @Min(1) final Long userId
    ) throws ResourceForbiddenException, ResourceNotFoundException {
        log.info("Called with contributionId {}, userId {}", contributionId, userId);

        final UserEntity user = this.findUser(userId);
        final ContributionEntity contribution = this.findContribution(contributionId);

        if(!CollectionUtils.containsAny(user.getPermissions(), contribution.getField().getNecessaryPermissions())) {
            throw new ResourceForbiddenException("No permissions");
        }

        contribution.setStatus(EditStatus.ACCEPTED);

        contribution.getIds().forEach(id -> {
            MovieInfo movieInfo = this.movieInfoRepository.findOne(id);
            movieInfo.setStatus(EditStatus.ACCEPTED);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rejectMovie(
            @Min(1) final Long movieId,
            @Min(1) final Long userId
    ) throws ResourceForbiddenException, ResourceNotFoundException {
        log.info("Called with movieId {}, userId {}", movieId, userId);

        final UserEntity user = this.findUser(userId);
        final MovieEntity movie = this.findMovie(movieId, EditStatus.WAITING);

        if(!user.getPermissions().contains(UserMoviePermission.ALL)
                || !user.getPermissions().contains(UserMoviePermission.NEW_MOVIE)) {
            throw new ResourceForbiddenException("No permissions");
        }

        movie.setStatus(EditStatus.REJECTED);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rejectContribution(
            @Min(1) final Long contributionId,
            @Min(1) final Long userId
    ) throws ResourceForbiddenException, ResourceNotFoundException {
        log.info("Called with contributionId {}, userId {}", contributionId, userId);

        final UserEntity user = this.findUser(userId);
        final ContributionEntity contribution = this.findContribution(contributionId);

        if(!CollectionUtils.containsAny(user.getPermissions(), contribution.getField().getNecessaryPermissions())) {
            throw new ResourceForbiddenException("No permissions");
        }

        contribution.setStatus(EditStatus.REJECTED);

        contribution.getIds().forEach(id -> {
            MovieInfo movieInfo = this.movieInfoRepository.findOne(id);
            movieInfo.setStatus(EditStatus.REJECTED);
        });
    }

    /**
     * {@inheritDoc}
     */
    public void saveDescription(
            final String description,
            final Long movieId,
            final Long userId
    ) throws ResourceNotFoundException {
        log.info("Called with description {}, movieId {}, userId {}", description, movieId, userId);

        final UserEntity user = this.findUser(userId);
        final MovieEntity movie = this.findMovie(movieId);

        final MovieDescription movieDescription = new MovieDescription(description);
        movieDescription.setMovie(movie);
        movieDescription.setUser(user);

        this.createContribution(Sets.newHashSet(movieDescription), movie, user, MovieField.DESCRIPTION);
    }

    /**
     * {@inheritDoc}
     */
    public void saveOtherTitles(
            final Set<OtherTitle> otherTitles,
            final Long movieId,
            final Long userId
    ) throws ResourceNotFoundException {
        log.info("Called with otherTitles {}, movieId {}, userId {}", otherTitles, movieId, userId);

        final Set<MovieOtherTitle> movieOtherTitles = new HashSet<>();
        final UserEntity user = this.findUser(userId);
        final MovieEntity movie = this.findMovie(movieId);

        otherTitles.forEach(otherTitle -> {
            final MovieOtherTitle movieOtherTitle = new MovieOtherTitle(otherTitle.getTitle(), otherTitle.getCountry());
            movieOtherTitle.setMovie(movie);
            movieOtherTitle.setUser(user);

            movieOtherTitles.add(movieOtherTitle);
        });

        this.createContribution(movieOtherTitles, movie, user, MovieField.OTHER_TITLE);
    }

    /**
     * {@inheritDoc}
     */
    public void saveBoxOffices(
            final Set<BoxOffice> boxOffices,
            final Long movieId,
            final Long userId
    ) throws ResourceNotFoundException {
        log.info("Called with boxOffices {}, movieId {}, userId {}", boxOffices, movieId, userId);

        final Set<MovieBoxOffice> movieBoxOffices = new HashSet<>();
        final UserEntity user = this.findUser(userId);
        final MovieEntity movie = this.findMovie(movieId);

        boxOffices.forEach(boxOffice -> {
            final MovieBoxOffice movieBoxOffice = new MovieBoxOffice(boxOffice.getBoxOffice(), boxOffice.getCountry());
            movieBoxOffice.setMovie(movie);
            movieBoxOffice.setUser(user);

            movieBoxOffices.add(movieBoxOffice);
        });

        this.createContribution(movieBoxOffices, movie, user, MovieField.BOX_OFFICE);
    }

    /**
     * {@inheritDoc}
     */
    public void saveSites(
            final Set<Site> sites,
            final Long movieId,
            final Long userId
    ) throws ResourceNotFoundException {
        log.info("Called with sites {}, movieId {}, userId {}", sites, movieId, userId);

        final Set<MovieSite> movieSites = new HashSet<>();
        final UserEntity user = this.findUser(userId);
        final MovieEntity movie = this.findMovie(movieId);

        sites.forEach(site -> {
            final MovieSite movieSite = new MovieSite(site.getSite(), site.getOfficial());
            movieSite.setMovie(movie);
            movieSite.setUser(user);

            movieSites.add(movieSite);
        });

        this.createContribution(movieSites, movie, user, MovieField.SITE);
    }

    /**
     * {@inheritDoc}
     */
    public void saveReleaseDates(
            final Set<ReleaseDate> releaseDates,
            final Long movieId,
            final Long userId
    ) throws ResourceNotFoundException {
        log.info("Called with releaseDates {}, movieId {}, userId {}", releaseDates, movieId, userId);

        final Set<MovieReleaseDate> movieReleaseDates = new HashSet<>();
        final UserEntity user = this.findUser(userId);
        final MovieEntity movie = this.findMovie(movieId);

        releaseDates.forEach(releaseDate -> {
            final MovieReleaseDate movieReleaseDate = new MovieReleaseDate(releaseDate.getReleaseDate(),
                    releaseDate.getCountry());
            movieReleaseDate.setMovie(movie);
            movieReleaseDate.setUser(user);

            movieReleaseDates.add(movieReleaseDate);
        });

        this.createContribution(movieReleaseDates, movie, user, MovieField.RELEASE_DATE);
    }

    /**
     * {@inheritDoc}
     */
    public void saveStoryline(
            final String storyline,
            final Long movieId,
            final Long userId
    ) throws ResourceNotFoundException {
        log.info("Called with storyline {}, movieId {}, userId {}", storyline, movieId, userId);

        final UserEntity user = this.findUser(userId);
        final MovieEntity movie = this.findMovie(movieId);

        final MovieStoryline movieStoryline = new MovieStoryline(storyline);
        movieStoryline.setMovie(movie);
        movieStoryline.setUser(user);

        this.createContribution(Sets.newHashSet(movieStoryline), movie, user, MovieField.STORYLINE);
    }

    /**
     * {@inheritDoc}
     */
    public void saveCountries(
            final Set<CountryType> countries,
            final Long movieId,
            final Long userId
    ) throws ResourceNotFoundException {
        log.info("Called with countries {}, movieId {}, userId {}", countries, movieId, userId);

        final Set<MovieCountry> movieCountries = new HashSet<>();
        final UserEntity user = this.findUser(userId);
        final MovieEntity movie = this.findMovie(movieId);

        countries.forEach(country -> {
            final MovieCountry movieCountry = new MovieCountry(country);
            movieCountry.setMovie(movie);
            movieCountry.setUser(user);

            movieCountries.add(movieCountry);
        });

        this.createContribution(movieCountries, movie, user, MovieField.COUNTRY);
    }

    /**
     * {@inheritDoc}
     */
    public void saveLanguages(
            final Set<LanguageType> languages,
            final Long movieId,
            final Long userId
    ) throws ResourceNotFoundException {
        log.info("Called with languages {}, movieId {}, userId {}", languages, movieId, userId);

        final Set<MovieLanguage> movieLanguages = new HashSet<>();
        final UserEntity user = this.findUser(userId);
        final MovieEntity movie = this.findMovie(movieId);

        languages.forEach(language -> {
            final MovieLanguage movieLanguage = new MovieLanguage(language);
            movieLanguage.setMovie(movie);
            movieLanguage.setUser(user);

            movieLanguages.add(movieLanguage);
        });

        this.createContribution(movieLanguages, movie, user, MovieField.LANGUAGE);
    }

    /**
     * {@inheritDoc}
     */
    public void saveGenres(
            final Set<GenreType> genres,
            final Long movieId,
            final Long userId
    ) throws ResourceNotFoundException {
        log.info("Called with genres {}, movieId {}, userId {}", genres, movieId, userId);

        final Set<MovieGenre> movieGenres = new HashSet<>();
        final UserEntity user = this.findUser(userId);
        final MovieEntity movie = this.findMovie(movieId);

        genres.forEach(genre -> {
            final MovieGenre movieGenre = new MovieGenre(genre);
            movieGenre.setMovie(movie);
            movieGenre.setUser(user);

            movieGenres.add(movieGenre);
        });

        this.createContribution(movieGenres, movie, user, MovieField.GENRE);
    }

    /**
     * Create a contributions for the new movie.
     *
     * @param movieDTO MovieDTO object
     * @param movie MovieEntity object
     * @param user UserEntity object
     */
    private void createNewMovieContributions(
            final MovieDTO movieDTO,
            final MovieEntity movie,
            final UserEntity user
    ) {
        movieDTO.getDescription().ifPresent(description -> this.saveDescription(description, movie.getId(), user.getId()));
        if(!movieDTO.getOtherTitles().isEmpty()) {
            this.saveOtherTitles(movieDTO.getOtherTitles(), movie.getId(), user.getId());
        }
        if(!movieDTO.getBoxOffices().isEmpty()) {
            this.saveBoxOffices(movieDTO.getBoxOffices(), movie.getId(), user.getId());
        }
        if(!movieDTO.getSites().isEmpty()) {
            this.saveSites(movieDTO.getSites(), movie.getId(), user.getId());
        }
        if(!movieDTO.getReleaseDates().isEmpty()) {
            this.saveReleaseDates(movieDTO.getReleaseDates(), movie.getId(), user.getId());
        }
        movieDTO.getStoryline().ifPresent(storyline -> this.saveStoryline(storyline, movie.getId(), user.getId()));
        if(!movieDTO.getCountries().isEmpty()) {
            this.saveCountries(movieDTO.getCountries(), movie.getId(), user.getId());
        }
        if(!movieDTO.getLanguages().isEmpty()) {
            this.saveLanguages(movieDTO.getLanguages(), movie.getId(), user.getId());
        }
        if(!movieDTO.getGenres().isEmpty()) {
            this.saveGenres(movieDTO.getGenres(), movie.getId(), user.getId());
        }
    }

    /**
     * Create a contribution.
     *
     * @param setElements Set of elements to be included in the contribution
     * @param movie The movie
     * @param user The user
     * @param field The movie field for which the contribution will be made
     * @param <T> An object of the type inheriting from MovieInfo
     */
    private <T extends MovieInfo> void createContribution(
            final Set<T> setElements,
            final MovieEntity movie,
            final UserEntity user,
            final MovieField field
    ) {
        final Set<Long> ids = new HashSet<>();

        setElements.forEach(element -> {
            this.entityManager.persist(element);
            ids.add(element.getId());
        });

        final ContributionEntity contribution = new ContributionEntity();
        contribution.setMovie(movie);
        contribution.setUser(user);
        contribution.setIds(ids);
        contribution.setField(field);

        this.entityManager.persist(contribution);
    }

    /**
     * Helper method to find the user entity.
     *
     * @param id The user ID
     * @return The user
     */
    private UserEntity findUser(final Long id) {
        return this.userRepository
                .findByIdAndEnabledTrue(id)
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
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No movie found with id " + id));
    }

    /**
     * Helper method to find the movie entity.
     *
     * @param id The movie ID
     * @param status The movie status
     * @return The movie
     */
    private MovieEntity findMovie(final Long id, final EditStatus status) {
        return this.movieRepository
                .findByIdAndStatus(id, status)
                .orElseThrow(() -> new ResourceNotFoundException("No movie found with id " + id));
    }

    /**
     * Helper method to find the contribution entity.
     *
     * @param id The contribution ID
     * @return The contribution
     */
    private ContributionEntity findContribution(final Long id) {
        return this.contributionRepository
                .findByIdAndStatus(id, EditStatus.WAITING)
                .orElseThrow(() -> new ResourceNotFoundException("No contribution found with id " + id));
    }
}

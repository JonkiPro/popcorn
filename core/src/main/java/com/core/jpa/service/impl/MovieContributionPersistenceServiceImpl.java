package com.core.jpa.service.impl;

import com.common.dto.movie.*;
import com.common.dto.movie.request.*;
import com.common.exception.ResourceConflictException;
import com.common.exception.ResourceForbiddenException;
import com.common.exception.ResourceNotFoundException;
import com.core.jpa.entity.ContributionEntity;
import com.core.jpa.entity.MovieEntity;
import com.core.jpa.entity.UserEntity;
import com.core.jpa.entity.movie.*;
import com.core.jpa.entity.movie.MovieInfoEntity;
import com.core.jpa.repository.ContributionRepository;
import com.core.jpa.repository.MovieInfoRepository;
import com.core.jpa.repository.MovieRepository;
import com.core.jpa.repository.UserRepository;
import com.core.jpa.service.MovieContributionPersistenceService;
import com.core.jpa.service.MoviePersistenceService;
import com.core.movie.DataStatus;
import com.core.movie.MovieField;
import com.core.movie.VerificationStatus;
import com.google.common.collect.Iterables;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * JPA implementation of the Movie Contribution Persistence Service.
 */
@Service("movieContributionPersistenceService")
@Slf4j
@Transactional(
        rollbackFor = {
                ResourceForbiddenException.class,
                ResourceNotFoundException.class,
                ResourceConflictException.class,
                ConstraintViolationException.class
        }
)
@Validated
public class MovieContributionPersistenceServiceImpl implements MovieContributionPersistenceService {

    private final MovieRepository movieRepository;
    private final MovieInfoRepository movieInfoRepository;
    private final ContributionRepository contributionRepository;
    private final UserRepository userRepository;
    private final MoviePersistenceService moviePersistenceService;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Constructor.
     *
     * @param movieRepository The movie repository to use
     * @param movieInfoRepository The movie info repository to use
     * @param contributionRepository The contribution repository to use
     * @param userRepository The user repository to use
     * @param moviePersistenceService The movie persistence service to use
     */
    @Autowired
    public MovieContributionPersistenceServiceImpl(
            @NotNull final MovieRepository movieRepository,
            @NotNull final MovieInfoRepository movieInfoRepository,
            @NotNull final ContributionRepository contributionRepository,
            @NotNull final UserRepository userRepository,
            @NotNull final MoviePersistenceService moviePersistenceService
    ) {
        this.movieRepository = movieRepository;
        this.movieInfoRepository = movieInfoRepository;
        this.contributionRepository = contributionRepository;
        this.userRepository = userRepository;
        this.moviePersistenceService = moviePersistenceService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateContributionStatus(
            @Min(1) final Long contributionId,
            @Min(1) final Long userId,
            @NotNull final VerificationStatus status,
            final String comment
    ) throws ResourceForbiddenException, ResourceNotFoundException {
        log.info("Called with contributionId {}, userId {}, status {}, comment {}", contributionId, userId, status, comment);

        final UserEntity user = this.findUser(userId);
        final ContributionEntity contribution = this.findContribution(contributionId, DataStatus.WAITING);

        if(!CollectionUtils.containsAny(user.getPermissions(), contribution.getField().getNecessaryPermissions())) {
            throw new ResourceForbiddenException("No permissions");
        }

        contribution.setVerificationComment(comment);
        contribution.setVerificationDate(new Date());
        contribution.setVerificationUser(user);
        contribution.setStatus(status.getDataStatus());

        if(status == VerificationStatus.ACCEPT) {
            this.acceptContribution(contribution);
        } else if(status == VerificationStatus.REJECT) {
            this.rejectContribution(contribution);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long createOtherTitleContribution(
            @NotNull @Valid final ContributionNew<OtherTitle> contribution,
            @Min(1) final Long movieId,
            @Min(1) final Long userId
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with contribution {}, movieId {}, userId {}",
                contribution, movieId, userId);

        final UserEntity user = this.findUser(userId);
        final MovieEntity movie = this.findMovie(movieId, DataStatus.ACCEPTED);
        final List<MovieOtherTitleEntity> otherTitlesEntities = movie.getOtherTitles().stream()
                .filter(otherTitle -> otherTitle.getStatus() == DataStatus.ACCEPTED
                        && otherTitle.isReportedForUpdate() == false && otherTitle.isReportedForDelete() == false
                ).collect(Collectors.toList());
        final List<Long> idsToAdd = new ArrayList<>();
        final Map<Long, Long> idsToUpdate = new HashMap<>();

        this.validIds(otherTitlesEntities, contribution);

        if (CollectionUtils.containsAny(contribution.getElementsToUpdate().keySet(), contribution.getIdsToDelete())) {
            throw new ResourceConflictException("Conflict of operation on the same element");
        }

        contribution.getElementsToAdd()
                .forEach(otherTitle -> {
                    final Long id = this.moviePersistenceService.createOtherTitle(otherTitle, movie, user);
                    idsToAdd.add(id);
                });
        contribution.getElementsToUpdate()
                .forEach((key, value) -> {
                    final Long id = this.moviePersistenceService.createOtherTitle(value, movie, user);
                    this.setReportedForUpdate(otherTitlesEntities, key);
                    idsToUpdate.put(id, key);
                });
        contribution.getIdsToDelete()
                .forEach(id ->
                        this.setReportedForDelete(otherTitlesEntities, id)
                );

        return this.createContributions(idsToAdd, contribution.getIdsToDelete(), idsToUpdate, movie, user,
                MovieField.OTHER_TITLE, contribution.getSources(), contribution.getComment());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateOtherTitleContribution(
            @NotNull @Valid final ContributionUpdate<OtherTitle> contribution,
            @Min(1) final Long contributionId,
            @Min(1) final Long userId
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with contribution {}, contributionId {}, userId {}",
                contribution, contributionId, userId);

        final UserEntity user = this.findUser(userId);
        final ContributionEntity contributionEntity = this.findContribution(contributionId, DataStatus.WAITING, user, MovieField.OTHER_TITLE);

        this.validIds(contributionEntity, contribution);
        this.cleanUp(contributionEntity, contribution, contributionEntity.getMovie().getOtherTitles());

        contribution.getElementsToAdd().forEach((key, value) -> {
            this.moviePersistenceService.updateOtherTitle(value, key, contributionEntity.getMovie());
        });
        contribution.getElementsToUpdate().forEach((key, value) -> {
            this.moviePersistenceService.updateOtherTitle(value, key, contributionEntity.getMovie());
        });
        contribution.getNewElementsToAdd()
                .forEach(otherTitle -> {
                    final Long id = this.moviePersistenceService.createOtherTitle(otherTitle, contributionEntity.getMovie(), user);
                    contributionEntity.getIdsToAdd().add(id);
                });

        contributionEntity.setSources(contribution.getSources());
        Optional.ofNullable(contribution.getComment()).ifPresent(contributionEntity::setUserComment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long createReleaseDateContribution(
            @NotNull @Valid final ContributionNew<ReleaseDate> contribution,
            @Min(1) final Long movieId,
            @Min(1) final Long userId
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with contribution {}, movieId {}, userId {}",
                contribution, movieId, userId);

        final UserEntity user = this.findUser(userId);
        final MovieEntity movie = this.findMovie(movieId, DataStatus.ACCEPTED);
        final List<MovieReleaseDateEntity> releaseDatesEntities = movie.getReleaseDates().stream()
                .filter(releaseDate -> releaseDate.getStatus() == DataStatus.ACCEPTED
                        && releaseDate.isReportedForUpdate() == false && releaseDate.isReportedForDelete() == false
                ).collect(Collectors.toList());
        final List<Long> idsToAdd = new ArrayList<>();
        final Map<Long, Long> idsToUpdate = new HashMap<>();

        this.validIds(releaseDatesEntities, contribution);

        if (CollectionUtils.containsAny(contribution.getElementsToUpdate().keySet(), contribution.getIdsToDelete())) {
            throw new ResourceConflictException("Conflict of operation on the same element");
        }

        contribution.getElementsToAdd()
                .forEach(releaseDate -> {
                    final Long id = this.moviePersistenceService.createReleaseDate(releaseDate, movie, user);
                    idsToAdd.add(id);
                });
        contribution.getElementsToUpdate()
                .forEach((key, value) -> {
                    final Long id = this.moviePersistenceService.createReleaseDate(value, movie, user);
                    this.setReportedForUpdate(releaseDatesEntities, key);
                    idsToUpdate.put(id, key);
                });
        contribution.getIdsToDelete()
                .forEach(id ->
                        this.setReportedForDelete(releaseDatesEntities, id)
                );

        return this.createContributions(idsToAdd, contribution.getIdsToDelete(), idsToUpdate, movie, user,
                MovieField.RELEASE_DATE, contribution.getSources(), contribution.getComment());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateReleaseDateContribution(
            @NotNull @Valid final ContributionUpdate<ReleaseDate> contribution,
            @Min(1) final Long contributionId,
            @Min(1) final Long userId
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with contribution {}, contributionId {}, userId {}",
                contribution, contributionId, userId);

        final UserEntity user = this.findUser(userId);
        final ContributionEntity contributionEntity = this.findContribution(contributionId, DataStatus.WAITING, user, MovieField.RELEASE_DATE);

        this.validIds(contributionEntity, contribution);
        this.cleanUp(contributionEntity, contribution, contributionEntity.getMovie().getReleaseDates());

        contribution.getElementsToAdd().forEach((key, value) -> {
            this.moviePersistenceService.updateReleaseDate(value, key, contributionEntity.getMovie());
        });
        contribution.getElementsToUpdate().forEach((key, value) -> {
            this.moviePersistenceService.updateReleaseDate(value, key, contributionEntity.getMovie());
        });
        contribution.getNewElementsToAdd()
                .forEach(releaseDate -> {
                    final Long id = this.moviePersistenceService.createReleaseDate(releaseDate, contributionEntity.getMovie(), user);
                    contributionEntity.getIdsToAdd().add(id);
                });

        contributionEntity.setSources(contribution.getSources());
        Optional.ofNullable(contribution.getComment()).ifPresent(contributionEntity::setUserComment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long createStorylineContribution(
            @NotNull @Valid final ContributionNew<Storyline> contribution,
            @Min(1) final Long movieId,
            @Min(1) final Long userId
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with contribution {}, movieId {}, userId {}",
                contribution, movieId, userId);

        final UserEntity user = this.findUser(userId);
        final MovieEntity movie = this.findMovie(movieId, DataStatus.ACCEPTED);
        final List<MovieStorylineEntity> storylinesEntities = movie.getStorylines().stream()
                .filter(storyline -> storyline.getStatus() == DataStatus.ACCEPTED
                        && storyline.isReportedForUpdate() == false && storyline.isReportedForDelete() == false
                ).collect(Collectors.toList());
        final List<Long> idsToAdd = new ArrayList<>();
        final Map<Long, Long> idsToUpdate = new HashMap<>();

        this.validIds(storylinesEntities, contribution);

        if (CollectionUtils.containsAny(contribution.getElementsToUpdate().keySet(), contribution.getIdsToDelete())) {
            throw new ResourceConflictException("Conflict of operation on the same element");
        }

        contribution.getElementsToAdd()
                .forEach(storyline -> {
                    final Long id = this.moviePersistenceService.createStoryline(storyline, movie, user);
                    idsToAdd.add(id);
                });
        contribution.getElementsToUpdate()
                .forEach((key, value) -> {
                    final Long id = this.moviePersistenceService.createStoryline(value, movie, user);
                    this.setReportedForUpdate(storylinesEntities, key);
                    idsToUpdate.put(id, key);
                });
        contribution.getIdsToDelete()
                .forEach(id ->
                        this.setReportedForDelete(storylinesEntities, id)
                );

        return this.createContributions(idsToAdd, contribution.getIdsToDelete(), idsToUpdate, movie, user,
                MovieField.STORYLINE, contribution.getSources(), contribution.getComment());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateStorylineContribution(
            @NotNull @Valid final ContributionUpdate<Storyline> contribution,
            @Min(1) final Long contributionId,
            @Min(1) final Long userId
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with contribution {}, contributionId {}, userId {}",
                contribution, contributionId, userId);

        final UserEntity user = this.findUser(userId);
        final ContributionEntity contributionEntity = this.findContribution(contributionId, DataStatus.WAITING, user, MovieField.STORYLINE);

        this.validIds(contributionEntity, contribution);
        this.cleanUp(contributionEntity, contribution, contributionEntity.getMovie().getStorylines());

        contribution.getElementsToAdd().forEach((key, value) -> {
            this.moviePersistenceService.updateStoryline(value, key, contributionEntity.getMovie());
        });
        contribution.getElementsToUpdate().forEach((key, value) -> {
            this.moviePersistenceService.updateStoryline(value, key, contributionEntity.getMovie());
        });
        contribution.getNewElementsToAdd()
                .forEach(storyline -> {
                    final Long id = this.moviePersistenceService.createStoryline(storyline, contributionEntity.getMovie(), user);
                    contributionEntity.getIdsToAdd().add(id);
                });

        contributionEntity.setSources(contribution.getSources());
        Optional.ofNullable(contribution.getComment()).ifPresent(contributionEntity::setUserComment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long createBoxOfficeContribution(
            @NotNull @Valid final ContributionNew<BoxOffice> contribution,
            @Min(1) final Long movieId,
            @Min(1) final Long userId
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with contribution {}, movieId {}, userId {}",
                contribution, movieId, userId);

        final UserEntity user = this.findUser(userId);
        final MovieEntity movie = this.findMovie(movieId, DataStatus.ACCEPTED);
        final List<MovieBoxOfficeEntity> boxOfficesEntities = movie.getBoxOffices().stream()
                .filter(boxOffice -> boxOffice.getStatus() == DataStatus.ACCEPTED
                        && boxOffice.isReportedForUpdate() == false && boxOffice.isReportedForDelete() == false
                ).collect(Collectors.toList());
        final List<Long> idsToAdd = new ArrayList<>();
        final Map<Long, Long> idsToUpdate = new HashMap<>();

        this.validIds(boxOfficesEntities, contribution);

        if (CollectionUtils.containsAny(contribution.getElementsToUpdate().keySet(), contribution.getIdsToDelete())) {
            throw new ResourceConflictException("Conflict of operation on the same element");
        }

        contribution.getElementsToAdd()
                .forEach(boxOffice -> {
                    final Long id = this.moviePersistenceService.createBoxOffice(boxOffice, movie, user);
                    idsToAdd.add(id);
                });
        contribution.getElementsToUpdate()
                .forEach((key, value) -> {
                    final Long id = this.moviePersistenceService.createBoxOffice(value, movie, user);
                    this.setReportedForUpdate(boxOfficesEntities, key);
                    idsToUpdate.put(id, key);
                });
        contribution.getIdsToDelete()
                .forEach(id ->
                        this.setReportedForDelete(boxOfficesEntities, id)
                );

        return this.createContributions(idsToAdd, contribution.getIdsToDelete(), idsToUpdate, movie, user,
                MovieField.BOX_OFFICE, contribution.getSources(), contribution.getComment());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateBoxOfficeContribution(
            @NotNull @Valid final ContributionUpdate<BoxOffice> contribution,
            @Min(1) final Long contributionId,
            @Min(1) final Long userId
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with contribution {}, contributionId {}, userId {}",
                contribution, contributionId, userId);

        final UserEntity user = this.findUser(userId);
        final ContributionEntity contributionEntity = this.findContribution(contributionId, DataStatus.WAITING, user, MovieField.BOX_OFFICE);

        this.validIds(contributionEntity, contribution);
        this.cleanUp(contributionEntity, contribution, contributionEntity.getMovie().getBoxOffices());

        contribution.getElementsToAdd().forEach((key, value) -> {
            this.moviePersistenceService.updateBoxOffice(value, key, contributionEntity.getMovie());
        });
        contribution.getElementsToUpdate().forEach((key, value) -> {
            this.moviePersistenceService.updateBoxOffice(value, key, contributionEntity.getMovie());
        });
        contribution.getNewElementsToAdd()
                .forEach(boxOffice -> {
                    final Long id = this.moviePersistenceService.createBoxOffice(boxOffice, contributionEntity.getMovie(), user);
                    contributionEntity.getIdsToAdd().add(id);
                });

        contributionEntity.setSources(contribution.getSources());
        Optional.ofNullable(contribution.getComment()).ifPresent(contributionEntity::setUserComment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long createSiteContribution(
            @NotNull @Valid final ContributionNew<Site> contribution,
            @Min(1) final Long movieId,
            @Min(1) final Long userId
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with contribution {}, movieId {}, userId {}",
                contribution, movieId, userId);

        final UserEntity user = this.findUser(userId);
        final MovieEntity movie = this.findMovie(movieId, DataStatus.ACCEPTED);
        final List<MovieSiteEntity> sitesEntities = movie.getSites().stream()
                .filter(site -> site.getStatus() == DataStatus.ACCEPTED
                        && site.isReportedForUpdate() == false && site.isReportedForDelete() == false
                ).collect(Collectors.toList());
        final List<Long> idsToAdd = new ArrayList<>();
        final Map<Long, Long> idsToUpdate = new HashMap<>();

        this.validIds(sitesEntities, contribution);

        if (CollectionUtils.containsAny(contribution.getElementsToUpdate().keySet(), contribution.getIdsToDelete())) {
            throw new ResourceConflictException("Conflict of operation on the same element");
        }

        contribution.getElementsToAdd()
                .forEach(site -> {
                    final Long id = this.moviePersistenceService.createSite(site, movie, user);
                    idsToAdd.add(id);
                });
        contribution.getElementsToUpdate()
                .forEach((key, value) -> {
                    final Long id = this.moviePersistenceService.createSite(value, movie, user);
                    this.setReportedForUpdate(sitesEntities, key);
                    idsToUpdate.put(id, key);
                });
        contribution.getIdsToDelete()
                .forEach(id ->
                        this.setReportedForDelete(sitesEntities, id)
                );

        return this.createContributions(idsToAdd, contribution.getIdsToDelete(), idsToUpdate, movie, user,
                MovieField.SITE, contribution.getSources(), contribution.getComment());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateSiteContribution(
            @NotNull @Valid final ContributionUpdate<Site> contribution,
            @Min(1) final Long contributionId,
            @Min(1) final Long userId
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with contribution {}, contributionId {}, userId {}",
                contribution, contributionId, userId);

        final UserEntity user = this.findUser(userId);
        final ContributionEntity contributionEntity = this.findContribution(contributionId, DataStatus.WAITING, user, MovieField.SITE);

        this.validIds(contributionEntity, contribution);
        this.cleanUp(contributionEntity, contribution, contributionEntity.getMovie().getSites());

        contribution.getElementsToAdd().forEach((key, value) -> {
            this.moviePersistenceService.updateSite(value, key, contributionEntity.getMovie());
        });
        contribution.getElementsToUpdate().forEach((key, value) -> {
            this.moviePersistenceService.updateSite(value, key, contributionEntity.getMovie());
        });
        contribution.getNewElementsToAdd()
                .forEach(site -> {
                    final Long id = this.moviePersistenceService.createSite(site, contributionEntity.getMovie(), user);
                    contributionEntity.getIdsToAdd().add(id);
                });

        contributionEntity.setSources(contribution.getSources());
        Optional.ofNullable(contribution.getComment()).ifPresent(contributionEntity::setUserComment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long createCountryContribution(
            @NotNull @Valid final ContributionNew<Country> contribution,
            @Min(1) final Long movieId,
            @Min(1) final Long userId
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with contribution {}, movieId {}, userId {}",
                contribution, movieId, userId);

        final UserEntity user = this.findUser(userId);
        final MovieEntity movie = this.findMovie(movieId, DataStatus.ACCEPTED);
        final List<MovieCountryEntity> countriesEntities = movie.getCountries().stream()
                .filter(country -> country.getStatus() == DataStatus.ACCEPTED
                        && country.isReportedForUpdate() == false && country.isReportedForDelete() == false
                ).collect(Collectors.toList());
        final List<Long> idsToAdd = new ArrayList<>();
        final Map<Long, Long> idsToUpdate = new HashMap<>();

        this.validIds(countriesEntities, contribution);

        if (CollectionUtils.containsAny(contribution.getElementsToUpdate().keySet(), contribution.getIdsToDelete())) {
            throw new ResourceConflictException("Conflict of operation on the same element");
        }

        contribution.getElementsToAdd()
                .forEach(country -> {
                    final Long id = this.moviePersistenceService.createCountry(country, movie, user);
                    idsToAdd.add(id);
                });
        contribution.getElementsToUpdate()
                .forEach((key, value) -> {
                    final Long id = this.moviePersistenceService.createCountry(value, movie, user);
                    this.setReportedForUpdate(countriesEntities, key);
                    idsToUpdate.put(id, key);
                });
        contribution.getIdsToDelete()
                .forEach(id ->
                        this.setReportedForDelete(countriesEntities, id)
                );

        return this.createContributions(idsToAdd, contribution.getIdsToDelete(), idsToUpdate, movie, user,
                MovieField.COUNTRY, contribution.getSources(), contribution.getComment());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateCountryContribution(
            @NotNull @Valid final ContributionUpdate<Country> contribution,
            @Min(1) final Long contributionId,
            @Min(1) final Long userId
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with contribution {}, contributionId {}, userId {}",
                contribution, contributionId, userId);

        final UserEntity user = this.findUser(userId);
        final ContributionEntity contributionEntity = this.findContribution(contributionId, DataStatus.WAITING, user, MovieField.COUNTRY);

        this.validIds(contributionEntity, contribution);
        this.cleanUp(contributionEntity, contribution, contributionEntity.getMovie().getCountries());

        contribution.getElementsToAdd().forEach((key, value) -> {
            this.moviePersistenceService.updateCountry(value, key, contributionEntity.getMovie());
        });
        contribution.getElementsToUpdate().forEach((key, value) -> {
            this.moviePersistenceService.updateCountry(value, key, contributionEntity.getMovie());
        });
        contribution.getNewElementsToAdd()
                .forEach(country -> {
                    final Long id = this.moviePersistenceService.createCountry(country, contributionEntity.getMovie(), user);
                    contributionEntity.getIdsToAdd().add(id);
                });

        contributionEntity.setSources(contribution.getSources());
        Optional.ofNullable(contribution.getComment()).ifPresent(contributionEntity::setUserComment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long createLanguageContribution(
            @NotNull @Valid final ContributionNew<Language> contribution,
            @Min(1) final Long movieId,
            @Min(1) final Long userId
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with contribution {}, movieId {}, userId {}",
                contribution, movieId, userId);

        final UserEntity user = this.findUser(userId);
        final MovieEntity movie = this.findMovie(movieId, DataStatus.ACCEPTED);
        final List<MovieLanguageEntity> languagesEntities = movie.getLanguages().stream()
                .filter(language -> language.getStatus() == DataStatus.ACCEPTED
                        && language.isReportedForUpdate() == false && language.isReportedForDelete() == false
                ).collect(Collectors.toList());
        final List<Long> idsToAdd = new ArrayList<>();
        final Map<Long, Long> idsToUpdate = new HashMap<>();

        this.validIds(languagesEntities, contribution);

        if (CollectionUtils.containsAny(contribution.getElementsToUpdate().keySet(), contribution.getIdsToDelete())) {
            throw new ResourceConflictException("Conflict of operation on the same element");
        }

        contribution.getElementsToAdd()
                .forEach(language -> {
                    final Long id = this.moviePersistenceService.createLanguage(language, movie, user);
                    idsToAdd.add(id);
                });
        contribution.getElementsToUpdate()
                .forEach((key, value) -> {
                    final Long id = this.moviePersistenceService.createLanguage(value, movie, user);
                    this.setReportedForUpdate(languagesEntities, key);
                    idsToUpdate.put(id, key);
                });
        contribution.getIdsToDelete()
                .forEach(id ->
                        this.setReportedForDelete(languagesEntities, id)
                );

        return this.createContributions(idsToAdd, contribution.getIdsToDelete(), idsToUpdate, movie, user,
                MovieField.LANGUAGE, contribution.getSources(), contribution.getComment());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateLanguageContribution(
            @NotNull @Valid final ContributionUpdate<Language> contribution,
            @Min(1) final Long contributionId,
            @Min(1) final Long userId
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with contribution {}, contributionId {}, userId {}",
                contribution, contributionId, userId);

        final UserEntity user = this.findUser(userId);
        final ContributionEntity contributionEntity = this.findContribution(contributionId, DataStatus.WAITING, user, MovieField.LANGUAGE);

        this.validIds(contributionEntity, contribution);
        this.cleanUp(contributionEntity, contribution, contributionEntity.getMovie().getLanguages());

        contribution.getElementsToAdd().forEach((key, value) -> {
            this.moviePersistenceService.updateLanguage(value, key, contributionEntity.getMovie());
        });
        contribution.getElementsToUpdate().forEach((key, value) -> {
            this.moviePersistenceService.updateLanguage(value, key, contributionEntity.getMovie());
        });
        contribution.getNewElementsToAdd()
                .forEach(language -> {
                    final Long id = this.moviePersistenceService.createLanguage(language, contributionEntity.getMovie(), user);
                    contributionEntity.getIdsToAdd().add(id);
                });

        contributionEntity.setSources(contribution.getSources());
        Optional.ofNullable(contribution.getComment()).ifPresent(contributionEntity::setUserComment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long createGenreContribution(
            @NotNull @Valid final ContributionNew<Genre> contribution,
            @Min(1) final Long movieId,
            @Min(1) final Long userId
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with contribution {}, movieId {}, userId {}",
                contribution, movieId, userId);

        final UserEntity user = this.findUser(userId);
        final MovieEntity movie = this.findMovie(movieId, DataStatus.ACCEPTED);
        final List<MovieGenreEntity> genresEntities = movie.getGenres().stream()
                .filter(genre -> genre.getStatus() == DataStatus.ACCEPTED
                        && genre.isReportedForUpdate() == false && genre.isReportedForDelete() == false
                ).collect(Collectors.toList());
        final List<Long> idsToAdd = new ArrayList<>();
        final Map<Long, Long> idsToUpdate = new HashMap<>();

        this.validIds(genresEntities, contribution);

        if (CollectionUtils.containsAny(contribution.getElementsToUpdate().keySet(), contribution.getIdsToDelete())) {
            throw new ResourceConflictException("Conflict of operation on the same element");
        }

        contribution.getElementsToAdd()
                .forEach(genre -> {
                    final Long id = this.moviePersistenceService.createGenre(genre, movie, user);
                    idsToAdd.add(id);
                });
        contribution.getElementsToUpdate()
                .forEach((key, value) -> {
                    final Long id = this.moviePersistenceService.createGenre(value, movie, user);
                    this.setReportedForUpdate(genresEntities, key);
                    idsToUpdate.put(id, key);
                });
        contribution.getIdsToDelete()
                .forEach(id ->
                        this.setReportedForDelete(genresEntities, id)
                );

        return this.createContributions(idsToAdd, contribution.getIdsToDelete(), idsToUpdate, movie, user,
                MovieField.GENRE, contribution.getSources(), contribution.getComment());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateGenreContribution(
            @NotNull @Valid final ContributionUpdate<Genre> contribution,
            @Min(1) final Long contributionId,
            @Min(1) final Long userId
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with contribution {}, contributionId {}, userId {}",
                contribution, contributionId, userId);

        final UserEntity user = this.findUser(userId);
        final ContributionEntity contributionEntity = this.findContribution(contributionId, DataStatus.WAITING, user, MovieField.GENRE);

        this.validIds(contributionEntity, contribution);
        this.cleanUp(contributionEntity, contribution, contributionEntity.getMovie().getGenres());

        contribution.getElementsToAdd().forEach((key, value) -> {
            this.moviePersistenceService.updateGenre(value, key, contributionEntity.getMovie());
        });
        contribution.getElementsToUpdate().forEach((key, value) -> {
            this.moviePersistenceService.updateGenre(value, key, contributionEntity.getMovie());
        });
        contribution.getNewElementsToAdd()
                .forEach(genre -> {
                    final Long id = this.moviePersistenceService.createGenre(genre, contributionEntity.getMovie(), user);
                    contributionEntity.getIdsToAdd().add(id);
                });

        contributionEntity.setSources(contribution.getSources());
        Optional.ofNullable(contribution.getComment()).ifPresent(contributionEntity::setUserComment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long createReviewContribution(
            @NotNull @Valid final ContributionNew<Review> contribution,
            @Min(1) final Long movieId,
            @Min(1) final Long userId
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with contribution {}, movieId {}, userId {}",
                contribution, movieId, userId);

        final UserEntity user = this.findUser(userId);
        final MovieEntity movie = this.findMovie(movieId, DataStatus.ACCEPTED);
        final List<MovieReviewEntity> reviewsEntities = movie.getReviews().stream()
                .filter(review -> review.getStatus() == DataStatus.ACCEPTED
                        && review.isReportedForUpdate() == false && review.isReportedForDelete() == false
                ).collect(Collectors.toList());
        final List<Long> idsToAdd = new ArrayList<>();
        final Map<Long, Long> idsToUpdate = new HashMap<>();

        this.validIds(reviewsEntities, contribution);

        if (CollectionUtils.containsAny(contribution.getElementsToUpdate().keySet(), contribution.getIdsToDelete())) {
            throw new ResourceConflictException("Conflict of operation on the same element");
        }

        contribution.getElementsToAdd()
                .forEach(review -> {
                    final Long id = this.moviePersistenceService.createReview(review, movie, user);
                    idsToAdd.add(id);
                });
        contribution.getElementsToUpdate()
                .forEach((key, value) -> {
                    final Long id = this.moviePersistenceService.createReview(value, movie, user);
                    this.setReportedForUpdate(reviewsEntities, key);
                    idsToUpdate.put(id, key);
                });
        contribution.getIdsToDelete()
                .forEach(id ->
                        this.setReportedForDelete(reviewsEntities, id)
                );

        return this.createContributions(idsToAdd, contribution.getIdsToDelete(), idsToUpdate, movie, user,
                MovieField.REVIEW, contribution.getSources(), contribution.getComment());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateReviewContribution(
            @NotNull @Valid final ContributionUpdate<Review> contribution,
            @Min(1) final Long contributionId,
            @Min(1) final Long userId
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with contribution {}, contributionId {}, userId {}",
                contribution, contributionId, userId);

        final UserEntity user = this.findUser(userId);
        final ContributionEntity contributionEntity = this.findContribution(contributionId, DataStatus.WAITING, user, MovieField.REVIEW);

        this.validIds(contributionEntity, contribution);
        this.cleanUp(contributionEntity, contribution, contributionEntity.getMovie().getReviews());

        contribution.getElementsToAdd().forEach((key, value) -> {
            this.moviePersistenceService.updateReview(value, key, contributionEntity.getMovie());
        });
        contribution.getElementsToUpdate().forEach((key, value) -> {
            this.moviePersistenceService.updateReview(value, key, contributionEntity.getMovie());
        });
        contribution.getNewElementsToAdd()
                .forEach(review -> {
                    final Long id = this.moviePersistenceService.createReview(review, contributionEntity.getMovie(), user);
                    contributionEntity.getIdsToAdd().add(id);
                });

        contributionEntity.setSources(contribution.getSources());
        Optional.ofNullable(contribution.getComment()).ifPresent(contributionEntity::setUserComment);
    }

    /**
     * Save the contribution.
     *
     * @param idsToAdd List of IDs to add
     * @param idsToDelete List of IDs to delete
     * @param idsToUpdate Map of IDs to update
     * @param movie The MovieEntity object
     * @param user The UserEntity object
     * @param field The movie field
     * @param sources Sources for contribution
     * @param comment Comment for contribution
     * @return The ID of the contribution created
     */
    private Long createContributions(
            final List<Long> idsToAdd,
            final List<Long> idsToDelete,
            final Map<Long, Long> idsToUpdate,
            final MovieEntity movie,
            final UserEntity user,
            final MovieField field,
            final Set<String> sources,
            final String comment
    ) {
        final ContributionEntity contribution = new ContributionEntity();
        contribution.setMovie(movie);
        contribution.setUser(user);
        if(!idsToAdd.isEmpty()) { contribution.setIdsToAdd(idsToAdd); }
        if(!idsToDelete.isEmpty()) { contribution.setIdsToDelete(idsToDelete); }
        if(!idsToUpdate.isEmpty()) { contribution.setIdsToUpdate(idsToUpdate); }
        contribution.setField(field);
        contribution.setSources(sources);
        Optional.ofNullable(comment).ifPresent(contribution::setUserComment);

        movie.getContributions().add(contribution);
        this.movieRepository.save(movie);

        return Iterables.getLast(movie.getContributions()).getId();
    }

    /**
     * Helper method to find the user entity.
     *
     * @param id The user ID
     * @return The user
     * @throws ResourceNotFoundException if no movie found
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
     * Helper method to find the contribution entity.
     *
     * @param id The contribution ID
     * @param status The contribution status
     * @return The contribution
     * @throws ResourceNotFoundException if no contribution found
     */
    private ContributionEntity findContribution(final Long id, final DataStatus status) throws ResourceNotFoundException {
        return this.contributionRepository
                .findByIdAndStatus(id, status)
                .orElseThrow(() -> new ResourceNotFoundException("No contribution found with id " + id));
    }

    /**
     * Helper method to find the contribution entity.
     *
     * @param id The contribution ID
     * @param status The contribution status
     * @param user The user
     * @param field The movie field
     * @return The contribution
     * @throws ResourceNotFoundException if no contribution found
     */
    private ContributionEntity findContribution(final Long id, final DataStatus status, final UserEntity user, final MovieField field)
            throws ResourceNotFoundException{
        return this.contributionRepository
                .findByIdAndStatusAndUserAndField(id, status, user, field)
                .orElseThrow(() -> new ResourceNotFoundException("No contribution found with id " + id));
    }

    /**
     * Helper method for comparing IDs.
     *
     * @param entities List of entities(MovieInfoEntity)
     * @param contributionNew The ContributionNew object
     * @throws ResourceNotFoundException if no element found
     */
    private void validIds(final List<? extends MovieInfoEntity> entities, final ContributionNew<? extends MovieInfoDTO> contributionNew)
            throws ResourceNotFoundException{
        this.validIds(entities, contributionNew.getElementsToUpdate().keySet());
        this.validIds(entities, contributionNew.getIdsToDelete());
    }

    /**
     * Helper method for comparing IDs from collection ids in list of IDs from entities.
     *
     * @param entities List of entities(MovieInfoEntity)
     * @param ids Collection of IDs
     * @throws ResourceNotFoundException if no element found
     */
    private void validIds(final List<? extends MovieInfoEntity> entities, final Collection<Long> ids)
            throws ResourceNotFoundException {
        if(!entities.isEmpty() && !ids.isEmpty()) {
            final List<Long> entitiesIds = new ArrayList<>();
            for (final MovieInfoEntity entity : entities) {
                entitiesIds.add(entity.getId());
            }

            for (final Long id : ids) {
                if (!entitiesIds.contains(id)) {
                    throw new ResourceNotFoundException("No element found with id " + id);
                }
            }
        } else if(entities.isEmpty() && !ids.isEmpty()) {
            throw new ResourceNotFoundException("No elements found for editing or deletion");
        }
    }

    /**
     * Helper method for comparing IDs.
     *
     * @param contributionEntity The ContributionEntity object
     * @param contributionUpdate The ContributionUpdate object
     * @throws ResourceNotFoundException if no element found
     */
    private void validIds(final ContributionEntity contributionEntity, final ContributionUpdate<? extends MovieInfoDTO> contributionUpdate)
            throws ResourceNotFoundException {
        this.validIds(contributionEntity.getIdsToAdd(), contributionUpdate.getElementsToAdd().keySet());
        this.validIds(contributionEntity.getIdsToUpdate().keySet(), contributionUpdate.getElementsToUpdate().keySet());
        this.validIds(contributionEntity.getIdsToDelete(), contributionUpdate.getIdsToDelete());
    }

    /**
     * Helper method for comparing IDs from collection collIds2 in collection collIds.
     *
     * @param collIds Collection of IDs
     * @param collIds2 Collection of IDs
     * @throws ResourceNotFoundException if no element found
     */
    private void validIds(final Collection<Long> collIds, final Collection<Long> collIds2)
            throws ResourceNotFoundException {
        collIds2
                .forEach(id -> {
                    if(!collIds.contains(id)) {
                        throw new ResourceNotFoundException("No element found with id " + id);
                    }
                });
    }

    /**
     * Helper method for reporting an element for deletion.
     *
     * @param entities List of entities(MovieInfoEntity)
     * @param id The MovieInfoEntity object ID
     */
    private void setReportedForDelete(final List<? extends MovieInfoEntity> entities, final Long id) {
        entities.forEach(entity -> {
            if(entity.getId().equals(id)) {
                entity.setReportedForDelete(true);
            }
        });
    }

    /**
     * Helper method for reporting an element for updated.
     *
     * @param entities List of entities(MovieInfoEntity)
     * @param id The MovieInfoEntity object ID
     */
    private void setReportedForUpdate(final List<? extends MovieInfoEntity> entities, final Long id) {
        entities.forEach(entity -> {
            if(entity.getId().equals(id)) {
                entity.setReportedForUpdate(true);
            }
        });
    }

    /**
     * Helper method to clean the list of element IDs.
     *
     * @param contributionEntity The ContributionEntity object
     * @param contributionUpdate The ContributionUpdate object
     * @param entities List of entities(MovieInfoEntity)
     */
    private void cleanUp(final ContributionEntity contributionEntity, final ContributionUpdate<? extends MovieInfoDTO> contributionUpdate,
                         final List<? extends MovieInfoEntity> entities) {
        this.cleanUpIdsToAdd(contributionEntity.getIdsToAdd(), contributionUpdate.getElementsToAdd().keySet(), entities);
        this.cleanUpIdsToUpdate(contributionEntity.getIdsToUpdate(), contributionUpdate.getElementsToUpdate().keySet());
        this.cleanUpIdsToDelete(contributionEntity.getIdsToDelete(), contributionUpdate.getIdsToDelete());
    }

    /**
     * Helper method to clean the list of element IDs to be added.
     *
     * @param idsToAddFromEntity List of element IDs to be added. List from the entity
     * @param idsToAddFromDto List of element IDs to be added. List from the DTO
     * @param entities List of entities(MovieInfoEntity)
     */
    private void cleanUpIdsToAdd(final List<Long> idsToAddFromEntity, final Set<Long> idsToAddFromDto,
                                 final List<? extends MovieInfoEntity> entities) {
        for (final Iterator<Long> it = idsToAddFromEntity.iterator(); it.hasNext(); ) {
            final Long id = it.next();
            if (!idsToAddFromDto.contains(id)) {
                it.remove();
                this.delete(entities, id);
            }
        }
    }

    /**
     * Helper method to clean the list of element IDs to be updated.
     *
     * @param idsToUpdateFromEntity List of element IDs to be updated. List from the entity
     * @param idsToUpdateFromDto List of element IDs to be updated. List from the DTO
     */
    private void cleanUpIdsToUpdate(final Map<Long, Long> idsToUpdateFromEntity, final Set<Long> idsToUpdateFromDto) {
        for (final Iterator<Map.Entry<Long, Long>> it = idsToUpdateFromEntity.entrySet().iterator(); it.hasNext(); ) {
            if (!idsToUpdateFromDto.contains(it.next().getKey())) {
                final MovieInfoEntity oldElement = this.movieInfoRepository.findOne(it.next().getValue());
                final MovieInfoEntity newElement = this.movieInfoRepository.findOne(it.next().getKey());

                it.remove();
                this.entityManager.remove(newElement);
                oldElement.setReportedForUpdate(false);
            }
        }
    }

    /**
     * Helper method to clean the list of element IDs to be deleted.
     *
     * @param idsToDeleteFromEntity List of element IDs to be deleted. List from the entity
     * @param idsToDeleteFromDto List of element IDs to be deleted. List from the DTO
     */
    private void cleanUpIdsToDelete(final List<Long> idsToDeleteFromEntity, final List<Long> idsToDeleteFromDto) {
        for(final Iterator<Long> it = idsToDeleteFromEntity.iterator(); it.hasNext();) {
            final Long id = it.next();
            if(!idsToDeleteFromDto.contains(id)) {
                this.movieInfoRepository.findOne(id).setReportedForDelete(false);
                it.remove();
            }
        }
    }

    /**
     * Helper method to delete the MovieInfoEntity entity by ID.
     *
     * @param entities List of entities(MovieInfoEntity)
     * @param id The MovieInfoEntity object ID
     */
    private void delete(final List<? extends MovieInfoEntity> entities, final Long id) {
        entities.removeIf(movieInfo -> movieInfo.getId().equals(id));
    }

    /**
     * Accept the contribution.
     *
     * @param contribution The ContributionEntity object
     */
    private void acceptContribution(final ContributionEntity contribution) {
        contribution.getIdsToAdd().forEach(id -> {
            final MovieInfoEntity element = this.movieInfoRepository.findOne(id);
            element.setStatus(DataStatus.ACCEPTED);
        });
        contribution.getIdsToDelete().forEach(id -> {
            final MovieInfoEntity element = this.movieInfoRepository.findOne(id);
            element.setStatus(DataStatus.DELETED);
        });
        if(contribution.getField() == MovieField.STORYLINE
                || contribution.getField() == MovieField.REVIEW) {
            this.updatePatch(contribution);
        } else {
            contribution.getIdsToUpdate().forEach((key, value) -> {
                final MovieInfoEntity newElement = this.movieInfoRepository.findOne(key);
                final MovieInfoEntity oldElement = this.movieInfoRepository.findOne(value);

                newElement.setStatus(DataStatus.ACCEPTED);
                oldElement.setStatus(DataStatus.EDITED);
            });
        }
    }

    /**
     * Reject the contribution.
     *
     * @param contribution The ContributionEntity object
     */
    private void rejectContribution(final ContributionEntity contribution) {
        contribution.getIdsToAdd().forEach(id -> {
            final MovieInfoEntity element = this.movieInfoRepository.findOne(id);
            element.setStatus(DataStatus.REJECTED);
        });
        contribution.getIdsToDelete().forEach(id -> {
            final MovieInfoEntity element = this.movieInfoRepository.findOne(id);
            element.setReportedForDelete(false);
        });
        contribution.getIdsToUpdate().forEach((key, value) -> {
            final MovieInfoEntity newElement = this.movieInfoRepository.findOne(key);
            final MovieInfoEntity oldElement = this.movieInfoRepository.findOne(value);

            newElement.setStatus(DataStatus.REJECTED);
            oldElement.setReportedForUpdate(false);
        });
    }

    /**
     * Helper method to update storyline and review.
     *
     * @param contribution The ContributionEntity object
     * @throws ResourceConflictException if the element exists
     */
    private void updatePatch(final ContributionEntity contribution) throws ResourceConflictException {
        if(contribution.getField() == MovieField.STORYLINE) {
            contribution.getIdsToUpdate().forEach((key, value) -> {
                final MovieStorylineEntity newStoryline = this.entityManager.find(MovieStorylineEntity.class, key);
                final MovieStorylineEntity oldStoryline = this.entityManager.find(MovieStorylineEntity.class, value);

                this.moviePersistenceService.updateStoryline(newStoryline.getDTO(), value, contribution.getMovie());

                oldStoryline.setReportedForUpdate(false);
                newStoryline.setStatus(DataStatus.AMENDMENT_ACCEPTED);
            });
        } else if(contribution.getField() == MovieField.REVIEW) {
            contribution.getIdsToUpdate().forEach((key, value) -> {
                final MovieReviewEntity newReview = this.entityManager.find(MovieReviewEntity.class, key);
                final MovieReviewEntity oldReview = this.entityManager.find(MovieReviewEntity.class, value);

                this.moviePersistenceService.updateReview(newReview.getDTO(), value, contribution.getMovie());

                oldReview.setReportedForUpdate(false);
                newReview.setStatus(DataStatus.AMENDMENT_ACCEPTED);
            });
        }
    }
}

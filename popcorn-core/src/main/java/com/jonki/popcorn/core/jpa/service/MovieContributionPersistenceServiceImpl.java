package com.jonki.popcorn.core.jpa.service;

import com.google.common.collect.Iterables;
import com.jonki.popcorn.common.dto.DataStatus;
import com.jonki.popcorn.common.dto.MovieField;
import com.jonki.popcorn.common.dto.StorageDirectory;
import com.jonki.popcorn.common.dto.VerificationStatus;
import com.jonki.popcorn.common.dto.movie.BoxOffice;
import com.jonki.popcorn.common.dto.movie.Country;
import com.jonki.popcorn.common.dto.movie.Genre;
import com.jonki.popcorn.common.dto.movie.Language;
import com.jonki.popcorn.common.dto.movie.MovieInfoDTO;
import com.jonki.popcorn.common.dto.movie.OtherTitle;
import com.jonki.popcorn.common.dto.movie.Outline;
import com.jonki.popcorn.common.dto.movie.ReleaseDate;
import com.jonki.popcorn.common.dto.movie.Review;
import com.jonki.popcorn.common.dto.movie.Site;
import com.jonki.popcorn.common.dto.movie.Summary;
import com.jonki.popcorn.common.dto.movie.Synopsis;
import com.jonki.popcorn.common.dto.movie.request.ContributionNew;
import com.jonki.popcorn.common.dto.movie.request.ContributionUpdate;
import com.jonki.popcorn.common.dto.movie.request.ImageRequest;
import com.jonki.popcorn.common.exception.ResourceConflictException;
import com.jonki.popcorn.common.exception.ResourceForbiddenException;
import com.jonki.popcorn.common.exception.ResourceNotFoundException;
import com.jonki.popcorn.common.exception.ResourcePreconditionException;
import com.jonki.popcorn.common.exception.ResourceServerException;
import com.jonki.popcorn.core.jpa.entity.ContributionEntity;
import com.jonki.popcorn.core.jpa.entity.MovieEntity;
import com.jonki.popcorn.core.jpa.entity.UserEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieBoxOfficeEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieCountryEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieFileEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieGenreEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieInfoEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieLanguageEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieOtherTitleEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieOutlineEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MoviePhotoEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MoviePosterEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieReleaseDateEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieReviewEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieSiteEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieSummaryEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieSynopsisEntity;
import com.jonki.popcorn.core.jpa.repository.ContributionRepository;
import com.jonki.popcorn.core.jpa.repository.MovieInfoRepository;
import com.jonki.popcorn.core.jpa.repository.MovieRepository;
import com.jonki.popcorn.core.jpa.repository.UserRepository;
import com.jonki.popcorn.core.service.AuthorizationService;
import com.jonki.popcorn.core.service.MovieContributionPersistenceService;
import com.jonki.popcorn.core.service.MoviePersistenceService;
import com.jonki.popcorn.core.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
                ResourcePreconditionException.class,
                ResourceServerException.class,
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
    private final StorageService storageService;
    private final AuthorizationService authorizationServicea;

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
     * @param storageService The storage service to use
     * @param authorizationService The authorization service to use
     */
    @Autowired
    public MovieContributionPersistenceServiceImpl(
            @NotNull final MovieRepository movieRepository,
            @NotNull final MovieInfoRepository movieInfoRepository,
            @NotNull final ContributionRepository contributionRepository,
            @NotNull final UserRepository userRepository,
            @NotNull final MoviePersistenceService moviePersistenceService,
            @Qualifier("googleStorageService") @NotNull final StorageService storageService,
            @NotNull final AuthorizationService authorizationService
    ) {
        this.movieRepository = movieRepository;
        this.movieInfoRepository = movieInfoRepository;
        this.contributionRepository = contributionRepository;
        this.userRepository = userRepository;
        this.moviePersistenceService = moviePersistenceService;
        this.storageService = storageService;
        this.authorizationServicea = authorizationService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateContributionStatus(
            @Min(1) final Long id,
            @NotNull final VerificationStatus status,
            final String comment
    ) throws ResourceForbiddenException, ResourceNotFoundException {
        log.info("Called with id {}, status {}, comment {}", id, status, comment);

        final UserEntity user = this.findUser(this.authorizationServicea.getUserId());
        final ContributionEntity contribution = this.findContribution(id, DataStatus.WAITING);

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
            @Min(1) final Long movieId,
            @NotNull @Valid final ContributionNew<OtherTitle> contribution
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with movieId {}, contribution {}",
                movieId, contribution);

        final UserEntity user = this.findUser(this.authorizationServicea.getUserId());
        final MovieEntity movie = this.findMovie(movieId, DataStatus.ACCEPTED);
        final List<MovieOtherTitleEntity> otherTitlesEntities = movie.getOtherTitles().stream()
                .filter(otherTitle -> otherTitle.getStatus() == DataStatus.ACCEPTED
                        && !otherTitle.isReportedForUpdate() && !otherTitle.isReportedForDelete()
                ).collect(Collectors.toList());
        final Set<Long> idsToAdd = new HashSet<>();
        final Map<Long, Long> idsToUpdate = new HashMap<>();

        this.validIds(otherTitlesEntities, contribution);

        if (CollectionUtils.containsAny(contribution.getElementsToUpdate().keySet(), contribution.getIdsToDelete())) {
            throw new ResourceConflictException("Conflict of operation on the same element");
        }

        contribution.getElementsToAdd()
                .forEach(otherTitle -> {
                    final Long id = this.moviePersistenceService.createOtherTitle(otherTitle, movie);
                    idsToAdd.add(id);
                });
        contribution.getElementsToUpdate()
                .forEach((key, value) -> {
                    final Long id = this.moviePersistenceService.createOtherTitle(value, movie);
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
            @Min(1) final Long contributionId,
            @NotNull @Valid final ContributionUpdate<OtherTitle> contribution
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with contributionId {}, contribution {}",
                contributionId, contribution);

        final UserEntity user = this.findUser(this.authorizationServicea.getUserId());
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
                    final Long id = this.moviePersistenceService.createOtherTitle(otherTitle, contributionEntity.getMovie());
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
            @Min(1) final Long movieId,
            @NotNull @Valid final ContributionNew<ReleaseDate> contribution
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with movieId {}, contribution {}",
                movieId, contribution);

        final UserEntity user = this.findUser(this.authorizationServicea.getUserId());
        final MovieEntity movie = this.findMovie(movieId, DataStatus.ACCEPTED);
        final List<MovieReleaseDateEntity> releaseDatesEntities = movie.getReleaseDates().stream()
                .filter(releaseDate -> releaseDate.getStatus() == DataStatus.ACCEPTED
                        && !releaseDate.isReportedForUpdate() && !releaseDate.isReportedForDelete()
                ).collect(Collectors.toList());
        final Set<Long> idsToAdd = new HashSet<>();
        final Map<Long, Long> idsToUpdate = new HashMap<>();

        this.validIds(releaseDatesEntities, contribution);

        if (CollectionUtils.containsAny(contribution.getElementsToUpdate().keySet(), contribution.getIdsToDelete())) {
            throw new ResourceConflictException("Conflict of operation on the same element");
        }

        contribution.getElementsToAdd()
                .forEach(releaseDate -> {
                    final Long id = this.moviePersistenceService.createReleaseDate(releaseDate, movie);
                    idsToAdd.add(id);
                });
        contribution.getElementsToUpdate()
                .forEach((key, value) -> {
                    final Long id = this.moviePersistenceService.createReleaseDate(value, movie);
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
            @Min(1) final Long contributionId,
            @NotNull @Valid final ContributionUpdate<ReleaseDate> contribution
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with contributionId {}, contribution {}",
                contributionId, contribution);

        final UserEntity user = this.findUser(this.authorizationServicea.getUserId());
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
                    final Long id = this.moviePersistenceService.createReleaseDate(releaseDate, contributionEntity.getMovie());
                    contributionEntity.getIdsToAdd().add(id);
                });

        contributionEntity.setSources(contribution.getSources());
        Optional.ofNullable(contribution.getComment()).ifPresent(contributionEntity::setUserComment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long createOutlineContribution(
            @Min(1) final Long movieId,
            @NotNull @Valid final ContributionNew<Outline> contribution
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with movieId {}, contribution {}",
                movieId, contribution);

        final UserEntity user = this.findUser(this.authorizationServicea.getUserId());
        final MovieEntity movie = this.findMovie(movieId, DataStatus.ACCEPTED);
        final List<MovieOutlineEntity> outlineEntities = movie.getOutlines().stream()
                .filter(outline -> outline.getStatus() == DataStatus.ACCEPTED
                        && !outline.isReportedForUpdate() && !outline.isReportedForDelete()
                ).collect(Collectors.toList());
        final Set<Long> idsToAdd = new HashSet<>();
        final Map<Long, Long> idsToUpdate = new HashMap<>();

        this.validIds(outlineEntities, contribution);

        if (CollectionUtils.containsAny(contribution.getElementsToUpdate().keySet(), contribution.getIdsToDelete())) {
            throw new ResourceConflictException("Conflict of operation on the same element");
        }

        contribution.getElementsToAdd()
                .forEach(outline -> {
                    final Long id = this.moviePersistenceService.createOutline(outline, movie);
                    idsToAdd.add(id);
                });
        contribution.getElementsToUpdate()
                .forEach((key, value) -> {
                    final Long id = this.moviePersistenceService.createOutline(value, movie);
                    this.setReportedForUpdate(outlineEntities, key);
                    idsToUpdate.put(id, key);
                });
        contribution.getIdsToDelete()
                .forEach(id ->
                        this.setReportedForDelete(outlineEntities, id)
                );

        return this.createContributions(idsToAdd, contribution.getIdsToDelete(), idsToUpdate, movie, user,
                MovieField.OUTLINE, contribution.getSources(), contribution.getComment());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateOutlineContribution(
            @Min(1) final Long contributionId,
            @NotNull @Valid final ContributionUpdate<Outline> contribution
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with contributionId {}, contribution {}",
                contributionId, contribution);

        final UserEntity user = this.findUser(this.authorizationServicea.getUserId());
        final ContributionEntity contributionEntity = this.findContribution(contributionId, DataStatus.WAITING, user, MovieField.OUTLINE);

        this.validIds(contributionEntity, contribution);
        this.cleanUp(contributionEntity, contribution, contributionEntity.getMovie().getOutlines());

        contribution.getElementsToAdd().forEach((key, value) -> {
            this.moviePersistenceService.updateOutline(value, key, contributionEntity.getMovie());
        });
        contribution.getElementsToUpdate().forEach((key, value) -> {
            this.moviePersistenceService.updateOutline(value, key, contributionEntity.getMovie());
        });
        contribution.getNewElementsToAdd()
                .forEach(outline -> {
                    final Long id = this.moviePersistenceService.createOutline(outline, contributionEntity.getMovie());
                    contributionEntity.getIdsToAdd().add(id);
                });

        contributionEntity.setSources(contribution.getSources());
        Optional.ofNullable(contribution.getComment()).ifPresent(contributionEntity::setUserComment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long createSummaryContribution(
            @Min(1) final Long movieId,
            @NotNull @Valid final ContributionNew<Summary> contribution
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with movieId {}, contribution {}",
                movieId, contribution);

        final UserEntity user = this.findUser(this.authorizationServicea.getUserId());
        final MovieEntity movie = this.findMovie(movieId, DataStatus.ACCEPTED);
        final List<MovieSummaryEntity> summaryEntities = movie.getSummaries().stream()
                .filter(summary -> summary.getStatus() == DataStatus.ACCEPTED
                        && !summary.isReportedForUpdate() && !summary.isReportedForDelete()
                ).collect(Collectors.toList());
        final Set<Long> idsToAdd = new HashSet<>();
        final Map<Long, Long> idsToUpdate = new HashMap<>();

        this.validIds(summaryEntities, contribution);

        if (CollectionUtils.containsAny(contribution.getElementsToUpdate().keySet(), contribution.getIdsToDelete())) {
            throw new ResourceConflictException("Conflict of operation on the same element");
        }

        contribution.getElementsToAdd()
                .forEach(summary -> {
                    final Long id = this.moviePersistenceService.createSummary(summary, movie);
                    idsToAdd.add(id);
                });
        contribution.getElementsToUpdate()
                .forEach((key, value) -> {
                    final Long id = this.moviePersistenceService.createSummary(value, movie);
                    this.setReportedForUpdate(summaryEntities, key);
                    idsToUpdate.put(id, key);
                });
        contribution.getIdsToDelete()
                .forEach(id ->
                        this.setReportedForDelete(summaryEntities, id)
                );

        return this.createContributions(idsToAdd, contribution.getIdsToDelete(), idsToUpdate, movie, user,
                MovieField.SUMMARY, contribution.getSources(), contribution.getComment());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateSummaryContribution(
            @Min(1) final Long contributionId,
            @NotNull @Valid final ContributionUpdate<Summary> contribution
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with contributionId {}, contribution {}",
                contributionId, contribution);

        final UserEntity user = this.findUser(this.authorizationServicea.getUserId());
        final ContributionEntity contributionEntity = this.findContribution(contributionId, DataStatus.WAITING, user, MovieField.SUMMARY);

        this.validIds(contributionEntity, contribution);
        this.cleanUp(contributionEntity, contribution, contributionEntity.getMovie().getSummaries());

        contribution.getElementsToAdd().forEach((key, value) -> {
            this.moviePersistenceService.updateSummary(value, key, contributionEntity.getMovie());
        });
        contribution.getElementsToUpdate().forEach((key, value) -> {
            this.moviePersistenceService.updateSummary(value, key, contributionEntity.getMovie());
        });
        contribution.getNewElementsToAdd()
                .forEach(summary -> {
                    final Long id = this.moviePersistenceService.createSummary(summary, contributionEntity.getMovie());
                    contributionEntity.getIdsToAdd().add(id);
                });

        contributionEntity.setSources(contribution.getSources());
        Optional.ofNullable(contribution.getComment()).ifPresent(contributionEntity::setUserComment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long createSynopsisContribution(
            @Min(1) final Long movieId,
            @NotNull @Valid final ContributionNew<Synopsis> contribution
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with movieId {}, contribution {}",
                movieId, contribution);

        final UserEntity user = this.findUser(this.authorizationServicea.getUserId());
        final MovieEntity movie = this.findMovie(movieId, DataStatus.ACCEPTED);
        final List<MovieSynopsisEntity> synopsisEntities = movie.getSynopses().stream()
                .filter(synopsis -> synopsis.getStatus() == DataStatus.ACCEPTED
                        && !synopsis.isReportedForUpdate() && !synopsis.isReportedForDelete()
                ).collect(Collectors.toList());
        final Set<Long> idsToAdd = new HashSet<>();
        final Map<Long, Long> idsToUpdate = new HashMap<>();

        this.validIds(synopsisEntities, contribution);

        if (CollectionUtils.containsAny(contribution.getElementsToUpdate().keySet(), contribution.getIdsToDelete())) {
            throw new ResourceConflictException("Conflict of operation on the same element");
        }

        contribution.getElementsToAdd()
                .forEach(synopsis -> {
                    final Long id = this.moviePersistenceService.createSynopsis(synopsis, movie);
                    idsToAdd.add(id);
                });
        contribution.getElementsToUpdate()
                .forEach((key, value) -> {
                    final Long id = this.moviePersistenceService.createSynopsis(value, movie);
                    this.setReportedForUpdate(synopsisEntities, key);
                    idsToUpdate.put(id, key);
                });
        contribution.getIdsToDelete()
                .forEach(id ->
                        this.setReportedForDelete(synopsisEntities, id)
                );

        return this.createContributions(idsToAdd, contribution.getIdsToDelete(), idsToUpdate, movie, user,
                MovieField.SYNOPSIS, contribution.getSources(), contribution.getComment());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateSynopsisContribution(
            @Min(1) final Long contributionId,
            @NotNull @Valid final ContributionUpdate<Synopsis> contribution
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with contributionId {}, contribution {}",
                contributionId, contribution);

        final UserEntity user = this.findUser(this.authorizationServicea.getUserId());
        final ContributionEntity contributionEntity = this.findContribution(contributionId, DataStatus.WAITING, user, MovieField.SYNOPSIS);

        this.validIds(contributionEntity, contribution);
        this.cleanUp(contributionEntity, contribution, contributionEntity.getMovie().getSynopses());

        contribution.getElementsToAdd().forEach((key, value) -> {
            this.moviePersistenceService.updateSynopsis(value, key, contributionEntity.getMovie());
        });
        contribution.getElementsToUpdate().forEach((key, value) -> {
            this.moviePersistenceService.updateSynopsis(value, key, contributionEntity.getMovie());
        });
        contribution.getNewElementsToAdd()
                .forEach(synopsis -> {
                    final Long id = this.moviePersistenceService.createSynopsis(synopsis, contributionEntity.getMovie());
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
            @Min(1) final Long movieId,
            @NotNull @Valid final ContributionNew<BoxOffice> contribution
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with movieId {}, contribution {}",
                movieId, contribution);

        final UserEntity user = this.findUser(this.authorizationServicea.getUserId());
        final MovieEntity movie = this.findMovie(movieId, DataStatus.ACCEPTED);
        final List<MovieBoxOfficeEntity> boxOfficesEntities = movie.getBoxOffices().stream()
                .filter(boxOffice -> boxOffice.getStatus() == DataStatus.ACCEPTED
                        && !boxOffice.isReportedForUpdate() && !boxOffice.isReportedForDelete()
                ).collect(Collectors.toList());
        final Set<Long> idsToAdd = new HashSet<>();
        final Map<Long, Long> idsToUpdate = new HashMap<>();

        this.validIds(boxOfficesEntities, contribution);

        if (CollectionUtils.containsAny(contribution.getElementsToUpdate().keySet(), contribution.getIdsToDelete())) {
            throw new ResourceConflictException("Conflict of operation on the same element");
        }

        contribution.getElementsToAdd()
                .forEach(boxOffice -> {
                    final Long id = this.moviePersistenceService.createBoxOffice(boxOffice, movie);
                    idsToAdd.add(id);
                });
        contribution.getElementsToUpdate()
                .forEach((key, value) -> {
                    final Long id = this.moviePersistenceService.createBoxOffice(value, movie);
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
            @Min(1) final Long contributionId,
            @NotNull @Valid final ContributionUpdate<BoxOffice> contribution
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with contributionId {}, contribution {}",
                contributionId, contribution);

        final UserEntity user = this.findUser(this.authorizationServicea.getUserId());
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
                    final Long id = this.moviePersistenceService.createBoxOffice(boxOffice, contributionEntity.getMovie());
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
            @Min(1) final Long movieId,
            @NotNull @Valid final ContributionNew<Site> contribution
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with movieId {}, contribution {}",
                movieId, contribution);

        final UserEntity user = this.findUser(this.authorizationServicea.getUserId());
        final MovieEntity movie = this.findMovie(movieId, DataStatus.ACCEPTED);
        final List<MovieSiteEntity> sitesEntities = movie.getSites().stream()
                .filter(site -> site.getStatus() == DataStatus.ACCEPTED
                        && !site.isReportedForUpdate() && !site.isReportedForDelete()
                ).collect(Collectors.toList());
        final Set<Long> idsToAdd = new HashSet<>();
        final Map<Long, Long> idsToUpdate = new HashMap<>();

        this.validIds(sitesEntities, contribution);

        if (CollectionUtils.containsAny(contribution.getElementsToUpdate().keySet(), contribution.getIdsToDelete())) {
            throw new ResourceConflictException("Conflict of operation on the same element");
        }

        contribution.getElementsToAdd()
                .forEach(site -> {
                    final Long id = this.moviePersistenceService.createSite(site, movie);
                    idsToAdd.add(id);
                });
        contribution.getElementsToUpdate()
                .forEach((key, value) -> {
                    final Long id = this.moviePersistenceService.createSite(value, movie);
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
            @Min(1) final Long contributionId,
            @NotNull @Valid final ContributionUpdate<Site> contribution
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with contributionId {}, contribution {}",
                contributionId, contribution);

        final UserEntity user = this.findUser(this.authorizationServicea.getUserId());
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
                    final Long id = this.moviePersistenceService.createSite(site, contributionEntity.getMovie());
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
            @Min(1) final Long movieId,
            @NotNull @Valid final ContributionNew<Country> contribution
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with movieId {}, contribution {}",
                movieId, contribution);

        final UserEntity user = this.findUser(this.authorizationServicea.getUserId());
        final MovieEntity movie = this.findMovie(movieId, DataStatus.ACCEPTED);
        final List<MovieCountryEntity> countriesEntities = movie.getCountries().stream()
                .filter(country -> country.getStatus() == DataStatus.ACCEPTED
                        && !country.isReportedForUpdate() && !country.isReportedForDelete()
                ).collect(Collectors.toList());
        final Set<Long> idsToAdd = new HashSet<>();
        final Map<Long, Long> idsToUpdate = new HashMap<>();

        this.validIds(countriesEntities, contribution);

        if (CollectionUtils.containsAny(contribution.getElementsToUpdate().keySet(), contribution.getIdsToDelete())) {
            throw new ResourceConflictException("Conflict of operation on the same element");
        }

        contribution.getElementsToAdd()
                .forEach(country -> {
                    final Long id = this.moviePersistenceService.createCountry(country, movie);
                    idsToAdd.add(id);
                });
        contribution.getElementsToUpdate()
                .forEach((key, value) -> {
                    final Long id = this.moviePersistenceService.createCountry(value, movie);
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
            @Min(1) final Long contributionId,
            @NotNull @Valid final ContributionUpdate<Country> contribution
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with contributionId {}, contribution {}",
                contributionId, contribution);

        final UserEntity user = this.findUser(this.authorizationServicea.getUserId());
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
                    final Long id = this.moviePersistenceService.createCountry(country, contributionEntity.getMovie());
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
            @Min(1) final Long movieId,
            @NotNull @Valid final ContributionNew<Language> contribution
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with movieId {}, contribution {}",
                movieId, contribution);

        final UserEntity user = this.findUser(this.authorizationServicea.getUserId());
        final MovieEntity movie = this.findMovie(movieId, DataStatus.ACCEPTED);
        final List<MovieLanguageEntity> languagesEntities = movie.getLanguages().stream()
                .filter(language -> language.getStatus() == DataStatus.ACCEPTED
                        && !language.isReportedForUpdate() && !language.isReportedForDelete()
                ).collect(Collectors.toList());
        final Set<Long> idsToAdd = new HashSet<>();
        final Map<Long, Long> idsToUpdate = new HashMap<>();

        this.validIds(languagesEntities, contribution);

        if (CollectionUtils.containsAny(contribution.getElementsToUpdate().keySet(), contribution.getIdsToDelete())) {
            throw new ResourceConflictException("Conflict of operation on the same element");
        }

        contribution.getElementsToAdd()
                .forEach(language -> {
                    final Long id = this.moviePersistenceService.createLanguage(language, movie);
                    idsToAdd.add(id);
                });
        contribution.getElementsToUpdate()
                .forEach((key, value) -> {
                    final Long id = this.moviePersistenceService.createLanguage(value, movie);
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
            @Min(1) final Long contributionId,
            @NotNull @Valid final ContributionUpdate<Language> contribution
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with contributionId {}, contribution {}",
                contributionId, contribution);

        final UserEntity user = this.findUser(this.authorizationServicea.getUserId());
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
                    final Long id = this.moviePersistenceService.createLanguage(language, contributionEntity.getMovie());
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
            @Min(1) final Long movieId,
            @NotNull @Valid final ContributionNew<Genre> contribution
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with movieId {}, contribution {}",
                movieId, contribution);

        final UserEntity user = this.findUser(this.authorizationServicea.getUserId());
        final MovieEntity movie = this.findMovie(movieId, DataStatus.ACCEPTED);
        final List<MovieGenreEntity> genresEntities = movie.getGenres().stream()
                .filter(genre -> genre.getStatus() == DataStatus.ACCEPTED
                        && !genre.isReportedForUpdate() && !genre.isReportedForDelete()
                ).collect(Collectors.toList());
        final Set<Long> idsToAdd = new HashSet<>();
        final Map<Long, Long> idsToUpdate = new HashMap<>();

        this.validIds(genresEntities, contribution);

        if (CollectionUtils.containsAny(contribution.getElementsToUpdate().keySet(), contribution.getIdsToDelete())) {
            throw new ResourceConflictException("Conflict of operation on the same element");
        }

        contribution.getElementsToAdd()
                .forEach(genre -> {
                    final Long id = this.moviePersistenceService.createGenre(genre, movie);
                    idsToAdd.add(id);
                });
        contribution.getElementsToUpdate()
                .forEach((key, value) -> {
                    final Long id = this.moviePersistenceService.createGenre(value, movie);
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
            @Min(1) final Long contributionId,
            @NotNull @Valid final ContributionUpdate<Genre> contribution
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with contributionId {}, contribution {}",
                contributionId, contribution);

        final UserEntity user = this.findUser(this.authorizationServicea.getUserId());
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
                    final Long id = this.moviePersistenceService.createGenre(genre, contributionEntity.getMovie());
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
            @Min(1) final Long movieId,
            @NotNull @Valid final ContributionNew<Review> contribution
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with movieId {}, contribution {}",
                movieId, contribution);

        final UserEntity user = this.findUser(this.authorizationServicea.getUserId());
        final MovieEntity movie = this.findMovie(movieId, DataStatus.ACCEPTED);
        final List<MovieReviewEntity> reviewsEntities = movie.getReviews().stream()
                .filter(review -> review.getStatus() == DataStatus.ACCEPTED
                        && !review.isReportedForUpdate() && !review.isReportedForDelete()
                ).collect(Collectors.toList());
        final Set<Long> idsToAdd = new HashSet<>();
        final Map<Long, Long> idsToUpdate = new HashMap<>();

        this.validIds(reviewsEntities, contribution);

        if (CollectionUtils.containsAny(contribution.getElementsToUpdate().keySet(), contribution.getIdsToDelete())) {
            throw new ResourceConflictException("Conflict of operation on the same element");
        }

        contribution.getElementsToAdd()
                .forEach(review -> {
                    final Long id = this.moviePersistenceService.createReview(review, movie);
                    idsToAdd.add(id);
                });
        contribution.getElementsToUpdate()
                .forEach((key, value) -> {
                    final Long id = this.moviePersistenceService.createReview(value, movie);
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
            @Min(1) final Long contributionId,
            @NotNull @Valid final ContributionUpdate<Review> contribution
    ) throws ResourceNotFoundException, ResourceConflictException {
        log.info("Called with contributionId {}, contribution {}",
                contributionId, contribution);

        final UserEntity user = this.findUser(this.authorizationServicea.getUserId());
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
                    final Long id = this.moviePersistenceService.createReview(review, contributionEntity.getMovie());
                    contributionEntity.getIdsToAdd().add(id);
                });

        contributionEntity.setSources(contribution.getSources());
        Optional.ofNullable(contribution.getComment()).ifPresent(contributionEntity::setUserComment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long createPhotoContribution(
            @Min(1) final Long movieId,
            @NotNull @Valid final ContributionNew<ImageRequest> contribution
    ) throws ResourceNotFoundException, ResourceConflictException, ResourcePreconditionException, ResourceServerException {
        log.info("Called with movieId {}, contribution {}",
                movieId, contribution);

        final UserEntity user = this.findUser(this.authorizationServicea.getUserId());
        final MovieEntity movie = this.findMovie(movieId, DataStatus.ACCEPTED);
        final List<MoviePhotoEntity> photosEntities = movie.getPhotos().stream()
                .filter(photo -> photo.getStatus() == DataStatus.ACCEPTED
                        && !photo.isReportedForUpdate() && !photo.isReportedForDelete()
                ).collect(Collectors.toList());
        final Set<Long> idsToAdd = new HashSet<>();
        final Map<Long, Long> idsToUpdate = new HashMap<>();

        this.validIds(photosEntities, contribution);

        if (CollectionUtils.containsAny(contribution.getElementsToUpdate().keySet(), contribution.getIdsToDelete())) {
            throw new ResourceConflictException("Conflict of operation on the same element");
        }

        contribution.getElementsToAdd()
                .forEach(photo -> {
                    final String idInCloud = this.storageService.save(photo.getFile(), StorageDirectory.IMAGE);
                    final ImageRequest.Builder builder = new ImageRequest.Builder().withIdInCloud(idInCloud);
                    final Long id = this.moviePersistenceService.createPhoto(builder.build(), movie);
                    idsToAdd.add(id);
                });
        contribution.getElementsToUpdate()
                .forEach((key, value) -> {
                    final String idInCloud = this.storageService.save(value.getFile(), StorageDirectory.IMAGE);
                    final ImageRequest.Builder builder = new ImageRequest.Builder().withIdInCloud(idInCloud);
                    final Long id = this.moviePersistenceService.createPhoto(builder.build(), movie);
                    this.setReportedForUpdate(photosEntities, key);
                    idsToUpdate.put(id, key);
                });
        contribution.getIdsToDelete()
                .forEach(id ->
                        this.setReportedForDelete(photosEntities, id)
                );

        return this.createContributions(idsToAdd, contribution.getIdsToDelete(), idsToUpdate, movie, user,
                MovieField.PHOTO, contribution.getSources(), contribution.getComment());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePhotoContribution(
            @Min(1) final Long contributionId,
            @NotNull @Valid final ContributionUpdate<ImageRequest> contribution
    ) throws ResourceNotFoundException, ResourceConflictException, ResourcePreconditionException, ResourceServerException {
        log.info("Called with contributionId {}, contribution {}",
                contributionId, contribution);

        final UserEntity user = this.findUser(this.authorizationServicea.getUserId());
        final ContributionEntity contributionEntity = this.findContribution(contributionId, DataStatus.WAITING, user, MovieField.PHOTO);

        final Set<Long> idsToAddBeforeCleanUp = new HashSet<>(contributionEntity.getIdsToAdd());
        final Set<Long> idsToUpdateBeforeCleanUp = new HashSet<>(contributionEntity.getIdsToUpdate().keySet());

        this.validIds(contributionEntity, contribution);
        this.cleanUp(contributionEntity, contribution, contributionEntity.getMovie().getPhotos());

        for(final Long id : idsToAddBeforeCleanUp) {
            if(!contributionEntity.getIdsToAdd().contains(id)) {
                this.storageService.delete(this.entityManager.find(MoviePhotoEntity.class, id).getIdInCloud());
            }
        }
        for(final Long id : idsToUpdateBeforeCleanUp) {
            if(!contributionEntity.getIdsToUpdate().keySet().contains(id)) {
                this.storageService.delete(this.entityManager.find(MoviePhotoEntity.class, id).getIdInCloud());
            }
        }

        contribution.getElementsToAdd().forEach((key, value) -> {
            final String idInCloud = this.swap(key, value.getFile(), StorageDirectory.IMAGE, MoviePhotoEntity.class);
            final ImageRequest.Builder builder = new ImageRequest.Builder().withIdInCloud(idInCloud);
            this.moviePersistenceService.updatePhoto(builder.build(), key, contributionEntity.getMovie());
        });
        contribution.getElementsToUpdate().forEach((key, value) -> {
            final String idInCloud = this.swap(key, value.getFile(), StorageDirectory.IMAGE, MoviePhotoEntity.class);
            final ImageRequest.Builder builder = new ImageRequest.Builder().withIdInCloud(idInCloud);
            this.moviePersistenceService.updatePhoto(builder.build(), key, contributionEntity.getMovie());
        });
        contribution.getNewElementsToAdd()
                .forEach(photo -> {
                    final String idInCloud = this.storageService.save(photo.getFile(), StorageDirectory.IMAGE);
                    final ImageRequest.Builder builder = new ImageRequest.Builder().withIdInCloud(idInCloud);
                    final Long id = this.moviePersistenceService.createPhoto(builder.build(), contributionEntity.getMovie());
                    contributionEntity.getIdsToAdd().add(id);
                });

        contributionEntity.setSources(contribution.getSources());
        Optional.ofNullable(contribution.getComment()).ifPresent(contributionEntity::setUserComment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long createPosterContribution(
            @Min(1) final Long movieId,
            @NotNull @Valid final ContributionNew<ImageRequest> contribution
    ) throws ResourceNotFoundException, ResourceConflictException, ResourcePreconditionException, ResourceServerException {
        log.info("Called with movieId {}, contribution {}",
                movieId, contribution);

        final UserEntity user = this.findUser(this.authorizationServicea.getUserId());
        final MovieEntity movie = this.findMovie(movieId, DataStatus.ACCEPTED);
        final List<MoviePosterEntity> postersEntities = movie.getPosters().stream()
                .filter(poster -> poster.getStatus() == DataStatus.ACCEPTED
                        && !poster.isReportedForUpdate() && !poster.isReportedForDelete()
                ).collect(Collectors.toList());
        final Set<Long> idsToAdd = new HashSet<>();
        final Map<Long, Long> idsToUpdate = new HashMap<>();

        this.validIds(postersEntities, contribution);

        if (CollectionUtils.containsAny(contribution.getElementsToUpdate().keySet(), contribution.getIdsToDelete())) {
            throw new ResourceConflictException("Conflict of operation on the same element");
        }

        contribution.getElementsToAdd()
                .forEach(poster -> {
                    final String idInCloud = this.storageService.save(poster.getFile(), StorageDirectory.IMAGE);
                    final ImageRequest.Builder builder = new ImageRequest.Builder().withIdInCloud(idInCloud);
                    final Long id = this.moviePersistenceService.createPoster(builder.build(), movie);
                    idsToAdd.add(id);
                });
        contribution.getElementsToUpdate()
                .forEach((key, value) -> {
                    final String idInCloud = this.storageService.save(value.getFile(), StorageDirectory.IMAGE);
                    final ImageRequest.Builder builder = new ImageRequest.Builder().withIdInCloud(idInCloud);
                    final Long id = this.moviePersistenceService.createPoster(builder.build(), movie);
                    this.setReportedForUpdate(postersEntities, key);
                    idsToUpdate.put(id, key);
                });
        contribution.getIdsToDelete()
                .forEach(id ->
                        this.setReportedForDelete(postersEntities, id)
                );

        return this.createContributions(idsToAdd, contribution.getIdsToDelete(), idsToUpdate, movie, user,
                MovieField.POSTER, contribution.getSources(), contribution.getComment());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updatePosterContribution(
            @Min(1) final Long contributionId,
            @NotNull @Valid final ContributionUpdate<ImageRequest> contribution
    ) throws ResourceNotFoundException, ResourceConflictException, ResourcePreconditionException, ResourceServerException {
        log.info("Called with contributionId {}, contribution {}",
                contributionId, contribution);

        final UserEntity user = this.findUser(this.authorizationServicea.getUserId());
        final ContributionEntity contributionEntity = this.findContribution(contributionId, DataStatus.WAITING, user, MovieField.POSTER);

        final Set<Long> idsToAddBeforeCleanUp = new HashSet<>(contributionEntity.getIdsToAdd());
        final Set<Long> idsToUpdateBeforeCleanUp = new HashSet<>(contributionEntity.getIdsToUpdate().keySet());

        this.validIds(contributionEntity, contribution);
        this.cleanUp(contributionEntity, contribution, contributionEntity.getMovie().getPosters());

        for(final Long id : idsToAddBeforeCleanUp) {
            if(!contributionEntity.getIdsToAdd().contains(id)) {
                this.storageService.delete(this.entityManager.find(MoviePosterEntity.class, id).getIdInCloud());
            }
        }
        for(final Long id : idsToUpdateBeforeCleanUp) {
            if(!contributionEntity.getIdsToUpdate().keySet().contains(id)) {
                this.storageService.delete(this.entityManager.find(MoviePosterEntity.class, id).getIdInCloud());
            }
        }

        contribution.getElementsToAdd().forEach((key, value) -> {
            final String idInCloud = this.swap(key, value.getFile(), StorageDirectory.IMAGE, MoviePhotoEntity.class);
            final ImageRequest.Builder builder = new ImageRequest.Builder().withIdInCloud(idInCloud);
            this.moviePersistenceService.updatePoster(builder.build(), key, contributionEntity.getMovie());
        });
        contribution.getElementsToUpdate().forEach((key, value) -> {
            final String idInCloud = this.swap(key, value.getFile(), StorageDirectory.IMAGE, MoviePhotoEntity.class);
            final ImageRequest.Builder builder = new ImageRequest.Builder().withIdInCloud(idInCloud);
            this.moviePersistenceService.updatePoster(builder.build(), key, contributionEntity.getMovie());
        });
        contribution.getNewElementsToAdd()
                .forEach(poster -> {
                    final String idInCloud = this.storageService.save(poster.getFile(), StorageDirectory.IMAGE);
                    final ImageRequest.Builder builder = new ImageRequest.Builder().withIdInCloud(idInCloud);
                    final Long id = this.moviePersistenceService.createPoster(builder.build(), contributionEntity.getMovie());
                    contributionEntity.getIdsToAdd().add(id);
                });

        contributionEntity.setSources(contribution.getSources());
        Optional.ofNullable(contribution.getComment()).ifPresent(contributionEntity::setUserComment);
    }

    /**
     * Save the contribution.
     *
     * @param idsToAdd Set of IDs to add
     * @param idsToDelete Set of IDs to delete
     * @param idsToUpdate Map of IDs to update
     * @param movie The MovieEntity object
     * @param user The UserEntity object
     * @param field The movie field
     * @param sources Sources for contribution
     * @param comment Comment for contribution
     * @return The ID of the contribution created
     */
    private Long createContributions(
            final Set<Long> idsToAdd,
            final Set<Long> idsToDelete,
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
     * @param idsToAddFromEntity Set of element IDs to be added. List from the entity
     * @param idsToAddFromDto List of element IDs to be added. List from the DTO
     * @param entities List of entities(MovieInfoEntity)
     */
    private void cleanUpIdsToAdd(final Set<Long> idsToAddFromEntity, final Set<Long> idsToAddFromDto,
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
     * @param idsToUpdateFromDto Set of element IDs to be updated. List from the DTO
     */
    private void cleanUpIdsToUpdate(final Map<Long, Long> idsToUpdateFromEntity, final Set<Long> idsToUpdateFromDto) {
        for (final Iterator<Map.Entry<Long, Long>> it = idsToUpdateFromEntity.entrySet().iterator(); it.hasNext(); ) {
            if (!idsToUpdateFromDto.contains(it.next().getKey())) {
                final MovieInfoEntity oldElement = this.movieInfoRepository.getOne(it.next().getValue());
                final MovieInfoEntity newElement = this.movieInfoRepository.getOne(it.next().getKey());

                it.remove();
                this.entityManager.remove(newElement);
                oldElement.setReportedForUpdate(false);
            }
        }
    }

    /**
     * Helper method to clean the list of element IDs to be deleted.
     *
     * @param idsToDeleteFromEntity Set of element IDs to be deleted. List from the entity
     * @param idsToDeleteFromDto Set of element IDs to be deleted. List from the DTO
     */
    private void cleanUpIdsToDelete(final Set<Long> idsToDeleteFromEntity, final Set<Long> idsToDeleteFromDto) {
        for(final Iterator<Long> it = idsToDeleteFromEntity.iterator(); it.hasNext();) {
            final Long id = it.next();
            if(!idsToDeleteFromDto.contains(id)) {
                this.movieInfoRepository.getOne(id).setReportedForDelete(false);
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
            final MovieInfoEntity element = this.movieInfoRepository.getOne(id);
            element.setStatus(DataStatus.ACCEPTED);
        });
        contribution.getIdsToDelete().forEach(id -> {
            final MovieInfoEntity element = this.movieInfoRepository.getOne(id);
            element.setStatus(DataStatus.DELETED);
        });
        if(contribution.getField() == MovieField.OUTLINE
                || contribution.getField() == MovieField.SUMMARY
                || contribution.getField() == MovieField.SYNOPSIS
                || contribution.getField() == MovieField.REVIEW) {
            this.updatePatch(contribution);
        } else {
            contribution.getIdsToUpdate().forEach((key, value) -> {
                final MovieInfoEntity newElement = this.movieInfoRepository.getOne(key);
                final MovieInfoEntity oldElement = this.movieInfoRepository.getOne(value);

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
            final MovieInfoEntity element = this.movieInfoRepository.getOne(id);
            element.setStatus(DataStatus.REJECTED);
        });
        contribution.getIdsToDelete().forEach(id -> {
            final MovieInfoEntity element = this.movieInfoRepository.getOne(id);
            element.setReportedForDelete(false);
        });
        contribution.getIdsToUpdate().forEach((key, value) -> {
            final MovieInfoEntity newElement = this.movieInfoRepository.getOne(key);
            final MovieInfoEntity oldElement = this.movieInfoRepository.getOne(value);

            newElement.setStatus(DataStatus.REJECTED);
            oldElement.setReportedForUpdate(false);
        });
    }

    /**
     * Helper method to update: outline, summary, synopsis, review.
     *
     * @param contribution The ContributionEntity object
     * @throws ResourceConflictException if the element exists
     */
    private void updatePatch(final ContributionEntity contribution) throws ResourceConflictException {
        if(contribution.getField() == MovieField.OUTLINE) {
            contribution.getIdsToUpdate().forEach((key, value) -> {
                final MovieOutlineEntity newOutline = this.entityManager.find(MovieOutlineEntity.class, key);
                final MovieOutlineEntity oldOutline = this.entityManager.find(MovieOutlineEntity.class, value);

                this.moviePersistenceService.updateOutline(ServiceUtils.toOutlineDto(newOutline), value, contribution.getMovie());

                oldOutline.setReportedForUpdate(false);
                newOutline.setStatus(DataStatus.AMENDMENT_ACCEPTED);
            });
        } else if(contribution.getField() == MovieField.SUMMARY) {
            contribution.getIdsToUpdate().forEach((key, value) -> {
                final MovieSummaryEntity newSummary = this.entityManager.find(MovieSummaryEntity.class, key);
                final MovieSummaryEntity oldSummary = this.entityManager.find(MovieSummaryEntity.class, value);

                this.moviePersistenceService.updateSummary(ServiceUtils.toSummaryDto(newSummary), value, contribution.getMovie());

                oldSummary.setReportedForUpdate(false);
                newSummary.setStatus(DataStatus.AMENDMENT_ACCEPTED);
            });
        } else if(contribution.getField() == MovieField.SYNOPSIS) {
            contribution.getIdsToUpdate().forEach((key, value) -> {
                final MovieSynopsisEntity newSynopsis= this.entityManager.find(MovieSynopsisEntity.class, key);
                final MovieSynopsisEntity oldSynopsis = this.entityManager.find(MovieSynopsisEntity.class, value);

                this.moviePersistenceService.updateSynopsis(ServiceUtils.toSynopsisDto(newSynopsis), value, contribution.getMovie());

                oldSynopsis.setReportedForUpdate(false);
                newSynopsis.setStatus(DataStatus.AMENDMENT_ACCEPTED);
            });
        } else if(contribution.getField() == MovieField.REVIEW) {
            contribution.getIdsToUpdate().forEach((key, value) -> {
                final MovieReviewEntity newReview = this.entityManager.find(MovieReviewEntity.class, key);
                final MovieReviewEntity oldReview = this.entityManager.find(MovieReviewEntity.class, value);

                this.moviePersistenceService.updateReview(ServiceUtils.toReviewDto(newReview), value, contribution.getMovie());

                oldReview.setReportedForUpdate(false);
                newReview.setStatus(DataStatus.AMENDMENT_ACCEPTED);
            });
        }
    }

    /**
     * Helper method for replacing a file in the cloud.
     *
     * @param id Identity of the entity
     * @param file File to save
     * @param storageDirectory The directory to which the file should be saved
     * @param clazz Type of entity class representing the file
     * @return File ID in the cloud
     * @throws ResourcePreconditionException if an I/O error occurs or incorrect content type
     * @throws ResourceServerException if an error occurred with the server
     */
    private String swap(final Long id, final File file, final StorageDirectory storageDirectory, final Class<? extends MovieFileEntity> clazz)
            throws ResourcePreconditionException, ResourceServerException {
        this.storageService.delete(this.entityManager.find(clazz, id).getIdInCloud());
        return this.storageService.save(file, storageDirectory);
    }
}

package com.core.jpa.service.impl;

import com.common.dto.Contribution;
import com.common.dto.movie.*;
import com.common.dto.search.ContributionSearchResult;
import com.common.exception.ResourceNotFoundException;
import com.core.jpa.entity.ContributionEntity;
import com.core.jpa.entity.MovieEntity;
import com.core.jpa.entity.movie.*;
import com.core.jpa.repository.ContributionRepository;
import com.core.jpa.repository.MovieRepository;
import com.core.jpa.service.MovieContributionSearchService;
import com.core.jpa.specifications.ContributionSpecs;
import com.core.movie.DataStatus;
import com.core.movie.MovieField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * JPA implementation of the Movie Contribution Search Service.
 */
@Service("movieContributionSearchService")
@Slf4j
@Transactional(readOnly = true)
@Validated
public class MovieContributionSearchServiceImpl implements MovieContributionSearchService {

    private final MovieRepository movieRepository;
    private final ContributionRepository contributionRepository;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Constructor.
     *
     * @param movieRepository The movie repository to use
     * @param contributionRepository The contribution repository to use
     */
    @Autowired
    public MovieContributionSearchServiceImpl(
            @NotNull final MovieRepository movieRepository,
            @NotNull final ContributionRepository contributionRepository
    ) {
        this.movieRepository = movieRepository;
        this.contributionRepository = contributionRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<ContributionSearchResult> getContributions(
            @Min(1) final Long id,
            @Nullable final MovieField field,
            @Nullable final DataStatus status,
            @Nullable final Date fromDate,
            @Nullable final Date toDate,
            @NotNull final Pageable page
    ) throws ResourceNotFoundException {
        log.info("Called with id {}, field {}, status {}, fromDate {}, toDate {}," +
                " page {}", id, field, status, fromDate, toDate, page);

        @SuppressWarnings("unchecked")
        final Page<ContributionEntity> contributionEntities = this.contributionRepository.findAll(
                ContributionSpecs.getFindPredicate(
                        this.findMovie(id),
                        field,
                        status,
                        fromDate,
                        toDate),
                page);

        return contributionEntities.map(ContributionEntity::getSearchResultDTO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Contribution<OtherTitle> getOtherTitleContribution(
            @Min(1) final Long contributionId
    ) throws ResourceNotFoundException {
        log.info("Called with contributionId {}", contributionId);

        final ContributionEntity contributionEntity = this.findContribution(contributionId, MovieField.OTHER_TITLE);
        final Contribution<OtherTitle> contribution = contributionEntity.getDTO();

        contributionEntity.getIdsToAdd()
                .forEach(id -> {
                    final MovieOtherTitleEntity otherTitle = this.entityManager.find(MovieOtherTitleEntity.class, id);
                    contribution.getElementsToAdd().put(otherTitle.getId(), otherTitle.getDTO());
                });
        contributionEntity.getIdsToUpdate()
                .forEach((key, value) -> {
                    final MovieOtherTitleEntity otherTitleToUpdate = this.entityManager.find(MovieOtherTitleEntity.class, key);
                    final MovieOtherTitleEntity otherTitleUpdated = this.entityManager.find(MovieOtherTitleEntity.class, value);
                    contribution.getElementsToUpdate().put(otherTitleToUpdate.getId(), otherTitleToUpdate.getDTO());
                    contribution.getElementsUpdated().put(otherTitleToUpdate.getId(), otherTitleUpdated.getDTO());
                });
        contributionEntity.getIdsToDelete()
                .forEach(id -> {
                    final MovieOtherTitleEntity otherTitle = this.entityManager.find(MovieOtherTitleEntity.class, id);
                    contribution.getElementsToDelete().put(otherTitle.getId(), otherTitle.getDTO());
                });

        return contribution;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Contribution<ReleaseDate> getReleaseDateContribution(
            @Min(1) final Long contributionId
    ) throws ResourceNotFoundException {
        log.info("Called with contributionId {}", contributionId);

        final ContributionEntity contributionEntity = this.findContribution(contributionId, MovieField.RELEASE_DATE);
        final Contribution<ReleaseDate> contribution = contributionEntity.getDTO();

        contributionEntity.getIdsToAdd()
                .forEach(id -> {
                    final MovieReleaseDateEntity releaseDate = this.entityManager.find(MovieReleaseDateEntity.class, id);
                    contribution.getElementsToAdd().put(releaseDate.getId(), releaseDate.getDTO());
                });
        contributionEntity.getIdsToUpdate()
                .forEach((key, value) -> {
                    final MovieReleaseDateEntity releaseDateToUpdate = this.entityManager.find(MovieReleaseDateEntity.class, key);
                    final MovieReleaseDateEntity releaseDateUpdated = this.entityManager.find(MovieReleaseDateEntity.class, value);
                    contribution.getElementsToUpdate().put(releaseDateToUpdate.getId(), releaseDateToUpdate.getDTO());
                    contribution.getElementsUpdated().put(releaseDateToUpdate.getId(), releaseDateUpdated.getDTO());
                });
        contributionEntity.getIdsToDelete()
                .forEach(id -> {
                    final MovieReleaseDateEntity releaseDate = this.entityManager.find(MovieReleaseDateEntity.class, id);
                    contribution.getElementsToDelete().put(releaseDate.getId(), releaseDate.getDTO());
                });

        return contribution;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Contribution<Storyline> getStorylineContribution(
            @Min(1) final Long contributionId
    ) throws ResourceNotFoundException {
        log.info("Called with contributionId {}", contributionId);

        final ContributionEntity contributionEntity = this.findContribution(contributionId, MovieField.STORYLINE);
        final Contribution<Storyline> contribution = contributionEntity.getDTO();

        contributionEntity.getIdsToAdd()
                .forEach(id -> {
                    final MovieStorylineEntity storyline = this.entityManager.find(MovieStorylineEntity.class, id);
                    contribution.getElementsToAdd().put(storyline.getId(), storyline.getDTO());
                });
        contributionEntity.getIdsToUpdate()
                .forEach((key, value) -> {
                    final MovieStorylineEntity storylineToUpdate = this.entityManager.find(MovieStorylineEntity.class, key);
                    final MovieStorylineEntity storylineUpdated = this.entityManager.find(MovieStorylineEntity.class, value);
                    contribution.getElementsToUpdate().put(storylineToUpdate.getId(), storylineToUpdate.getDTO());
                    contribution.getElementsUpdated().put(storylineToUpdate.getId(), storylineUpdated.getDTO());
                });
        contributionEntity.getIdsToDelete()
                .forEach(id -> {
                    final MovieStorylineEntity storyline = this.entityManager.find(MovieStorylineEntity.class, id);
                    contribution.getElementsToDelete().put(storyline.getId(), storyline.getDTO());
                });

        return contribution;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Contribution<BoxOffice> getBoxOfficeContribution(
            @Min(1) final Long contributionId
    ) throws ResourceNotFoundException {
        log.info("Called with contributionId {}", contributionId);

        final ContributionEntity contributionEntity = this.findContribution(contributionId, MovieField.BOX_OFFICE);
        final Contribution<BoxOffice> contribution = contributionEntity.getDTO();

        contributionEntity.getIdsToAdd()
                .forEach(id -> {
                    final MovieBoxOfficeEntity boxOffice = this.entityManager.find(MovieBoxOfficeEntity.class, id);
                    contribution.getElementsToAdd().put(boxOffice.getId(), boxOffice.getDTO());
                });
        contributionEntity.getIdsToUpdate()
                .forEach((key, value) -> {
                    final MovieBoxOfficeEntity boxOfficeToUpdate = this.entityManager.find(MovieBoxOfficeEntity.class, key);
                    final MovieBoxOfficeEntity boxOfficeUpdated = this.entityManager.find(MovieBoxOfficeEntity.class, value);
                    contribution.getElementsToUpdate().put(boxOfficeToUpdate.getId(), boxOfficeToUpdate.getDTO());
                    contribution.getElementsUpdated().put(boxOfficeToUpdate.getId(), boxOfficeUpdated.getDTO());
                });
        contributionEntity.getIdsToDelete()
                .forEach(id -> {
                    final MovieBoxOfficeEntity boxOffice = this.entityManager.find(MovieBoxOfficeEntity.class, id);
                    contribution.getElementsToDelete().put(boxOffice.getId(), boxOffice.getDTO());
                });

        return contribution;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Contribution<Site> getSiteContribution(
            @Min(1) final Long contributionId
    ) throws ResourceNotFoundException {
        log.info("Called with contributionId {}", contributionId);

        final ContributionEntity contributionEntity = this.findContribution(contributionId, MovieField.SITE);
        final Contribution<Site> contribution = contributionEntity.getDTO();

        contributionEntity.getIdsToAdd()
                .forEach(id -> {
                    final MovieSiteEntity site = this.entityManager.find(MovieSiteEntity.class, id);
                    contribution.getElementsToAdd().put(site.getId(), site.getDTO());
                });
        contributionEntity.getIdsToUpdate()
                .forEach((key, value) -> {
                    final MovieSiteEntity siteToUpdate = this.entityManager.find(MovieSiteEntity.class, key);
                    final MovieSiteEntity siteUpdated = this.entityManager.find(MovieSiteEntity.class, value);
                    contribution.getElementsToUpdate().put(siteToUpdate.getId(), siteToUpdate.getDTO());
                    contribution.getElementsUpdated().put(siteToUpdate.getId(), siteUpdated.getDTO());
                });
        contributionEntity.getIdsToDelete()
                .forEach(id -> {
                    final MovieSiteEntity site = this.entityManager.find(MovieSiteEntity.class, id);
                    contribution.getElementsToDelete().put(site.getId(), site.getDTO());
                });

        return contribution;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Contribution<Country> getCountryContribution(
            @Min(1) final Long contributionId
    ) throws ResourceNotFoundException {
        log.info("Called with contributionId {}", contributionId);

        final ContributionEntity contributionEntity = this.findContribution(contributionId, MovieField.COUNTRY);
        final Contribution<Country> contribution = contributionEntity.getDTO();

        contributionEntity.getIdsToAdd()
                .forEach(id -> {
                    final MovieCountryEntity country = this.entityManager.find(MovieCountryEntity.class, id);
                    contribution.getElementsToAdd().put(country.getId(), country.getDTO());
                });
        contributionEntity.getIdsToUpdate()
                .forEach((key, value) -> {
                    final MovieCountryEntity countryToUpdate = this.entityManager.find(MovieCountryEntity.class, key);
                    final MovieCountryEntity countryUpdated = this.entityManager.find(MovieCountryEntity.class, value);
                    contribution.getElementsToUpdate().put(countryToUpdate.getId(), countryToUpdate.getDTO());
                    contribution.getElementsUpdated().put(countryToUpdate.getId(), countryUpdated.getDTO());
                });
        contributionEntity.getIdsToDelete()
                .forEach(id -> {
                    final MovieCountryEntity country = this.entityManager.find(MovieCountryEntity.class, id);
                    contribution.getElementsToDelete().put(country.getId(), country.getDTO());
                });

        return contribution;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Contribution<Language> getLanguageContribution(
            @Min(1) final Long contributionId
    ) throws ResourceNotFoundException {
        log.info("Called with contributionId {}", contributionId);

        final ContributionEntity contributionEntity = this.findContribution(contributionId, MovieField.LANGUAGE);
        final Contribution<Language> contribution = contributionEntity.getDTO();

        contributionEntity.getIdsToAdd()
                .forEach(id -> {
                    final MovieLanguageEntity language = this.entityManager.find(MovieLanguageEntity.class, id);
                    contribution.getElementsToAdd().put(language.getId(), language.getDTO());
                });
        contributionEntity.getIdsToUpdate()
                .forEach((key, value) -> {
                    final MovieLanguageEntity languageToUpdate = this.entityManager.find(MovieLanguageEntity.class, key);
                    final MovieLanguageEntity languageUpdated = this.entityManager.find(MovieLanguageEntity.class, value);
                    contribution.getElementsToUpdate().put(languageToUpdate.getId(), languageToUpdate.getDTO());
                    contribution.getElementsUpdated().put(languageToUpdate.getId(), languageUpdated.getDTO());
                });
        contributionEntity.getIdsToDelete()
                .forEach(id -> {
                    final MovieLanguageEntity language = this.entityManager.find(MovieLanguageEntity.class, id);
                    contribution.getElementsToDelete().put(language.getId(), language.getDTO());
                });

        return contribution;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Contribution<Genre> getGenreContribution(
            @Min(1) final Long contributionId
    ) throws ResourceNotFoundException {
        log.info("Called with contributionId {}", contributionId);

        final ContributionEntity contributionEntity = this.findContribution(contributionId, MovieField.GENRE);
        final Contribution<Genre> contribution = contributionEntity.getDTO();

        contributionEntity.getIdsToAdd()
                .forEach(id -> {
                    final MovieGenreEntity genre = this.entityManager.find(MovieGenreEntity.class, id);
                    contribution.getElementsToAdd().put(genre.getId(), genre.getDTO());
                });
        contributionEntity.getIdsToUpdate()
                .forEach((key, value) -> {
                    final MovieGenreEntity genreToUpdate = this.entityManager.find(MovieGenreEntity.class, key);
                    final MovieGenreEntity genreUpdated = this.entityManager.find(MovieGenreEntity.class, value);
                    contribution.getElementsToUpdate().put(genreToUpdate.getId(), genreToUpdate.getDTO());
                    contribution.getElementsUpdated().put(genreToUpdate.getId(), genreUpdated.getDTO());
                });
        contributionEntity.getIdsToDelete()
                .forEach(id -> {
                    final MovieGenreEntity genre = this.entityManager.find(MovieGenreEntity.class, id);
                    contribution.getElementsToDelete().put(genre.getId(), genre.getDTO());
                });

        return contribution;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Contribution<Review> getReviewContribution(
            @Min(1) final Long contributionId
    ) throws ResourceNotFoundException {
        log.info("Called with contributionId {}", contributionId);

        final ContributionEntity contributionEntity = this.findContribution(contributionId, MovieField.REVIEW);
        final Contribution<Review> contribution = contributionEntity.getDTO();

        contributionEntity.getIdsToAdd()
                .forEach(id -> {
                    final MovieReviewEntity review = this.entityManager.find(MovieReviewEntity.class, id);
                    contribution.getElementsToAdd().put(review.getId(), review.getDTO());
                });
        contributionEntity.getIdsToUpdate()
                .forEach((key, value) -> {
                    final MovieReviewEntity reviewToUpdate = this.entityManager.find(MovieReviewEntity.class, key);
                    final MovieReviewEntity reviewUpdated = this.entityManager.find(MovieReviewEntity.class, value);
                    contribution.getElementsToUpdate().put(reviewToUpdate.getId(), reviewToUpdate.getDTO());
                    contribution.getElementsUpdated().put(reviewToUpdate.getId(), reviewUpdated.getDTO());
                });
        contributionEntity.getIdsToDelete()
                .forEach(id -> {
                    final MovieReviewEntity review = this.entityManager.find(MovieReviewEntity.class, id);
                    contribution.getElementsToDelete().put(review.getId(), review.getDTO());
                });

        return contribution;
    }

    /**
     * Helper method to find the contribution entity.
     *
     * @param id The contribution ID
     * @param field The movie field
     * @return The contribution
     * @throws ResourceNotFoundException if no contribution found
     */
    private ContributionEntity findContribution(final Long id, final MovieField field) throws ResourceNotFoundException {
        return this.contributionRepository
                .findByIdAndField(id, field)
                .orElseThrow(() -> new ResourceNotFoundException("No contribution found with id " + id));
    }

    /**
     * Helper method to find the movie entity.
     *
     * @param id The movie ID
     * @return The movie
     * @throws ResourceNotFoundException if no movie found
     */
    private MovieEntity findMovie(final Long id) throws ResourceNotFoundException {
        return this.movieRepository
                .findByIdAndStatus(id, DataStatus.ACCEPTED)
                .orElseThrow(() -> new ResourceNotFoundException("No movie found with id " + id));
    }
}

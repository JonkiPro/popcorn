package com.jonki.popcorn.core.jpa.service;

import com.jonki.popcorn.common.dto.Contribution;
import com.jonki.popcorn.common.dto.movie.*;
import com.jonki.popcorn.common.dto.movie.response.ImageResponse;
import com.jonki.popcorn.common.dto.search.ContributionSearchResult;
import com.jonki.popcorn.common.exception.ResourceNotFoundException;
import com.jonki.popcorn.core.jpa.entity.ContributionEntity;
import com.jonki.popcorn.core.jpa.entity.ContributionEntity_;
import com.jonki.popcorn.core.jpa.entity.MovieEntity;
import com.jonki.popcorn.core.jpa.repository.ContributionRepository;
import com.jonki.popcorn.core.jpa.repository.MovieRepository;
import com.jonki.popcorn.core.service.MovieContributionSearchService;
import com.jonki.popcorn.core.jpa.specification.ContributionSpecs;
import com.jonki.popcorn.common.dto.DataStatus;
import com.jonki.popcorn.common.dto.MovieField;
import com.google.common.collect.Lists;
import com.jonki.popcorn.core.jpa.entity.movie.*;
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
    public Page<ContributionSearchResult> findContributions(
            @Nullable @Min(1) final Long id,
            @Nullable final MovieField field,
            @Nullable final DataStatus status,
            @Nullable final Date fromDate,
            @Nullable final Date toDate,
            @NotNull final Pageable page
    ) throws ResourceNotFoundException {
        log.info("Called with id {}, field {}, status {}, fromDate {}, toDate {}," +
                " page {}", id, field, status, fromDate, toDate, page);

        final CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
        final CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        final Root<ContributionEntity> root = countQuery.from(ContributionEntity.class);

        final Predicate whereClause = ContributionSpecs
                .getFindPredicate(
                        root,
                        cb,
                        id != null ? this.findMovie(id) : null,
                        field,
                        status,
                        fromDate,
                        toDate
                );

        countQuery.select(cb.count(root)).where(whereClause);

        final Long count = this.entityManager.createQuery(countQuery).getSingleResult();

        if (count > 0) {
            final CriteriaQuery<ContributionSearchResult> contentQuery = cb.createQuery(ContributionSearchResult.class);
            contentQuery.from(ContributionEntity.class);

            contentQuery.multiselect(
                    root.get(ContributionEntity_.id),
                    root.get(ContributionEntity_.field),
                    root.get(ContributionEntity_.created)
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

            final List<ContributionSearchResult> results = this.entityManager
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
    @SuppressWarnings("unchecked")
    public Contribution<OtherTitle> getOtherTitleContribution(
            @Min(1) final Long contributionId
    ) throws ResourceNotFoundException {
        log.info("Called with contributionId {}", contributionId);

        final ContributionEntity contributionEntity = this.findContribution(contributionId, MovieField.OTHER_TITLE);
        final Contribution<OtherTitle> contribution = ServiceUtils.toContributionDto(contributionEntity);

        contributionEntity.getIdsToAdd()
                .forEach(id -> {
                    final MovieOtherTitleEntity otherTitle = this.entityManager.find(MovieOtherTitleEntity.class, id);
                    contribution.getElementsToAdd().put(otherTitle.getId(), ServiceUtils.toOtherTitleDto(otherTitle));
                });
        contributionEntity.getIdsToUpdate()
                .forEach((key, value) -> {
                    final MovieOtherTitleEntity otherTitleToUpdate = this.entityManager.find(MovieOtherTitleEntity.class, key);
                    final MovieOtherTitleEntity otherTitleUpdated = this.entityManager.find(MovieOtherTitleEntity.class, value);
                    contribution.getElementsToUpdate().put(otherTitleToUpdate.getId(), ServiceUtils.toOtherTitleDto(otherTitleToUpdate));
                    contribution.getElementsUpdated().put(otherTitleToUpdate.getId(), ServiceUtils.toOtherTitleDto(otherTitleUpdated));
                });
        contributionEntity.getIdsToDelete()
                .forEach(id -> {
                    final MovieOtherTitleEntity otherTitle = this.entityManager.find(MovieOtherTitleEntity.class, id);
                    contribution.getElementsToDelete().put(otherTitle.getId(), ServiceUtils.toOtherTitleDto(otherTitle));
                });

        return contribution;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public Contribution<ReleaseDate> getReleaseDateContribution(
            @Min(1) final Long contributionId
    ) throws ResourceNotFoundException {
        log.info("Called with contributionId {}", contributionId);

        final ContributionEntity contributionEntity = this.findContribution(contributionId, MovieField.RELEASE_DATE);
        final Contribution<ReleaseDate> contribution = ServiceUtils.toContributionDto(contributionEntity);

        contributionEntity.getIdsToAdd()
                .forEach(id -> {
                    final MovieReleaseDateEntity releaseDate = this.entityManager.find(MovieReleaseDateEntity.class, id);
                    contribution.getElementsToAdd().put(releaseDate.getId(), ServiceUtils.toReleaseDateDto(releaseDate));
                });
        contributionEntity.getIdsToUpdate()
                .forEach((key, value) -> {
                    final MovieReleaseDateEntity releaseDateToUpdate = this.entityManager.find(MovieReleaseDateEntity.class, key);
                    final MovieReleaseDateEntity releaseDateUpdated = this.entityManager.find(MovieReleaseDateEntity.class, value);
                    contribution.getElementsToUpdate().put(releaseDateToUpdate.getId(), ServiceUtils.toReleaseDateDto(releaseDateToUpdate));
                    contribution.getElementsUpdated().put(releaseDateToUpdate.getId(), ServiceUtils.toReleaseDateDto(releaseDateUpdated));
                });
        contributionEntity.getIdsToDelete()
                .forEach(id -> {
                    final MovieReleaseDateEntity releaseDate = this.entityManager.find(MovieReleaseDateEntity.class, id);
                    contribution.getElementsToDelete().put(releaseDate.getId(), ServiceUtils.toReleaseDateDto(releaseDate));
                });

        return contribution;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public Contribution<Outline> getOutlineContribution(
            @Min(1) final Long contributionId
    ) throws ResourceNotFoundException {
        log.info("Called with contributionId {}", contributionId);

        final ContributionEntity contributionEntity = this.findContribution(contributionId, MovieField.OUTLINE);
        final Contribution<Outline> contribution = ServiceUtils.toContributionDto(contributionEntity);

        contributionEntity.getIdsToAdd()
                .forEach(id -> {
                    final MovieOutlineEntity outline = this.entityManager.find(MovieOutlineEntity.class, id);
                    contribution.getElementsToAdd().put(outline.getId(), ServiceUtils.toOutlineDto(outline));
                });
        contributionEntity.getIdsToUpdate()
                .forEach((key, value) -> {
                    final MovieOutlineEntity outlineToUpdate = this.entityManager.find(MovieOutlineEntity.class, key);
                    final MovieOutlineEntity outlineUpdated = this.entityManager.find(MovieOutlineEntity.class, value);
                    contribution.getElementsToUpdate().put(outlineToUpdate.getId(), ServiceUtils.toOutlineDto(outlineToUpdate));
                    contribution.getElementsUpdated().put(outlineToUpdate.getId(), ServiceUtils.toOutlineDto(outlineUpdated));
                });
        contributionEntity.getIdsToDelete()
                .forEach(id -> {
                    final MovieOutlineEntity outline = this.entityManager.find(MovieOutlineEntity.class, id);
                    contribution.getElementsToDelete().put(outline.getId(), ServiceUtils.toOutlineDto(outline));
                });

        return contribution;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public Contribution<Summary> getSummaryContribution(
            @Min(1) final Long contributionId
    ) throws ResourceNotFoundException {
        log.info("Called with contributionId {}", contributionId);

        final ContributionEntity contributionEntity = this.findContribution(contributionId, MovieField.SUMMARY);
        final Contribution<Summary> contribution = ServiceUtils.toContributionDto(contributionEntity);

        contributionEntity.getIdsToAdd()
                .forEach(id -> {
                    final MovieSummaryEntity summary = this.entityManager.find(MovieSummaryEntity.class, id);
                    contribution.getElementsToAdd().put(summary.getId(), ServiceUtils.toSummaryDto(summary));
                });
        contributionEntity.getIdsToUpdate()
                .forEach((key, value) -> {
                    final MovieSummaryEntity summaryToUpdate = this.entityManager.find(MovieSummaryEntity.class, key);
                    final MovieSummaryEntity summaryUpdated = this.entityManager.find(MovieSummaryEntity.class, value);
                    contribution.getElementsToUpdate().put(summaryToUpdate.getId(), ServiceUtils.toSummaryDto(summaryToUpdate));
                    contribution.getElementsUpdated().put(summaryToUpdate.getId(), ServiceUtils.toSummaryDto(summaryUpdated));
                });
        contributionEntity.getIdsToDelete()
                .forEach(id -> {
                    final MovieSummaryEntity summary = this.entityManager.find(MovieSummaryEntity.class, id);
                    contribution.getElementsToDelete().put(summary.getId(), ServiceUtils.toSummaryDto(summary));
                });

        return contribution;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public Contribution<Synopsis> getSynopsisContribution(
            @Min(1) final Long contributionId
    ) throws ResourceNotFoundException {
        log.info("Called with contributionId {}", contributionId);

        final ContributionEntity contributionEntity = this.findContribution(contributionId, MovieField.SYNOPSIS);
        final Contribution<Synopsis> contribution = ServiceUtils.toContributionDto(contributionEntity);

        contributionEntity.getIdsToAdd()
                .forEach(id -> {
                    final MovieSynopsisEntity synopsis = this.entityManager.find(MovieSynopsisEntity.class, id);
                    contribution.getElementsToAdd().put(synopsis.getId(), ServiceUtils.toSynopsisDto(synopsis));
                });
        contributionEntity.getIdsToUpdate()
                .forEach((key, value) -> {
                    final MovieSynopsisEntity synopsisToUpdate = this.entityManager.find(MovieSynopsisEntity.class, key);
                    final MovieSynopsisEntity synopsisUpdated = this.entityManager.find(MovieSynopsisEntity.class, value);
                    contribution.getElementsToUpdate().put(synopsisToUpdate.getId(), ServiceUtils.toSynopsisDto(synopsisToUpdate));
                    contribution.getElementsUpdated().put(synopsisToUpdate.getId(), ServiceUtils.toSynopsisDto(synopsisUpdated));
                });
        contributionEntity.getIdsToDelete()
                .forEach(id -> {
                    final MovieSynopsisEntity synopsis = this.entityManager.find(MovieSynopsisEntity.class, id);
                    contribution.getElementsToDelete().put(synopsis.getId(), ServiceUtils.toSynopsisDto(synopsis));
                });

        return contribution;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public Contribution<BoxOffice> getBoxOfficeContribution(
            @Min(1) final Long contributionId
    ) throws ResourceNotFoundException {
        log.info("Called with contributionId {}", contributionId);

        final ContributionEntity contributionEntity = this.findContribution(contributionId, MovieField.BOX_OFFICE);
        final Contribution<BoxOffice> contribution = ServiceUtils.toContributionDto(contributionEntity);

        contributionEntity.getIdsToAdd()
                .forEach(id -> {
                    final MovieBoxOfficeEntity boxOffice = this.entityManager.find(MovieBoxOfficeEntity.class, id);
                    contribution.getElementsToAdd().put(boxOffice.getId(), ServiceUtils.toBoxOfficeDto(boxOffice));
                });
        contributionEntity.getIdsToUpdate()
                .forEach((key, value) -> {
                    final MovieBoxOfficeEntity boxOfficeToUpdate = this.entityManager.find(MovieBoxOfficeEntity.class, key);
                    final MovieBoxOfficeEntity boxOfficeUpdated = this.entityManager.find(MovieBoxOfficeEntity.class, value);
                    contribution.getElementsToUpdate().put(boxOfficeToUpdate.getId(), ServiceUtils.toBoxOfficeDto(boxOfficeToUpdate));
                    contribution.getElementsUpdated().put(boxOfficeToUpdate.getId(), ServiceUtils.toBoxOfficeDto(boxOfficeUpdated));
                });
        contributionEntity.getIdsToDelete()
                .forEach(id -> {
                    final MovieBoxOfficeEntity boxOffice = this.entityManager.find(MovieBoxOfficeEntity.class, id);
                    contribution.getElementsToDelete().put(boxOffice.getId(), ServiceUtils.toBoxOfficeDto(boxOffice));
                });

        return contribution;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public Contribution<Site> getSiteContribution(
            @Min(1) final Long contributionId
    ) throws ResourceNotFoundException {
        log.info("Called with contributionId {}", contributionId);

        final ContributionEntity contributionEntity = this.findContribution(contributionId, MovieField.SITE);
        final Contribution<Site> contribution = ServiceUtils.toContributionDto(contributionEntity);

        contributionEntity.getIdsToAdd()
                .forEach(id -> {
                    final MovieSiteEntity site = this.entityManager.find(MovieSiteEntity.class, id);
                    contribution.getElementsToAdd().put(site.getId(), ServiceUtils.toSiteDto(site));
                });
        contributionEntity.getIdsToUpdate()
                .forEach((key, value) -> {
                    final MovieSiteEntity siteToUpdate = this.entityManager.find(MovieSiteEntity.class, key);
                    final MovieSiteEntity siteUpdated = this.entityManager.find(MovieSiteEntity.class, value);
                    contribution.getElementsToUpdate().put(siteToUpdate.getId(), ServiceUtils.toSiteDto(siteToUpdate));
                    contribution.getElementsUpdated().put(siteToUpdate.getId(), ServiceUtils.toSiteDto(siteUpdated));
                });
        contributionEntity.getIdsToDelete()
                .forEach(id -> {
                    final MovieSiteEntity site = this.entityManager.find(MovieSiteEntity.class, id);
                    contribution.getElementsToDelete().put(site.getId(), ServiceUtils.toSiteDto(site));
                });

        return contribution;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public Contribution<Country> getCountryContribution(
            @Min(1) final Long contributionId
    ) throws ResourceNotFoundException {
        log.info("Called with contributionId {}", contributionId);

        final ContributionEntity contributionEntity = this.findContribution(contributionId, MovieField.COUNTRY);
        final Contribution<Country> contribution = ServiceUtils.toContributionDto(contributionEntity);

        contributionEntity.getIdsToAdd()
                .forEach(id -> {
                    final MovieCountryEntity country = this.entityManager.find(MovieCountryEntity.class, id);
                    contribution.getElementsToAdd().put(country.getId(), ServiceUtils.toCountryDto(country));
                });
        contributionEntity.getIdsToUpdate()
                .forEach((key, value) -> {
                    final MovieCountryEntity countryToUpdate = this.entityManager.find(MovieCountryEntity.class, key);
                    final MovieCountryEntity countryUpdated = this.entityManager.find(MovieCountryEntity.class, value);
                    contribution.getElementsToUpdate().put(countryToUpdate.getId(), ServiceUtils.toCountryDto(countryToUpdate));
                    contribution.getElementsUpdated().put(countryToUpdate.getId(), ServiceUtils.toCountryDto(countryUpdated));
                });
        contributionEntity.getIdsToDelete()
                .forEach(id -> {
                    final MovieCountryEntity country = this.entityManager.find(MovieCountryEntity.class, id);
                    contribution.getElementsToDelete().put(country.getId(), ServiceUtils.toCountryDto(country));
                });

        return contribution;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public Contribution<Language> getLanguageContribution(
            @Min(1) final Long contributionId
    ) throws ResourceNotFoundException {
        log.info("Called with contributionId {}", contributionId);

        final ContributionEntity contributionEntity = this.findContribution(contributionId, MovieField.LANGUAGE);
        final Contribution<Language> contribution = ServiceUtils.toContributionDto(contributionEntity);

        contributionEntity.getIdsToAdd()
                .forEach(id -> {
                    final MovieLanguageEntity language = this.entityManager.find(MovieLanguageEntity.class, id);
                    contribution.getElementsToAdd().put(language.getId(), ServiceUtils.toLanguageDto(language));
                });
        contributionEntity.getIdsToUpdate()
                .forEach((key, value) -> {
                    final MovieLanguageEntity languageToUpdate = this.entityManager.find(MovieLanguageEntity.class, key);
                    final MovieLanguageEntity languageUpdated = this.entityManager.find(MovieLanguageEntity.class, value);
                    contribution.getElementsToUpdate().put(languageToUpdate.getId(), ServiceUtils.toLanguageDto(languageToUpdate));
                    contribution.getElementsUpdated().put(languageToUpdate.getId(), ServiceUtils.toLanguageDto(languageUpdated));
                });
        contributionEntity.getIdsToDelete()
                .forEach(id -> {
                    final MovieLanguageEntity language = this.entityManager.find(MovieLanguageEntity.class, id);
                    contribution.getElementsToDelete().put(language.getId(), ServiceUtils.toLanguageDto(language));
                });

        return contribution;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public Contribution<Genre> getGenreContribution(
            @Min(1) final Long contributionId
    ) throws ResourceNotFoundException {
        log.info("Called with contributionId {}", contributionId);

        final ContributionEntity contributionEntity = this.findContribution(contributionId, MovieField.GENRE);
        final Contribution<Genre> contribution = ServiceUtils.toContributionDto(contributionEntity);

        contributionEntity.getIdsToAdd()
                .forEach(id -> {
                    final MovieGenreEntity genre = this.entityManager.find(MovieGenreEntity.class, id);
                    contribution.getElementsToAdd().put(genre.getId(), ServiceUtils.toGenreDto(genre));
                });
        contributionEntity.getIdsToUpdate()
                .forEach((key, value) -> {
                    final MovieGenreEntity genreToUpdate = this.entityManager.find(MovieGenreEntity.class, key);
                    final MovieGenreEntity genreUpdated = this.entityManager.find(MovieGenreEntity.class, value);
                    contribution.getElementsToUpdate().put(genreToUpdate.getId(), ServiceUtils.toGenreDto(genreToUpdate));
                    contribution.getElementsUpdated().put(genreToUpdate.getId(), ServiceUtils.toGenreDto(genreUpdated));
                });
        contributionEntity.getIdsToDelete()
                .forEach(id -> {
                    final MovieGenreEntity genre = this.entityManager.find(MovieGenreEntity.class, id);
                    contribution.getElementsToDelete().put(genre.getId(), ServiceUtils.toGenreDto(genre));
                });

        return contribution;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public Contribution<Review> getReviewContribution(
            @Min(1) final Long contributionId
    ) throws ResourceNotFoundException {
        log.info("Called with contributionId {}", contributionId);

        final ContributionEntity contributionEntity = this.findContribution(contributionId, MovieField.REVIEW);
        final Contribution<Review> contribution = ServiceUtils.toContributionDto(contributionEntity);

        contributionEntity.getIdsToAdd()
                .forEach(id -> {
                    final MovieReviewEntity review = this.entityManager.find(MovieReviewEntity.class, id);
                    contribution.getElementsToAdd().put(review.getId(), ServiceUtils.toReviewDto(review));
                });
        contributionEntity.getIdsToUpdate()
                .forEach((key, value) -> {
                    final MovieReviewEntity reviewToUpdate = this.entityManager.find(MovieReviewEntity.class, key);
                    final MovieReviewEntity reviewUpdated = this.entityManager.find(MovieReviewEntity.class, value);
                    contribution.getElementsToUpdate().put(reviewToUpdate.getId(), ServiceUtils.toReviewDto(reviewToUpdate));
                    contribution.getElementsUpdated().put(reviewToUpdate.getId(), ServiceUtils.toReviewDto(reviewUpdated));
                });
        contributionEntity.getIdsToDelete()
                .forEach(id -> {
                    final MovieReviewEntity review = this.entityManager.find(MovieReviewEntity.class, id);
                    contribution.getElementsToDelete().put(review.getId(), ServiceUtils.toReviewDto(review));
                });

        return contribution;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public Contribution<ImageResponse> getPhotoContribution(
            @Min(1) final Long contributionId
    ) throws ResourceNotFoundException {
        log.info("Called with contributionId {}", contributionId);

        final ContributionEntity contributionEntity = this.findContribution(contributionId, MovieField.PHOTO);
        final Contribution<ImageResponse> contribution = ServiceUtils.toContributionDto(contributionEntity);

        contributionEntity.getIdsToAdd()
                .forEach(id -> {
                    final MoviePhotoEntity photo = this.entityManager.find(MoviePhotoEntity.class, id);
                    contribution.getElementsToAdd().put(photo.getId(), ServiceUtils.toImageResponseDto(photo));
                });
        contributionEntity.getIdsToUpdate()
                .forEach((key, value) -> {
                    final MoviePhotoEntity photoToUpdate = this.entityManager.find(MoviePhotoEntity.class, key);
                    final MoviePhotoEntity photoUpdated = this.entityManager.find(MoviePhotoEntity.class, value);
                    contribution.getElementsToUpdate().put(photoToUpdate.getId(), ServiceUtils.toImageResponseDto(photoToUpdate));
                    contribution.getElementsUpdated().put(photoToUpdate.getId(), ServiceUtils.toImageResponseDto(photoUpdated));
                });
        contributionEntity.getIdsToDelete()
                .forEach(id -> {
                    final MoviePhotoEntity photo = this.entityManager.find(MoviePhotoEntity.class, id);
                    contribution.getElementsToDelete().put(photo.getId(), ServiceUtils.toImageResponseDto(photo));
                });

        return contribution;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public Contribution<ImageResponse> getPosterContribution(
            @Min(1) final Long contributionId
    ) throws ResourceNotFoundException {
        log.info("Called with contributionId {}", contributionId);

        final ContributionEntity contributionEntity = this.findContribution(contributionId, MovieField.POSTER);
        final Contribution<ImageResponse> contribution = ServiceUtils.toContributionDto(contributionEntity);

        contributionEntity.getIdsToAdd()
                .forEach(id -> {
                    final MoviePosterEntity poster = this.entityManager.find(MoviePosterEntity.class, id);
                    contribution.getElementsToAdd().put(poster.getId(), ServiceUtils.toImageResponseDto(poster));
                });
        contributionEntity.getIdsToUpdate()
                .forEach((key, value) -> {
                    final MoviePosterEntity posterToUpdate = this.entityManager.find(MoviePosterEntity.class, key);
                    final MoviePosterEntity posterUpdated = this.entityManager.find(MoviePosterEntity.class, value);
                    contribution.getElementsToUpdate().put(posterToUpdate.getId(), ServiceUtils.toImageResponseDto(posterToUpdate));
                    contribution.getElementsUpdated().put(posterToUpdate.getId(), ServiceUtils.toImageResponseDto(posterUpdated));
                });
        contributionEntity.getIdsToDelete()
                .forEach(id -> {
                    final MoviePosterEntity poster = this.entityManager.find(MoviePosterEntity.class, id);
                    contribution.getElementsToDelete().put(poster.getId(), ServiceUtils.toImageResponseDto(poster));
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

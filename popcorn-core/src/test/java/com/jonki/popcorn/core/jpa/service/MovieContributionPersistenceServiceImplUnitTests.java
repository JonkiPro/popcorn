package com.jonki.popcorn.core.jpa.service;

import com.google.common.collect.Sets;
import com.jonki.popcorn.common.dto.DataStatus;
import com.jonki.popcorn.common.dto.MovieField;
import com.jonki.popcorn.common.dto.UserMoviePermission;
import com.jonki.popcorn.common.dto.VerificationStatus;
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
import com.jonki.popcorn.common.dto.movie.request.ImageRequest;
import com.jonki.popcorn.common.dto.movie.type.CountryType;
import com.jonki.popcorn.common.dto.movie.type.GenreType;
import com.jonki.popcorn.common.dto.movie.type.LanguageType;
import com.jonki.popcorn.common.dto.movie.type.SiteType;
import com.jonki.popcorn.common.dto.request.ContributionNewRequest;
import com.jonki.popcorn.common.dto.request.ContributionUpdateRequest;
import com.jonki.popcorn.common.exception.ResourceException;
import com.jonki.popcorn.common.exception.ResourceForbiddenException;
import com.jonki.popcorn.common.exception.ResourceNotFoundException;
import com.jonki.popcorn.core.jpa.entity.ContributionEntity;
import com.jonki.popcorn.core.jpa.entity.MovieEntity;
import com.jonki.popcorn.core.jpa.entity.UserEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieOtherTitleEntity;
import com.jonki.popcorn.core.jpa.repository.ContributionRepository;
import com.jonki.popcorn.core.jpa.repository.MovieInfoRepository;
import com.jonki.popcorn.core.jpa.repository.MovieRepository;
import com.jonki.popcorn.core.jpa.repository.UserRepository;
import com.jonki.popcorn.core.service.AuthorizationService;
import com.jonki.popcorn.core.service.MoviePersistenceService;
import com.jonki.popcorn.core.service.StorageService;
import com.jonki.popcorn.test.category.UnitTest;
import com.jonki.popcorn.test.supplier.RandomSupplier;
import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

/**
 * Unit tests for MovieContributionPersistenceServiceImpl.
 */
@Category(UnitTest.class)
public class MovieContributionPersistenceServiceImplUnitTests {

    private ContributionRepository contributionRepository;
    private MovieRepository movieRepository;
    private MovieInfoRepository movieInfoRepository;
    private UserRepository userRepository;
    private MoviePersistenceService moviePersistenceService;
    private AuthorizationService authorizationService;
    private MovieContributionPersistenceServiceImpl movieContributionPersistenceService;

    /**
     * Setup for the tests.
     */
    @Before
    public void setup() {
        this.contributionRepository = Mockito.mock(ContributionRepository.class);
        this.movieRepository = Mockito.mock(MovieRepository.class);
        this.movieInfoRepository = Mockito.mock(MovieInfoRepository.class);
        this.userRepository = Mockito.mock(UserRepository.class);
        this.moviePersistenceService = Mockito.mock(MoviePersistenceService.class);
        this.authorizationService = Mockito.mock(AuthorizationService.class);
        this.movieContributionPersistenceService = new MovieContributionPersistenceServiceImpl(
                this.contributionRepository,
                this.movieRepository,
                this.movieInfoRepository,
                this.userRepository,
                this.moviePersistenceService,
                Mockito.mock(StorageService.class),
                this.authorizationService
        );
    }

    /**
     * Test the updateContributionStatus method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canUpdateContributionStatus() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long contributionId = new Random().nextLong();
        final String comment = UUID.randomUUID().toString();
        final Date date = new Date();
        final VerificationStatus verificationStatus = VerificationStatus.ACCEPT;
        final UserEntity userEntity = new UserEntity();
        userEntity.getPermissions().add(UserMoviePermission.ALL);
        final ContributionEntity contributionEntity = new ContributionEntity();
        contributionEntity.getIdsToAdd().add(1L);
        contributionEntity.setField(MovieField.OTHER_TITLE);
        final MovieOtherTitleEntity movieOtherTitleEntity = new MovieOtherTitleEntity();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.contributionRepository.findByIdAndStatus(contributionId, DataStatus.WAITING))
                .thenReturn(Optional.of(contributionEntity));
        Mockito
                .when(this.movieInfoRepository.getOne(1L))
                .thenReturn(movieOtherTitleEntity);
        this.movieContributionPersistenceService.updateContributionStatus(contributionId, verificationStatus, comment);

        Assert.assertThat(contributionEntity.getVerificationComment().orElseGet(RandomSupplier.STRING), Matchers.is(comment));
        Assert.assertTrue(
                date.before(contributionEntity.getVerificationDate().orElseGet(RandomSupplier.DATE))
                        || date.equals(contributionEntity.getVerificationDate().orElseGet(RandomSupplier.DATE))
        );
        Assert.assertThat(contributionEntity.getVerificationUser().orElse(null), Matchers.is(userEntity));
        Assert.assertThat(contributionEntity.getStatus(), Matchers.is(verificationStatus.getDataStatus()));

        Assert.assertThat(movieOtherTitleEntity.getStatus(), Matchers.is(DataStatus.ACCEPTED));
    }

    /**
     * Test the updateContributionStatus method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceForbiddenException.class)
    public void cantUpdateContributionStatusIfUserDoesNotPermissions() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long contributionId = new Random().nextLong();
        final String comment = UUID.randomUUID().toString();
        final VerificationStatus verificationStatus = VerificationStatus.ACCEPT;
        final UserEntity userEntity = new UserEntity();
        userEntity.getPermissions().add(UserMoviePermission.BOX_OFFICE);
        final ContributionEntity contributionEntity = new ContributionEntity();
        contributionEntity.setField(MovieField.OTHER_TITLE);
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.contributionRepository.findByIdAndStatus(contributionId, DataStatus.WAITING))
                .thenReturn(Optional.of(contributionEntity));
        this.movieContributionPersistenceService.updateContributionStatus(contributionId, verificationStatus, comment);
    }

    /**
     * Test the createOtherTitleContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateOtherTitleContribution() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long movieId = new Random().nextLong();
        final UserEntity userEntity = new UserEntity();
        final MovieEntity movieEntity = new MovieEntity();
        final OtherTitle otherTitle = new OtherTitle.Builder(UUID.randomUUID().toString(), CountryType.USA).build();
        final ContributionNewRequest contributionNewRequest = new ContributionNewRequest.Builder<>(
                Sets.newHashSet(UUID.randomUUID().toString())
        )
                .withElementsToAdd(Lists.newArrayList(otherTitle))
                .build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.movieRepository.findByIdAndStatus(movieId, DataStatus.ACCEPTED))
                .thenReturn(Optional.of(movieEntity));
        Mockito
                .when(this.moviePersistenceService.createOtherTitle(otherTitle, movieEntity))
                .thenReturn(1L);
        this.movieContributionPersistenceService.createOtherTitleContribution(movieId, contributionNewRequest);

        Assert.assertFalse(movieEntity.getContributions().isEmpty());
        Assert.assertThat(new ArrayList<>(movieEntity.getContributions().get(0).getIdsToAdd()).get(0), Matchers.is(1L));
    }

    /**
     * Test the createOtherTitleContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantCreateOtherTitleContributionIfNoElementsFoundForEditingOrDeletion() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long movieId = new Random().nextLong();
        final UserEntity userEntity = new UserEntity();
        final MovieEntity movieEntity = new MovieEntity();
        final Map<Long, Object> map = new HashMap<>();
        map.put(1L, new OtherTitle.Builder(UUID.randomUUID().toString(), CountryType.USA).build());
        final ContributionNewRequest contributionNewRequest = new ContributionNewRequest.Builder<>(
                Sets.newHashSet(UUID.randomUUID().toString())
        )
                .withElementsToUpdate(map)
                .build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.movieRepository.findByIdAndStatus(movieId, DataStatus.ACCEPTED))
                .thenReturn(Optional.of(movieEntity));
        this.movieContributionPersistenceService.createOtherTitleContribution(movieId, contributionNewRequest);
    }

    /**
     * Test the updateOtherTitleContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantUpdateOtherTitleContributionIfNoElementFound() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long movieId = new Random().nextLong();
        final UserEntity userEntity = new UserEntity();
        final ContributionEntity contributionEntity = new ContributionEntity();
        final Map<Long, Object> map = new HashMap<>();
        map.put(1L, new OtherTitle.Builder(UUID.randomUUID().toString(), CountryType.USA).build());
        final ContributionUpdateRequest contributionUpdateRequest = new ContributionUpdateRequest.Builder<>(
                Sets.newHashSet(UUID.randomUUID().toString())
        )
                .withElementsToUpdate(map)
                .build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.contributionRepository.findByIdAndStatusAndUserAndField(movieId, DataStatus.WAITING, userEntity, MovieField.OTHER_TITLE))
                .thenReturn(Optional.of(contributionEntity));
        this.movieContributionPersistenceService.updateOtherTitleContribution(movieId, contributionUpdateRequest);
    }

    /**
     * Test the createReleaseDateContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateReleaseDateContribution() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long movieId = new Random().nextLong();
        final UserEntity userEntity = new UserEntity();
        final MovieEntity movieEntity = new MovieEntity();
        final ReleaseDate releaseDate = new ReleaseDate.Builder(new Date(), CountryType.USA).build();
        final ContributionNewRequest contributionNewRequest = new ContributionNewRequest.Builder<>(
                Sets.newHashSet(UUID.randomUUID().toString())
        )
                .withElementsToAdd(Lists.newArrayList(releaseDate))
                .build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.movieRepository.findByIdAndStatus(movieId, DataStatus.ACCEPTED))
                .thenReturn(Optional.of(movieEntity));
        Mockito
                .when(this.moviePersistenceService.createReleaseDate(releaseDate, movieEntity))
                .thenReturn(1L);
        this.movieContributionPersistenceService.createReleaseDateContribution(movieId, contributionNewRequest);

        Assert.assertFalse(movieEntity.getContributions().isEmpty());
        Assert.assertThat(new ArrayList<>(movieEntity.getContributions().get(0).getIdsToAdd()).get(0), Matchers.is(1L));
    }

    /**
     * Test the createReleaseDateContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantCreateReleaseDateContributionIfNoElementsFoundForEditingOrDeletion() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long movieId = new Random().nextLong();
        final UserEntity userEntity = new UserEntity();
        final MovieEntity movieEntity = new MovieEntity();
        final Map<Long, Object> map = new HashMap<>();
        map.put(1L, new ReleaseDate.Builder(new Date(), CountryType.USA).build());
        final ContributionNewRequest contributionNewRequest = new ContributionNewRequest.Builder<>(
                Sets.newHashSet(UUID.randomUUID().toString())
        )
                .withElementsToUpdate(map)
                .build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.movieRepository.findByIdAndStatus(movieId, DataStatus.ACCEPTED))
                .thenReturn(Optional.of(movieEntity));
        this.movieContributionPersistenceService.createReleaseDateContribution(movieId, contributionNewRequest);
    }

    /**
     * Test the updateReleaseDateContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantUpdateReleaseDateContributionIfNoElementFound() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long movieId = new Random().nextLong();
        final UserEntity userEntity = new UserEntity();
        final ContributionEntity contributionEntity = new ContributionEntity();
        final Map<Long, Object> map = new HashMap<>();
        map.put(1L, new ReleaseDate.Builder(new Date(), CountryType.USA).build());
        final ContributionUpdateRequest contributionUpdateRequest = new ContributionUpdateRequest.Builder<>(
                Sets.newHashSet(UUID.randomUUID().toString())
        )
                .withElementsToUpdate(map)
                .build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.contributionRepository.findByIdAndStatusAndUserAndField(movieId, DataStatus.WAITING, userEntity, MovieField.RELEASE_DATE))
                .thenReturn(Optional.of(contributionEntity));
        this.movieContributionPersistenceService.updateReleaseDateContribution(movieId, contributionUpdateRequest);
    }

    /**
     * Test the createOutlineContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateOutlineContribution() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long movieId = new Random().nextLong();
        final UserEntity userEntity = new UserEntity();
        final MovieEntity movieEntity = new MovieEntity();
        final Outline outline = new Outline.Builder(UUID.randomUUID().toString()).build();
        final ContributionNewRequest contributionNewRequest = new ContributionNewRequest.Builder<>(
                Sets.newHashSet(UUID.randomUUID().toString())
        )
                .withElementsToAdd(Lists.newArrayList(outline))
                .build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.movieRepository.findByIdAndStatus(movieId, DataStatus.ACCEPTED))
                .thenReturn(Optional.of(movieEntity));
        Mockito
                .when(this.moviePersistenceService.createOutline(outline, movieEntity))
                .thenReturn(1L);
        this.movieContributionPersistenceService.createOutlineContribution(movieId, contributionNewRequest);

        Assert.assertFalse(movieEntity.getContributions().isEmpty());
        Assert.assertThat(new ArrayList<>(movieEntity.getContributions().get(0).getIdsToAdd()).get(0), Matchers.is(1L));
    }

    /**
     * Test the createOutlineContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantCreateOutlineContributionIfNoElementsFoundForEditingOrDeletion() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long movieId = new Random().nextLong();
        final UserEntity userEntity = new UserEntity();
        final MovieEntity movieEntity = new MovieEntity();
        final Map<Long, Object> map = new HashMap<>();
        map.put(1L, new Outline.Builder(UUID.randomUUID().toString()).build());
        final ContributionNewRequest contributionNewRequest = new ContributionNewRequest.Builder<>(
                Sets.newHashSet(UUID.randomUUID().toString())
        )
                .withElementsToUpdate(map)
                .build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.movieRepository.findByIdAndStatus(movieId, DataStatus.ACCEPTED))
                .thenReturn(Optional.of(movieEntity));
        this.movieContributionPersistenceService.createOutlineContribution(movieId, contributionNewRequest);
    }

    /**
     * Test the updateOutlineContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantUpdateOutlineContributionIfNoElementFound() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long movieId = new Random().nextLong();
        final UserEntity userEntity = new UserEntity();
        final ContributionEntity contributionEntity = new ContributionEntity();
        final Map<Long, Object> map = new HashMap<>();
        map.put(1L, new Outline.Builder(UUID.randomUUID().toString()).build());
        final ContributionUpdateRequest contributionUpdateRequest = new ContributionUpdateRequest.Builder<>(
                Sets.newHashSet(UUID.randomUUID().toString())
        )
                .withElementsToUpdate(map)
                .build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.contributionRepository.findByIdAndStatusAndUserAndField(movieId, DataStatus.WAITING, userEntity, MovieField.OUTLINE))
                .thenReturn(Optional.of(contributionEntity));
        this.movieContributionPersistenceService.updateOutlineContribution(movieId, contributionUpdateRequest);
    }

    /**
     * Test the createSummaryContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateSummaryContribution() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long movieId = new Random().nextLong();
        final UserEntity userEntity = new UserEntity();
        final MovieEntity movieEntity = new MovieEntity();
        final Summary summary = new Summary.Builder(UUID.randomUUID().toString()).build();
        final ContributionNewRequest contributionNewRequest = new ContributionNewRequest.Builder<>(
                Sets.newHashSet(UUID.randomUUID().toString())
        )
                .withElementsToAdd(Lists.newArrayList(summary))
                .build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.movieRepository.findByIdAndStatus(movieId, DataStatus.ACCEPTED))
                .thenReturn(Optional.of(movieEntity));
        Mockito
                .when(this.moviePersistenceService.createSummary(summary, movieEntity))
                .thenReturn(1L);
        this.movieContributionPersistenceService.createSummaryContribution(movieId, contributionNewRequest);

        Assert.assertFalse(movieEntity.getContributions().isEmpty());
        Assert.assertThat(new ArrayList<>(movieEntity.getContributions().get(0).getIdsToAdd()).get(0), Matchers.is(1L));
    }

    /**
     * Test the createSummaryContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantCreateSummaryContributionIfNoElementsFoundForEditingOrDeletion() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long movieId = new Random().nextLong();
        final UserEntity userEntity = new UserEntity();
        final MovieEntity movieEntity = new MovieEntity();
        final Map<Long, Object> map = new HashMap<>();
        map.put(1L, new Summary.Builder(UUID.randomUUID().toString()).build());
        final ContributionNewRequest contributionNewRequest = new ContributionNewRequest.Builder<>(
                Sets.newHashSet(UUID.randomUUID().toString())
        )
                .withElementsToUpdate(map)
                .build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.movieRepository.findByIdAndStatus(movieId, DataStatus.ACCEPTED))
                .thenReturn(Optional.of(movieEntity));
        this.movieContributionPersistenceService.createSummaryContribution(movieId, contributionNewRequest);
    }

    /**
     * Test the updateSummaryContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantUpdateSummaryContributionIfNoElementFound() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long movieId = new Random().nextLong();
        final UserEntity userEntity = new UserEntity();
        final ContributionEntity contributionEntity = new ContributionEntity();
        final Map<Long, Object> map = new HashMap<>();
        map.put(1L, new Summary.Builder(UUID.randomUUID().toString()).build());
        final ContributionUpdateRequest contributionUpdateRequest = new ContributionUpdateRequest.Builder<>(
                Sets.newHashSet(UUID.randomUUID().toString())
        )
                .withElementsToUpdate(map)
                .build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.contributionRepository.findByIdAndStatusAndUserAndField(movieId, DataStatus.WAITING, userEntity, MovieField.SUMMARY))
                .thenReturn(Optional.of(contributionEntity));
        this.movieContributionPersistenceService.updateSummaryContribution(movieId, contributionUpdateRequest);
    }

    /**
     * Test the createSynopsisContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateSynopsisContribution() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long movieId = new Random().nextLong();
        final UserEntity userEntity = new UserEntity();
        final MovieEntity movieEntity = new MovieEntity();
        final Synopsis synopsis = new Synopsis.Builder(UUID.randomUUID().toString()).build();
        final ContributionNewRequest contributionNewRequest = new ContributionNewRequest.Builder<>(
                Sets.newHashSet(UUID.randomUUID().toString())
        )
                .withElementsToAdd(Lists.newArrayList(synopsis))
                .build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.movieRepository.findByIdAndStatus(movieId, DataStatus.ACCEPTED))
                .thenReturn(Optional.of(movieEntity));
        Mockito
                .when(this.moviePersistenceService.createSynopsis(synopsis, movieEntity))
                .thenReturn(1L);
        this.movieContributionPersistenceService.createSynopsisContribution(movieId, contributionNewRequest);

        Assert.assertFalse(movieEntity.getContributions().isEmpty());
        Assert.assertThat(new ArrayList<>(movieEntity.getContributions().get(0).getIdsToAdd()).get(0), Matchers.is(1L));
    }

    /**
     * Test the createSynopsisContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantCreateSynopsisContributionIfNoElementsFoundForEditingOrDeletion() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long movieId = new Random().nextLong();
        final UserEntity userEntity = new UserEntity();
        final MovieEntity movieEntity = new MovieEntity();
        final Map<Long, Object> map = new HashMap<>();
        map.put(1L, new Synopsis.Builder(UUID.randomUUID().toString()).build());
        final ContributionNewRequest contributionNewRequest = new ContributionNewRequest.Builder<>(
                Sets.newHashSet(UUID.randomUUID().toString())
        )
                .withElementsToUpdate(map)
                .build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.movieRepository.findByIdAndStatus(movieId, DataStatus.ACCEPTED))
                .thenReturn(Optional.of(movieEntity));
        this.movieContributionPersistenceService.createSynopsisContribution(movieId, contributionNewRequest);
    }

    /**
     * Test the updateSynopsisContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantUpdateSynopsisContributionIfNoElementFound() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long movieId = new Random().nextLong();
        final UserEntity userEntity = new UserEntity();
        final ContributionEntity contributionEntity = new ContributionEntity();
        final Map<Long, Object> map = new HashMap<>();
        map.put(1L, new Synopsis.Builder(UUID.randomUUID().toString()).build());
        final ContributionUpdateRequest contributionUpdateRequest = new ContributionUpdateRequest.Builder<>(
                Sets.newHashSet(UUID.randomUUID().toString())
        )
                .withElementsToUpdate(map)
                .build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.contributionRepository.findByIdAndStatusAndUserAndField(movieId, DataStatus.WAITING, userEntity, MovieField.SYNOPSIS))
                .thenReturn(Optional.of(contributionEntity));
        this.movieContributionPersistenceService.updateSynopsisContribution(movieId, contributionUpdateRequest);
    }

    /**
     * Test the createBoxOfficeContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateBoxOfficeContribution() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long movieId = new Random().nextLong();
        final UserEntity userEntity = new UserEntity();
        final MovieEntity movieEntity = new MovieEntity();
        final BoxOffice boxOffice = new BoxOffice.Builder(new BigDecimal("1000"), CountryType.USA).build();
        final ContributionNewRequest contributionNewRequest = new ContributionNewRequest.Builder<>(
                Sets.newHashSet(UUID.randomUUID().toString())
        )
                .withElementsToAdd(Lists.newArrayList(boxOffice))
                .build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.movieRepository.findByIdAndStatus(movieId, DataStatus.ACCEPTED))
                .thenReturn(Optional.of(movieEntity));
        Mockito
                .when(this.moviePersistenceService.createBoxOffice(boxOffice, movieEntity))
                .thenReturn(1L);
        this.movieContributionPersistenceService.createBoxOfficeContribution(movieId, contributionNewRequest);

        Assert.assertFalse(movieEntity.getContributions().isEmpty());
        Assert.assertThat(new ArrayList<>(movieEntity.getContributions().get(0).getIdsToAdd()).get(0), Matchers.is(1L));
    }

    /**
     * Test the createBoxOfficeContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantCreateBoxOfficeContributionIfNoElementsFoundForEditingOrDeletion() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long movieId = new Random().nextLong();
        final UserEntity userEntity = new UserEntity();
        final MovieEntity movieEntity = new MovieEntity();
        final Map<Long, Object> map = new HashMap<>();
        map.put(1L, new BoxOffice.Builder(new BigDecimal("1000"), CountryType.USA).build());
        final ContributionNewRequest contributionNewRequest = new ContributionNewRequest.Builder<>(
                Sets.newHashSet(UUID.randomUUID().toString())
        )
                .withElementsToUpdate(map)
                .build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.movieRepository.findByIdAndStatus(movieId, DataStatus.ACCEPTED))
                .thenReturn(Optional.of(movieEntity));
        this.movieContributionPersistenceService.createBoxOfficeContribution(movieId, contributionNewRequest);
    }

    /**
     * Test the updateBoxOfficeContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantUpdateBoxOfficeContributionIfNoElementFound() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long movieId = new Random().nextLong();
        final UserEntity userEntity = new UserEntity();
        final ContributionEntity contributionEntity = new ContributionEntity();
        final Map<Long, Object> map = new HashMap<>();
        map.put(1L, new BoxOffice.Builder(new BigDecimal("1000"), CountryType.USA).build());
        final ContributionUpdateRequest contributionUpdateRequest = new ContributionUpdateRequest.Builder<>(
                Sets.newHashSet(UUID.randomUUID().toString())
        )
                .withElementsToUpdate(map)
                .build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.contributionRepository.findByIdAndStatusAndUserAndField(movieId, DataStatus.WAITING, userEntity, MovieField.BOX_OFFICE))
                .thenReturn(Optional.of(contributionEntity));
        this.movieContributionPersistenceService.updateBoxOfficeContribution(movieId, contributionUpdateRequest);
    }

    /**
     * Test the createSiteContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateSiteContribution() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long movieId = new Random().nextLong();
        final UserEntity userEntity = new UserEntity();
        final MovieEntity movieEntity = new MovieEntity();
        final Site site = new Site.Builder(UUID.randomUUID().toString(), SiteType.OFFICIAL).build();
        final ContributionNewRequest contributionNewRequest = new ContributionNewRequest.Builder<>(
                Sets.newHashSet(UUID.randomUUID().toString())
        )
                .withElementsToAdd(Lists.newArrayList(site))
                .build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.movieRepository.findByIdAndStatus(movieId, DataStatus.ACCEPTED))
                .thenReturn(Optional.of(movieEntity));
        Mockito
                .when(this.moviePersistenceService.createSite(site, movieEntity))
                .thenReturn(1L);
        this.movieContributionPersistenceService.createSiteContribution(movieId, contributionNewRequest);

        Assert.assertFalse(movieEntity.getContributions().isEmpty());
        Assert.assertThat(new ArrayList<>(movieEntity.getContributions().get(0).getIdsToAdd()).get(0), Matchers.is(1L));
    }

    /**
     * Test the createSiteContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantCreateSiteContributionIfNoElementsFoundForEditingOrDeletion() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long movieId = new Random().nextLong();
        final UserEntity userEntity = new UserEntity();
        final MovieEntity movieEntity = new MovieEntity();
        final Map<Long, Object> map = new HashMap<>();
        map.put(1L, new Site.Builder(UUID.randomUUID().toString(), SiteType.OFFICIAL).build());
        final ContributionNewRequest contributionNewRequest = new ContributionNewRequest.Builder<>(
                Sets.newHashSet(UUID.randomUUID().toString())
        )
                .withElementsToUpdate(map)
                .build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.movieRepository.findByIdAndStatus(movieId, DataStatus.ACCEPTED))
                .thenReturn(Optional.of(movieEntity));
        this.movieContributionPersistenceService.createSiteContribution(movieId, contributionNewRequest);
    }

    /**
     * Test the updateSiteContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantUpdateSiteContributionIfNoElementFound() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long movieId = new Random().nextLong();
        final UserEntity userEntity = new UserEntity();
        final ContributionEntity contributionEntity = new ContributionEntity();
        final Map<Long, Object> map = new HashMap<>();
        map.put(1L, new Site.Builder(UUID.randomUUID().toString(), SiteType.OFFICIAL).build());
        final ContributionUpdateRequest contributionUpdateRequest = new ContributionUpdateRequest.Builder<>(
                Sets.newHashSet(UUID.randomUUID().toString())
        )
                .withElementsToUpdate(map)
                .build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.contributionRepository.findByIdAndStatusAndUserAndField(movieId, DataStatus.WAITING, userEntity, MovieField.SITE))
                .thenReturn(Optional.of(contributionEntity));
        this.movieContributionPersistenceService.updateSiteContribution(movieId, contributionUpdateRequest);
    }

    /**
     * Test the createCountryContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateCountryContribution() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long movieId = new Random().nextLong();
        final UserEntity userEntity = new UserEntity();
        final MovieEntity movieEntity = new MovieEntity();
        final Country country = new Country.Builder(CountryType.USA).build();
        final ContributionNewRequest contributionNewRequest = new ContributionNewRequest.Builder<>(
                Sets.newHashSet(UUID.randomUUID().toString())
        )
                .withElementsToAdd(Lists.newArrayList(country))
                .build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.movieRepository.findByIdAndStatus(movieId, DataStatus.ACCEPTED))
                .thenReturn(Optional.of(movieEntity));
        Mockito
                .when(this.moviePersistenceService.createCountry(country, movieEntity))
                .thenReturn(1L);
        this.movieContributionPersistenceService.createCountryContribution(movieId, contributionNewRequest);

        Assert.assertFalse(movieEntity.getContributions().isEmpty());
        Assert.assertThat(new ArrayList<>(movieEntity.getContributions().get(0).getIdsToAdd()).get(0), Matchers.is(1L));
    }

    /**
     * Test the createCountryContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantCreateCountryContributionIfNoElementsFoundForEditingOrDeletion() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long movieId = new Random().nextLong();
        final UserEntity userEntity = new UserEntity();
        final MovieEntity movieEntity = new MovieEntity();
        final Map<Long, Object> map = new HashMap<>();
        map.put(1L, new Country.Builder(CountryType.USA).build());
        final ContributionNewRequest contributionNewRequest = new ContributionNewRequest.Builder<>(
                Sets.newHashSet(UUID.randomUUID().toString())
        )
                .withElementsToUpdate(map)
                .build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.movieRepository.findByIdAndStatus(movieId, DataStatus.ACCEPTED))
                .thenReturn(Optional.of(movieEntity));
        this.movieContributionPersistenceService.createCountryContribution(movieId, contributionNewRequest);
    }

    /**
     * Test the updateCountryContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantUpdateCountryContributionIfNoElementFound() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long movieId = new Random().nextLong();
        final UserEntity userEntity = new UserEntity();
        final ContributionEntity contributionEntity = new ContributionEntity();
        final Map<Long, Object> map = new HashMap<>();
        map.put(1L, new Country.Builder(CountryType.USA).build());
        final ContributionUpdateRequest contributionUpdateRequest = new ContributionUpdateRequest.Builder<>(
                Sets.newHashSet(UUID.randomUUID().toString())
        )
                .withElementsToUpdate(map)
                .build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.contributionRepository.findByIdAndStatusAndUserAndField(movieId, DataStatus.WAITING, userEntity, MovieField.COUNTRY))
                .thenReturn(Optional.of(contributionEntity));
        this.movieContributionPersistenceService.updateCountryContribution(movieId, contributionUpdateRequest);
    }

    /**
     * Test the createLanguageContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateLanguageContribution() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long movieId = new Random().nextLong();
        final UserEntity userEntity = new UserEntity();
        final MovieEntity movieEntity = new MovieEntity();
        final Language language = new Language.Builder(LanguageType.ENGLISH).build();
        final ContributionNewRequest contributionNewRequest = new ContributionNewRequest.Builder<>(
                Sets.newHashSet(UUID.randomUUID().toString())
        )
                .withElementsToAdd(Lists.newArrayList(language))
                .build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.movieRepository.findByIdAndStatus(movieId, DataStatus.ACCEPTED))
                .thenReturn(Optional.of(movieEntity));
        Mockito
                .when(this.moviePersistenceService.createLanguage(language, movieEntity))
                .thenReturn(1L);
        this.movieContributionPersistenceService.createLanguageContribution(movieId, contributionNewRequest);

        Assert.assertFalse(movieEntity.getContributions().isEmpty());
        Assert.assertThat(new ArrayList<>(movieEntity.getContributions().get(0).getIdsToAdd()).get(0), Matchers.is(1L));
    }

    /**
     * Test the createLanguageContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantCreateLanguageContributionIfNoElementsFoundForEditingOrDeletion() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long movieId = new Random().nextLong();
        final UserEntity userEntity = new UserEntity();
        final MovieEntity movieEntity = new MovieEntity();
        final Map<Long, Object> map = new HashMap<>();
        map.put(1L, new Language.Builder(LanguageType.ENGLISH).build());
        final ContributionNewRequest contributionNewRequest = new ContributionNewRequest.Builder<>(
                Sets.newHashSet(UUID.randomUUID().toString())
        )
                .withElementsToUpdate(map)
                .build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.movieRepository.findByIdAndStatus(movieId, DataStatus.ACCEPTED))
                .thenReturn(Optional.of(movieEntity));
        this.movieContributionPersistenceService.createLanguageContribution(movieId, contributionNewRequest);
    }

    /**
     * Test the updateLanguageContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantUpdateLanguageContributionIfNoElementFound() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long movieId = new Random().nextLong();
        final UserEntity userEntity = new UserEntity();
        final ContributionEntity contributionEntity = new ContributionEntity();
        final Map<Long, Object> map = new HashMap<>();
        map.put(1L, new Language.Builder(LanguageType.ENGLISH).build());
        final ContributionUpdateRequest contributionUpdateRequest = new ContributionUpdateRequest.Builder<>(
                Sets.newHashSet(UUID.randomUUID().toString())
        )
                .withElementsToUpdate(map)
                .build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.contributionRepository.findByIdAndStatusAndUserAndField(movieId, DataStatus.WAITING, userEntity, MovieField.LANGUAGE))
                .thenReturn(Optional.of(contributionEntity));
        this.movieContributionPersistenceService.updateLanguageContribution(movieId, contributionUpdateRequest);
    }

    /**
     * Test the createGenreContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateGenreContribution() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long movieId = new Random().nextLong();
        final UserEntity userEntity = new UserEntity();
        final MovieEntity movieEntity = new MovieEntity();
        final Genre genre = new Genre.Builder(GenreType.ACTION).build();
        final ContributionNewRequest contributionNewRequest = new ContributionNewRequest.Builder<>(
                Sets.newHashSet(UUID.randomUUID().toString())
        )
                .withElementsToAdd(Lists.newArrayList(genre))
                .build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.movieRepository.findByIdAndStatus(movieId, DataStatus.ACCEPTED))
                .thenReturn(Optional.of(movieEntity));
        Mockito
                .when(this.moviePersistenceService.createGenre(genre, movieEntity))
                .thenReturn(1L);
        this.movieContributionPersistenceService.createGenreContribution(movieId, contributionNewRequest);

        Assert.assertFalse(movieEntity.getContributions().isEmpty());
        Assert.assertThat(new ArrayList<>(movieEntity.getContributions().get(0).getIdsToAdd()).get(0), Matchers.is(1L));
    }

    /**
     * Test the createGenreContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantCreateGenreContributionIfNoElementsFoundForEditingOrDeletion() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long movieId = new Random().nextLong();
        final UserEntity userEntity = new UserEntity();
        final MovieEntity movieEntity = new MovieEntity();
        final Map<Long, Object> map = new HashMap<>();
        map.put(1L, new Genre.Builder(GenreType.ACTION).build());
        final ContributionNewRequest contributionNewRequest = new ContributionNewRequest.Builder<>(
                Sets.newHashSet(UUID.randomUUID().toString())
        )
                .withElementsToUpdate(map)
                .build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.movieRepository.findByIdAndStatus(movieId, DataStatus.ACCEPTED))
                .thenReturn(Optional.of(movieEntity));
        this.movieContributionPersistenceService.createGenreContribution(movieId, contributionNewRequest);
    }

    /**
     * Test the updateGenreContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantUpdateGenreContributionIfNoElementFound() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long movieId = new Random().nextLong();
        final UserEntity userEntity = new UserEntity();
        final ContributionEntity contributionEntity = new ContributionEntity();
        final Map<Long, Object> map = new HashMap<>();
        map.put(1L, new Genre.Builder(GenreType.ACTION).build());
        final ContributionUpdateRequest contributionUpdateRequest = new ContributionUpdateRequest.Builder<>(
                Sets.newHashSet(UUID.randomUUID().toString())
        )
                .withElementsToUpdate(map)
                .build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.contributionRepository.findByIdAndStatusAndUserAndField(movieId, DataStatus.WAITING, userEntity, MovieField.GENRE))
                .thenReturn(Optional.of(contributionEntity));
        this.movieContributionPersistenceService.updateGenreContribution(movieId, contributionUpdateRequest);
    }

    /**
     * Test the createReviewContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateReviewContribution() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long movieId = new Random().nextLong();
        final UserEntity userEntity = new UserEntity();
        final MovieEntity movieEntity = new MovieEntity();
        final Review review = new Review.Builder(UUID.randomUUID().toString(), UUID.randomUUID().toString(), true).build();
        final ContributionNewRequest contributionNewRequest = new ContributionNewRequest.Builder<>(
                Sets.newHashSet(UUID.randomUUID().toString())
        )
                .withElementsToAdd(Lists.newArrayList(review))
                .build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.movieRepository.findByIdAndStatus(movieId, DataStatus.ACCEPTED))
                .thenReturn(Optional.of(movieEntity));
        Mockito
                .when(this.moviePersistenceService.createReview(review, movieEntity))
                .thenReturn(1L);
        this.movieContributionPersistenceService.createReviewContribution(movieId, contributionNewRequest);

        Assert.assertFalse(movieEntity.getContributions().isEmpty());
        Assert.assertThat(new ArrayList<>(movieEntity.getContributions().get(0).getIdsToAdd()).get(0), Matchers.is(1L));
    }

    /**
     * Test the createReviewContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantCreateReviewContributionIfNoElementsFoundForEditingOrDeletion() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long movieId = new Random().nextLong();
        final UserEntity userEntity = new UserEntity();
        final MovieEntity movieEntity = new MovieEntity();
        final Map<Long, Object> map = new HashMap<>();
        map.put(1L, new Review.Builder(UUID.randomUUID().toString(), UUID.randomUUID().toString(), true).build());
        final ContributionNewRequest contributionNewRequest = new ContributionNewRequest.Builder<>(
                Sets.newHashSet(UUID.randomUUID().toString())
        )
                .withElementsToUpdate(map)
                .build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.movieRepository.findByIdAndStatus(movieId, DataStatus.ACCEPTED))
                .thenReturn(Optional.of(movieEntity));
        this.movieContributionPersistenceService.createReviewContribution(movieId, contributionNewRequest);
    }

    /**
     * Test the updateReviewContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantUpdateReviewContributionIfNoElementFound() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long movieId = new Random().nextLong();
        final UserEntity userEntity = new UserEntity();
        final ContributionEntity contributionEntity = new ContributionEntity();
        final Map<Long, Object> map = new HashMap<>();
        map.put(1L, new Review.Builder(UUID.randomUUID().toString(), UUID.randomUUID().toString(), true).build());
        final ContributionUpdateRequest contributionUpdateRequest = new ContributionUpdateRequest.Builder<>(
                Sets.newHashSet(UUID.randomUUID().toString())
        )
                .withElementsToUpdate(map)
                .build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.contributionRepository.findByIdAndStatusAndUserAndField(movieId, DataStatus.WAITING, userEntity, MovieField.REVIEW))
                .thenReturn(Optional.of(contributionEntity));
        this.movieContributionPersistenceService.updateReviewContribution(movieId, contributionUpdateRequest);
    }

    /**
     * Test the createPhotoContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreatePhotoContribution() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long movieId = new Random().nextLong();
        final UserEntity userEntity = new UserEntity();
        final MovieEntity movieEntity = new MovieEntity();
        final ImageRequest imageRequest = new ImageRequest.Builder()
                .withFile(Mockito.mock(File.class))
                .withIdInCloud(UUID.randomUUID().toString())
                .build();
        final ContributionNewRequest contributionNewRequest = new ContributionNewRequest.Builder<>(
                Sets.newHashSet(UUID.randomUUID().toString())
        )
                .withElementsToAdd(Lists.newArrayList(imageRequest))
                .build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.movieRepository.findByIdAndStatus(movieId, DataStatus.ACCEPTED))
                .thenReturn(Optional.of(movieEntity));
        Mockito
                .when(this.moviePersistenceService.createPhoto(imageRequest, movieEntity))
                .thenReturn(1L);
        this.movieContributionPersistenceService.createPhotoContribution(movieId, contributionNewRequest);

        Assert.assertFalse(movieEntity.getContributions().isEmpty());
        Assert.assertThat(new ArrayList<>(movieEntity.getContributions().get(0).getIdsToAdd()).get(0), Matchers.is(1L));
    }

    /**
     * Test the createPhotoContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantCreatePhotoContributionIfNoElementsFoundForEditingOrDeletion() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long movieId = new Random().nextLong();
        final UserEntity userEntity = new UserEntity();
        final MovieEntity movieEntity = new MovieEntity();
        final Map<Long, Object> map = new HashMap<>();
        map.put(1L,
                new ImageRequest.Builder()
                        .withFile(Mockito.mock(File.class))
                        .withIdInCloud(UUID.randomUUID().toString())
                        .build()
        );
        final ContributionNewRequest contributionNewRequest = new ContributionNewRequest.Builder<>(
                Sets.newHashSet(UUID.randomUUID().toString())
        )
                .withElementsToUpdate(map)
                .build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.movieRepository.findByIdAndStatus(movieId, DataStatus.ACCEPTED))
                .thenReturn(Optional.of(movieEntity));
        this.movieContributionPersistenceService.createPhotoContribution(movieId, contributionNewRequest);
    }

    /**
     * Test the updatePhotoContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantUpdatePhotoContributionIfNoElementFound() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long movieId = new Random().nextLong();
        final UserEntity userEntity = new UserEntity();
        final ContributionEntity contributionEntity = new ContributionEntity();
        final Map<Long, Object> map = new HashMap<>();
        map.put(1L,
                new ImageRequest.Builder()
                        .withFile(Mockito.mock(File.class))
                        .withIdInCloud(UUID.randomUUID().toString())
                        .build()
        );
        final ContributionUpdateRequest contributionUpdateRequest = new ContributionUpdateRequest.Builder<>(
                Sets.newHashSet(UUID.randomUUID().toString())
        )
                .withElementsToUpdate(map)
                .build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.contributionRepository.findByIdAndStatusAndUserAndField(movieId, DataStatus.WAITING, userEntity, MovieField.PHOTO))
                .thenReturn(Optional.of(contributionEntity));
        this.movieContributionPersistenceService.updatePhotoContribution(movieId, contributionUpdateRequest);
    }

    /**
     * Test the createPosterContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreatePosterContribution() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long movieId = new Random().nextLong();
        final UserEntity userEntity = new UserEntity();
        final MovieEntity movieEntity = new MovieEntity();
        final ImageRequest imageRequest = new ImageRequest.Builder()
                .withFile(Mockito.mock(File.class))
                .withIdInCloud(UUID.randomUUID().toString())
                .build();
        final ContributionNewRequest contributionNewRequest = new ContributionNewRequest.Builder<>(
                Sets.newHashSet(UUID.randomUUID().toString())
        )
                .withElementsToAdd(Lists.newArrayList(imageRequest))
                .build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.movieRepository.findByIdAndStatus(movieId, DataStatus.ACCEPTED))
                .thenReturn(Optional.of(movieEntity));
        Mockito
                .when(this.moviePersistenceService.createPoster(imageRequest, movieEntity))
                .thenReturn(1L);
        this.movieContributionPersistenceService.createPosterContribution(movieId, contributionNewRequest);

        Assert.assertFalse(movieEntity.getContributions().isEmpty());
        Assert.assertThat(new ArrayList<>(movieEntity.getContributions().get(0).getIdsToAdd()).get(0), Matchers.is(1L));
    }

    /**
     * Test the createPosterContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantCreatePosterContributionIfNoElementsFoundForEditingOrDeletion() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long movieId = new Random().nextLong();
        final UserEntity userEntity = new UserEntity();
        final MovieEntity movieEntity = new MovieEntity();
        final Map<Long, Object> map = new HashMap<>();
        map.put(1L,
                new ImageRequest.Builder()
                        .withFile(Mockito.mock(File.class))
                        .withIdInCloud(UUID.randomUUID().toString())
                        .build()
        );
        final ContributionNewRequest contributionNewRequest = new ContributionNewRequest.Builder<>(
                Sets.newHashSet(UUID.randomUUID().toString())
        )
                .withElementsToUpdate(map)
                .build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.movieRepository.findByIdAndStatus(movieId, DataStatus.ACCEPTED))
                .thenReturn(Optional.of(movieEntity));
        this.movieContributionPersistenceService.createPosterContribution(movieId, contributionNewRequest);
    }

    /**
     * Test the updatePosterContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantUpdatePosterContributionIfNoElementFound() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long movieId = new Random().nextLong();
        final UserEntity userEntity = new UserEntity();
        final ContributionEntity contributionEntity = new ContributionEntity();
        final Map<Long, Object> map = new HashMap<>();
        map.put(1L,
                new ImageRequest.Builder()
                        .withFile(Mockito.mock(File.class))
                        .withIdInCloud(UUID.randomUUID().toString())
                        .build()
        );
        final ContributionUpdateRequest contributionUpdateRequest = new ContributionUpdateRequest.Builder<>(
                Sets.newHashSet(UUID.randomUUID().toString())
        )
                .withElementsToUpdate(map)
                .build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.contributionRepository.findByIdAndStatusAndUserAndField(movieId, DataStatus.WAITING, userEntity, MovieField.POSTER))
                .thenReturn(Optional.of(contributionEntity));
        this.movieContributionPersistenceService.updatePosterContribution(movieId, contributionUpdateRequest);
    }
}

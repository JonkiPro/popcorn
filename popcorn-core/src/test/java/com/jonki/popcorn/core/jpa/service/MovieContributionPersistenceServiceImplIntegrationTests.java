package com.jonki.popcorn.core.jpa.service;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.jonki.popcorn.common.dto.Contribution;
import com.jonki.popcorn.common.dto.DataStatus;
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
import com.jonki.popcorn.core.jpa.entity.movie.MovieBoxOfficeEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieCountryEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieGenreEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieLanguageEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieOtherTitleEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieOutlineEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieReleaseDateEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieReviewEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieSiteEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieSummaryEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieSynopsisEntity;
import com.jonki.popcorn.core.security.model.CustomUserDetails;
import com.jonki.popcorn.core.service.MovieContributionPersistenceService;
import com.jonki.popcorn.core.service.MovieContributionSearchService;
import com.jonki.popcorn.test.category.IntegrationTest;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

/**
 * Integration tests for MovieContributionPersistenceServiceImpl.
 */
@Category(IntegrationTest.class)
@DatabaseSetup("MovieContributionPersistenceServiceImplIntegrationTests/init.xml")
@DatabaseTearDown("cleanup.xml")
@Transactional
public class MovieContributionPersistenceServiceImplIntegrationTests extends DBIntegrationTestBase {

    private static final String USER_1_ID = "user1";
    private static final String USER_1_USERNAME = "jonki1";
    private static final String USER_1_PASSWORD = "$2a$12$O02MQJS6AnCJiRw1nF6XsO7udBCza591oUKUbRC4/XrUmPXZaSjVK";

    private static final Long MOV_1_ID = 2L;
    private static final Long MOV_2_ID = 3L;

    private static final Long OTHER_TITLE_ID = 33L;
    private static final String OTHER_TITLE = "Film1";
    private static final CountryType OTHER_TITLE_COUNTRY = CountryType.POLAND;

    private static final Long RELEASE_DATE_ID = 21L;
    private static final String RELEASE_DATE = "2018-07-24";
    private static final CountryType RELEASE_DATE_COUNTRY = CountryType.USA;

    private static final Long OUTLINE_ID = 22L;
    private static final String OUTLINE = "Some outline";

    private static final Long SUMMARY_ID = 23L;
    private static final String SUMMARY = "Some summary";

    private static final Long SYNOPSIS_ID = 24L;
    private static final String SYNOPSIS = "Some synopsis";

    private static final Long BOX_OFFICE_ID = 25L;
    private static final BigDecimal BOX_OFFICE = new BigDecimal("1000.00");
    private static final CountryType BOX_OFFICE_COUNTRY = CountryType.USA;

    private static final Long SITE_ID = 26L;
    private static final String SITE = "http://www.someUrl.com";
    private static final SiteType SITE_OFFICIAL = SiteType.NON_OFFICIAL;

    private static final Long COUNTRY_ID = 27L;
    private static final CountryType COUNTRY = CountryType.USA;

    private static final Long LANGUAGE_ID = 28L;
    private static final LanguageType LANGUAGE = LanguageType.ENGLISH;

    private static final Long GENRE_ID = 29L;
    private static final GenreType GENRE = GenreType.COMEDY;

    private static final Long REVIEW_ID = 34L;
    private static final String REVIEW = "Text";
    private static final String REVIEW_TITLE = "Title";
    private static final boolean REVIEW_SPOILER = false;

    private static final Long PHOTO_ID = 31L;
    private static final String PHOTO_ID_IN_CLOUD = "1234";

    private static final Long POSTER_ID = 32L;
    private static final String POSTER_ID_IN_CLOUD = "2345";

    private static final Long CONTR_1_ID = 2L;
    private static final Long CONTR_2_ID = 3L;
    private static final Long CONTR_3_ID = 4L;
    private static final Long CONTR_4_ID = 5L;
    private static final Long CONTR_5_ID = 6L;
    private static final Long CONTR_6_ID = 7L;
    private static final Long CONTR_7_ID = 8L;
    private static final Long CONTR_8_ID = 9L;
    private static final Long CONTR_9_ID = 10L;
    private static final Long CONTR_10_ID = 11L;
    private static final Long CONTR_11_ID = 12L;
    private static final Long CONTR_12_ID = 13L;
    private static final Long CONTR_13_ID = 14L;
    private static final Long CONTR_14_ID = 15L;

    @Autowired
    private MovieContributionPersistenceService movieContributionPersistenceService;

    @Autowired
    private MovieContributionSearchService movieContributionSearchService;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Setup.
     */
    @Before
    public void setup() {
        Assert.assertThat(this.contributionRepository.count(), Matchers.is(14L));

        final Authentication authentication = new UsernamePasswordAuthenticationToken(
                new CustomUserDetails(
                        USER_1_USERNAME,
                        USER_1_PASSWORD,
                        AuthorityUtils.createAuthorityList("ROLE_USER"),
                        USER_1_ID
                ),
                null,
                AuthorityUtils.createAuthorityList("ROLE_USER"));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * Test the updateContributionStatus method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canUpdateContributionStatus() throws ResourceException {
        final String comment = UUID.randomUUID().toString();
        final Date date = new Date();
        this.movieContributionPersistenceService.updateContributionStatus(
                CONTR_1_ID,
                VerificationStatus.ACCEPT,
                comment
        );

        final Contribution<ReleaseDate> contribution
                = this.movieContributionSearchService.getReleaseDateContribution(CONTR_1_ID);

        Assert.assertThat(contribution.getVerificationComment(), Matchers.is(comment));
        Assert.assertTrue(
                date.before(contribution.getVerificationDate())
                        || date.equals(contribution.getVerificationDate())
        );
        Assert.assertThat(contribution.getVerificationUser().getUsername(), Matchers.is(USER_1_USERNAME));
        Assert.assertThat(contribution.getStatus(), Matchers.is(DataStatus.ACCEPTED));
    }

    /**
     * Test the createOtherTitleContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateOtherTitleContribution() throws ResourceException {
        final OtherTitle otherTitle = new OtherTitle.Builder(OTHER_TITLE, OTHER_TITLE_COUNTRY).build();
        final ContributionNewRequest contributionNewRequest = new ContributionNewRequest.Builder<>(
                Sets.newHashSet("http://www.someUrl.com")
        )
                .withElementsToAdd(Lists.newArrayList(otherTitle))
                .build();

        Assert.assertThat(this.movieRepository.getOne(MOV_1_ID).getContributions().size(), Matchers.is(13));

        this.movieContributionPersistenceService.createOtherTitleContribution(MOV_1_ID, contributionNewRequest);

        Assert.assertThat(this.movieRepository.getOne(MOV_1_ID).getContributions().size(), Matchers.is(14));
    }

    /**
     * Test the updateOtherTitleContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canUpdateOtherTitleContribution() throws ResourceException {
        final String newOtherTitle = UUID.randomUUID().toString();
        final CountryType newCountry = CountryType.USA;
        final Map<Long, Object> map = new HashMap<>();
        map.put(OTHER_TITLE_ID, new OtherTitle.Builder(newOtherTitle, newCountry).build());
        final ContributionUpdateRequest contributionUpdateRequest = new ContributionUpdateRequest.Builder<>(
                Sets.newHashSet("http://www.someUrl.com")
        )
                .withElementsToAdd(map)
                .build();

        final Long id
                = new ArrayList<>(this.movieRepository.getOne(MOV_1_ID).getContributions().get(12).getIdsToAdd()).get(0);

        MovieOtherTitleEntity movieOtherTitleEntity = this.entityManager.find(MovieOtherTitleEntity.class, id);

        Assert.assertThat(movieOtherTitleEntity.getTitle(), Matchers.is(OTHER_TITLE));
        Assert.assertThat(movieOtherTitleEntity.getCountry(), Matchers.is(OTHER_TITLE_COUNTRY));

        this.movieContributionPersistenceService.updateOtherTitleContribution(CONTR_13_ID, contributionUpdateRequest);

        movieOtherTitleEntity = this.entityManager.find(MovieOtherTitleEntity.class, id);

        Assert.assertThat(movieOtherTitleEntity.getTitle(), Matchers.is(newOtherTitle));
        Assert.assertThat(movieOtherTitleEntity.getCountry(), Matchers.is(newCountry));
    }

    /**
     * Test the createReleaseDateContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateReleaseDateContribution() throws ResourceException {
        final ReleaseDate releaseDate = new ReleaseDate.Builder(new Date(), CountryType.USA).build();
        final ContributionNewRequest contributionNewRequest = new ContributionNewRequest.Builder<>(
                Sets.newHashSet("http://www.someUrl.com")
        )
                .withElementsToAdd(Lists.newArrayList(releaseDate))
                .build();

        Assert.assertThat(this.movieRepository.getOne(MOV_1_ID).getContributions().size(), Matchers.is(13));

        this.movieContributionPersistenceService.createReleaseDateContribution(MOV_1_ID, contributionNewRequest);

        Assert.assertThat(this.movieRepository.getOne(MOV_1_ID).getContributions().size(), Matchers.is(14));
    }

    /**
     * Test the updateReleaseDateContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canUpdateReleaseDateContribution() throws ResourceException {
        final Date newDate = new Date();
        final CountryType newCountry = CountryType.POLAND;
        final Map<Long, Object> map = new HashMap<>();
        map.put(RELEASE_DATE_ID, new ReleaseDate.Builder(newDate, newCountry).build());
        final ContributionUpdateRequest contributionUpdateRequest = new ContributionUpdateRequest.Builder<>(
                Sets.newHashSet("http://www.someUrl.com")
        )
                .withElementsToAdd(map)
                .build();

        final Long id
                = new ArrayList<>(this.movieRepository.getOne(MOV_1_ID).getContributions().get(0).getIdsToAdd()).get(0);

        MovieReleaseDateEntity movieReleaseDateEntity = this.entityManager.find(MovieReleaseDateEntity.class, id);

        Assert.assertThat(movieReleaseDateEntity.getDate().toString(), Matchers.is(RELEASE_DATE));
        Assert.assertThat(movieReleaseDateEntity.getCountry(), Matchers.is(RELEASE_DATE_COUNTRY));

        this.movieContributionPersistenceService.updateReleaseDateContribution(CONTR_1_ID, contributionUpdateRequest);

        movieReleaseDateEntity = this.entityManager.find(MovieReleaseDateEntity.class, id);

        Assert.assertThat(movieReleaseDateEntity.getDate().toString(), Matchers.is(newDate.toString()));
        Assert.assertThat(movieReleaseDateEntity.getCountry(), Matchers.is(newCountry));
    }

    /**
     * Test the createOutlineContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateOutlineContribution() throws ResourceException {
        final Outline outline = new Outline.Builder(UUID.randomUUID().toString()).build();
        final ContributionNewRequest contributionNewRequest = new ContributionNewRequest.Builder<>(
                Sets.newHashSet("http://www.someUrl.com")
        )
                .withElementsToAdd(Lists.newArrayList(outline))
                .build();

        Assert.assertThat(this.movieRepository.getOne(MOV_1_ID).getContributions().size(), Matchers.is(13));

        this.movieContributionPersistenceService.createOutlineContribution(MOV_1_ID, contributionNewRequest);

        Assert.assertThat(this.movieRepository.getOne(MOV_1_ID).getContributions().size(), Matchers.is(14));
    }

    /**
     * Test the updateOutlineContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canUpdateOutlineContribution() throws ResourceException {
        final String newOutline = UUID.randomUUID().toString();
        final Map<Long, Object> map = new HashMap<>();
        map.put(OUTLINE_ID, new Outline.Builder(newOutline).build());
        final ContributionUpdateRequest contributionUpdateRequest = new ContributionUpdateRequest.Builder<>(
                Sets.newHashSet("http://www.someUrl.com")
        )
                .withElementsToAdd(map)
                .build();

        final Long id
                = new ArrayList<>(this.movieRepository.getOne(MOV_1_ID).getContributions().get(1).getIdsToAdd()).get(0);

        MovieOutlineEntity movieOutlineEntity = this.entityManager.find(MovieOutlineEntity.class, id);

        Assert.assertThat(movieOutlineEntity.getOutline(), Matchers.is(OUTLINE));

        this.movieContributionPersistenceService.updateOutlineContribution(CONTR_2_ID, contributionUpdateRequest);

        movieOutlineEntity = this.entityManager.find(MovieOutlineEntity.class, id);

        Assert.assertThat(movieOutlineEntity.getOutline(), Matchers.is(newOutline));
    }

    /**
     * Test the createSummaryContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateSummaryContribution() throws ResourceException {
        final Random r = new Random();
        final StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 239; ++i) {
            final char c = (char)(r.nextInt((int)(Character.MAX_VALUE)));
            sb.append(c);
        }

        final Summary summary = new Summary.Builder(sb.toString()).build();
        final ContributionNewRequest contributionNewRequest = new ContributionNewRequest.Builder<>(
                Sets.newHashSet("http://www.someUrl.com")
        )
                .withElementsToAdd(Lists.newArrayList(summary))
                .build();

        Assert.assertThat(this.movieRepository.getOne(MOV_1_ID).getContributions().size(), Matchers.is(13));

        this.movieContributionPersistenceService.createSummaryContribution(MOV_1_ID, contributionNewRequest);

        Assert.assertThat(this.movieRepository.getOne(MOV_1_ID).getContributions().size(), Matchers.is(14));
    }

    /**
     * Test the updateSummaryContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canUpdateSummaryContribution() throws ResourceException {
        final Random r = new Random();
        final StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 239; ++i) {
            final char c = (char)(r.nextInt((int)(Character.MAX_VALUE)));
            sb.append(c);
        }

        final String newSummary = sb.toString();
        final Map<Long, Object> map = new HashMap<>();
        map.put(SUMMARY_ID, new Summary.Builder(newSummary).build());
        final ContributionUpdateRequest contributionUpdateRequest = new ContributionUpdateRequest.Builder<>(
                Sets.newHashSet("http://www.someUrl.com")
        )
                .withElementsToAdd(map)
                .build();

        final Long id
                = new ArrayList<>(this.movieRepository.getOne(MOV_1_ID).getContributions().get(2).getIdsToAdd()).get(0);

        MovieSummaryEntity movieSummaryEntity = this.entityManager.find(MovieSummaryEntity.class, id);

        Assert.assertThat(movieSummaryEntity.getSummary(), Matchers.is(SUMMARY));

        this.movieContributionPersistenceService.updateSummaryContribution(CONTR_3_ID, contributionUpdateRequest);

        movieSummaryEntity = this.entityManager.find(MovieSummaryEntity.class, id);

        Assert.assertThat(movieSummaryEntity.getSummary(), Matchers.is(newSummary));
    }

    /**
     * Test the createSynopsisContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateSynopsisContribution() throws ResourceException {
        final Synopsis synopsis = new Synopsis.Builder(UUID.randomUUID().toString()).build();
        final ContributionNewRequest contributionNewRequest = new ContributionNewRequest.Builder<>(
                Sets.newHashSet("http://www.someUrl.com")
        )
                .withElementsToAdd(Lists.newArrayList(synopsis))
                .build();

        Assert.assertThat(this.movieRepository.getOne(MOV_1_ID).getContributions().size(), Matchers.is(13));

        this.movieContributionPersistenceService.createSynopsisContribution(MOV_1_ID, contributionNewRequest);

        Assert.assertThat(this.movieRepository.getOne(MOV_1_ID).getContributions().size(), Matchers.is(14));
    }

    /**
     * Test the updateSynopsisContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canUpdateSynopsisContribution() throws ResourceException {
        final String newSynopsis = UUID.randomUUID().toString();
        final Map<Long, Object> map = new HashMap<>();
        map.put(SYNOPSIS_ID, new Synopsis.Builder(newSynopsis).build());
        final ContributionUpdateRequest contributionUpdateRequest = new ContributionUpdateRequest.Builder<>(
                Sets.newHashSet("http://www.someUrl.com")
        )
                .withElementsToAdd(map)
                .build();

        final Long id
                = new ArrayList<>(this.movieRepository.getOne(MOV_1_ID).getContributions().get(3).getIdsToAdd()).get(0);

        MovieSynopsisEntity movieSynopsisEntity = this.entityManager.find(MovieSynopsisEntity.class, id);

        Assert.assertThat(movieSynopsisEntity.getSynopsis(), Matchers.is(SYNOPSIS));

        this.movieContributionPersistenceService.updateSynopsisContribution(CONTR_4_ID, contributionUpdateRequest);

        movieSynopsisEntity = this.entityManager.find(MovieSynopsisEntity.class, id);

        Assert.assertThat(movieSynopsisEntity.getSynopsis(), Matchers.is(newSynopsis));
    }

    /**
     * Test the createBoxOfficeContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateBoxOfficeContribution() throws ResourceException {
        final BoxOffice boxOffice = new BoxOffice.Builder(new BigDecimal("1000"), CountryType.USA).build();
        final ContributionNewRequest contributionNewRequest = new ContributionNewRequest.Builder<>(
                Sets.newHashSet("http://www.someUrl.com")
        )
                .withElementsToAdd(Lists.newArrayList(boxOffice))
                .build();

        Assert.assertThat(this.movieRepository.getOne(MOV_1_ID).getContributions().size(), Matchers.is(13));

        this.movieContributionPersistenceService.createBoxOfficeContribution(MOV_1_ID, contributionNewRequest);

        Assert.assertThat(this.movieRepository.getOne(MOV_1_ID).getContributions().size(), Matchers.is(14));
    }

    /**
     * Test the updateBoxOfficeContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canUpdateBoxOfficeContribution() throws ResourceException {
        final BigDecimal newBoxOffice = new BigDecimal("1000");
        final CountryType newCountry = CountryType.POLAND;
        final Map<Long, Object> map = new HashMap<>();
        map.put(BOX_OFFICE_ID, new BoxOffice.Builder(newBoxOffice, newCountry).build());
        final ContributionUpdateRequest contributionUpdateRequest = new ContributionUpdateRequest.Builder<>(
                Sets.newHashSet("http://www.someUrl.com")
        )
                .withElementsToAdd(map)
                .build();

        final Long id
                = new ArrayList<>(this.movieRepository.getOne(MOV_1_ID).getContributions().get(4).getIdsToAdd()).get(0);

        MovieBoxOfficeEntity movieBoxOfficeEntity = this.entityManager.find(MovieBoxOfficeEntity.class, id);

        Assert.assertThat(movieBoxOfficeEntity.getBoxOffice(), Matchers.is(BOX_OFFICE));
        Assert.assertThat(movieBoxOfficeEntity.getCountry(), Matchers.is(BOX_OFFICE_COUNTRY));

        this.movieContributionPersistenceService.updateBoxOfficeContribution(CONTR_5_ID, contributionUpdateRequest);

        movieBoxOfficeEntity = this.entityManager.find(MovieBoxOfficeEntity.class, id);

        Assert.assertThat(movieBoxOfficeEntity.getBoxOffice(), Matchers.is(newBoxOffice));
        Assert.assertThat(movieBoxOfficeEntity.getCountry(), Matchers.is(newCountry));
    }

    /**
     * Test the createSiteContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateSiteContribution() throws ResourceException {
        final Site site = new Site.Builder("http://www.someUrl.com", SiteType.OFFICIAL).build();
        final ContributionNewRequest contributionNewRequest = new ContributionNewRequest.Builder<>(
                Sets.newHashSet("http://www.someUrl.com")
        )
                .withElementsToAdd(Lists.newArrayList(site))
                .build();

        Assert.assertThat(this.movieRepository.getOne(MOV_1_ID).getContributions().size(), Matchers.is(13));

        this.movieContributionPersistenceService.createSiteContribution(MOV_1_ID, contributionNewRequest);

        Assert.assertThat(this.movieRepository.getOne(MOV_1_ID).getContributions().size(), Matchers.is(14));
    }

    /**
     * Test the updateSiteContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canUpdateSiteContribution() throws ResourceException {
        final String newSite = "http://www.someUrl2.com";
        final SiteType newSiteType = SiteType.OFFICIAL;
        final Map<Long, Object> map = new HashMap<>();
        map.put(SITE_ID, new Site.Builder(newSite, newSiteType).build());
        final ContributionUpdateRequest contributionUpdateRequest = new ContributionUpdateRequest.Builder<>(
                Sets.newHashSet("http://www.someUrl.com")
        )
                .withElementsToAdd(map)
                .build();

        final Long id
                = new ArrayList<>(this.movieRepository.getOne(MOV_1_ID).getContributions().get(5).getIdsToAdd()).get(0);

        MovieSiteEntity movieSiteEntity = this.entityManager.find(MovieSiteEntity.class, id);

        Assert.assertThat(movieSiteEntity.getSite(), Matchers.is(SITE));
        Assert.assertThat(movieSiteEntity.getOfficial(), Matchers.is(SITE_OFFICIAL));

        this.movieContributionPersistenceService.updateSiteContribution(CONTR_6_ID, contributionUpdateRequest);

        movieSiteEntity = this.entityManager.find(MovieSiteEntity.class, id);

        Assert.assertThat(movieSiteEntity.getSite(), Matchers.is(newSite));
        Assert.assertThat(movieSiteEntity.getOfficial(), Matchers.is(newSiteType));
    }

    /**
     * Test the createSiteContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateCountryContribution() throws ResourceException {
        final Country country = new Country.Builder(CountryType.USA).build();
        final ContributionNewRequest contributionNewRequest = new ContributionNewRequest.Builder<>(
                Sets.newHashSet("http://www.someUrl.com")
        )
                .withElementsToAdd(Lists.newArrayList(country))
                .build();

        Assert.assertThat(this.movieRepository.getOne(MOV_1_ID).getContributions().size(), Matchers.is(13));

        this.movieContributionPersistenceService.createCountryContribution(MOV_1_ID, contributionNewRequest);

        Assert.assertThat(this.movieRepository.getOne(MOV_1_ID).getContributions().size(), Matchers.is(14));
    }

    /**
     * Test the updateCountryContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canUpdateCountryContribution() throws ResourceException {
        final CountryType newCountry = CountryType.POLAND;
        final Map<Long, Object> map = new HashMap<>();
        map.put(COUNTRY_ID, new Country.Builder(newCountry).build());
        final ContributionUpdateRequest contributionUpdateRequest = new ContributionUpdateRequest.Builder<>(
                Sets.newHashSet("http://www.someUrl.com")
        )
                .withElementsToAdd(map)
                .build();

        final Long id
                = new ArrayList<>(this.movieRepository.getOne(MOV_1_ID).getContributions().get(6).getIdsToAdd()).get(0);

        MovieCountryEntity movieCountryEntity = this.entityManager.find(MovieCountryEntity.class, id);

        Assert.assertThat(movieCountryEntity.getCountry(), Matchers.is(COUNTRY));

        this.movieContributionPersistenceService.updateCountryContribution(CONTR_7_ID, contributionUpdateRequest);

        movieCountryEntity = this.entityManager.find(MovieCountryEntity.class, id);

        Assert.assertThat(movieCountryEntity.getCountry(), Matchers.is(newCountry));
    }

    /**
     * Test the createLanguageContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateLanguageContribution() throws ResourceException {
        final Language language = new Language.Builder(LanguageType.ENGLISH).build();
        final ContributionNewRequest contributionNewRequest = new ContributionNewRequest.Builder<>(
                Sets.newHashSet("http://www.someUrl.com")
        )
                .withElementsToAdd(Lists.newArrayList(language))
                .build();

        Assert.assertThat(this.movieRepository.getOne(MOV_1_ID).getContributions().size(), Matchers.is(13));

        this.movieContributionPersistenceService.createLanguageContribution(MOV_1_ID, contributionNewRequest);

        Assert.assertThat(this.movieRepository.getOne(MOV_1_ID).getContributions().size(), Matchers.is(14));
    }

    /**
     * Test the updateLanguageContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canUpdateLanguageContribution() throws ResourceException {
        final LanguageType newLanguage = LanguageType.POLISH;
        final Map<Long, Object> map = new HashMap<>();
        map.put(LANGUAGE_ID, new Language.Builder(newLanguage).build());
        final ContributionUpdateRequest contributionUpdateRequest = new ContributionUpdateRequest.Builder<>(
                Sets.newHashSet("http://www.someUrl.com")
        )
                .withElementsToAdd(map)
                .build();

        final Long id
                = new ArrayList<>(this.movieRepository.getOne(MOV_1_ID).getContributions().get(7).getIdsToAdd()).get(0);

        MovieLanguageEntity movieLanguageEntity = this.entityManager.find(MovieLanguageEntity.class, id);

        Assert.assertThat(movieLanguageEntity.getLanguage(), Matchers.is(LANGUAGE));

        this.movieContributionPersistenceService.updateLanguageContribution(CONTR_8_ID, contributionUpdateRequest);

        movieLanguageEntity = this.entityManager.find(MovieLanguageEntity.class, id);

        Assert.assertThat(movieLanguageEntity.getLanguage(), Matchers.is(newLanguage));
    }

    /**
     * Test the createGenreContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateGenreContribution() throws ResourceException {
        final Genre genre = new Genre.Builder(GenreType.ACTION).build();
        final ContributionNewRequest contributionNewRequest = new ContributionNewRequest.Builder<>(
                Sets.newHashSet("http://www.someUrl.com")
        )
                .withElementsToAdd(Lists.newArrayList(genre))
                .build();

        Assert.assertThat(this.movieRepository.getOne(MOV_1_ID).getContributions().size(), Matchers.is(13));

        this.movieContributionPersistenceService.createGenreContribution(MOV_1_ID, contributionNewRequest);

        Assert.assertThat(this.movieRepository.getOne(MOV_1_ID).getContributions().size(), Matchers.is(14));
    }

    /**
     * Test the updateGenreContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canUpdateGenreContribution() throws ResourceException {
        final GenreType newGenre = GenreType.ACTION;
        final Map<Long, Object> map = new HashMap<>();
        map.put(GENRE_ID, new Genre.Builder(newGenre).build());
        final ContributionUpdateRequest contributionUpdateRequest = new ContributionUpdateRequest.Builder<>(
                Sets.newHashSet("http://www.someUrl.com")
        )
                .withElementsToAdd(map)
                .build();

        final Long id
                = new ArrayList<>(this.movieRepository.getOne(MOV_1_ID).getContributions().get(8).getIdsToAdd()).get(0);

        MovieGenreEntity movieGenreEntity = this.entityManager.find(MovieGenreEntity.class, id);

        Assert.assertThat(movieGenreEntity.getGenre(), Matchers.is(GENRE));

        this.movieContributionPersistenceService.updateGenreContribution(CONTR_9_ID, contributionUpdateRequest);

        movieGenreEntity = this.entityManager.find(MovieGenreEntity.class, id);

        Assert.assertThat(movieGenreEntity.getGenre(), Matchers.is(newGenre));
    }

    /**
     * Test the createReviewContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateReviewContribution() throws ResourceException {
        final Review review = new Review.Builder(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                false
        ).build();
        final ContributionNewRequest contributionNewRequest = new ContributionNewRequest.Builder<>(
                Sets.newHashSet("http://www.someUrl.com")
        )
                .withElementsToAdd(Lists.newArrayList(review))
                .build();

        Assert.assertThat(this.movieRepository.getOne(MOV_2_ID).getContributions().size(), Matchers.is(1));

        this.movieContributionPersistenceService.createReviewContribution(MOV_2_ID, contributionNewRequest);

        Assert.assertThat(this.movieRepository.getOne(MOV_2_ID).getContributions().size(), Matchers.is(2));
    }

    /**
     * Test the updateReviewContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canUpdateReviewContribution() throws ResourceException {
        final String newReview = UUID.randomUUID().toString();
        final String newTitle = UUID.randomUUID().toString();
        final boolean newSpoiler = true;
        final Map<Long, Object> map = new HashMap<>();
        map.put(REVIEW_ID, new Review.Builder(newTitle, newReview, newSpoiler).build());
        final ContributionUpdateRequest contributionUpdateRequest = new ContributionUpdateRequest.Builder<>(
                Sets.newHashSet("http://www.someUrl.com")
        )
                .withElementsToAdd(map)
                .build();

        final Long id
                = new ArrayList<>(this.movieRepository.getOne(MOV_2_ID).getContributions().get(0).getIdsToAdd()).get(0);

        MovieReviewEntity movieReviewEntity = this.entityManager.find(MovieReviewEntity.class, id);

        Assert.assertThat(movieReviewEntity.getReview(), Matchers.is(REVIEW));
        Assert.assertThat(movieReviewEntity.getTitle(), Matchers.is(REVIEW_TITLE));
        Assert.assertFalse(movieReviewEntity.isSpoiler());

        this.movieContributionPersistenceService.updateReviewContribution(CONTR_14_ID, contributionUpdateRequest);

        movieReviewEntity = this.entityManager.find(MovieReviewEntity.class, id);

        Assert.assertThat(movieReviewEntity.getReview(), Matchers.is(newReview));
        Assert.assertThat(movieReviewEntity.getTitle(), Matchers.is(newTitle));
        Assert.assertTrue(movieReviewEntity.isSpoiler());
    }

    /**
     * Test the createPhotoContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreatePhotoContribution() throws ResourceException {
        final ImageRequest imageRequest = new ImageRequest.Builder()
                .withFile(
                        new File(
                                Objects.requireNonNull(
                                        this.getClass().getClassLoader().getResource("test-image.png")
                                ).getFile()
                        )
                ).build();
        final ContributionNewRequest contributionNewRequest = new ContributionNewRequest.Builder<>(
                Sets.newHashSet("http://www.someUrl.com")
        )
                .withElementsToAdd(Lists.newArrayList(imageRequest))
                .build();

        Assert.assertThat(this.movieRepository.getOne(MOV_1_ID).getContributions().size(), Matchers.is(13));

        this.movieContributionPersistenceService.createPhotoContribution(MOV_1_ID, contributionNewRequest);

        Assert.assertThat(this.movieRepository.getOne(MOV_1_ID).getContributions().size(), Matchers.is(14));
    }

    /**
     * Test the createPosterContribution method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreatePosterContribution() throws ResourceException {
        final ImageRequest imageRequest = new ImageRequest.Builder()
                .withFile(
                        new File(
                                Objects.requireNonNull(
                                        this.getClass().getClassLoader().getResource("test-image.png")
                                ).getFile()
                        )
                ).build();
        final ContributionNewRequest contributionNewRequest = new ContributionNewRequest.Builder<>(
                Sets.newHashSet("http://www.someUrl.com")
        )
                .withElementsToAdd(Lists.newArrayList(imageRequest))
                .build();

        Assert.assertThat(this.movieRepository.getOne(MOV_1_ID).getContributions().size(), Matchers.is(13));

        this.movieContributionPersistenceService.createPosterContribution(MOV_1_ID, contributionNewRequest);

        Assert.assertThat(this.movieRepository.getOne(MOV_1_ID).getContributions().size(), Matchers.is(14));
    }
}

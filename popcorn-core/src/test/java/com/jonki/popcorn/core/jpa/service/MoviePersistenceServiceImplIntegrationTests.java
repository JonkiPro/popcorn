package com.jonki.popcorn.core.jpa.service;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
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
import com.jonki.popcorn.common.dto.movie.request.RateRequest;
import com.jonki.popcorn.common.dto.movie.response.ImageResponse;
import com.jonki.popcorn.common.dto.movie.type.CountryType;
import com.jonki.popcorn.common.dto.movie.type.GenreType;
import com.jonki.popcorn.common.dto.movie.type.LanguageType;
import com.jonki.popcorn.common.dto.movie.type.MovieType;
import com.jonki.popcorn.common.dto.movie.type.SiteType;
import com.jonki.popcorn.common.dto.request.MovieRequest;
import com.jonki.popcorn.common.exception.ResourceException;
import com.jonki.popcorn.core.jpa.entity.MovieEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieBoxOfficeEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieCountryEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieGenreEntity;
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
import com.jonki.popcorn.core.security.model.CustomUserDetails;
import com.jonki.popcorn.core.service.MoviePersistenceService;
import com.jonki.popcorn.core.service.MovieSearchService;
import com.jonki.popcorn.core.util.CollectorUtils;
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
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Integration tests for MoviePersistenceServiceImpl.
 */
@Category(IntegrationTest.class)
@DatabaseSetup("MoviePersistenceServiceImplIntegrationTests/init.xml")
@DatabaseTearDown("cleanup.xml")
@Transactional
public class MoviePersistenceServiceImplIntegrationTests extends DBIntegrationTestBase {

    private static final String USER_1_ID = "user1";
    private static final String USER_1_USERNAME = "jonki1";
    private static final String USER_1_PASSWORD = "$2a$12$O02MQJS6AnCJiRw1nF6XsO7udBCza591oUKUbRC4/XrUmPXZaSjVK";

    private static final Long MOV_1_ID = 2L;
    private static final Long MOV_2_ID = 3L;
    private static final Long MOV_3_ID = 4L;
    private static final Long MOV_4_ID = 5L;
    private static final Long MOV_5_ID = 6L;

    private static final Long OTHER_TITLE_ID = 33L;
    private static final String OTHER_TITLE = "Mov1";
    private static final CountryType OTHER_TITLE_COUNTRY = CountryType.USA;

    private static final Long RELEASE_DATE_ID = 34L;
    private static final String RELEASE_DATE = "2018-07-24";
    private static final CountryType RELEASE_DATE_COUNTRY = CountryType.USA;

    private static final Long OUTLINE_ID = 35L;
    private static final String OUTLINE = "Some outline";

    private static final Long SUMMARY_ID = 36L;
    private static final String SUMMARY = "Some summary";

    private static final Long SYNOPSIS_ID = 37L;
    private static final String SYNOPSIS = "Some synopsis";

    private static final Long BOX_OFFICE_ID = 38L;
    private static final BigDecimal BOX_OFFICE = new BigDecimal("1000.00");
    private static final CountryType BOX_OFFICE_COUNTRY = CountryType.USA;

    private static final Long SITE_ID = 39L;
    private static final String SITE = "http://www.someUrl.com";
    private static final SiteType SITE_OFFICIAL = SiteType.NON_OFFICIAL;

    private static final Long COUNTRY_ID = 40L;
    private static final CountryType COUNTRY = CountryType.USA;

    private static final Long LANGUAGE_ID = 41L;
    private static final LanguageType LANGUAGE = LanguageType.ENGLISH;

    private static final Long GENRE_ID = 42L;
    private static final GenreType GENRE = GenreType.COMEDY;

    private static final Long REVIEW_ID = 46L;
    private static final String REVIEW = "Text";
    private static final String REVIEW_TITLE = "Title";
    private static final boolean REVIEW_SPOILER = false;

    private static final Long PHOTO_ID = 44L;
    private static final String PHOTO_ID_IN_CLOUD = "1234";

    private static final Long POSTER_ID = 45L;
    private static final String POSTER_ID_IN_CLOUD = "2345";

    @Autowired
    private MoviePersistenceService moviePersistenceService;

    @Autowired
    private MovieSearchService movieSearchService;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Setup.
     */
    @Before
    public void setup() {
        Assert.assertThat(this.movieRepository.count(), Matchers.is(5L));
        Assert.assertThat(this.movieInfoRepository.count(), Matchers.is(14L));

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
     * Test the createMovie method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void testCreateMovie() throws ResourceException {
        final String title = UUID.randomUUID().toString();
        final MovieType movieType = MovieType.CINEMA;
        final MovieRequest movieRequest = new MovieRequest.Builder(
                title,
                movieType
        ).build();

        Assert.assertThat(this.movieRepository.count(), Matchers.is(5L));
        this.moviePersistenceService.createMovie(movieRequest);
        Assert.assertThat(this.movieRepository.count(), Matchers.is(6L));

        final MovieEntity movieEntity = this.movieRepository.findAll()
                .stream()
                .filter(movie -> movie.getTitle().equals(title))
                .collect(CollectorUtils.singletonCollector());

        Assert.assertThat(title, Matchers.is(movieEntity.getTitle()));
        Assert.assertThat(movieType, Matchers.is(movieEntity.getType()));
    }

    /**
     * Test the updateMovieStatus method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canUpdateMovieStatus() throws ResourceException {
        this.moviePersistenceService.updateMovieStatus(MOV_2_ID, VerificationStatus.ACCEPT);
        this.movieSearchService.getMovie(MOV_2_ID);
    }

    /**
     * Test the createOtherTitle method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateOtherTitle() throws ResourceException {
        final OtherTitle otherTitle = new OtherTitle.Builder(
                UUID.randomUUID().toString(),
                CountryType.USA
        ).build();

        final String query =
                "select e from " +  MovieOtherTitleEntity.class.getSimpleName()
                        + " e where e.status = :status";

        List<MovieOtherTitleEntity> resultList = this.entityManager.createQuery(query, MovieOtherTitleEntity.class)
                .setParameter("status", DataStatus.WAITING).getResultList();

        Assert.assertEquals(1, resultList.size());

        this.moviePersistenceService.createOtherTitle(
                otherTitle,
                this.movieRepository.findByIdAndStatus(MOV_1_ID, DataStatus.ACCEPTED)
                        .orElseThrow(IllegalArgumentException::new)
        );

        resultList = this.entityManager.createQuery(query, MovieOtherTitleEntity.class)
                .setParameter("status", DataStatus.WAITING).getResultList();

        Assert.assertEquals(2, resultList.size());
    }

    /**
     * Test the updateOtherTitle method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canUpdateOtherTitle() throws ResourceException {
        final String newTitle = UUID.randomUUID().toString();
        final CountryType newCountry = CountryType.POLAND;
        final OtherTitle otherTitle = new OtherTitle.Builder(
                newTitle,
                newCountry
        ).build();

        final MovieEntity movieEntity = this.movieRepository.findByIdAndStatus(MOV_1_ID, DataStatus.ACCEPTED)
                .orElseThrow(IllegalArgumentException::new);

        final String query =
                "select e from " +  MovieOtherTitleEntity.class.getSimpleName()
                        + " e where e.status = :status and e.movie = :movie";

        List<MovieOtherTitleEntity> resultList = this.entityManager.createQuery(query, MovieOtherTitleEntity.class)
                .setParameter("status", DataStatus.WAITING)
                .setParameter("movie", movieEntity)
                .getResultList();

        OtherTitle ot = ServiceUtils.toOtherTitleDto(resultList.get(0));

        Assert.assertThat(ot.getTitle(), Matchers.is(OTHER_TITLE));
        Assert.assertThat(ot.getCountry(), Matchers.is(OTHER_TITLE_COUNTRY));

        this.moviePersistenceService.updateOtherTitle(
                otherTitle,
                OTHER_TITLE_ID,
                movieEntity
        );

        resultList = this.entityManager.createQuery(query, MovieOtherTitleEntity.class)
                .setParameter("status", DataStatus.WAITING)
                .setParameter("movie", movieEntity)
                .getResultList();

        ot = ServiceUtils.toOtherTitleDto(resultList.get(0));

        Assert.assertThat(ot.getTitle(), Matchers.is(newTitle));
        Assert.assertThat(ot.getCountry(), Matchers.is(newCountry));
    }

    /**
     * Test the createReleaseDate method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateReleaseDate() throws ResourceException {
        final ReleaseDate releaseDate = new ReleaseDate.Builder(
                new Date(),
                CountryType.USA
        ).build();

        final String query =
                "select e from " +  MovieReleaseDateEntity.class.getSimpleName()
                        + " e where e.status = :status";

        List<MovieReleaseDateEntity> resultList = this.entityManager.createQuery(query, MovieReleaseDateEntity.class)
                .setParameter("status", DataStatus.WAITING).getResultList();

        Assert.assertEquals(1, resultList.size());

        this.moviePersistenceService.createReleaseDate(
                releaseDate,
                this.movieRepository.findByIdAndStatus(MOV_1_ID, DataStatus.ACCEPTED)
                        .orElseThrow(IllegalArgumentException::new)
        );

        resultList = this.entityManager.createQuery(query, MovieReleaseDateEntity.class)
                .setParameter("status", DataStatus.WAITING).getResultList();

        Assert.assertEquals(2, resultList.size());
    }

    /**
     * Test the updateReleaseDate method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canUpdateReleaseDate() throws ResourceException {
        final Date newReleaseDare = new Date();
        final CountryType newCountry = CountryType.POLAND;
        final ReleaseDate releaseDate = new ReleaseDate.Builder(
                newReleaseDare,
                newCountry
        ).build();

        final MovieEntity movieEntity = this.movieRepository.findByIdAndStatus(MOV_1_ID, DataStatus.ACCEPTED)
                .orElseThrow(IllegalArgumentException::new);

        final String query =
                "select e from " +  MovieReleaseDateEntity.class.getSimpleName()
                        + " e where e.status = :status and e.movie = :movie";

        List<MovieReleaseDateEntity> resultList = this.entityManager.createQuery(query, MovieReleaseDateEntity.class)
                .setParameter("status", DataStatus.WAITING)
                .setParameter("movie", movieEntity)
                .getResultList();

        ReleaseDate rd = ServiceUtils.toReleaseDateDto(resultList.get(0));

        Assert.assertThat(rd.getDate().toString(), Matchers.is(RELEASE_DATE));
        Assert.assertThat(rd.getCountry(), Matchers.is(RELEASE_DATE_COUNTRY));

        this.moviePersistenceService.updateReleaseDate(
                releaseDate,
                RELEASE_DATE_ID,
                movieEntity
        );

        resultList = this.entityManager.createQuery(query, MovieReleaseDateEntity.class)
                .setParameter("status", DataStatus.WAITING)
                .setParameter("movie", movieEntity)
                .getResultList();

        rd = ServiceUtils.toReleaseDateDto(resultList.get(0));

        Assert.assertThat(rd.getDate(), Matchers.is(newReleaseDare));
        Assert.assertThat(rd.getCountry(), Matchers.is(newCountry));
    }

    /**
     * Test the createOutline method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateOutline() throws ResourceException {
        final Outline outline = new Outline.Builder(
                UUID.randomUUID().toString()
        ).build();

        final String query =
                "select e from " +  MovieOutlineEntity.class.getSimpleName()
                        + " e where e.status = :status";

        List<MovieOutlineEntity> resultList = this.entityManager.createQuery(query, MovieOutlineEntity.class)
                .setParameter("status", DataStatus.WAITING).getResultList();

        Assert.assertEquals(1, resultList.size());

        this.moviePersistenceService.createOutline(
                outline,
                this.movieRepository.findByIdAndStatus(MOV_1_ID, DataStatus.ACCEPTED)
                        .orElseThrow(IllegalArgumentException::new)
        );

        resultList = this.entityManager.createQuery(query, MovieOutlineEntity.class)
                .setParameter("status", DataStatus.WAITING).getResultList();

        Assert.assertEquals(2, resultList.size());
    }

    /**
     * Test the updateOutline method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canUpdateOutline() throws ResourceException {
        final String newOutline = UUID.randomUUID().toString();
        final Outline outline = new Outline.Builder(
                newOutline
        ).build();

        final MovieEntity movieEntity = this.movieRepository.findByIdAndStatus(MOV_1_ID, DataStatus.ACCEPTED)
                .orElseThrow(IllegalArgumentException::new);

        final String query =
                "select e from " +  MovieOutlineEntity.class.getSimpleName()
                        + " e where e.status = :status and e.movie = :movie";

        List<MovieOutlineEntity> resultList = this.entityManager.createQuery(query, MovieOutlineEntity.class)
                .setParameter("status", DataStatus.WAITING)
                .setParameter("movie", movieEntity)
                .getResultList();

        Outline o = ServiceUtils.toOutlineDto(resultList.get(0));

        Assert.assertThat(o.getOutline(), Matchers.is(OUTLINE));

        this.moviePersistenceService.updateOutline(
                outline,
                OUTLINE_ID,
                movieEntity
        );

        resultList = this.entityManager.createQuery(query, MovieOutlineEntity.class)
                .setParameter("status", DataStatus.WAITING)
                .setParameter("movie", movieEntity)
                .getResultList();

        o = ServiceUtils.toOutlineDto(resultList.get(0));

        Assert.assertThat(o.getOutline(), Matchers.is(newOutline));
    }

    /**
     * Test the createSummary method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateSummary() throws ResourceException {
        final Random r = new Random();
        final StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 239; ++i) {
            final char c = (char)(r.nextInt((int)(Character.MAX_VALUE)));
            sb.append(c);
        }

        final Summary summary = new Summary.Builder(
                sb.toString()
        ).build();

        final String query =
                "select e from " +  MovieSummaryEntity.class.getSimpleName()
                        + " e where e.status = :status";

        List<MovieSummaryEntity> resultList = this.entityManager.createQuery(query, MovieSummaryEntity.class)
                .setParameter("status", DataStatus.WAITING).getResultList();

        Assert.assertEquals(1, resultList.size());

        this.moviePersistenceService.createSummary(
                summary,
                this.movieRepository.findByIdAndStatus(MOV_1_ID, DataStatus.ACCEPTED)
                        .orElseThrow(IllegalArgumentException::new)
        );

        resultList = this.entityManager.createQuery(query, MovieSummaryEntity.class)
                .setParameter("status", DataStatus.WAITING).getResultList();

        Assert.assertEquals(2, resultList.size());
    }

    /**
     * Test the updateSummary method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canUpdateSummary() throws ResourceException {
        final Random r = new Random();
        final StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 239; ++i) {
            final char c = (char)(r.nextInt((int)(Character.MAX_VALUE)));
            sb.append(c);
        }
        final String newSummary = sb.toString();
        final Summary summary = new Summary.Builder(
                newSummary
        ).build();

        final MovieEntity movieEntity = this.movieRepository.findByIdAndStatus(MOV_1_ID, DataStatus.ACCEPTED)
                .orElseThrow(IllegalArgumentException::new);

        final String query =
                "select e from " +  MovieSummaryEntity.class.getSimpleName()
                        + " e where e.status = :status and e.movie = :movie";

        List<MovieSummaryEntity> resultList = this.entityManager.createQuery(query, MovieSummaryEntity.class)
                .setParameter("status", DataStatus.WAITING)
                .setParameter("movie", movieEntity)
                .getResultList();

        Summary s = ServiceUtils.toSummaryDto(resultList.get(0));

        Assert.assertThat(s.getSummary(), Matchers.is(SUMMARY));

        this.moviePersistenceService.updateSummary(
                summary,
                SUMMARY_ID,
                movieEntity
        );

        resultList = this.entityManager.createQuery(query, MovieSummaryEntity.class)
                .setParameter("status", DataStatus.WAITING)
                .setParameter("movie", movieEntity)
                .getResultList();

        s = ServiceUtils.toSummaryDto(resultList.get(0));

        Assert.assertThat(s.getSummary(), Matchers.is(newSummary));
    }

    /**
     * Test the createSynopsis method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateSynopsis() throws ResourceException {
        final Synopsis synopsis = new Synopsis.Builder(
                UUID.randomUUID().toString()
        ).build();

        final String query =
                "select e from " +  MovieSynopsisEntity.class.getSimpleName()
                        + " e where e.status = :status";

        List<MovieSynopsisEntity> resultList = this.entityManager.createQuery(query, MovieSynopsisEntity.class)
                .setParameter("status", DataStatus.WAITING).getResultList();

        Assert.assertEquals(1, resultList.size());

        this.moviePersistenceService.createSynopsis(
                synopsis,
                this.movieRepository.findByIdAndStatus(MOV_1_ID, DataStatus.ACCEPTED)
                        .orElseThrow(IllegalArgumentException::new)
        );

        resultList = this.entityManager.createQuery(query, MovieSynopsisEntity.class)
                .setParameter("status", DataStatus.WAITING).getResultList();

        Assert.assertEquals(2, resultList.size());
    }

    /**
     * Test the updateSynopsis method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canUpdateSynopsis() throws ResourceException {
        final String newSynopsis = UUID.randomUUID().toString();
        final Synopsis synopsis = new Synopsis.Builder(
                newSynopsis
        ).build();

        final MovieEntity movieEntity = this.movieRepository.findByIdAndStatus(MOV_1_ID, DataStatus.ACCEPTED)
                .orElseThrow(IllegalArgumentException::new);

        final String query =
                "select e from " +  MovieSynopsisEntity.class.getSimpleName()
                        + " e where e.status = :status and e.movie = :movie";

        List<MovieSynopsisEntity> resultList = this.entityManager.createQuery(query, MovieSynopsisEntity.class)
                .setParameter("status", DataStatus.WAITING)
                .setParameter("movie", movieEntity)
                .getResultList();

        Synopsis s = ServiceUtils.toSynopsisDto(resultList.get(0));

        Assert.assertThat(s.getSynopsis(), Matchers.is(SYNOPSIS));

        this.moviePersistenceService.updateSynopsis(
                synopsis,
                SYNOPSIS_ID,
                movieEntity
        );

        resultList = this.entityManager.createQuery(query, MovieSynopsisEntity.class)
                .setParameter("status", DataStatus.WAITING)
                .setParameter("movie", movieEntity)
                .getResultList();

        s = ServiceUtils.toSynopsisDto(resultList.get(0));

        Assert.assertThat(s.getSynopsis(), Matchers.is(newSynopsis));
    }

    /**
     * Test the createBoxOffice method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateBoxOffice() throws ResourceException {
        final BoxOffice boxOffice = new BoxOffice.Builder(
                new BigDecimal("1000"),
                CountryType.USA
        ).build();

        final String query =
                "select e from " +  MovieBoxOfficeEntity.class.getSimpleName()
                        + " e where e.status = :status";

        List<MovieBoxOfficeEntity> resultList = this.entityManager.createQuery(query, MovieBoxOfficeEntity.class)
                .setParameter("status", DataStatus.WAITING).getResultList();

        Assert.assertEquals(1, resultList.size());

        this.moviePersistenceService.createBoxOffice(
                boxOffice,
                this.movieRepository.findByIdAndStatus(MOV_1_ID, DataStatus.ACCEPTED)
                        .orElseThrow(IllegalArgumentException::new)
        );

        resultList = this.entityManager.createQuery(query, MovieBoxOfficeEntity.class)
                .setParameter("status", DataStatus.WAITING).getResultList();

        Assert.assertEquals(2, resultList.size());
    }

    /**
     * Test the updateBoxOffice method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canUpdateBoxOffice() throws ResourceException {
        final BigDecimal newBoxOffice = new BigDecimal("1000");
        final CountryType newCountry = CountryType.POLAND;
        final BoxOffice boxOffice = new BoxOffice.Builder(
                newBoxOffice,
                newCountry
        ).build();

        final MovieEntity movieEntity = this.movieRepository.findByIdAndStatus(MOV_1_ID, DataStatus.ACCEPTED)
                .orElseThrow(IllegalArgumentException::new);

        final String query =
                "select e from " +  MovieBoxOfficeEntity.class.getSimpleName()
                        + " e where e.status = :status and e.movie = :movie";

        List<MovieBoxOfficeEntity> resultList = this.entityManager.createQuery(query, MovieBoxOfficeEntity.class)
                .setParameter("status", DataStatus.WAITING)
                .setParameter("movie", movieEntity)
                .getResultList();

        BoxOffice bo = ServiceUtils.toBoxOfficeDto(resultList.get(0));

        Assert.assertThat(bo.getBoxOffice(), Matchers.is(BOX_OFFICE));
        Assert.assertThat(bo.getCountry(), Matchers.is(BOX_OFFICE_COUNTRY));

        this.moviePersistenceService.updateBoxOffice(
                boxOffice,
                BOX_OFFICE_ID,
                movieEntity
        );

        resultList = this.entityManager.createQuery(query, MovieBoxOfficeEntity.class)
                .setParameter("status", DataStatus.WAITING)
                .setParameter("movie", movieEntity)
                .getResultList();

        bo = ServiceUtils.toBoxOfficeDto(resultList.get(0));

        Assert.assertThat(bo.getBoxOffice(), Matchers.is(newBoxOffice));
        Assert.assertThat(bo.getCountry(), Matchers.is(newCountry));
    }

    /**
     * Test the createSite method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateSite() throws ResourceException {
        final Site site = new Site.Builder(
                "http://www.someAddressUrl.com",
                SiteType.OFFICIAL
        ).build();

        final String query =
                "select e from " +  MovieSiteEntity.class.getSimpleName()
                        + " e where e.status = :status";

        List<MovieSiteEntity> resultList = this.entityManager.createQuery(query, MovieSiteEntity.class)
                .setParameter("status", DataStatus.WAITING).getResultList();

        Assert.assertEquals(1, resultList.size());

        this.moviePersistenceService.createSite(
                site,
                this.movieRepository.findByIdAndStatus(MOV_1_ID, DataStatus.ACCEPTED)
                        .orElseThrow(IllegalArgumentException::new)
        );

        resultList = this.entityManager.createQuery(query, MovieSiteEntity.class)
                .setParameter("status", DataStatus.WAITING).getResultList();

        Assert.assertEquals(2, resultList.size());
    }

    /**
     * Test the updateSite method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canUpdateSite() throws ResourceException {
        final String newSite = "http://www/someUrl2.com";
        final SiteType newSiteType = SiteType.OFFICIAL;
        final Site site = new Site.Builder(
                newSite,
                newSiteType
        ).build();

        final MovieEntity movieEntity = this.movieRepository.findByIdAndStatus(MOV_1_ID, DataStatus.ACCEPTED)
                .orElseThrow(IllegalArgumentException::new);

        final String query =
                "select e from " +  MovieSiteEntity.class.getSimpleName()
                        + " e where e.status = :status and e.movie = :movie";

        List<MovieSiteEntity> resultList = this.entityManager.createQuery(query, MovieSiteEntity.class)
                .setParameter("status", DataStatus.WAITING)
                .setParameter("movie", movieEntity)
                .getResultList();

        Site s = ServiceUtils.toSiteDto(resultList.get(0));

        Assert.assertThat(s.getSite(), Matchers.is(SITE));
        Assert.assertThat(s.getOfficial(), Matchers.is(SITE_OFFICIAL));

        this.moviePersistenceService.updateSite(
                site,
                SITE_ID,
                movieEntity
        );

        resultList = this.entityManager.createQuery(query, MovieSiteEntity.class)
                .setParameter("status", DataStatus.WAITING)
                .setParameter("movie", movieEntity)
                .getResultList();

        s = ServiceUtils.toSiteDto(resultList.get(0));

        Assert.assertThat(s.getSite(), Matchers.is(newSite));
        Assert.assertThat(s.getOfficial(), Matchers.is(newSiteType));
    }

    /**
     * Test the createCountry method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateCountry() throws ResourceException {
        final Country country = new Country.Builder(
                CountryType.USA
        ).build();

        final String query =
                "select e from " +  MovieCountryEntity.class.getSimpleName()
                        + " e where e.status = :status";

        List<MovieCountryEntity> resultList = this.entityManager.createQuery(query, MovieCountryEntity.class)
                .setParameter("status", DataStatus.WAITING).getResultList();

        Assert.assertEquals(1, resultList.size());

        this.moviePersistenceService.createCountry(
                country,
                this.movieRepository.findByIdAndStatus(MOV_1_ID, DataStatus.ACCEPTED)
                        .orElseThrow(IllegalArgumentException::new)
        );

        resultList = this.entityManager.createQuery(query, MovieCountryEntity.class)
                .setParameter("status", DataStatus.WAITING).getResultList();

        Assert.assertEquals(2, resultList.size());
    }

    /**
     * Test the updateCountry method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canUpdateCountry() throws ResourceException {
        final CountryType newCountry = CountryType.POLAND;
        final Country country = new Country.Builder(
                newCountry
        ).build();

        final MovieEntity movieEntity = this.movieRepository.findByIdAndStatus(MOV_1_ID, DataStatus.ACCEPTED)
                .orElseThrow(IllegalArgumentException::new);

        final String query =
                "select e from " +  MovieCountryEntity.class.getSimpleName()
                        + " e where e.status = :status and e.movie = :movie";

        List<MovieCountryEntity> resultList = this.entityManager.createQuery(query, MovieCountryEntity.class)
                .setParameter("status", DataStatus.WAITING)
                .setParameter("movie", movieEntity)
                .getResultList();

        Country c = ServiceUtils.toCountryDto(resultList.get(0));

        Assert.assertThat(c.getCountry(), Matchers.is(COUNTRY));

        this.moviePersistenceService.updateCountry(
                country,
                COUNTRY_ID,
                movieEntity
        );

        resultList = this.entityManager.createQuery(query, MovieCountryEntity.class)
                .setParameter("status", DataStatus.WAITING)
                .setParameter("movie", movieEntity)
                .getResultList();

        c = ServiceUtils.toCountryDto(resultList.get(0));

        Assert.assertThat(c.getCountry(), Matchers.is(newCountry));
    }

    /**
     * Test the createLanguage method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateLanguage() throws ResourceException {
        final Language language = new Language.Builder(
                LanguageType.ENGLISH
        ).build();

        final String query =
                "select e from " +  MovieLanguageEntity.class.getSimpleName()
                        + " e where e.status = :status";

        List<MovieLanguageEntity> resultList = this.entityManager.createQuery(query, MovieLanguageEntity.class)
                .setParameter("status", DataStatus.WAITING).getResultList();

        Assert.assertEquals(1, resultList.size());

        this.moviePersistenceService.createLanguage(
                language,
                this.movieRepository.findByIdAndStatus(MOV_1_ID, DataStatus.ACCEPTED)
                        .orElseThrow(IllegalArgumentException::new)
        );

        resultList = this.entityManager.createQuery(query, MovieLanguageEntity.class)
                .setParameter("status", DataStatus.WAITING).getResultList();

        Assert.assertEquals(2, resultList.size());
    }

    /**
     * Test the updateLanguage method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canUpdateLanguage() throws ResourceException {
        final LanguageType newLanguage = LanguageType.POLISH;
        final Language language = new Language.Builder(
                newLanguage
        ).build();

        final MovieEntity movieEntity = this.movieRepository.findByIdAndStatus(MOV_1_ID, DataStatus.ACCEPTED)
                .orElseThrow(IllegalArgumentException::new);

        final String query =
                "select e from " +  MovieLanguageEntity.class.getSimpleName()
                        + " e where e.status = :status and e.movie = :movie";

        List<MovieLanguageEntity> resultList = this.entityManager.createQuery(query, MovieLanguageEntity.class)
                .setParameter("status", DataStatus.WAITING)
                .setParameter("movie", movieEntity)
                .getResultList();

        Language l = ServiceUtils.toLanguageDto(resultList.get(0));

        Assert.assertThat(l.getLanguage(), Matchers.is(LANGUAGE));

        this.moviePersistenceService.updateLanguage(
                language,
                LANGUAGE_ID,
                movieEntity
        );

        resultList = this.entityManager.createQuery(query, MovieLanguageEntity.class)
                .setParameter("status", DataStatus.WAITING)
                .setParameter("movie", movieEntity)
                .getResultList();

        l = ServiceUtils.toLanguageDto(resultList.get(0));

        Assert.assertThat(l.getLanguage(), Matchers.is(newLanguage));
    }

    /**
     * Test the createGenre method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateGenre() throws ResourceException {
        final Genre genre = new Genre.Builder(
                GenreType.ACTION
        ).build();

        final String query =
                "select e from " +  MovieGenreEntity.class.getSimpleName()
                        + " e where e.status = :status";

        List<MovieGenreEntity> resultList = this.entityManager.createQuery(query, MovieGenreEntity.class)
                .setParameter("status", DataStatus.WAITING).getResultList();

        Assert.assertEquals(1, resultList.size());

        this.moviePersistenceService.createGenre(
                genre,
                this.movieRepository.findByIdAndStatus(MOV_1_ID, DataStatus.ACCEPTED)
                        .orElseThrow(IllegalArgumentException::new)
        );

        resultList = this.entityManager.createQuery(query, MovieGenreEntity.class)
                .setParameter("status", DataStatus.WAITING).getResultList();

        Assert.assertEquals(2, resultList.size());
    }

    /**
     * Test the updateGenre method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canUpdateGenre() throws ResourceException {
        final GenreType newGenre = GenreType.ACTION;
        final Genre genre = new Genre.Builder(
                newGenre
        ).build();

        final MovieEntity movieEntity = this.movieRepository.findByIdAndStatus(MOV_1_ID, DataStatus.ACCEPTED)
                .orElseThrow(IllegalArgumentException::new);

        final String query =
                "select e from " +  MovieGenreEntity.class.getSimpleName()
                        + " e where e.status = :status and e.movie = :movie";

        List<MovieGenreEntity> resultList = this.entityManager.createQuery(query, MovieGenreEntity.class)
                .setParameter("status", DataStatus.WAITING)
                .setParameter("movie", movieEntity)
                .getResultList();

        Genre g = ServiceUtils.toGenreDto(resultList.get(0));

        Assert.assertThat(g.getGenre(), Matchers.is(GENRE));

        this.moviePersistenceService.updateGenre(
                genre,
                GENRE_ID,
                movieEntity
        );

        resultList = this.entityManager.createQuery(query, MovieGenreEntity.class)
                .setParameter("status", DataStatus.WAITING)
                .setParameter("movie", movieEntity)
                .getResultList();

        g = ServiceUtils.toGenreDto(resultList.get(0));

        Assert.assertThat(g.getGenre(), Matchers.is(newGenre));
    }

    /**
     * Test the createReview method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateReview() throws ResourceException {
        final Review review = new Review.Builder(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                true
        ).build();

        final String query =
                "select e from " +  MovieReviewEntity.class.getSimpleName()
                        + " e where e.status = :status";

        List<MovieReviewEntity> resultList = this.entityManager.createQuery(query, MovieReviewEntity.class)
                .setParameter("status", DataStatus.WAITING).getResultList();

        Assert.assertEquals(2, resultList.size());

        this.moviePersistenceService.createReview(
                review,
                this.movieRepository.findByIdAndStatus(MOV_5_ID, DataStatus.ACCEPTED)
                        .orElseThrow(IllegalArgumentException::new)
        );

        resultList = this.entityManager.createQuery(query, MovieReviewEntity.class)
                .setParameter("status", DataStatus.WAITING).getResultList();

        Assert.assertEquals(3, resultList.size());
    }

    /**
     * Test the updateReview method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canUpdateReview() throws ResourceException {
        final String newReview = UUID.randomUUID().toString();
        final String newTitle = UUID.randomUUID().toString();
        final boolean newSpoiler = true;
        final Review review = new Review.Builder(
                newTitle,
                newReview,
                newSpoiler
        ).build();

        final MovieEntity movieEntity = this.movieRepository.findByIdAndStatus(MOV_5_ID, DataStatus.ACCEPTED)
                .orElseThrow(IllegalArgumentException::new);

        final String query =
                "select e from " +  MovieReviewEntity.class.getSimpleName()
                        + " e where e.status = :status and e.movie = :movie";

        List<MovieReviewEntity> resultList = this.entityManager.createQuery(query, MovieReviewEntity.class)
                .setParameter("status", DataStatus.WAITING)
                .setParameter("movie", movieEntity)
                .getResultList();

        Review r = ServiceUtils.toReviewDto(resultList.get(0));

        Assert.assertThat(r.getReview(), Matchers.is(REVIEW));
        Assert.assertThat(r.getTitle(), Matchers.is(REVIEW_TITLE));
        Assert.assertFalse(r.isSpoiler());

        this.moviePersistenceService.updateReview(
                review,
                REVIEW_ID,
                movieEntity
        );

        resultList = this.entityManager.createQuery(query, MovieReviewEntity.class)
                .setParameter("status", DataStatus.WAITING)
                .setParameter("movie", movieEntity)
                .getResultList();

        r = ServiceUtils.toReviewDto(resultList.get(0));

        Assert.assertThat(r.getReview(), Matchers.is(newReview));
        Assert.assertThat(r.getTitle(), Matchers.is(newTitle));
        Assert.assertTrue(r.isSpoiler());
    }

    /**
     * Test the createPhoto method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreatePhoto() throws ResourceException {
        final ImageRequest photo = new ImageRequest.Builder()
                .withIdInCloud("1")
                .build();

        final String query =
                "select e from " +  MoviePhotoEntity.class.getSimpleName()
                        + " e where e.status = :status";

        List<MoviePhotoEntity> resultList = this.entityManager.createQuery(query, MoviePhotoEntity.class)
                .setParameter("status", DataStatus.WAITING).getResultList();

        Assert.assertEquals(1, resultList.size());

        this.moviePersistenceService.createPhoto(
                photo,
                this.movieRepository.findByIdAndStatus(MOV_1_ID, DataStatus.ACCEPTED)
                        .orElseThrow(IllegalArgumentException::new)
        );

        resultList = this.entityManager.createQuery(query, MoviePhotoEntity.class)
                .setParameter("status", DataStatus.WAITING).getResultList();

        Assert.assertEquals(2, resultList.size());
    }

    /**
     * Test the updatePhoto method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canUpdatePhoto() throws ResourceException {
        final String newIdInCloud = "2345";
        final ImageRequest photo = new ImageRequest.Builder()
                .withIdInCloud(newIdInCloud)
                .build();

        final MovieEntity movieEntity = this.movieRepository.findByIdAndStatus(MOV_1_ID, DataStatus.ACCEPTED)
                .orElseThrow(IllegalArgumentException::new);

        final String query =
                "select e from " +  MoviePhotoEntity.class.getSimpleName()
                        + " e where e.status = :status and e.movie = :movie";

        List<MoviePhotoEntity> resultList = this.entityManager.createQuery(query, MoviePhotoEntity.class)
                .setParameter("status", DataStatus.WAITING)
                .setParameter("movie", movieEntity)
                .getResultList();

        ImageResponse ir = ServiceUtils.toImageResponseDto(resultList.get(0));

        Assert.assertThat(ir.getSrc().substring(ir.getSrc().length()-4), Matchers.is(PHOTO_ID_IN_CLOUD));

        this.moviePersistenceService.updatePhoto(
                photo,
                PHOTO_ID,
                movieEntity
        );

        resultList = this.entityManager.createQuery(query, MoviePhotoEntity.class)
                .setParameter("status", DataStatus.WAITING)
                .setParameter("movie", movieEntity)
                .getResultList();

        ir = ServiceUtils.toImageResponseDto(resultList.get(0));

        Assert.assertThat(ir.getSrc().substring(ir.getSrc().length()-4), Matchers.is(newIdInCloud));
    }

    /**
     * Test the createPoster method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreatePoster() throws ResourceException {
        final ImageRequest poster = new ImageRequest.Builder()
                .withIdInCloud("1")
                .build();

        final String query =
                "select e from " +  MoviePosterEntity.class.getSimpleName()
                        + " e where e.status = :status";

        List<MoviePosterEntity> resultList = this.entityManager.createQuery(query, MoviePosterEntity.class)
                .setParameter("status", DataStatus.WAITING).getResultList();

        Assert.assertEquals(1, resultList.size());

        this.moviePersistenceService.createPoster(
                poster,
                this.movieRepository.findByIdAndStatus(MOV_1_ID, DataStatus.ACCEPTED)
                        .orElseThrow(IllegalArgumentException::new)
        );

        resultList = this.entityManager.createQuery(query, MoviePosterEntity.class)
                .setParameter("status", DataStatus.WAITING).getResultList();

        Assert.assertEquals(2, resultList.size());
    }

    /**
     * Test the updatePoster method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canUpdatePoster() throws ResourceException {
        final String newIdInCloud = "1234";
        final ImageRequest poster = new ImageRequest.Builder()
                .withIdInCloud(newIdInCloud)
                .build();

        final MovieEntity movieEntity = this.movieRepository.findByIdAndStatus(MOV_1_ID, DataStatus.ACCEPTED)
                .orElseThrow(IllegalArgumentException::new);

        final String query =
                "select e from " +  MoviePosterEntity.class.getSimpleName()
                        + " e where e.status = :status and e.movie = :movie";

        List<MoviePosterEntity> resultList = this.entityManager.createQuery(query, MoviePosterEntity.class)
                .setParameter("status", DataStatus.WAITING)
                .setParameter("movie", movieEntity)
                .getResultList();

        ImageResponse ir = ServiceUtils.toImageResponseDto(resultList.get(0));

        Assert.assertThat(ir.getSrc().substring(ir.getSrc().length()-4), Matchers.is(POSTER_ID_IN_CLOUD));

        this.moviePersistenceService.updatePoster(
                poster,
                POSTER_ID,
                movieEntity
        );

        resultList = this.entityManager.createQuery(query, MoviePosterEntity.class)
                .setParameter("status", DataStatus.WAITING)
                .setParameter("movie", movieEntity)
                .getResultList();

        ir = ServiceUtils.toImageResponseDto(resultList.get(0));

        Assert.assertThat(ir.getSrc().substring(ir.getSrc().length()-4), Matchers.is(newIdInCloud));
    }

    /**
     * Test the saveRating method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canSaveRating() throws ResourceException {
        final RateRequest rateRequest = new RateRequest.Builder(
                1
        ).build();

        Assert.assertEquals(this.movieSearchService.getRatings(MOV_3_ID).size(), 0);

        this.moviePersistenceService.saveRating(MOV_3_ID, rateRequest);

        Assert.assertEquals(this.movieSearchService.getRatings(MOV_3_ID).size(), 1);
    }

    /**
     * Test the saveRating method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canUpdateRating() throws ResourceException {
        final RateRequest rateRequest = new RateRequest.Builder(
                10
        ).build();

        MovieEntity movie = this.movieRepository.getOne(MOV_4_ID);

        Assert.assertThat(movie.getRating().orElse(null), Matchers.is(1F));
        Assert.assertThat(movie.getRatings().size(), Matchers.is(1));

        this.moviePersistenceService.saveRating(MOV_4_ID, rateRequest);

        movie = this.movieRepository.getOne(MOV_4_ID);

        Assert.assertThat(movie.getRating().orElse(null), Matchers.is((float) rateRequest.getRate()));
        Assert.assertThat(movie.getRatings().size(), Matchers.is(1));
    }
}

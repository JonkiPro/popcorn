package com.jonki.popcorn.core.jpa.service;

import com.jonki.popcorn.common.dto.DataStatus;
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
import com.jonki.popcorn.common.dto.movie.request.RateRequest;
import com.jonki.popcorn.common.dto.movie.type.CountryType;
import com.jonki.popcorn.common.dto.movie.type.GenreType;
import com.jonki.popcorn.common.dto.movie.type.LanguageType;
import com.jonki.popcorn.common.dto.movie.type.MovieType;
import com.jonki.popcorn.common.dto.movie.type.SiteType;
import com.jonki.popcorn.common.dto.request.MovieRequest;
import com.jonki.popcorn.common.exception.ResourceConflictException;
import com.jonki.popcorn.common.exception.ResourceException;
import com.jonki.popcorn.common.exception.ResourceForbiddenException;
import com.jonki.popcorn.core.jpa.entity.MovieEntity;
import com.jonki.popcorn.core.jpa.entity.UserEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieBoxOfficeEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieCountryEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieGenreEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieLanguageEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieOtherTitleEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieOutlineEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieRateEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieReleaseDateEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieReviewEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieSiteEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieSummaryEntity;
import com.jonki.popcorn.core.jpa.entity.movie.MovieSynopsisEntity;
import com.jonki.popcorn.core.jpa.repository.MovieRepository;
import com.jonki.popcorn.core.jpa.repository.UserRepository;
import com.jonki.popcorn.core.service.AuthorizationService;
import com.jonki.popcorn.test.category.UnitTest;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

/**
 * Unit tests for MoviePersistenceServiceImpl.
 */
@Category(UnitTest.class)
public class MoviePersistenceServiceImplUnitTests {

    private MovieRepository movieRepository;
    private UserRepository userRepository;
    private AuthorizationService authorizationService;
    private MoviePersistenceServiceImpl moviePersistenceService;

    /**
     * Setup for the tests.
     */
    @Before
    public void setup() {
        this.movieRepository = Mockito.mock(MovieRepository.class);
        this.userRepository = Mockito.mock(UserRepository.class);
        this.authorizationService = Mockito.mock(AuthorizationService.class);
        this.moviePersistenceService = new MoviePersistenceServiceImpl(
                this.movieRepository,
                this.userRepository,
                this.authorizationService
        );
    }

    /**
     * Test the createMovie method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void testCreateMovie() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final UserEntity userEntity = new UserEntity();
        final String title = UUID.randomUUID().toString();
        final MovieType movieType = MovieType.CINEMA;
        final MovieRequest movieRequest = new MovieRequest.Builder(
                title,
                MovieType.CINEMA
        ).build();

        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));

        final ArgumentCaptor<MovieEntity> argument = ArgumentCaptor.forClass(MovieEntity.class);
        this.moviePersistenceService.createMovie(movieRequest);
        Mockito.verify(this.movieRepository).save(argument.capture());

        Assert.assertEquals(title, argument.getValue().getTitle());
        Assert.assertEquals(movieType, argument.getValue().getType());
        Assert.assertThat(argument.getValue().getOtherTitles().size(), Is.is(1));
    }

    /**
     * Test the updateMovieStatus method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canUpdateMovieStatus() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long movieId = new Random().nextLong();
        final UserEntity userEntity = new UserEntity();
        userEntity.getPermissions().add(UserMoviePermission.ALL);
        final MovieEntity movieEntity = new MovieEntity();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.movieRepository.findByIdAndStatus(movieId, DataStatus.WAITING))
                .thenReturn(Optional.of(movieEntity));
        this.moviePersistenceService.updateMovieStatus(movieId, VerificationStatus.ACCEPT);
    }

    /**
     * Test the updateMovieStatus method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceForbiddenException.class)
    public void cantUpdateMovieStatusIfUserDoesNotPermissions() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long movieId = new Random().nextLong();
        final UserEntity userEntity = new UserEntity();
        final MovieEntity movieEntity = new MovieEntity();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.movieRepository.findByIdAndStatus(movieId, DataStatus.WAITING))
                .thenReturn(Optional.of(movieEntity));
        this.moviePersistenceService.updateMovieStatus(movieId, VerificationStatus.ACCEPT);
    }

    /**
     * Test the createOtherTitle method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateOtherTitle() throws ResourceException {
        final MovieEntity movieEntity = new MovieEntity();
        final OtherTitle otherTitle = new OtherTitle.Builder(
                UUID.randomUUID().toString(),
                CountryType.USA
        ).build();
        this.moviePersistenceService.createOtherTitle(otherTitle, movieEntity);
    }

    /**
     * Test the createOtherTitle method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceConflictException.class)
    public void cantCreateOtherTitleIfExistsDuplicate() throws ResourceException {
        final MovieEntity movieEntity = new MovieEntity();
        final OtherTitle otherTitle = new OtherTitle.Builder(
                UUID.randomUUID().toString(),
                CountryType.USA
        ).build();
        final MovieOtherTitleEntity otherTitleEntity = new MovieOtherTitleEntity(otherTitle.getTitle(), otherTitle.getCountry(), null);
        otherTitleEntity.setStatus(DataStatus.ACCEPTED);
        movieEntity.getOtherTitles().add(otherTitleEntity);
        this.moviePersistenceService.createOtherTitle(otherTitle, movieEntity);
    }

    /**
     * Test the createReleaseDate method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateReleaseDate() throws ResourceException {
        final MovieEntity movieEntity = new MovieEntity();
        final ReleaseDate releaseDate = new ReleaseDate.Builder(
                new Date(),
                CountryType.USA
        ).build();
        this.moviePersistenceService.createReleaseDate(releaseDate, movieEntity);
    }

    /**
     * Test the createReleaseDate method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceConflictException.class)
    public void cantCreateReleaseDateIfExistsDuplicate() throws ResourceException {
        final MovieEntity movieEntity = new MovieEntity();
        final ReleaseDate releaseDate = new ReleaseDate.Builder(
                new Date(),
                CountryType.USA
        ).build();
        final MovieReleaseDateEntity releaseDateEntity = new MovieReleaseDateEntity(releaseDate.getDate(), releaseDate.getCountry());
        releaseDateEntity.setStatus(DataStatus.ACCEPTED);
        movieEntity.getReleaseDates().add(releaseDateEntity);
        this.moviePersistenceService.createReleaseDate(releaseDate, movieEntity);
    }

    /**
     * Test the createOutline method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateOutline() throws ResourceException {
        final MovieEntity movieEntity = new MovieEntity();
        final Outline outline = new Outline.Builder(
                UUID.randomUUID().toString()
        ).build();
        this.moviePersistenceService.createOutline(outline, movieEntity);
    }

    /**
     * Test the createOutline method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceConflictException.class)
    public void cantCreateOutlineIfExistsDuplicate() throws ResourceException {
        final MovieEntity movieEntity = new MovieEntity();
        final Outline outline = new Outline.Builder(
                UUID.randomUUID().toString()
        ).build();
        final MovieOutlineEntity outlineEntity = new MovieOutlineEntity(outline.getOutline());
        outlineEntity.setStatus(DataStatus.ACCEPTED);
        movieEntity.getOutlines().add(outlineEntity);
        this.moviePersistenceService.createOutline(outline, movieEntity);
    }

    /**
     * Test the createSummary method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateSummary() throws ResourceException {
        final MovieEntity movieEntity = new MovieEntity();
        final Summary summary = new Summary.Builder(
                UUID.randomUUID().toString()
        ).build();
        this.moviePersistenceService.createSummary(summary, movieEntity);
    }

    /**
     * Test the createSummary method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceConflictException.class)
    public void cantCreateSummaryIfExistsDuplicate() throws ResourceException {
        final MovieEntity movieEntity = new MovieEntity();
        final Summary summary = new Summary.Builder(
                UUID.randomUUID().toString()
        ).build();
        final MovieSummaryEntity summaryEntity = new MovieSummaryEntity(summary.getSummary());
        summaryEntity.setStatus(DataStatus.ACCEPTED);
        movieEntity.getSummaries().add(summaryEntity);
        this.moviePersistenceService.createSummary(summary, movieEntity);
    }

    /**
     * Test the createSynopsis method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateSynopsis() throws ResourceException {
        final MovieEntity movieEntity = new MovieEntity();
        final Synopsis synopsis = new Synopsis.Builder(
                UUID.randomUUID().toString()
        ).build();
        this.moviePersistenceService.createSynopsis(synopsis, movieEntity);
    }

    /**
     * Test the createSynopsis method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceConflictException.class)
    public void cantCreateSynopsisIfExistsDuplicate() throws ResourceException {
        final MovieEntity movieEntity = new MovieEntity();
        final Synopsis synopsis = new Synopsis.Builder(
                UUID.randomUUID().toString()
        ).build();
        final MovieSynopsisEntity synopsisEntity = new MovieSynopsisEntity(synopsis.getSynopsis());
        synopsisEntity.setStatus(DataStatus.ACCEPTED);
        movieEntity.getSynopses().add(synopsisEntity);
        this.moviePersistenceService.createSynopsis(synopsis, movieEntity);
    }

    /**
     * Test the createBoxOffice method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateBoxOffice() throws ResourceException {
        final MovieEntity movieEntity = new MovieEntity();
        final BoxOffice boxOffice = new BoxOffice.Builder(
                new BigDecimal("1000"),
                CountryType.USA
        ).build();
        this.moviePersistenceService.createBoxOffice(boxOffice, movieEntity);
    }

    /**
     * Test the createBoxOffice method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceConflictException.class)
    public void cantCreateBoxOfficeIfExistsDuplicate() throws ResourceException {
        final MovieEntity movieEntity = new MovieEntity();
        final BoxOffice boxOffice = new BoxOffice.Builder(
                new BigDecimal("1000"),
                CountryType.USA
        ).build();
        final MovieBoxOfficeEntity boxOfficeEntity = new MovieBoxOfficeEntity(boxOffice.getBoxOffice(), boxOffice.getCountry());
        boxOfficeEntity.setStatus(DataStatus.ACCEPTED);
        movieEntity.getBoxOffices().add(boxOfficeEntity);
        this.moviePersistenceService.createBoxOffice(boxOffice, movieEntity);
    }

    /**
     * Test the createSite method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateSite() throws ResourceException {
        final MovieEntity movieEntity = new MovieEntity();
        final Site site = new Site.Builder(
                UUID.randomUUID().toString(),
                SiteType.OFFICIAL
        ).build();
        this.moviePersistenceService.createSite(site, movieEntity);
    }

    /**
     * Test the createSite method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceConflictException.class)
    public void cantCreateSiteIfExistsDuplicate() throws ResourceException {
        final MovieEntity movieEntity = new MovieEntity();
        final Site site = new Site.Builder(
                UUID.randomUUID().toString(),
                SiteType.OFFICIAL
        ).build();
        final MovieSiteEntity siteEntity = new MovieSiteEntity(site.getSite(), site.getOfficial());
        siteEntity.setStatus(DataStatus.ACCEPTED);
        movieEntity.getSites().add(siteEntity);
        this.moviePersistenceService.createSite(site, movieEntity);
    }

    /**
     * Test the createCountry method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateCountry() throws ResourceException {
        final MovieEntity movieEntity = new MovieEntity();
        final Country country = new Country.Builder(
                CountryType.USA
        ).build();
        this.moviePersistenceService.createCountry(country, movieEntity);
    }

    /**
     * Test the createCountry method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceConflictException.class)
    public void cantCreateCountryIfExistsDuplicate() throws ResourceException {
        final MovieEntity movieEntity = new MovieEntity();
        final Country country = new Country.Builder(
                CountryType.USA
        ).build();
        final MovieCountryEntity countryEntity = new MovieCountryEntity(country.getCountry());
        countryEntity.setStatus(DataStatus.ACCEPTED);
        movieEntity.getCountries().add(countryEntity);
        this.moviePersistenceService.createCountry(country, movieEntity);
    }

    /**
     * Test the createLanguage method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateLanguage() throws ResourceException {
        final MovieEntity movieEntity = new MovieEntity();
        final Language language = new Language.Builder(
                LanguageType.ENGLISH
        ).build();
        this.moviePersistenceService.createLanguage(language, movieEntity);
    }

    /**
     * Test the createLanguage method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceConflictException.class)
    public void cantCreateLanguageIfExistsDuplicate() throws ResourceException {
        final MovieEntity movieEntity = new MovieEntity();
        final Language language = new Language.Builder(
                LanguageType.ENGLISH
        ).build();
        final MovieLanguageEntity languageEntity = new MovieLanguageEntity(language.getLanguage());
        languageEntity.setStatus(DataStatus.ACCEPTED);
        movieEntity.getLanguages().add(languageEntity);
        this.moviePersistenceService.createLanguage(language, movieEntity);
    }

    /**
     * Test the createGenre method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateGenre() throws ResourceException {
        final MovieEntity movieEntity = new MovieEntity();
        final Genre genre = new Genre.Builder(
                GenreType.ACTION
        ).build();
        this.moviePersistenceService.createGenre(genre, movieEntity);
    }

    /**
     * Test the createGenre method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceConflictException.class)
    public void cantCreateGenreIfExistsDuplicate() throws ResourceException {
        final MovieEntity movieEntity = new MovieEntity();
        final Genre genre = new Genre.Builder(
                GenreType.ACTION
        ).build();
        final MovieGenreEntity genreEntity = new MovieGenreEntity(genre.getGenre());
        genreEntity.setStatus(DataStatus.ACCEPTED);
        movieEntity.getGenres().add(genreEntity);
        this.moviePersistenceService.createGenre(genre, movieEntity);
    }

    /**
     * Test the createReview method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreateReview() throws ResourceException {
        final MovieEntity movieEntity = new MovieEntity();
        final Review review = new Review.Builder(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                true
        ).build();
        this.moviePersistenceService.createReview(review, movieEntity);
    }

    /**
     * Test the createReview method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceConflictException.class)
    public void cantCreateReviewIfExistsDuplicate() throws ResourceException {
        final MovieEntity movieEntity = new MovieEntity();
        final Review review = new Review.Builder(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString(),
                true
        ).build();
        final MovieReviewEntity reviewEntity = new MovieReviewEntity(review.getTitle(), review.getReview(), review.isSpoiler());
        reviewEntity.setStatus(DataStatus.ACCEPTED);
        movieEntity.getReviews().add(reviewEntity);
        this.moviePersistenceService.createReview(review, movieEntity);
    }

    /**
     * Test the createPhoto method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreatePhoto() throws ResourceException {
        final MovieEntity movieEntity = new MovieEntity();
        final ImageRequest photo = new ImageRequest.Builder().withIdInCloud("1").build();
        this.moviePersistenceService.createPhoto(photo, movieEntity);
    }

    /**
     * Test the createPoster method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canCreatePoster() throws ResourceException {
        final MovieEntity movieEntity = new MovieEntity();
        final ImageRequest poster = new ImageRequest.Builder().withIdInCloud("1").build();
        this.moviePersistenceService.createPoster(poster, movieEntity);
    }

    /**
     * Test the saveRating method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canSaveRating() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long movieId = new Random().nextLong();
        final UserEntity userEntity = new UserEntity();
        final MovieEntity movieEntity = new MovieEntity();
        final RateRequest rateRequest = new RateRequest.Builder(
                1
        ).build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.movieRepository.findByIdAndStatus(movieId, DataStatus.ACCEPTED))
                .thenReturn(Optional.of(movieEntity));
        this.moviePersistenceService.saveRating(movieId, rateRequest);
    }

    /**
     * Test the saveRating method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canUpdateRating() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long movieId = new Random().nextLong();
        final UserEntity userEntity = new UserEntity();
        userEntity.setUniqueId(userId);
        final MovieEntity movieEntity = new MovieEntity();
        final MovieRateEntity movieRateEntity = new MovieRateEntity();
        movieRateEntity.setUser(userEntity);
        movieRateEntity.setMovie(movieEntity);
        movieRateEntity.setRate(8);
        movieEntity.getRatings().add(movieRateEntity);
        final RateRequest rateRequest = new RateRequest.Builder(
                7
        ).build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.movieRepository.findByIdAndStatus(movieId, DataStatus.ACCEPTED))
                .thenReturn(Optional.of(movieEntity));
        this.moviePersistenceService.saveRating(movieId, rateRequest);
    }

    /**
     * Test the saveRating method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceConflictException.class)
    public void cantSaveRatingIfMovieHadNoPremiere() throws ResourceException {
        final String userId = UUID.randomUUID().toString();
        final Long movieId = new Random().nextLong();
        final UserEntity userEntity = new UserEntity();
        userEntity.setUniqueId(userId);
        final MovieEntity movieEntity = new MovieEntity();
        Date dt = new Date();
        final Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, 1);
        dt = c.getTime();
        movieEntity.getReleaseDates().add(new MovieReleaseDateEntity(dt, CountryType.USA));
        final RateRequest rateRequest = new RateRequest.Builder(
                7
        ).build();
        Mockito.when(this.authorizationService.getUserId()).thenReturn(userId);
        Mockito
                .when(this.userRepository.findByUniqueIdAndEnabledTrue(userId))
                .thenReturn(Optional.of(userEntity));
        Mockito
                .when(this.movieRepository.findByIdAndStatus(movieId, DataStatus.ACCEPTED))
                .thenReturn(Optional.of(movieEntity));
        this.moviePersistenceService.saveRating(movieId, rateRequest);
    }
}

package com.jonki.popcorn.core.jpa.service;

import com.google.common.collect.Lists;
import com.jonki.popcorn.common.dto.DataStatus;
import com.jonki.popcorn.common.dto.StorageProvider;
import com.jonki.popcorn.common.exception.ResourceException;
import com.jonki.popcorn.common.exception.ResourceNotFoundException;
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
import com.jonki.popcorn.core.jpa.repository.MovieRepository;
import com.jonki.popcorn.core.jpa.repository.UserRepository;
import com.jonki.popcorn.core.service.AuthorizationService;
import com.jonki.popcorn.test.category.UnitTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

/**
 * Unit tests for MovieSearchServiceImpl.
 */
@Category(UnitTest.class)
public class MovieSearchServiceImplUnitTests {

    private MovieRepository movieRepository;
    private MovieSearchServiceImpl service;

    /**
     * Setup for the tests.
     */
    @Before
    public void setup() {
        this.movieRepository = Mockito.mock(MovieRepository.class);
        this.service = new MovieSearchServiceImpl(
                this.movieRepository,
                Mockito.mock(UserRepository.class),
                Mockito.mock(AuthorizationService.class)
        );
    }

    /**
     * Test the getMovie method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantGetMovieIfDoesNotExist() throws ResourceException {
        final Long id = new Random().nextLong();
        Mockito.when(this.movieRepository.findByIdAndStatus(id, DataStatus.ACCEPTED)).thenReturn(Optional.empty());
        this.service.getMovie(id);
    }

    /**
     * Test the getUserMovie method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantGetUserMovieIfDoesNotExist() throws ResourceException {
        final Long id = new Random().nextLong();
        Mockito.when(this.movieRepository.findByIdAndStatus(id, DataStatus.ACCEPTED)).thenReturn(Optional.empty());
        this.service.getUserMovie(id);
    }

    /**
     * Test the getRatings method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantGetMovieRatingsIfMovieDoesNotExist() throws ResourceException {
        final Long id = new Random().nextLong();
        Mockito
                .when(this.movieRepository.findByIdAndStatus(id, DataStatus.ACCEPTED))
                .thenReturn(Optional.empty());
        this.service.getRatings(id);
    }

    /**
     * Test the getRatings method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetMovieRatings() throws ResourceException {
        final Long id = new Random().nextLong();
        final MovieEntity entity = Mockito.mock(MovieEntity.class);
        Mockito.when(entity.getRatings()).thenReturn(Lists.newArrayList());
        Mockito
                .when(this.movieRepository.findByIdAndStatus(id, DataStatus.ACCEPTED))
                .thenReturn(Optional.of(entity));
        Assert.assertTrue(this.service.getRatings(id).isEmpty());
    }

    /**
     * Test the getTitles method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantGetMovieTitlesIfMovieDoesNotExist() throws ResourceException {
        final Long id = new Random().nextLong();
        Mockito
                .when(this.movieRepository.findByIdAndStatus(id, DataStatus.ACCEPTED))
                .thenReturn(Optional.empty());
        this.service.getTitles(id);
    }

    /**
     * Test the getTitles method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetMovieTitles() throws ResourceException {
        final List<MovieOtherTitleEntity> otherTitleList = new ArrayList<>();
        MovieOtherTitleEntity ot = new MovieOtherTitleEntity();
        ot.setStatus(DataStatus.ACCEPTED);
        otherTitleList.add(ot);
        ot = new MovieOtherTitleEntity();
        ot.setStatus(DataStatus.REJECTED);
        otherTitleList.add(ot);

        final Long id = new Random().nextLong();
        final MovieEntity entity = Mockito.mock(MovieEntity.class);
        Mockito.when(entity.getOtherTitles()).thenReturn(otherTitleList);
        Mockito
                .when(this.movieRepository.findByIdAndStatus(id, DataStatus.ACCEPTED))
                .thenReturn(Optional.of(entity));
        Assert.assertEquals(1, this.service.getTitles(id).size());
    }

    /**
     * Test the getReleaseDates method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantGetMovieReleaseDatesIfMovieDoesNotExist() throws ResourceException {
        final Long id = new Random().nextLong();
        Mockito
                .when(this.movieRepository.findByIdAndStatus(id, DataStatus.ACCEPTED))
                .thenReturn(Optional.empty());
        this.service.getReleaseDates(id);
    }

    /**
     * Test the getReleaseDates method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetMovieReleaseDates() throws ResourceException {
        final List<MovieReleaseDateEntity> releaseDateList = new ArrayList<>();
        MovieReleaseDateEntity rd = new MovieReleaseDateEntity();
        rd.setStatus(DataStatus.ACCEPTED);
        releaseDateList.add(rd);
        rd = new MovieReleaseDateEntity();
        rd.setStatus(DataStatus.REJECTED);
        releaseDateList.add(rd);

        final Long id = new Random().nextLong();
        final MovieEntity entity = Mockito.mock(MovieEntity.class);
        Mockito.when(entity.getReleaseDates()).thenReturn(releaseDateList);
        Mockito
                .when(this.movieRepository.findByIdAndStatus(id, DataStatus.ACCEPTED))
                .thenReturn(Optional.of(entity));
        Assert.assertEquals(1, this.service.getReleaseDates(id).size());
    }

    /**
     * Test the getOutlines method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantGetMovieOutlinesIfMovieDoesNotExist() throws ResourceException {
        final Long id = new Random().nextLong();
        Mockito
                .when(this.movieRepository.findByIdAndStatus(id, DataStatus.ACCEPTED))
                .thenReturn(Optional.empty());
        this.service.getOutlines(id);
    }

    /**
     * Test the getOutlines method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetMovieOutlines() throws ResourceException {
        final List<MovieOutlineEntity> outlineList = new ArrayList<>();
        MovieOutlineEntity o = new MovieOutlineEntity();
        o.setStatus(DataStatus.ACCEPTED);
        outlineList.add(o);
        o = new MovieOutlineEntity();
        o.setStatus(DataStatus.REJECTED);
        outlineList.add(o);

        final Long id = new Random().nextLong();
        final MovieEntity entity = Mockito.mock(MovieEntity.class);
        Mockito.when(entity.getOutlines()).thenReturn(outlineList);
        Mockito
                .when(this.movieRepository.findByIdAndStatus(id, DataStatus.ACCEPTED))
                .thenReturn(Optional.of(entity));
        Assert.assertEquals(1, this.service.getOutlines(id).size());
    }

    /**
     * Test the getSummaries method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantGetMovieSummariesIfMovieDoesNotExist() throws ResourceException {
        final Long id = new Random().nextLong();
        Mockito
                .when(this.movieRepository.findByIdAndStatus(id, DataStatus.ACCEPTED))
                .thenReturn(Optional.empty());
        this.service.getSummaries(id);
    }

    /**
     * Test the getSummaries method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetMovieSummaries() throws ResourceException {
        final List<MovieSummaryEntity> summaryList = new ArrayList<>();
        MovieSummaryEntity s = new MovieSummaryEntity();
        s.setStatus(DataStatus.ACCEPTED);
        summaryList.add(s);
        s = new MovieSummaryEntity();
        s.setStatus(DataStatus.REJECTED);
        summaryList.add(s);

        final Long id = new Random().nextLong();
        final MovieEntity entity = Mockito.mock(MovieEntity.class);
        Mockito.when(entity.getSummaries()).thenReturn(summaryList);
        Mockito
                .when(this.movieRepository.findByIdAndStatus(id, DataStatus.ACCEPTED))
                .thenReturn(Optional.of(entity));
        Assert.assertEquals(1, this.service.getSummaries(id).size());
    }

    /**
     * Test the getSynopses method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantGetMovieSynopsesIfMovieDoesNotExist() throws ResourceException {
        final Long id = new Random().nextLong();
        Mockito
                .when(this.movieRepository.findByIdAndStatus(id, DataStatus.ACCEPTED))
                .thenReturn(Optional.empty());
        this.service.getSynopses(id);
    }

    /**
     * Test the getSynopses method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetMovieSynopses() throws ResourceException {
        final List<MovieSynopsisEntity> synopsisList = new ArrayList<>();
        MovieSynopsisEntity s = new MovieSynopsisEntity();
        s.setStatus(DataStatus.ACCEPTED);
        synopsisList.add(s);
        s = new MovieSynopsisEntity();
        s.setStatus(DataStatus.REJECTED);
        synopsisList.add(s);

        final Long id = new Random().nextLong();
        final MovieEntity entity = Mockito.mock(MovieEntity.class);
        Mockito.when(entity.getSynopses()).thenReturn(synopsisList);
        Mockito
                .when(this.movieRepository.findByIdAndStatus(id, DataStatus.ACCEPTED))
                .thenReturn(Optional.of(entity));
        Assert.assertEquals(1, this.service.getSynopses(id).size());
    }

    /**
     * Test the getBoxOffices method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantGetMovieBoxOfficesIfMovieDoesNotExist() throws ResourceException {
        final Long id = new Random().nextLong();
        Mockito
                .when(this.movieRepository.findByIdAndStatus(id, DataStatus.ACCEPTED))
                .thenReturn(Optional.empty());
        this.service.getBoxOffices(id);
    }

    /**
     * Test the getBoxOffices method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetMovieBoxOffices() throws ResourceException {
        final List<MovieBoxOfficeEntity> boxOfficeList = new ArrayList<>();
        MovieBoxOfficeEntity bo = new MovieBoxOfficeEntity();
        bo.setStatus(DataStatus.ACCEPTED);
        boxOfficeList.add(bo);
        bo = new MovieBoxOfficeEntity();
        bo.setStatus(DataStatus.REJECTED);
        boxOfficeList.add(bo);

        final Long id = new Random().nextLong();
        final MovieEntity entity = Mockito.mock(MovieEntity.class);
        Mockito.when(entity.getBoxOffices()).thenReturn(boxOfficeList);
        Mockito
                .when(this.movieRepository.findByIdAndStatus(id, DataStatus.ACCEPTED))
                .thenReturn(Optional.of(entity));
        Assert.assertEquals(1, this.service.getBoxOffices(id).size());
    }

    /**
     * Test the getSites method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantGetMovieSitesIfMovieDoesNotExist() throws ResourceException {
        final Long id = new Random().nextLong();
        Mockito
                .when(this.movieRepository.findByIdAndStatus(id, DataStatus.ACCEPTED))
                .thenReturn(Optional.empty());
        this.service.getSites(id);
    }

    /**
     * Test the getSites method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetMovieSites() throws ResourceException {
        final List<MovieSiteEntity> siteList = new ArrayList<>();
        MovieSiteEntity s = new MovieSiteEntity();
        s.setStatus(DataStatus.ACCEPTED);
        siteList.add(s);
        s = new MovieSiteEntity();
        s.setStatus(DataStatus.REJECTED);
        siteList.add(s);

        final Long id = new Random().nextLong();
        final MovieEntity entity = Mockito.mock(MovieEntity.class);
        Mockito.when(entity.getSites()).thenReturn(siteList);
        Mockito
                .when(this.movieRepository.findByIdAndStatus(id, DataStatus.ACCEPTED))
                .thenReturn(Optional.of(entity));
        Assert.assertEquals(1, this.service.getSites(id).size());
    }

    /**
     * Test the getCountries method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantGetMovieCountriesIfMovieDoesNotExist() throws ResourceException {
        final Long id = new Random().nextLong();
        Mockito
                .when(this.movieRepository.findByIdAndStatus(id, DataStatus.ACCEPTED))
                .thenReturn(Optional.empty());
        this.service.getCountries(id);
    }

    /**
     * Test the getCountries method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetMovieCountries() throws ResourceException {
        final List<MovieCountryEntity> countryList = new ArrayList<>();
        MovieCountryEntity c = new MovieCountryEntity();
        c.setStatus(DataStatus.ACCEPTED);
        countryList.add(c);
        c = new MovieCountryEntity();
        c.setStatus(DataStatus.REJECTED);
        countryList.add(c);

        final Long id = new Random().nextLong();
        final MovieEntity entity = Mockito.mock(MovieEntity.class);
        Mockito.when(entity.getCountries()).thenReturn(countryList);
        Mockito
                .when(this.movieRepository.findByIdAndStatus(id, DataStatus.ACCEPTED))
                .thenReturn(Optional.of(entity));
        Assert.assertEquals(1, this.service.getCountries(id).size());
    }

    /**
     * Test the getLanguages method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantGetMovieLanguagesIfMovieDoesNotExist() throws ResourceException {
        final Long id = new Random().nextLong();
        Mockito
                .when(this.movieRepository.findByIdAndStatus(id, DataStatus.ACCEPTED))
                .thenReturn(Optional.empty());
        this.service.getLanguages(id);
    }

    /**
     * Test the getLanguages method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetMovieLanguages() throws ResourceException {
        final List<MovieLanguageEntity> languageList = new ArrayList<>();
        MovieLanguageEntity l = new MovieLanguageEntity();
        l.setStatus(DataStatus.ACCEPTED);
        languageList.add(l);
        l = new MovieLanguageEntity();
        l.setStatus(DataStatus.REJECTED);
        languageList.add(l);

        final Long id = new Random().nextLong();
        final MovieEntity entity = Mockito.mock(MovieEntity.class);
        Mockito.when(entity.getLanguages()).thenReturn(languageList);
        Mockito
                .when(this.movieRepository.findByIdAndStatus(id, DataStatus.ACCEPTED))
                .thenReturn(Optional.of(entity));
        Assert.assertEquals(1, this.service.getLanguages(id).size());
    }

    /**
     * Test the getGenres method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantGetMovieGenresIfMovieDoesNotExist() throws ResourceException {
        final Long id = new Random().nextLong();
        Mockito
                .when(this.movieRepository.findByIdAndStatus(id, DataStatus.ACCEPTED))
                .thenReturn(Optional.empty());
        this.service.getGenres(id);
    }

    /**
     * Test the getGenres method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetMovieGenres() throws ResourceException {
        final List<MovieGenreEntity> genreList = new ArrayList<>();
        MovieGenreEntity g = new MovieGenreEntity();
        g.setStatus(DataStatus.ACCEPTED);
        genreList.add(g);
        g = new MovieGenreEntity();
        g.setStatus(DataStatus.REJECTED);
        genreList.add(g);

        final Long id = new Random().nextLong();
        final MovieEntity entity = Mockito.mock(MovieEntity.class);
        Mockito.when(entity.getGenres()).thenReturn(genreList);
        Mockito
                .when(this.movieRepository.findByIdAndStatus(id, DataStatus.ACCEPTED))
                .thenReturn(Optional.of(entity));
        Assert.assertEquals(1, this.service.getGenres(id).size());
    }

    /**
     * Test the getReviews method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantGetMovieReviewsIfMovieDoesNotExist() throws ResourceException {
        final Long id = new Random().nextLong();
        Mockito
                .when(this.movieRepository.findByIdAndStatus(id, DataStatus.ACCEPTED))
                .thenReturn(Optional.empty());
        this.service.getReviews(id);
    }

    /**
     * Test the getReviews method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetMovieReviews() throws ResourceException {
        final List<MovieReviewEntity> reviewList = new ArrayList<>();
        MovieReviewEntity r = new MovieReviewEntity();
        r.setStatus(DataStatus.ACCEPTED);
        reviewList.add(r);
        r = new MovieReviewEntity();
        r.setStatus(DataStatus.REJECTED);
        reviewList.add(r);

        final Long id = new Random().nextLong();
        final MovieEntity entity = Mockito.mock(MovieEntity.class);
        Mockito.when(entity.getReviews()).thenReturn(reviewList);
        Mockito
                .when(this.movieRepository.findByIdAndStatus(id, DataStatus.ACCEPTED))
                .thenReturn(Optional.of(entity));
        Assert.assertEquals(1, this.service.getReviews(id).size());
    }

    /**
     * Test the getPhotos method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantGetMoviePhotosIfMovieDoesNotExist() throws ResourceException {
        final Long id = new Random().nextLong();
        Mockito
                .when(this.movieRepository.findByIdAndStatus(id, DataStatus.ACCEPTED))
                .thenReturn(Optional.empty());
        this.service.getPhotos(id);
    }

    /**
     * Test the getPhotos method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetMoviePhotos() throws ResourceException {
        final List<MoviePhotoEntity> photoList = new ArrayList<>();
        MoviePhotoEntity p = new MoviePhotoEntity();
        p.setStatus(DataStatus.ACCEPTED);
        p.setProvider(StorageProvider.GOOGLE);
        p.setIdInCloud(UUID.randomUUID().toString());
        photoList.add(p);
        p = new MoviePhotoEntity();
        p.setStatus(DataStatus.REJECTED);
        p.setProvider(StorageProvider.GOOGLE);
        p.setIdInCloud(UUID.randomUUID().toString());
        photoList.add(p);

        final Long id = new Random().nextLong();
        final MovieEntity entity = Mockito.mock(MovieEntity.class);
        Mockito.when(entity.getPhotos()).thenReturn(photoList);
        Mockito
                .when(this.movieRepository.findByIdAndStatus(id, DataStatus.ACCEPTED))
                .thenReturn(Optional.of(entity));
        Assert.assertEquals(1, this.service.getPhotos(id).size());
    }

    /**
     * Test the getPosters method.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourceNotFoundException.class)
    public void cantGetMoviePostersIfMovieDoesNotExist() throws ResourceException {
        final Long id = new Random().nextLong();
        Mockito
                .when(this.movieRepository.findByIdAndStatus(id, DataStatus.ACCEPTED))
                .thenReturn(Optional.empty());
        this.service.getPosters(id);
    }

    /**
     * Test the getPosters method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetMoviePosters() throws ResourceException {
        final List<MoviePosterEntity> posterList = new ArrayList<>();
        MoviePosterEntity p = new MoviePosterEntity();
        p.setStatus(DataStatus.ACCEPTED);
        p.setProvider(StorageProvider.GOOGLE);
        p.setIdInCloud(UUID.randomUUID().toString());
        posterList.add(p);
        p = new MoviePosterEntity();
        p.setStatus(DataStatus.REJECTED);
        p.setProvider(StorageProvider.GOOGLE);
        p.setIdInCloud(UUID.randomUUID().toString());
        posterList.add(p);

        final Long id = new Random().nextLong();
        final MovieEntity entity = Mockito.mock(MovieEntity.class);
        Mockito.when(entity.getPosters()).thenReturn(posterList);
        Mockito
                .when(this.movieRepository.findByIdAndStatus(id, DataStatus.ACCEPTED))
                .thenReturn(Optional.of(entity));
        Assert.assertEquals(1, this.service.getPosters(id).size());
    }
}

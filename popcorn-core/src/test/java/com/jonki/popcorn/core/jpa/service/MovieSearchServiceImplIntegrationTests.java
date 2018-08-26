package com.jonki.popcorn.core.jpa.service;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.jonki.popcorn.common.dto.Movie;
import com.jonki.popcorn.common.dto.UserMovie;
import com.jonki.popcorn.common.dto.movie.type.MovieType;
import com.jonki.popcorn.common.dto.search.MovieSearchResult;
import com.jonki.popcorn.common.exception.ResourceException;
import com.jonki.popcorn.core.security.model.CustomUserDetails;
import com.jonki.popcorn.core.service.MovieSearchService;
import com.jonki.popcorn.test.category.IntegrationTest;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Integration tests for MovieSearchServiceImpl.
 */
@Category(IntegrationTest.class)
@DatabaseSetup("MovieSearchServiceImplIntegrationTests/init.xml")
@DatabaseTearDown("cleanup.xml")
public class MovieSearchServiceImplIntegrationTests extends DBIntegrationTestBase {

    private static final String USER_1_ID = "user1";
    private static final String USER_1_USERNAME = "jonki1";
    private static final String USER_1_PASSWORD = "$2a$12$O02MQJS6AnCJiRw1nF6XsO7udBCza591oUKUbRC4/XrUmPXZaSjVK";

    private static final Long MOV_1_ID = 2L;
    private static final Long MOV_2_ID = 3L;
    private static final Long MOV_3_ID = 4L;
    private static final Long MOV_4_ID = 5L;
    private static final Long MOV_5_ID = 6L;
    private static final Long MOV_6_ID = 7L;
    private static final Long MOV_7_ID = 8L;
    private static final Long MOV_8_ID = 9L;
    private static final Long MOV_9_ID = 10L;
    private static final Long MOV_10_ID = 11L;
    private static final Long MOV_11_ID = 12L;
    private static final Long MOV_12_ID = 13L;
    private static final Long MOV_13_ID = 14L;

    private static final Long MOV_14_ID = 15L;
    private static final String MOV_14_TITLE = "Mov14";
    private static final MovieType MOV_14_TYPE= MovieType.CINEMA;
    private static final Float MOV_14_RATING = 10F;
    private static final Float MOV_14_YOUR_RATING = 10F;

    @Autowired
    private MovieSearchService movieSearchService;

    /**
     * Setup.
     */
    @Before
    public void setup() {
        Assert.assertThat(this.movieRepository.count(), Matchers.is(14L));

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
     * Make sure we can search movies successfully.
     */
    @Test
    public void canFindMovies() {
        //TODO: add more cases
        final Pageable page = PageRequest.of(0, 10, Sort.Direction.DESC, "id");
        Page<MovieSearchResult> movies = this.movieSearchService
                .findMovies(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        page
                );
        Assert.assertThat(movies.getTotalElements(), Matchers.is(14L));

        movies = this.movieSearchService
                .findMovies(
                        null,
                        MovieType.CINEMA,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        page
                );
        Assert.assertThat(movies.getTotalElements(), Matchers.is(12L));

        movies = this.movieSearchService
                .findMovies(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        6,
                        null,
                        page
                );
        Assert.assertThat(movies.getTotalElements(), Matchers.is(10L));

        movies = this.movieSearchService
                .findMovies(
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        6,
                        page
                );
        Assert.assertThat(movies.getTotalElements(), Matchers.is(7L));
    }

    /**
     * Test the getMovie method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetMovie() throws ResourceException {
        final Movie movie = this.movieSearchService.getMovie(MOV_14_ID);

        Assert.assertThat(movie.getId(), Matchers.is(MOV_14_ID.toString()));
        Assert.assertThat(movie.getTitle(), Matchers.is(MOV_14_TITLE));
        Assert.assertThat(movie.getType(), Matchers.is(MOV_14_TYPE));
        Assert.assertThat(movie.getRating(), Matchers.is(MOV_14_RATING));
    }

    /**
     * Test the getUserMovie method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetUserMovie() throws ResourceException {
        final UserMovie movie = this.movieSearchService.getUserMovie(MOV_14_ID);

        Assert.assertThat(movie.getId(), Matchers.is(MOV_14_ID.toString()));
        Assert.assertThat(movie.getTitle(), Matchers.is(MOV_14_TITLE));
        Assert.assertThat(movie.getType(), Matchers.is(MOV_14_TYPE));
        Assert.assertThat(movie.getRating(), Matchers.is(MOV_14_RATING));
        Assert.assertThat(movie.getYourRating(), Matchers.is(MOV_14_YOUR_RATING));
        Assert.assertTrue(movie.isFavorited());
    }

    /**
     * Test the getRatings method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetMovieRatings() throws ResourceException {
        Assert.assertEquals(1, this.movieSearchService.getRatings(MOV_14_ID).size());
    }

    /**
     * Test the getTitles method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetMovieTitles() throws ResourceException {
        Assert.assertEquals(2, this.movieSearchService.getTitles(MOV_1_ID).size());
    }

    /**
     * Test the getReleaseDates method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetMovieReleaseDates() throws ResourceException {
        Assert.assertEquals(3, this.movieSearchService.getReleaseDates(MOV_2_ID).size());
    }

    /**
     * Test the getOutlines method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetMovieOutlines() throws ResourceException {
        Assert.assertEquals(2, this.movieSearchService.getOutlines(MOV_3_ID).size());
    }

    /**
     * Test the getSummaries method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetMovieSummaries() throws ResourceException {
        Assert.assertEquals(3, this.movieSearchService.getSummaries(MOV_4_ID).size());
    }

    /**
     * Test the getSynopses method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetMovieSynopses() throws ResourceException {
        Assert.assertEquals(2, this.movieSearchService.getSynopses(MOV_5_ID).size());
    }

    /**
     * Test the getBoxOffices method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetMovieBoxOffices() throws ResourceException {
        Assert.assertEquals(3, this.movieSearchService.getBoxOffices(MOV_6_ID).size());
    }

    /**
     * Test the getSites method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetMovieSites() throws ResourceException {
        Assert.assertEquals(2, this.movieSearchService.getSites(MOV_7_ID).size());
    }

    /**
     * Test the getCountries method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetMovieCountries() throws ResourceException {
        Assert.assertEquals(3, this.movieSearchService.getCountries(MOV_8_ID).size());
    }

    /**
     * Test the getLanguages method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetMovieLanguages() throws ResourceException {
        Assert.assertEquals(2, this.movieSearchService.getLanguages(MOV_9_ID).size());
    }

    /**
     * Test the getGenres method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetMovieGenres() throws ResourceException {
        Assert.assertEquals(3, this.movieSearchService.getGenres(MOV_10_ID).size());
    }

    /**
     * Test the getReviews method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetMovieReviews() throws ResourceException {
        Assert.assertEquals(2, this.movieSearchService.getReviews(MOV_11_ID).size());
    }

    /**
     * Test the getPhotos method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetMoviePhotos() throws ResourceException {
        Assert.assertEquals(3, this.movieSearchService.getPhotos(MOV_12_ID).size());
    }

    /**
     * Test the getPosters method.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void canGetMoviePosters() throws ResourceException {
        Assert.assertEquals(2, this.movieSearchService.getPosters(MOV_13_ID).size());
    }
}

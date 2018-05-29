package com.jonki.popcorn.common.dto;

import com.jonki.popcorn.common.dto.movie.ReleaseDate;
import com.jonki.popcorn.common.dto.movie.type.CountryType;
import com.jonki.popcorn.common.dto.movie.type.GenreType;
import com.jonki.popcorn.common.dto.movie.type.LanguageType;
import com.jonki.popcorn.common.dto.movie.type.MovieType;
import com.jonki.popcorn.test.category.UnitTest;
import com.jonki.popcorn.test.supplier.RandomSupplier;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Tests for the UserMovie DTO.
 */
@Category(UnitTest.class)
public class UserMovieUnitTests {

    private static final String TITLE = RandomSupplier.STRING.get();
    private static final MovieType TYPE = MovieType.CINEMA;

    /**
     * Test to make sure we can build an user movie using the default builder constructor.
     */
    @Test
    public void canBuildUserMovie() {
        final UserMovie userMovie = new UserMovie.Builder(
                TITLE,
                TYPE
        ).build();
        Assert.assertThat(userMovie.getTitle(), Matchers.is(TITLE));
        Assert.assertThat(userMovie.getType(), Matchers.is(TYPE));
        Assert.assertFalse(Optional.ofNullable(userMovie.getYourRating()).isPresent());
        Assert.assertFalse(userMovie.isFavorited());
        Assert.assertFalse(Optional.ofNullable(userMovie.getTitleLocated()).isPresent());
        Assert.assertFalse(Optional.ofNullable(userMovie.getRating()).isPresent());
        Assert.assertFalse(Optional.ofNullable(userMovie.getNumberOfRatings()).isPresent());
        Assert.assertFalse(Optional.ofNullable(userMovie.getReleaseDate()).isPresent());
        Assert.assertThat(userMovie.getCountries(), Matchers.empty());
        Assert.assertThat(userMovie.getLanguages(), Matchers.empty());
        Assert.assertThat(userMovie.getGenres(), Matchers.empty());
        Assert.assertFalse(Optional.ofNullable(userMovie.getBoxofficeCumulative()).isPresent());
        Assert.assertFalse(Optional.ofNullable(userMovie.getOutline()).isPresent());
        Assert.assertFalse(Optional.ofNullable(userMovie.getSummary()).isPresent());
        Assert.assertFalse(Optional.ofNullable(userMovie.getId()).isPresent());
    }

    /**
     * Test to make sure we can build a user movie with all optional parameters.
     */
    @Test
    public void canBuildUserMovieWithOptionals() {
        final UserMovie.Builder builder = new UserMovie.Builder(
                TITLE,
                TYPE
        );

        final Float yourRating = (float) RandomSupplier.INT.get();
        builder.withYourRating(yourRating);

        final boolean favorited = true;
        builder.withFavorited(favorited);

        final String titleLocated = RandomSupplier.STRING.get();
        builder.withTitleLocated(titleLocated);

        final Float rating = (float) RandomSupplier.INT.get();
        builder.withRating(rating);

        final Integer numberOfRating = RandomSupplier.INT.get();
        builder.withNumberOfRating(numberOfRating);

        final ReleaseDate releaseDate = new ReleaseDate.Builder(RandomSupplier.DATE.get(), CountryType.USA).build();
        builder.withReleaseDate(releaseDate);

        final List<CountryType> countries = Collections.singletonList(CountryType.USA);
        builder.withCountries(countries);

        final List<LanguageType> languages = Collections.singletonList(LanguageType.ENGLISH);
        builder.withLanguages(languages);

        final List<GenreType> genres = Collections.singletonList(GenreType.ACTION);
        builder.withGenres(genres);

        final BigDecimal boxofficeCumulative = new BigDecimal(RandomSupplier.LONG.get());
        builder.withBoxofficeCumulative(boxofficeCumulative);

        final String outline = RandomSupplier.STRING.get();
        builder.withOutline(outline);

        final String summary = RandomSupplier.STRING.get();
        builder.withSummary(summary);

        final String id = RandomSupplier.STRING.get();
        builder.withId(id);

        final UserMovie userMovie = builder.build();
        Assert.assertThat(userMovie.getTitle(), Matchers.is(TITLE));
        Assert.assertThat(userMovie.getType(), Matchers.is(TYPE));
        Assert.assertThat(userMovie.getYourRating(), Matchers.is(yourRating));
        Assert.assertTrue(userMovie.isFavorited());
        Assert.assertThat(userMovie.getTitleLocated(), Matchers.is(titleLocated));
        Assert.assertThat(userMovie.getRating(), Matchers.is(rating));
        Assert.assertThat(userMovie.getNumberOfRatings(), Matchers.is(numberOfRating));
        Assert.assertThat(userMovie.getReleaseDate(), Matchers.is(releaseDate));
        Assert.assertThat(userMovie.getCountries(), Matchers.is(countries));
        Assert.assertThat(userMovie.getLanguages(), Matchers.is(languages));
        Assert.assertThat(userMovie.getGenres(), Matchers.is(genres));
        Assert.assertThat(userMovie.getBoxofficeCumulative(), Matchers.is(boxofficeCumulative));
        Assert.assertThat(userMovie.getOutline(), Matchers.is(outline));
        Assert.assertThat(userMovie.getSummary(), Matchers.is(summary));
        Assert.assertThat(Optional.of(userMovie.getId()).orElseThrow(IllegalArgumentException::new), Matchers.is(id));
    }

    /**
     * Test to make sure we can build an user movie with null collection parameters.
     */
    @Test
    public void canBuildUserMovieNullOptionals() {
        final UserMovie.Builder builder = new UserMovie.Builder(
                TITLE,
                TYPE
        );
        builder.withYourRating(null);
        builder.withTitleLocated(null);
        builder.withRating(null);
        builder.withNumberOfRating(null);
        builder.withReleaseDate(null);
        builder.withCountries(null);
        builder.withLanguages(null);
        builder.withGenres(null);
        builder.withBoxofficeCumulative(null);
        builder.withOutline(null);
        builder.withSummary(null);
        builder.withId(null);

        final UserMovie userMovie = builder.build();
        Assert.assertThat(userMovie.getTitle(), Matchers.is(TITLE));
        Assert.assertThat(userMovie.getType(), Matchers.is(TYPE));
        Assert.assertFalse(Optional.ofNullable(userMovie.getYourRating()).isPresent());
        Assert.assertFalse(Optional.ofNullable(userMovie.getTitleLocated()).isPresent());
        Assert.assertFalse(Optional.ofNullable(userMovie.getRating()).isPresent());
        Assert.assertFalse(Optional.ofNullable(userMovie.getNumberOfRatings()).isPresent());
        Assert.assertFalse(Optional.ofNullable(userMovie.getReleaseDate()).isPresent());
        Assert.assertThat(userMovie.getCountries(), Matchers.empty());
        Assert.assertThat(userMovie.getLanguages(), Matchers.empty());
        Assert.assertThat(userMovie.getGenres(), Matchers.empty());
        Assert.assertFalse(Optional.ofNullable(userMovie.getBoxofficeCumulative()).isPresent());
        Assert.assertFalse(Optional.ofNullable(userMovie.getOutline()).isPresent());
        Assert.assertFalse(Optional.ofNullable(userMovie.getSummary()).isPresent());
        Assert.assertFalse(Optional.ofNullable(userMovie.getId()).isPresent());
    }

    /**
     * Test to make sure we can build an user movie using the builder constructor with movie object.
     */
    @Test
    public void canBuildUserMovieWithMovieInConstructor() {
        final UserMovie userMovie = new UserMovie.Builder(
                new Movie.Builder(TITLE, TYPE).build()
        ).build();
        Assert.assertThat(userMovie.getTitle(), Matchers.is(TITLE));
        Assert.assertThat(userMovie.getType(), Matchers.is(TYPE));
        Assert.assertFalse(Optional.ofNullable(userMovie.getYourRating()).isPresent());
        Assert.assertFalse(userMovie.isFavorited());
        Assert.assertFalse(Optional.ofNullable(userMovie.getTitleLocated()).isPresent());
        Assert.assertFalse(Optional.ofNullable(userMovie.getRating()).isPresent());
        Assert.assertFalse(Optional.ofNullable(userMovie.getNumberOfRatings()).isPresent());
        Assert.assertFalse(Optional.ofNullable(userMovie.getReleaseDate()).isPresent());
        Assert.assertThat(userMovie.getCountries(), Matchers.empty());
        Assert.assertThat(userMovie.getLanguages(), Matchers.empty());
        Assert.assertThat(userMovie.getGenres(), Matchers.empty());
        Assert.assertFalse(Optional.ofNullable(userMovie.getBoxofficeCumulative()).isPresent());
        Assert.assertFalse(Optional.ofNullable(userMovie.getOutline()).isPresent());
        Assert.assertFalse(Optional.ofNullable(userMovie.getSummary()).isPresent());
        Assert.assertFalse(Optional.ofNullable(userMovie.getId()).isPresent());
    }

    /**
     * Test equals.
     */
    @Test
    public void canFindEquality() {
        final UserMovie.Builder builder = new UserMovie.Builder(
                TITLE,
                TYPE
        );
        builder.withYourRating(null);
        builder.withTitleLocated(null);
        builder.withRating(null);
        builder.withNumberOfRating(null);
        builder.withReleaseDate(null);
        builder.withCountries(null);
        builder.withLanguages(null);
        builder.withGenres(null);
        builder.withBoxofficeCumulative(null);
        builder.withOutline(null);
        builder.withSummary(null);
        builder.withId(UUID.randomUUID().toString());
        final UserMovie userMovie1 = builder.build();
        final UserMovie userMovie2 = builder.build();
        builder.withId(UUID.randomUUID().toString());
        final UserMovie userMovie3 = builder.build();

        Assert.assertTrue(userMovie1.equals(userMovie2));
        Assert.assertTrue(userMovie2.equals(userMovie1));
        Assert.assertFalse(userMovie1.equals(userMovie3));
    }

    /**
     * Test hash code.
     */
    @Test
    public void canUseHashCode() {
        final UserMovie.Builder builder = new UserMovie.Builder(
                TITLE,
                TYPE
        );
        builder.withYourRating(null);
        builder.withTitleLocated(null);
        builder.withRating(null);
        builder.withNumberOfRating(null);
        builder.withReleaseDate(null);
        builder.withCountries(null);
        builder.withLanguages(null);
        builder.withGenres(null);
        builder.withBoxofficeCumulative(null);
        builder.withOutline(null);
        builder.withSummary(null);
        builder.withId(UUID.randomUUID().toString());
        final UserMovie userMovie1 = builder.build();
        final UserMovie userMovie2 = builder.build();
        builder.withId(UUID.randomUUID().toString());
        final UserMovie userMovie3 = builder.build();

        Assert.assertEquals(userMovie1.hashCode(), userMovie2.hashCode());
        Assert.assertNotEquals(userMovie1.hashCode(), userMovie3.hashCode());
    }
}

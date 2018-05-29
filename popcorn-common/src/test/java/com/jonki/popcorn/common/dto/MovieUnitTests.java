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
 * Tests for the Movie DTO.
 */
@Category(UnitTest.class)
public class MovieUnitTests {

    private static final String TITLE = RandomSupplier.STRING.get();
    private static final MovieType TYPE = MovieType.CINEMA;

    /**
     * Test to make sure we can build an movie using the default builder constructor.
     */
    @Test
    public void canBuildMovie() {
        final Movie movie = new Movie.Builder(
                TITLE,
                TYPE
        ).build();
        Assert.assertThat(movie.getTitle(), Matchers.is(TITLE));
        Assert.assertThat(movie.getType(), Matchers.is(TYPE));
        Assert.assertFalse(Optional.ofNullable(movie.getTitleLocated()).isPresent());
        Assert.assertFalse(Optional.ofNullable(movie.getRating()).isPresent());
        Assert.assertFalse(Optional.ofNullable(movie.getNumberOfRatings()).isPresent());
        Assert.assertFalse(Optional.ofNullable(movie.getReleaseDate()).isPresent());
        Assert.assertThat(movie.getCountries(), Matchers.empty());
        Assert.assertThat(movie.getLanguages(), Matchers.empty());
        Assert.assertThat(movie.getGenres(), Matchers.empty());
        Assert.assertFalse(Optional.ofNullable(movie.getBoxofficeCumulative()).isPresent());
        Assert.assertFalse(Optional.ofNullable(movie.getOutline()).isPresent());
        Assert.assertFalse(Optional.ofNullable(movie.getSummary()).isPresent());
        Assert.assertFalse(Optional.ofNullable(movie.getId()).isPresent());
    }

    /**
     * Test to make sure we can build a movie with all optional parameters.
     */
    @Test
    public void canBuildMovieWithOptionals() {
        final Movie.Builder builder = new Movie.Builder(
                TITLE,
                TYPE
        );

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

        final Movie movie = builder.build();
        Assert.assertThat(movie.getTitle(), Matchers.is(TITLE));
        Assert.assertThat(movie.getType(), Matchers.is(TYPE));
        Assert.assertThat(movie.getTitleLocated(), Matchers.is(titleLocated));
        Assert.assertThat(movie.getRating(), Matchers.is(rating));
        Assert.assertThat(movie.getNumberOfRatings(), Matchers.is(numberOfRating));
        Assert.assertThat(movie.getReleaseDate(), Matchers.is(releaseDate));
        Assert.assertThat(movie.getCountries(), Matchers.is(countries));
        Assert.assertThat(movie.getLanguages(), Matchers.is(languages));
        Assert.assertThat(movie.getGenres(), Matchers.is(genres));
        Assert.assertThat(movie.getBoxofficeCumulative(), Matchers.is(boxofficeCumulative));
        Assert.assertThat(movie.getOutline(), Matchers.is(outline));
        Assert.assertThat(movie.getSummary(), Matchers.is(summary));
        Assert.assertThat(Optional.of(movie.getId()).orElseThrow(IllegalArgumentException::new), Matchers.is(id));
    }

    /**
     * Test to make sure we can build an movie with null collection parameters.
     */
    @Test
    public void canBuildMovieNullOptionals() {
        final Movie.Builder builder = new Movie.Builder(
                TITLE,
                TYPE
        );
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

        final Movie movie = builder.build();
        Assert.assertThat(movie.getTitle(), Matchers.is(TITLE));
        Assert.assertThat(movie.getType(), Matchers.is(TYPE));
        Assert.assertFalse(Optional.ofNullable(movie.getTitleLocated()).isPresent());
        Assert.assertFalse(Optional.ofNullable(movie.getRating()).isPresent());
        Assert.assertFalse(Optional.ofNullable(movie.getNumberOfRatings()).isPresent());
        Assert.assertFalse(Optional.ofNullable(movie.getReleaseDate()).isPresent());
        Assert.assertThat(movie.getCountries(), Matchers.empty());
        Assert.assertThat(movie.getLanguages(), Matchers.empty());
        Assert.assertThat(movie.getGenres(), Matchers.empty());
        Assert.assertFalse(Optional.ofNullable(movie.getBoxofficeCumulative()).isPresent());
        Assert.assertFalse(Optional.ofNullable(movie.getOutline()).isPresent());
        Assert.assertFalse(Optional.ofNullable(movie.getSummary()).isPresent());
        Assert.assertFalse(Optional.ofNullable(movie.getId()).isPresent());
    }

    /**
     * Test equals.
     */
    @Test
    public void canFindEquality() {
        final Movie.Builder builder = new Movie.Builder(
                TITLE,
                TYPE
        );
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
        final Movie movie1 = builder.build();
        final Movie movie2 = builder.build();
        builder.withId(UUID.randomUUID().toString());
        final Movie movie3 = builder.build();

        Assert.assertTrue(movie1.equals(movie2));
        Assert.assertTrue(movie2.equals(movie1));
        Assert.assertFalse(movie1.equals(movie3));
    }

    /**
     * Test hash code.
     */
    @Test
    public void canUseHashCode() {
        final Movie.Builder builder = new Movie.Builder(
                TITLE,
                TYPE
        );
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
        final Movie movie1 = builder.build();
        final Movie movie2 = builder.build();
        builder.withId(UUID.randomUUID().toString());
        final Movie movie3 = builder.build();

        Assert.assertEquals(movie1.hashCode(), movie2.hashCode());
        Assert.assertNotEquals(movie1.hashCode(), movie3.hashCode());
    }
}

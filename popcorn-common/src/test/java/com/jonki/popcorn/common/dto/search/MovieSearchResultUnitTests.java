package com.jonki.popcorn.common.dto.search;

import com.jonki.popcorn.common.dto.movie.type.MovieType;
import com.jonki.popcorn.test.category.UnitTest;
import com.jonki.popcorn.test.supplier.RandomSupplier;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * Tests for the MovieSearchResult DTO.
 */
@Category(UnitTest.class)
public class MovieSearchResultUnitTests {

    /**
     * Make sure constructor works.
     */
    @Test
    public void canConstruct() {
        final Long id = RandomSupplier.LONG.get();
        final String title = RandomSupplier.STRING.get();
        final MovieType type = MovieType.CINEMA;
        final Float rating = (float) RandomSupplier.INT.get();
        final MovieSearchResult searchResult
                = new MovieSearchResult(id, title, type, rating);

        Assert.assertThat(searchResult.getId(), Matchers.is(id.toString()));
        Assert.assertThat(searchResult.getTitle(), Matchers.is(title));
        Assert.assertThat(searchResult.getType(), Matchers.is(type));
        Assert.assertThat(searchResult.getRating(), Matchers.is(rating));
    }
}

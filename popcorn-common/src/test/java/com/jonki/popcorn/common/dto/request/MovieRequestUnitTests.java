package com.jonki.popcorn.common.dto.request;

import com.jonki.popcorn.common.dto.movie.type.MovieType;
import com.jonki.popcorn.test.category.UnitTest;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.UUID;

/**
 * Tests for the MovieRequest DTO.
 */
@Category(UnitTest.class)
public class MovieRequestUnitTests {

    private static final String TITLE = UUID.randomUUID().toString();
    private static final MovieType TYPE = MovieType.CINEMA;

    /**
     * Test to make sure we can build an movie request using the default builder constructor.
     */
    @Test
    public void canBuildMovieRequest() {
        final MovieRequest movieRequest = new MovieRequest.Builder(
                TITLE,
                TYPE
        ).build();
        Assert.assertThat(movieRequest.getTitle(), Matchers.is(TITLE));
        Assert.assertThat(movieRequest.getType(), Matchers.is(TYPE));
    }
}

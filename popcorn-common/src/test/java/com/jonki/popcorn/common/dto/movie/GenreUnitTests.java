package com.jonki.popcorn.common.dto.movie;

import com.jonki.popcorn.common.dto.movie.type.GenreType;
import com.jonki.popcorn.test.category.UnitTest;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * Tests for the Genre DTO.
 */
@Category(UnitTest.class)
public class GenreUnitTests {

    private static final GenreType GENRE_TYPE = GenreType.ACTION;

    /**
     * Test to make sure we can build an genre using the default builder constructor.
     */
    @Test
    public void canBuildGenre() {
        final Genre genre = new Genre.Builder(
                GENRE_TYPE
        ).build();
        Assert.assertThat(genre.getGenre(), Matchers.is(GENRE_TYPE));
    }

    /**
     * Test equals.
     */
    @Test
    public void canFindEquality() {
        final Genre.Builder builder = new Genre.Builder(
                GENRE_TYPE
        );
        final Genre genre1 = builder.build();
        final Genre genre2 = builder.build();

        Assert.assertTrue(genre1.equals(genre2));
        Assert.assertTrue(genre2.equals(genre1));
    }

    /**
     * Test hash code.
     */
    @Test
    public void canUseHashCode() {
        final Genre.Builder builder = new Genre.Builder(
                GENRE_TYPE
        );
        final Genre genre1 = builder.build();
        final Genre genre2 = builder.build();

        Assert.assertEquals(genre1.hashCode(), genre2.hashCode());
    }
}

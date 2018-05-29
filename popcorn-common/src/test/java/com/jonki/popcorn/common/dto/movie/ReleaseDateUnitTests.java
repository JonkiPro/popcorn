package com.jonki.popcorn.common.dto.movie;

import com.jonki.popcorn.common.dto.movie.type.CountryType;
import com.jonki.popcorn.test.category.UnitTest;
import com.jonki.popcorn.test.supplier.RandomSupplier;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Date;

/**
 * Tests for the ReleaseDate DTO.
 */
@Category(UnitTest.class)
public class ReleaseDateUnitTests {

    private static final Date DATE = RandomSupplier.DATE.get();
    private static final CountryType COUNTRY_TYPE = CountryType.USA;

    /**
     * Test to make sure we can build an release date using the default builder constructor.
     */
    @Test
    public void canBuildReleaseDate() {
        final ReleaseDate releaseDate = new ReleaseDate.Builder(
                DATE,
                COUNTRY_TYPE
        ).build();
        Assert.assertThat(releaseDate.getDate(), Matchers.is(DATE));
        Assert.assertThat(releaseDate.getCountry(), Matchers.is(COUNTRY_TYPE));
    }

    /**
     * Test equals.
     */
    @Test
    public void canFindEquality() {
        final ReleaseDate.Builder builder = new ReleaseDate.Builder(
                DATE,
                COUNTRY_TYPE
        );
        final ReleaseDate releaseDate1 = builder.build();
        final ReleaseDate releaseDate2 = builder.build();

        Assert.assertTrue(releaseDate1.equals(releaseDate2));
        Assert.assertTrue(releaseDate2.equals(releaseDate1));
    }

    /**
     * Test hash code.
     */
    @Test
    public void canUseHashCode() {
        final ReleaseDate.Builder builder = new ReleaseDate.Builder(
                DATE,
                COUNTRY_TYPE
        );
        final ReleaseDate releaseDate1 = builder.build();
        final ReleaseDate releaseDate2 = builder.build();

        Assert.assertEquals(releaseDate1.hashCode(), releaseDate2.hashCode());
    }
}

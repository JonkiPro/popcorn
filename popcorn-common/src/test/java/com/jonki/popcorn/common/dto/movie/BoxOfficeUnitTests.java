package com.jonki.popcorn.common.dto.movie;

import com.jonki.popcorn.common.dto.movie.type.CountryType;
import com.jonki.popcorn.test.category.UnitTest;
import com.jonki.popcorn.test.supplier.RandomSupplier;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.math.BigDecimal;

/**
 * Tests for the BoxOffice DTO.
 */
@Category(UnitTest.class)
public class BoxOfficeUnitTests {

    private static final BigDecimal BOX_OFFICE = new BigDecimal(RandomSupplier.LONG.get());
    private static final CountryType COUNTRY_TYPE = CountryType.USA;

    /**
     * Test to make sure we can build an box office using the default builder constructor.
     */
    @Test
    public void canBuildBoxOffice() {
        final BoxOffice boxOffice = new BoxOffice.Builder(
                BOX_OFFICE,
                COUNTRY_TYPE
        ).build();
        Assert.assertThat(boxOffice.getBoxOffice(), Matchers.is(BOX_OFFICE));
        Assert.assertThat(boxOffice.getCountry(), Matchers.is(COUNTRY_TYPE));
    }

    /**
     * Test equals.
     */
    @Test
    public void canFindEquality() {
        final BoxOffice.Builder builder = new BoxOffice.Builder(
                BOX_OFFICE,
                COUNTRY_TYPE
        );
        final BoxOffice boxOffice1 = builder.build();
        final BoxOffice boxOffice2 = builder.build();

        Assert.assertTrue(boxOffice1.equals(boxOffice2));
        Assert.assertTrue(boxOffice2.equals(boxOffice1));
    }

    /**
     * Test hash code.
     */
    @Test
    public void canUseHashCode() {
        final BoxOffice.Builder builder = new BoxOffice.Builder(
                BOX_OFFICE,
                COUNTRY_TYPE
        );
        final BoxOffice boxOffice1 = builder.build();
        final BoxOffice boxOffice2 = builder.build();

        Assert.assertEquals(boxOffice1.hashCode(), boxOffice2.hashCode());
    }
}

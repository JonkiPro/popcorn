package com.jonki.popcorn.common.dto.movie;

import com.jonki.popcorn.common.dto.movie.type.CountryType;
import com.jonki.popcorn.test.category.UnitTest;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * Tests for the Country DTO.
 */
@Category(UnitTest.class)
public class CountryUnitTests {

    private static final CountryType COUNTRY_TYPE = CountryType.USA;

    /**
     * Test to make sure we can build an country using the default builder constructor.
     */
    @Test
    public void canBuildCountry() {
        final Country country = new Country.Builder(
                COUNTRY_TYPE
        ).build();
        Assert.assertThat(country.getCountry(), Matchers.is(COUNTRY_TYPE));
    }

    /**
     * Test equals.
     */
    @Test
    public void canFindEquality() {
        final Country.Builder builder = new Country.Builder(
                COUNTRY_TYPE
        );
        final Country country1 = builder.build();
        final Country country2 = builder.build();

        Assert.assertTrue(country1.equals(country2));
        Assert.assertTrue(country2.equals(country1));
    }

    /**
     * Test hash code.
     */
    @Test
    public void canUseHashCode() {
        final Country.Builder builder = new Country.Builder(
                COUNTRY_TYPE
        );
        final Country country1 = builder.build();
        final Country country2 = builder.build();

        Assert.assertEquals(country1.hashCode(), country2.hashCode());
    }
}

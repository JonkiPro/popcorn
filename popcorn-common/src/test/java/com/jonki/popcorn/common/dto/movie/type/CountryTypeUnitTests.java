package com.jonki.popcorn.common.dto.movie.type;

import com.jonki.popcorn.test.category.UnitTest;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * Tests for the CountryType enum.
 */
@Category(UnitTest.class)
public class CountryTypeUnitTests {

    /**
     * Tests whether the country code is correct.
     */
    @Test
    public void testCorrectCountryCode() {
        Assert.assertThat(CountryType.USA.getCode(), Matchers.is("US"));
        Assert.assertThat(CountryType.POLAND.getCode(), Matchers.is("PL"));
    }
}

package com.jonki.popcorn.common.dto.movie.response;

import com.jonki.popcorn.test.category.UnitTest;
import com.jonki.popcorn.test.supplier.RandomSupplier;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Date;

/**
 * Tests for the RateResponse DTO.
 */
@Category(UnitTest.class)
public class RateResponseUnitTests {

    private static final Integer RATE = RandomSupplier.INT.get();
    private static final String USER = RandomSupplier.STRING.get();
    private static final Date DATE = RandomSupplier.DATE.get();

    /**
     * Test to make sure we can build an rate response using the default builder constructor.
     */
    @Test
    public void canBuildRateResponse() {
        final RateResponse rateResponse = new RateResponse.Builder(
                RATE,
                USER,
                DATE
        ).build();
        Assert.assertThat(rateResponse.getRate(), Matchers.is(RATE));
        Assert.assertThat(rateResponse.getUser(), Matchers.is(USER));
        Assert.assertThat(rateResponse.getDate(), Matchers.is(DATE));
    }

    /**
     * Test equals.
     */
    @Test
    public void canFindEquality() {
        final RateResponse.Builder builder = new RateResponse.Builder(
                RATE,
                USER,
                DATE
        );
        final RateResponse rateResponse1 = builder.build();
        final RateResponse rateResponse2 = builder.build();

        Assert.assertTrue(rateResponse1.equals(rateResponse2));
        Assert.assertTrue(rateResponse2.equals(rateResponse1));
    }

    /**
     * Test hash code.
     */
    @Test
    public void canUseHashCode() {
        final RateResponse.Builder builder = new RateResponse.Builder(
                RATE,
                USER,
                DATE
        );
        final RateResponse rateResponse1 = builder.build();
        final RateResponse rateResponse2 = builder.build();

        Assert.assertEquals(rateResponse1.hashCode(), rateResponse2.hashCode());
    }
}

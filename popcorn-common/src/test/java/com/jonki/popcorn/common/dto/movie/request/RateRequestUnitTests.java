package com.jonki.popcorn.common.dto.movie.request;

import com.jonki.popcorn.test.category.UnitTest;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * Tests for the RateRequest DTO.
 */
@Category(UnitTest.class)
public class RateRequestUnitTests {

    private static final Integer RATE = 8;

    /**
     * Test to make sure we can build an rate request using the default builder constructor.
     */
    @Test
    public void canBuildRateRequest() {
        final RateRequest rateRequest = new RateRequest.Builder(
                RATE
        ).build();
        Assert.assertThat(rateRequest.getRate(), Matchers.is(RATE));
    }
}

package com.jonki.popcorn.core.util;

import com.jonki.popcorn.test.category.UnitTest;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * Unit tests for RandomUtils.
 */
@Category(UnitTest.class)
public class RandomUtilsUnitTests {

    /**
     * Test that the password is generated correctly.
     */
    @Test
    public void testRandomPasswordNotEmptyAndNotNull() {
        final String randomPassword = RandomUtils.randomPassword();
        Assert.assertThat(randomPassword, Matchers.not(Matchers.isEmptyOrNullString()));
    }

    /**
     * Test that the token is generated correctly.
     */
    @Test
    public void testRandomTokenNotEmptyAndNotNull() {
        final String randomToken = RandomUtils.randomToken();
        Assert.assertThat(randomToken, Matchers.not(Matchers.isEmptyOrNullString()));
    }
}

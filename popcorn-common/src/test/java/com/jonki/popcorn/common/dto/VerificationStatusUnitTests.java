package com.jonki.popcorn.common.dto;

import com.jonki.popcorn.test.category.UnitTest;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * Tests for the VerificationStatus enum.
 */
@Category(UnitTest.class)
public class VerificationStatusUnitTests {

    /**
     * Tests whether the data status is correct.
     */
    @Test
    public void testCorrectDataStatus() {
        Assert.assertThat(VerificationStatus.ACCEPT.getDataStatus(), Matchers.is(DataStatus.ACCEPTED));
        Assert.assertThat(VerificationStatus.REJECT.getDataStatus(), Matchers.is(DataStatus.REJECTED));
    }
}

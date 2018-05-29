package com.jonki.popcorn.common.dto.movie;

import com.jonki.popcorn.test.category.UnitTest;
import com.jonki.popcorn.test.supplier.RandomSupplier;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * Tests for the Summary DTO.
 */
@Category(UnitTest.class)
public class SummaryUnitTests {

    private static final String SUMMARY = RandomSupplier.STRING.get();

    /**
     * Test to make sure we can build an summary using the default builder constructor.
     */
    @Test
    public void canBuildSummary() {
        final Summary summary = new Summary.Builder(
                SUMMARY
        ).build();
        Assert.assertThat(summary.getSummary(), Matchers.is(SUMMARY));
    }

    /**
     * Test equals.
     */
    @Test
    public void canFindEquality() {
        final Summary.Builder builder = new Summary.Builder(
                SUMMARY
        );
        final Summary summary1 = builder.build();
        final Summary summary2 = builder.build();

        Assert.assertTrue(summary1.equals(summary2));
        Assert.assertTrue(summary2.equals(summary1));
    }

    /**
     * Test hash code.
     */
    @Test
    public void canUseHashCode() {
        final Summary.Builder builder = new Summary.Builder(
                SUMMARY
        );
        final Summary summary1 = builder.build();
        final Summary summary2 = builder.build();

        Assert.assertEquals(summary1.hashCode(), summary2.hashCode());
    }
}

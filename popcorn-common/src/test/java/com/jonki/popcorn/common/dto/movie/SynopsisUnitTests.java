package com.jonki.popcorn.common.dto.movie;

import com.jonki.popcorn.test.category.UnitTest;
import com.jonki.popcorn.test.supplier.RandomSupplier;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * Tests for the Synopsis DTO.
 */
@Category(UnitTest.class)
public class SynopsisUnitTests {

    private static final String SYNOPSIS = RandomSupplier.STRING.get();

    /**
     * Test to make sure we can build an synopsis using the default builder constructor.
     */
    @Test
    public void canBuildSynopsis() {
        final Synopsis synopsis = new Synopsis.Builder(
                SYNOPSIS
        ).build();
        Assert.assertThat(synopsis.getSynopsis(), Matchers.is(SYNOPSIS));
    }

    /**
     * Test equals.
     */
    @Test
    public void canFindEquality() {
        final Synopsis.Builder builder = new Synopsis.Builder(
                SYNOPSIS
        );
        final Synopsis synopsis1 = builder.build();
        final Synopsis synopsis2 = builder.build();

        Assert.assertTrue(synopsis1.equals(synopsis2));
        Assert.assertTrue(synopsis2.equals(synopsis1));
    }

    /**
     * Test hash code.
     */
    @Test
    public void canUseHashCode() {
        final Synopsis.Builder builder = new Synopsis.Builder(
                SYNOPSIS
        );
        final Synopsis synopsis1 = builder.build();
        final Synopsis synopsis2 = builder.build();

        Assert.assertEquals(synopsis1.hashCode(), synopsis2.hashCode());
    }
}

package com.jonki.popcorn.common.dto.movie;

import com.jonki.popcorn.test.category.UnitTest;
import com.jonki.popcorn.test.supplier.RandomSupplier;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * Tests for the Outline DTO.
 */
@Category(UnitTest.class)
public class OutlineUnitTests {

    private static final String OUTLINE = RandomSupplier.STRING.get();

    /**
     * Test to make sure we can build an outline using the default builder constructor.
     */
    @Test
    public void canBuildOutline() {
        final Outline outline = new Outline.Builder(
                OUTLINE
        ).build();
        Assert.assertThat(outline.getOutline(), Matchers.is(OUTLINE));
    }

    /**
     * Test equals.
     */
    @Test
    public void canFindEquality() {
        final Outline.Builder builder = new Outline.Builder(
                OUTLINE
        );
        final Outline outline1 = builder.build();
        final Outline outline2 = builder.build();

        Assert.assertTrue(outline1.equals(outline2));
        Assert.assertTrue(outline2.equals(outline1));
    }

    /**
     * Test hash code.
     */
    @Test
    public void canUseHashCode() {
        final Outline.Builder builder = new Outline.Builder(
                OUTLINE
        );
        final Outline outline1 = builder.build();
        final Outline outline2 = builder.build();

        Assert.assertEquals(outline1.hashCode(), outline2.hashCode());
    }
}

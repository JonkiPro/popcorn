package com.jonki.popcorn.common.dto.movie;

import com.jonki.popcorn.test.category.UnitTest;
import com.jonki.popcorn.test.supplier.RandomSupplier;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * Tests for the Review DTO.
 */
@Category(UnitTest.class)
public class ReviewUnitTests {

    private static final String TITLE = RandomSupplier.STRING.get();
    private static final String REVIEW = RandomSupplier.STRING.get();
    private static final boolean SPOILER = true;

    /**
     * Test to make sure we can build an review using the default builder constructor.
     */
    @Test
    public void canBuildReview() {
        final Review review = new Review.Builder(
                TITLE,
                REVIEW,
                SPOILER
        ).build();
        Assert.assertThat(review.getTitle(), Matchers.is(TITLE));
        Assert.assertThat(review.getReview(), Matchers.is(REVIEW));
        Assert.assertTrue(review.isSpoiler());
    }

    /**
     * Test equals.
     */
    @Test
    public void canFindEquality() {
        final Review.Builder builder = new Review.Builder(
                TITLE,
                REVIEW,
                SPOILER
        );
        final Review review1 = builder.build();
        final Review review2 = builder.build();

        Assert.assertTrue(review1.equals(review2));
        Assert.assertTrue(review2.equals(review1));
    }

    /**
     * Test hash code.
     */
    @Test
    public void canUseHashCode() {
        final Review.Builder builder = new Review.Builder(
                TITLE,
                REVIEW,
                SPOILER
        );
        final Review review1 = builder.build();
        final Review review2 = builder.build();

        Assert.assertEquals(review1.hashCode(), review2.hashCode());
    }
}

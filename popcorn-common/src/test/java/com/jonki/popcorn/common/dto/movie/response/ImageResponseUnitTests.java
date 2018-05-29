package com.jonki.popcorn.common.dto.movie.response;

import com.jonki.popcorn.test.category.UnitTest;
import com.jonki.popcorn.test.supplier.RandomSupplier;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * Tests for the ImageResponse DTO.
 */
@Category(UnitTest.class)
public class ImageResponseUnitTests {

    private static final String IMAGE_SRC = RandomSupplier.STRING.get();

    /**
     * Test to make sure we can build an image response using the default builder constructor.
     */
    @Test
    public void canBuildImageResponse() {
        final ImageResponse imageResponse = new ImageResponse.Builder(
                IMAGE_SRC
        ).build();
        Assert.assertThat(imageResponse.getSrc(), Matchers.is(IMAGE_SRC));
    }

    /**
     * Test equals.
     */
    @Test
    public void canFindEquality() {
        final ImageResponse.Builder builder = new ImageResponse.Builder(
                IMAGE_SRC
        );
        final ImageResponse imageResponse1 = builder.build();
        final ImageResponse imageResponse2 = builder.build();

        Assert.assertTrue(imageResponse1.equals(imageResponse2));
        Assert.assertTrue(imageResponse2.equals(imageResponse1));
    }

    /**
     * Test hash code.
     */
    @Test
    public void canUseHashCode() {
        final ImageResponse.Builder builder = new ImageResponse.Builder(
                IMAGE_SRC
        );
        final ImageResponse imageResponse1 = builder.build();
        final ImageResponse imageResponse2 = builder.build();

        Assert.assertEquals(imageResponse1.hashCode(), imageResponse2.hashCode());
    }
}

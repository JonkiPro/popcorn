package com.jonki.popcorn.common.dto.movie.request;

import com.jonki.popcorn.test.category.UnitTest;
import com.jonki.popcorn.test.supplier.RandomSupplier;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import java.io.File;

/**
 * Tests for the ImageRequest DTO.
 */
@Category(UnitTest.class)
public class ImageRequestUnitTests {

    //Set up a mocked File-object
    private static final File MOCKED_FILE = Mockito.mock(File.class);
    private static final String ID_IN_CLOUD = RandomSupplier.STRING.get();

    /**
     * Test to make sure we can build a image request with all optional parameters.
     */
    @Test
    public void canBuildImageRequestWithOptionals() {
        final ImageRequest.Builder builder = new ImageRequest.Builder();

        Mockito.when(MOCKED_FILE.exists()).thenReturn(true);
        builder.withFile(MOCKED_FILE);

        builder.withIdInCloud(ID_IN_CLOUD);

        final ImageRequest imageRequest = builder.build();
        Assert.assertThat(imageRequest.getFile(), Matchers.is(MOCKED_FILE));
        Assert.assertThat(imageRequest.getIdInCloud(), Matchers.is(ID_IN_CLOUD));
    }

    /**
     * Test equals.
     */
    @Test
    public void canFindEquality() {
        final ImageRequest.Builder builder = new ImageRequest.Builder();
        builder.withFile(MOCKED_FILE);
        builder.withIdInCloud(ID_IN_CLOUD);
        final ImageRequest imageRequest1 = builder.build();
        final ImageRequest imageRequest2 = builder.build();

        Assert.assertTrue(imageRequest1.equals(imageRequest2));
        Assert.assertTrue(imageRequest2.equals(imageRequest1));
    }

    /**
     * Test hash code.
     */
    @Test
    public void canUseHashCode() {
        final ImageRequest.Builder builder = new ImageRequest.Builder();
        builder.withFile(MOCKED_FILE);
        builder.withIdInCloud(ID_IN_CLOUD);
        final ImageRequest imageRequest1 = builder.build();
        final ImageRequest imageRequest2 = builder.build();

        Assert.assertEquals(imageRequest1.hashCode(), imageRequest2.hashCode());
    }
}

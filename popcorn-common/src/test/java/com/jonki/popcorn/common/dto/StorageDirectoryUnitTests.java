package com.jonki.popcorn.common.dto;

import com.jonki.popcorn.test.category.UnitTest;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * Tests for the StorageDirectory enum.
 */
@Category(UnitTest.class)
public class StorageDirectoryUnitTests {

    /**
     * Tests whether the directory key is correct.
     */
    @Test
    public void testCorrectDirectoryKey() {
        Assert.assertThat(
                StorageDirectory.IMAGE.getGoogleDirectoryKey(),
                Matchers.is("1_5xTX_K63I5dAMIfPecMjiIsN4tMlfvW")
        );
        Assert.assertThat(
                StorageDirectory.AVATAR.getGoogleDirectoryKey(),
                Matchers.is("1Uz4gwteYTgWGKmJFpem0rn3WSXOiuWjd")
        );
    }
}

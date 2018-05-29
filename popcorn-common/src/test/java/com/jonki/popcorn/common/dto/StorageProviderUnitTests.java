package com.jonki.popcorn.common.dto;

import com.jonki.popcorn.test.category.UnitTest;
import com.jonki.popcorn.test.supplier.RandomSupplier;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * Tests for the StorageProvider enum.
 */
@Category(UnitTest.class)
public class StorageProviderUnitTests {

    /**
     * Tests whether the file address(URL) is correct.
     */
    @Test
    public void testCorrectUrlFile() {
        final String ID = RandomSupplier.STRING.get();

        Assert.assertThat(
                StorageProvider.GOOGLE.getUrlFile(ID),
                Matchers.is("https://drive.google.com/uc?id=" + ID)
        );
    }
}

package com.jonki.popcorn.core.util;

import com.jonki.popcorn.test.category.UnitTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * Unit tests for EncryptUtils.
 */
@Category(UnitTest.class)
public class EncryptUtilsUnitTests {

    /**
     * Test the validity of encryption and matching.
     */
    @Test
    public void testEncrypt() {
        final String password = "SomePassword1";
        final String encryptedPassword = EncryptUtils.encrypt(password);

        Assert.assertTrue(EncryptUtils.matches(password, encryptedPassword));
    }
}

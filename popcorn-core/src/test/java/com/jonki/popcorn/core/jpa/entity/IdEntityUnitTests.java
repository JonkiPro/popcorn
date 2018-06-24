package com.jonki.popcorn.core.jpa.entity;

import com.jonki.popcorn.test.category.UnitTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * Unit tests for the IdEntity class.
 */
@Category(UnitTest.class)
public class IdEntityUnitTests {

    /**
     * Test the toString method.
     */
    @Test
    public void testToString() {
        Assert.assertNotNull(new IdEntity().toString());
    }
}

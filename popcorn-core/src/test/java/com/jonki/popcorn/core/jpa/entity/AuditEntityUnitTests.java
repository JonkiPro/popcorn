package com.jonki.popcorn.core.jpa.entity;

import com.jonki.popcorn.test.category.UnitTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * Test the AuditEntity class.
 */
@Category(UnitTest.class)
public class AuditEntityUnitTests {

    /**
     * Test to make sure objects are constructed properly.
     */
    @Test
    public void testConstructor() {
        final AuditEntity a = new AuditEntity();
        Assert.assertNotNull(a.getCreated());
    }

    /**
     * Test the toString method.
     */
    @Test
    public void testToString() {
        Assert.assertNotNull(new AuditEntity().toString());
    }
}

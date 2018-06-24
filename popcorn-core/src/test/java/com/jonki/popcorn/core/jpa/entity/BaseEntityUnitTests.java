package com.jonki.popcorn.core.jpa.entity;

import com.jonki.popcorn.test.category.UnitTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.UUID;

/**
 * Test the BaseEntity class.
 */
@Category(UnitTest.class)
public class BaseEntityUnitTests {

    private static final String UNIQUE_ID = UUID.randomUUID().toString();

    private BaseEntity b;

    /**
     * Setup the tests.
     */
    @Before
    public void setup() {
        this.b = new BaseEntity();
        this.b.setUniqueId(UNIQUE_ID);
    }

    /**
     * Test the default Constructor.
     */
    @Test
    public void testDefaultConstructor() {
        final BaseEntity local = new BaseEntity();
        Assert.assertNotNull(local.getUniqueId());
    }

    /**
     * Test the getting and setting of the unique id.
     */
    @Test
    public void testSetUniqueId() {
        final BaseEntity local = new BaseEntity();
        Assert.assertNotNull(local.getUniqueId());
        Assert.assertNotEquals(local.getUniqueId(), UNIQUE_ID);
        local.setUniqueId(UNIQUE_ID);
        Assert.assertEquals(UNIQUE_ID, local.getUniqueId());
    }

    /**
     * Test to make sure @PrePersist annotation will do what we want before persistence.
     */
    @Test
    public void testOnCreateBaseEntity() {
        final BaseEntity local = new BaseEntity();
        local.setUniqueId(null);
        local.onCreateBaseEntity();
        Assert.assertNotNull(local.getUniqueId());
    }

    /**
     * Test to make sure equals and hash code only care about the unique id.
     */
    @Test
    public void testEqualsAndHashCode() {
        final String id = UUID.randomUUID().toString();
        final BaseEntity one = new BaseEntity();
        one.setUniqueId(id);
        final BaseEntity two = new BaseEntity();
        two.setUniqueId(id);
        final BaseEntity three = new BaseEntity();
        three.setUniqueId(UUID.randomUUID().toString());

        Assert.assertTrue(one.equals(two));
        Assert.assertFalse(one.equals(three));
        Assert.assertFalse(two.equals(three));

        Assert.assertEquals(one.hashCode(), two.hashCode());
        Assert.assertNotEquals(one.hashCode(), three.hashCode());
        Assert.assertNotEquals(two.hashCode(), three.hashCode());
    }

    /**
     * Test the toString method.
     */
    @Test
    public void testToString() {
        Assert.assertNotNull(this.b.toString());
    }
}

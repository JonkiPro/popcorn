package com.jonki.popcorn.core.util;

import com.jonki.popcorn.common.dto.MovieField;
import com.jonki.popcorn.common.exception.ResourceException;
import com.jonki.popcorn.common.exception.ResourcePreconditionException;
import com.jonki.popcorn.test.category.UnitTest;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * Unit tests for EnumUtils.
 */
@Category(UnitTest.class)
public class EnumUtilsUnitTests {

    /**
     * Test correct obtaining of the enumeration value for String.
     *
     * @throws ResourceException For any problem
     */
    @Test
    public void testGetEnumFromString() throws ResourceException {
        final MovieField movieField = EnumUtils.getEnumFromString(MovieField.class, "SYNOPSIS");
        Assert.assertThat(MovieField.SYNOPSIS, Matchers.is(movieField));
    }

    /**
     * Test for illegal String.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetEnumFromStringIllegal() throws ResourceException {
        EnumUtils.getEnumFromString(MovieField.class, "SYNOPSIS.");
    }

    /**
     * Test for NULL enum type.
     *
     * @throws ResourceException For any problem
     */
    @Test(expected = ResourcePreconditionException.class)
    public void testGetEnumFromStringEnumTypeNull() throws ResourceException {
        EnumUtils.getEnumFromString(null, "SYNOPSIS");
    }
}

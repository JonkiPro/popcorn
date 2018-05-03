package com.web;

import com.test.category.UnitTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * Unit tests for the WebApplication class.
 */
@Category(UnitTest.class)
public class WebApplicationUnitTests {

    /**
     * Make sure we can construct a new Web Application instance.
     */
    @Test
    public void canConstruct() {
        Assert.assertNotNull(new WebApplication());
    }
}

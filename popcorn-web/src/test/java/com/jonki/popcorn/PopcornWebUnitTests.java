package com.jonki.popcorn;

import com.jonki.popcorn.test.category.UnitTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * Unit tests for the PopcornWeb class.
 */
@Category(UnitTest.class)
public class PopcornWebUnitTests {

    /**
     * Make sure we can construct a new Popcorn Web instance.
     */
    @Test
    public void canConstruct() {
        Assert.assertNotNull(new PopcornWeb());
    }
}

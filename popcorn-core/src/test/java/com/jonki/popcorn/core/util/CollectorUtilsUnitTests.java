package com.jonki.popcorn.core.util;

import com.jonki.popcorn.test.category.UnitTest;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Arrays;
import java.util.List;

/**
 * Unit tests for CollectorUtils.
 */
@Category(UnitTest.class)
public class CollectorUtilsUnitTests {

    /**
     * Test the list with one filtered element.
     */
    @Test
    public void testSingletonCollectorWithOneElement() {
        final List<Integer> randomNumbers = Arrays.asList(1, 2, 3);
        final Integer firstNumber = randomNumbers.stream()
                .filter(number -> number > 2)
                .collect(CollectorUtils.singletonCollector());
        Assert.assertThat(firstNumber, Matchers.is(3));
    }

    /**
     * Test the list with several elements.
     */
    @Test(expected = IllegalStateException.class)
    public void testSingletonCollectorWithFewElements() {
        final List<Integer> randomNumbers = Arrays.asList(1, 2, 3);
        randomNumbers.stream().collect(CollectorUtils.singletonCollector());
    }
}

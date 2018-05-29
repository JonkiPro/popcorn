package com.jonki.popcorn.common.dto.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonki.popcorn.test.category.UnitTest;
import com.jonki.popcorn.test.supplier.RandomSupplier;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.IOException;
import java.util.UUID;

/**
 * Unit tests for the BaseSearchResult class.
 */
@Category(UnitTest.class)
public class BaseSearchResultUnitTests {

    /**
     * Make sure the constructor and getters work properly.
     */
    @Test
    public void canConstruct() {
        final String id = RandomSupplier.STRING.get();
        final BaseSearchResult searchResult = new BaseSearchResult(id);

        Assert.assertThat(searchResult.getId(), Matchers.is(id));
    }

    /**
     * Make sure the equals function only acts on id.
     */
    @Test
    public void canFindEquality() {
        final String id = UUID.randomUUID().toString();
        final BaseSearchResult searchResult1 = new BaseSearchResult(id);
        final BaseSearchResult searchResult2 = new BaseSearchResult(id);
        final BaseSearchResult searchResult3 = new BaseSearchResult(
                UUID.randomUUID().toString()
        );

        Assert.assertTrue(searchResult1.equals(searchResult2));
        Assert.assertFalse(searchResult1.equals(searchResult3));
    }

    /**
     * Make sure the hash code function only acts on id.
     */
    @Test
    public void canUseHashCode() {
        final String id = UUID.randomUUID().toString();
        final BaseSearchResult searchResult1 = new BaseSearchResult(id);
        final BaseSearchResult searchResult2 = new BaseSearchResult(id);
        final BaseSearchResult searchResult3 = new BaseSearchResult(
                UUID.randomUUID().toString()
        );

        Assert.assertEquals(searchResult1.hashCode(), searchResult2.hashCode());
        Assert.assertNotEquals(searchResult1.hashCode(), searchResult3.hashCode());
    }

    /**
     * Test to make sure we can create a valid JSON string from a DTO object.
     */
    @Test
    public void canCreateValidJsonString() {
        final BaseSearchResult searchResult = new BaseSearchResult(
                RandomSupplier.STRING.get()
        );

        final String json = searchResult.toString();
        try {
            new ObjectMapper().readTree(json);
        } catch (final IOException ioe) {
            Assert.fail();
        }
    }
}

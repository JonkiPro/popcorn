package com.jonki.popcorn.common.dto.movie;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonki.popcorn.test.category.UnitTest;
import com.jonki.popcorn.test.supplier.RandomSupplier;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.IOException;

/**
 * Unit tests for the MovieInfoDTO class.
 */
@Category(UnitTest.class)
public class MovieInfoDTOUnitTests {

    /**
     * Test to make sure we can create a valid JSON string from a DTO object.
     */
    @Test
    public void canCreateValidJsonString() {
        final Synopsis synopsis = new Synopsis.Builder(
                RandomSupplier.STRING.get()
        ).build();

        final String json = synopsis.toString();
        try {
            new ObjectMapper().readTree(json);
        } catch (final IOException ioe) {
            Assert.fail();
        }
    }
}

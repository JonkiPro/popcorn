package com.jonki.popcorn.common.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonki.popcorn.common.dto.movie.type.MovieType;
import com.jonki.popcorn.test.category.UnitTest;
import com.jonki.popcorn.test.supplier.RandomSupplier;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.IOException;

/**
 * Unit tests for the BaseDTO class.
 */
@Category(UnitTest.class)
public class BaseDTOUnitTests {

    /**
     * Test to make sure we can create a valid JSON string from a DTO object.
     */
    @Test
    public void canCreateValidJsonString() {
        final Movie movie = new Movie.Builder(
                RandomSupplier.STRING.get(),
                MovieType.CINEMA
        ).build();

        final String json = movie.toString();
        try {
            new ObjectMapper().readTree(json);
        } catch (final IOException ioe) {
            Assert.fail();
        }
    }
}

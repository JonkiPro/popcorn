package com.jonki.popcorn.common.dto.error;

import com.jonki.popcorn.test.category.UnitTest;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * Tests for the ErrorFieldDTO.
 */
@Category(UnitTest.class)
public class ErrorFieldDTOUnitTests {

    /**
     * Test to make sure we can build an ErrorFieldDTO using the constructor.
     */
    @Test
    public void canConstructErrorFieldDTO() {
        final String FIELD = "username";
        final String MESSAGE = "Username exists in database";

        final ErrorFieldDTO errorFieldDTO = new ErrorFieldDTO(FIELD, MESSAGE);
        Assert.assertThat(errorFieldDTO.getField(), Matchers.is(FIELD));
        Assert.assertThat(errorFieldDTO.getMessage(), Matchers.is(MESSAGE));
    }
}

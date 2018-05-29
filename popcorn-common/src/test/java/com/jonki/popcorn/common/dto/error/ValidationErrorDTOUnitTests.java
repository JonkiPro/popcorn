package com.jonki.popcorn.common.dto.error;

import com.jonki.popcorn.test.category.UnitTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * Tests for the ValidationErrorDTO.
 */
@Category(UnitTest.class)
public class ValidationErrorDTOUnitTests {

    /**
     * Test to make sure we can add error fields to the list.
     */
    @Test
    public void canAddFieldErrorsNotEmpty() {
        final ValidationErrorDTO validationErrorDTO = new ValidationErrorDTO();
        validationErrorDTO.addFieldError("username", "This field is required");
        Assert.assertFalse(validationErrorDTO.getFieldErrors().isEmpty());
    }

    /**
     * Test to make sure we can build an ValidationErrorDTO with an empty list of error fields.
     */
    @Test
    public void canAddFieldErrorsIsEmpty() {
        final ValidationErrorDTO validationErrorDTO = new ValidationErrorDTO();
        Assert.assertTrue(validationErrorDTO.getFieldErrors().isEmpty());
    }
}

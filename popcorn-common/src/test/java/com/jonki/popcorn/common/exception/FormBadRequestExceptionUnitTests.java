package com.jonki.popcorn.common.exception;

import com.jonki.popcorn.common.dto.error.ValidationErrorDTO;
import com.jonki.popcorn.test.category.UnitTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * Test the FormBadRequestException.
 */
@Category(UnitTest.class)
public class FormBadRequestExceptionUnitTests extends RuntimeException {

    /**
     * Test for adding errors.
     *
     * @throws ResourceBadRequestException For a bad request of the form
     */
    @Test(expected = FormBadRequestException.class)
    public void testAddErrors() throws FormBadRequestException {
        final ValidationErrorDTO ve = new ValidationErrorDTO();
        ve.addFieldError("username", "This field is required");
        Assert.assertFalse(ve.getFieldErrors().isEmpty());
        final FormBadRequestException fbre = new FormBadRequestException(ve);
        Assert.assertNotNull(fbre.getErrors());
        Assert.assertFalse(fbre.getErrors().getFieldErrors().isEmpty());
        throw fbre;
    }
}

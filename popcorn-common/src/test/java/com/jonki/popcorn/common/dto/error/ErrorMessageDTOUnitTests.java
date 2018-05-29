package com.jonki.popcorn.common.dto.error;

import com.jonki.popcorn.test.category.UnitTest;
import com.jonki.popcorn.test.supplier.RandomSupplier;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * Tests for the ErrorMessageDTO.
 */
@Category(UnitTest.class)
public class ErrorMessageDTOUnitTests {

    private static final int STATUS = RandomSupplier.INT.get();
    private static final int CODE = RandomSupplier.INT.get();
    private static final String MESSAGE = RandomSupplier.STRING.get();

    /**
     * Test to make sure we can build an ErrorMessageDTO using the constructor.
     */
    @Test
    public void canConstructErrorMessageDTO() {
        final ErrorMessageDTO errorMessageDTO = new ErrorMessageDTO(STATUS, CODE, MESSAGE);
        Assert.assertThat(errorMessageDTO.getStatus(), Matchers.is(STATUS));
        Assert.assertThat(errorMessageDTO.getCode(), Matchers.is(CODE));
        Assert.assertThat(errorMessageDTO.getMessage(), Matchers.is(MESSAGE));
    }

    /**
     * Test to make sure we can build an ErrorMessageDTO using the lombok`s builder constructor.
     */
    @Test
    public void canBuildErrorMessageDTO() {
        final ErrorMessageDTO errorMessageDTO = ErrorMessageDTO.builder()
                .status(STATUS)
                .code(CODE)
                .message(MESSAGE)
                .build();
        Assert.assertThat(errorMessageDTO.getStatus(), Matchers.is(STATUS));
        Assert.assertThat(errorMessageDTO.getCode(), Matchers.is(CODE));
        Assert.assertThat(errorMessageDTO.getMessage(), Matchers.is(MESSAGE));
    }
}

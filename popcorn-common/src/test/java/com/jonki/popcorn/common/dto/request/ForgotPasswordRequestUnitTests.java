package com.jonki.popcorn.common.dto.request;

import com.jonki.popcorn.test.category.UnitTest;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.UUID;

/**
 * Tests for the ForgotPasswordRequest DTO.
 */
@Category(UnitTest.class)
public class ForgotPasswordRequestUnitTests {

    private static final String USERNAME = UUID.randomUUID().toString();
    private static final String EMAIL = UUID.randomUUID().toString();

    /**
     * Test to make sure we can build an forgot password request using the default builder constructor.
     */
    @Test
    public void canBuildForgotPasswordRequest() {
        final ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest.Builder(
                USERNAME,
                EMAIL
        ).build();
        Assert.assertThat(forgotPasswordRequest.getUsername(), Matchers.is(USERNAME));
        Assert.assertThat(forgotPasswordRequest.getEmail(), Matchers.is(EMAIL));
    }
}

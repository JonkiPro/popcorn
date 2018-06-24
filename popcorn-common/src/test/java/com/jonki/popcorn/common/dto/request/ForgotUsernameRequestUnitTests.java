package com.jonki.popcorn.common.dto.request;

import com.jonki.popcorn.test.category.UnitTest;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.UUID;

/**
 * Tests for the ForgotUsernameRequest DTO.
 */
@Category(UnitTest.class)
public class ForgotUsernameRequestUnitTests {

    private static final String EMAIL = UUID.randomUUID().toString();

    /**
     * Test to make sure we can build an forgot username request using the default builder constructor.
     */
    @Test
    public void canBuildForgotUsernameRequest() {
        final ForgotUsernameRequest forgotUsernameRequest = new ForgotUsernameRequest.Builder(
                EMAIL
        ).build();
        Assert.assertThat(forgotUsernameRequest.getEmail(), Matchers.is(EMAIL));
    }
}

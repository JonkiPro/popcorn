package com.jonki.popcorn.common.dto.request;

import com.jonki.popcorn.test.category.UnitTest;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.UUID;

/**
 * Tests for the RegisterRequest DTO.
 */
@Category(UnitTest.class)
public class RegisterRequestUnitTests {

    private static final String USERNAME = UUID.randomUUID().toString();
    private static final String EMAIL = UUID.randomUUID().toString();
    private static final String PASSWORD = UUID.randomUUID().toString();
    private static final String PASSWORD_AGAIN = UUID.randomUUID().toString();
    private static final String RECAPTCHA = UUID.randomUUID().toString();

    /**
     * Test to make sure we can build an register request using the default builder constructor.
     */
    @Test
    public void canBuildRegisterRequest() {
        final RegisterRequest registerRequest = new RegisterRequest.Builder(
                USERNAME,
                EMAIL,
                PASSWORD,
                PASSWORD_AGAIN,
                RECAPTCHA
        ).build();
        Assert.assertThat(registerRequest.getUsername(), Matchers.is(USERNAME));
        Assert.assertThat(registerRequest.getEmail(), Matchers.is(EMAIL));
        Assert.assertThat(registerRequest.getPassword(), Matchers.is(PASSWORD));
        Assert.assertThat(registerRequest.getPasswordAgain(), Matchers.is(PASSWORD_AGAIN));
        Assert.assertThat(registerRequest.getReCaptcha(), Matchers.is(RECAPTCHA));
    }
}

package com.jonki.popcorn.common.dto.request;

import com.jonki.popcorn.test.category.UnitTest;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.UUID;

/**
 * Tests for the ChangeEmailRequest DTO.
 */
@Category(UnitTest.class)
public class ChangeEmailRequestUnitTests {

    private static final String PASSWORD = UUID.randomUUID().toString();
    private static final String EMAIL = UUID.randomUUID().toString();

    /**
     * Test to make sure we can build an change e-mail request using the default builder constructor.
     */
    @Test
    public void canBuildChangeEmailRequest() {
        final ChangeEmailRequest changeEmailRequest = new ChangeEmailRequest.Builder(
                PASSWORD,
                EMAIL
        ).build();
        Assert.assertThat(changeEmailRequest.getEmail(), Matchers.is(EMAIL));
        Assert.assertThat(changeEmailRequest.getPassword(), Matchers.is(PASSWORD));
    }
}

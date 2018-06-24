package com.jonki.popcorn.common.dto.request;

import com.jonki.popcorn.test.category.UnitTest;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.UUID;

/**
 * Tests for the ChangePasswordRequest DTO.
 */
@Category(UnitTest.class)
public class ChangePasswordRequestUnitTests {

    private static final String OLD_PASSWORD = UUID.randomUUID().toString();
    private static final String NEW_PASSWORD = UUID.randomUUID().toString();
    private static final String NEW_PASSWORD_AGAIN = NEW_PASSWORD;

    /**
     * Test to make sure we can build an change password request using the default builder constructor.
     */
    @Test
    public void canBuildChangePasswordRequest() {
        final ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest.Builder(
                OLD_PASSWORD,
                NEW_PASSWORD,
                NEW_PASSWORD_AGAIN
        ).build();
        Assert.assertThat(changePasswordRequest.getOldPassword(), Matchers.is(OLD_PASSWORD));
        Assert.assertThat(changePasswordRequest.getNewPassword(), Matchers.is(NEW_PASSWORD));
        Assert.assertThat(changePasswordRequest.getNewPasswordAgain(), Matchers.is(NEW_PASSWORD_AGAIN));
    }
}

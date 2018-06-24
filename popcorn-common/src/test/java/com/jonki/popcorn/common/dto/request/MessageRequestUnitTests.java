package com.jonki.popcorn.common.dto.request;

import com.jonki.popcorn.test.category.UnitTest;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.UUID;

/**
 * Tests for the MessageRequest DTO.
 */
@Category(UnitTest.class)
public class MessageRequestUnitTests {

    private static final String TO = UUID.randomUUID().toString();
    private static final String SUBJECT = UUID.randomUUID().toString();
    private static final String TEXT = UUID.randomUUID().toString();

    /**
     * Test to make sure we can build an message request using the default builder constructor.
     */
    @Test
    public void canBuildMessageRequest() {
        final MessageRequest messageRequest = new MessageRequest.Builder(
                TO,
                SUBJECT,
                TEXT
        ).build();
        Assert.assertThat(messageRequest.getTo(), Matchers.is(TO));
        Assert.assertThat(messageRequest.getSubject(), Matchers.is(SUBJECT));
        Assert.assertThat(messageRequest.getText(), Matchers.is(TEXT));
    }
}

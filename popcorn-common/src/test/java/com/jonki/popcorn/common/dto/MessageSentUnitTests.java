package com.jonki.popcorn.common.dto;

import com.jonki.popcorn.test.category.UnitTest;
import com.jonki.popcorn.test.supplier.RandomSupplier;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/**
 * Tests for the MessageSent DTO.
 */
@Category(UnitTest.class)
public class MessageSentUnitTests {

    private static final String SUBJECT = RandomSupplier.STRING.get();
    private static final String TEXT = RandomSupplier.STRING.get();
    private static final Date DATE = RandomSupplier.DATE.get();
    private static final ShallowUser RECIPIENT = new ShallowUser.Builder(RandomSupplier.STRING.get(), RandomSupplier.STRING.get()).build();

    /**
     * Test to make sure we can build an message sent using the default builder constructor.
     */
    @Test
    public void canBuildMessageSent() {
        final MessageSent messageSent = new MessageSent.Builder(
                SUBJECT,
                TEXT,
                DATE,
                RECIPIENT
        ).build();
        Assert.assertThat(messageSent.getSubject(), Matchers.is(SUBJECT));
        Assert.assertThat(messageSent.getText(), Matchers.is(TEXT));
        Assert.assertThat(messageSent.getDate(), Matchers.is(DATE));
        Assert.assertThat(messageSent.getRecipient(), Matchers.is(RECIPIENT));
        Assert.assertFalse(Optional.ofNullable(messageSent.getId()).isPresent());
    }

    /**
     * Test to make sure we can build a message sent with all optional parameters.
     */
    @Test
    public void canBuildMessageSentWithOptionals() {
        final MessageSent.Builder builder = new MessageSent.Builder(
                SUBJECT,
                TEXT,
                DATE,
                RECIPIENT
        );

        final String id = RandomSupplier.STRING.get();
        builder.withId(id);

        final MessageSent messageSent = builder.build();
        Assert.assertThat(messageSent.getSubject(), Matchers.is(SUBJECT));
        Assert.assertThat(messageSent.getText(), Matchers.is(TEXT));
        Assert.assertThat(messageSent.getDate(), Matchers.is(DATE));
        Assert.assertThat(messageSent.getRecipient(), Matchers.is(RECIPIENT));
        Assert.assertThat(Optional.of(messageSent.getId()).orElseThrow(IllegalArgumentException::new), Matchers.is(id));
    }

    /**
     * Test to make sure we can build an message sent with null collection parameters.
     */
    @Test
    public void canBuildMessageSentNullOptionals() {
        final MessageSent.Builder builder = new MessageSent.Builder(
                SUBJECT,
                TEXT,
                DATE,
                RECIPIENT
        );
        builder.withId(null);

        final MessageSent messageSent = builder.build();
        Assert.assertThat(messageSent.getSubject(), Matchers.is(SUBJECT));
        Assert.assertThat(messageSent.getText(), Matchers.is(TEXT));
        Assert.assertThat(messageSent.getDate(), Matchers.is(DATE));
        Assert.assertThat(messageSent.getRecipient(), Matchers.is(RECIPIENT));
        Assert.assertFalse(Optional.ofNullable(messageSent.getId()).isPresent());
    }

    /**
     * Test equals.
     */
    @Test
    public void canFindEquality() {
        final MessageSent.Builder builder = new MessageSent.Builder(
                SUBJECT,
                TEXT,
                DATE,
                RECIPIENT
        );
        builder.withId(UUID.randomUUID().toString());
        final MessageSent messageSent1 = builder.build();
        final MessageSent messageSent2 = builder.build();
        builder.withId(UUID.randomUUID().toString());
        final MessageSent messageSent3 = builder.build();

        Assert.assertTrue(messageSent1.equals(messageSent2));
        Assert.assertTrue(messageSent2.equals(messageSent1));
        Assert.assertFalse(messageSent1.equals(messageSent3));
    }

    /**
     * Test hash code.
     */
    @Test
    public void canUseHashCode() {
        final MessageSent.Builder builder = new MessageSent.Builder(
                SUBJECT,
                TEXT,
                DATE,
                RECIPIENT
        );
        builder.withId(UUID.randomUUID().toString());
        final MessageSent messageSent1 = builder.build();
        final MessageSent messageSent2 = builder.build();
        builder.withId(UUID.randomUUID().toString());
        final MessageSent messageSent3 = builder.build();

        Assert.assertEquals(messageSent1.hashCode(), messageSent2.hashCode());
        Assert.assertNotEquals(messageSent1.hashCode(), messageSent3.hashCode());
    }
}

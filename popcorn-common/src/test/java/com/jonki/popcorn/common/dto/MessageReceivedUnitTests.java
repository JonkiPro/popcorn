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
 * Tests for the MessageReceived DTO.
 */
@Category(UnitTest.class)
public class MessageReceivedUnitTests {

    private static final String SUBJECT = RandomSupplier.STRING.get();
    private static final String TEXT = RandomSupplier.STRING.get();
    private static final Date DATE = RandomSupplier.DATE.get();
    private static final ShallowUser SENDER = new ShallowUser.Builder(RandomSupplier.STRING.get(), RandomSupplier.STRING.get()).build();
    private static final Date DATE_OF_READ = RandomSupplier.DATE.get();

    /**
     * Test to make sure we can build an message received using the default builder constructor.
     */
    @Test
    public void canBuildMessageReceived() {
        final MessageReceived messageReceived = new MessageReceived.Builder(
                SUBJECT,
                TEXT,
                DATE,
                SENDER,
                DATE_OF_READ
        ).build();
        Assert.assertThat(messageReceived.getSubject(), Matchers.is(SUBJECT));
        Assert.assertThat(messageReceived.getText(), Matchers.is(TEXT));
        Assert.assertThat(messageReceived.getDate(), Matchers.is(DATE));
        Assert.assertThat(messageReceived.getSender(), Matchers.is(SENDER));
        Assert.assertThat(messageReceived.getDateOfRead(), Matchers.is(DATE_OF_READ));
        Assert.assertFalse(Optional.ofNullable(messageReceived.getId()).isPresent());
    }

    /**
     * Test to make sure we can build a message received with all optional parameters.
     */
    @Test
    public void canBuildMessageReceivedWithOptionals() {
        final MessageReceived.Builder builder = new MessageReceived.Builder(
                SUBJECT,
                TEXT,
                DATE,
                SENDER,
                DATE_OF_READ
        );

        final String id = RandomSupplier.STRING.get();
        builder.withId(id);

        final MessageReceived messageReceived = builder.build();
        Assert.assertThat(messageReceived.getSubject(), Matchers.is(SUBJECT));
        Assert.assertThat(messageReceived.getText(), Matchers.is(TEXT));
        Assert.assertThat(messageReceived.getDate(), Matchers.is(DATE));
        Assert.assertThat(messageReceived.getSender(), Matchers.is(SENDER));
        Assert.assertThat(messageReceived.getDateOfRead(), Matchers.is(DATE_OF_READ));
        Assert.assertThat(Optional.of(messageReceived.getId()).orElseThrow(IllegalArgumentException::new), Matchers.is(id));
    }

    /**
     * Test to make sure we can build an message received with null collection parameters.
     */
    @Test
    public void canBuildMessageReceivedNullOptionals() {
        final MessageReceived.Builder builder = new MessageReceived.Builder(
                SUBJECT,
                TEXT,
                DATE,
                SENDER,
                DATE_OF_READ
        );
        builder.withId(null);

        final MessageReceived messageReceived = builder.build();
        Assert.assertThat(messageReceived.getSubject(), Matchers.is(SUBJECT));
        Assert.assertThat(messageReceived.getText(), Matchers.is(TEXT));
        Assert.assertThat(messageReceived.getDate(), Matchers.is(DATE));
        Assert.assertThat(messageReceived.getSender(), Matchers.is(SENDER));
        Assert.assertThat(messageReceived.getDateOfRead(), Matchers.is(DATE_OF_READ));
        Assert.assertFalse(Optional.ofNullable(messageReceived.getId()).isPresent());
    }

    /**
     * Test equals.
     */
    @Test
    public void canFindEquality() {
        final MessageReceived.Builder builder = new MessageReceived.Builder(
                SUBJECT,
                TEXT,
                DATE,
                SENDER,
                DATE_OF_READ
        );
        builder.withId(UUID.randomUUID().toString());
        final MessageReceived messageReceived1 = builder.build();
        final MessageReceived messageReceived2 = builder.build();
        builder.withId(UUID.randomUUID().toString());
        final MessageReceived messageReceived3 = builder.build();

        Assert.assertTrue(messageReceived1.equals(messageReceived2));
        Assert.assertTrue(messageReceived2.equals(messageReceived1));
        Assert.assertFalse(messageReceived1.equals(messageReceived3));
    }

    /**
     * Test hash code.
     */
    @Test
    public void canUseHashCode() {
        final MessageReceived.Builder builder = new MessageReceived.Builder(
                SUBJECT,
                TEXT,
                DATE,
                SENDER,
                DATE_OF_READ
        );
        builder.withId(UUID.randomUUID().toString());
        final MessageReceived messageReceived1 = builder.build();
        final MessageReceived messageReceived2 = builder.build();
        builder.withId(UUID.randomUUID().toString());
        final MessageReceived messageReceived3 = builder.build();

        Assert.assertEquals(messageReceived1.hashCode(), messageReceived2.hashCode());
        Assert.assertNotEquals(messageReceived1.hashCode(), messageReceived3.hashCode());
    }
}

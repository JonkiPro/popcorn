package com.jonki.popcorn.core.jpa.entity;

import com.jonki.popcorn.test.category.UnitTest;
import com.jonki.popcorn.test.supplier.RandomSupplier;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.UUID;

/**
 * Test the MessageEntity class.
 */
@Category(UnitTest.class)
public class MessageEntityUnitTests extends EntityTestsBase {

    private static final UserEntity SENDER = new UserEntity();
    private static final UserEntity RECIPIENT = new UserEntity();
    private static final String SUBJECT = RandomSupplier.STRING.get();
    private static final String TEXT = RandomSupplier.STRING.get();

    private MessageEntity m;

    /**
     * Setup the tests.
     */
    @Before
    public void setup() {
        this.m = new MessageEntity();
        this.m.setSender(SENDER);
        this.m.setRecipient(RECIPIENT);
        this.m.setSubject(SUBJECT);
        this.m.setText(TEXT);
    }

    /**
     * Test the default Constructor.
     */
    @Test
    public void testDefaultConstructor() {
        final MessageEntity entity = new MessageEntity();
        Assert.assertNull(entity.getSender());
        Assert.assertNull(entity.getRecipient());
        Assert.assertNull(entity.getSubject());
        Assert.assertNull(entity.getText());
        Assert.assertFalse(entity.getDateOfRead().isPresent());
        Assert.assertFalse(entity.isVisibleForSender());
        Assert.assertFalse(entity.isVisibleForRecipient());
    }

    /**
     * Make sure validation works on valid movies.
     */
    @Test
    public void testValidate() {
        this.validate(this.m);
    }

    /**
     * Make sure validation works on with failure from super class.
     */
    @Test(expected = ConstraintViolationException.class)
    public void testValidateSender() {
        this.m.setSender(null);
        this.validate(this.m);
    }

    /**
     * Make sure validation works on with failure from super class.
     */
    @Test(expected = ConstraintViolationException.class)
    public void testValidateRecipient() {
        this.m.setRecipient(null);
        this.validate(this.m);
    }

    /**
     * Make sure validation works on with failure from super class.
     */
    @Test(expected = ConstraintViolationException.class)
    public void testValidateSubject() {
        this.m.setSubject("");
        this.validate(this.m);
    }

    /**
     * Make sure validation works on with failure from super class.
     */
    @Test(expected = ConstraintViolationException.class)
    public void testValidateText() {
        this.m.setText("");
        this.validate(this.m);
    }

    /**
     * Test validate with exception from super class.
     */
    @Test(expected = ConstraintViolationException.class)
    public void testValidateBadSuperClass() {
        this.validate(new MessageEntity());
    }

    /**
     * Test setting the sender (user).
     */
    @Test
    public void testSetSender() {
        Assert.assertNotNull(this.m.getSender());
        final UserEntity user = new UserEntity();
        user.setUniqueId(UUID.randomUUID().toString());
        this.m.setSender(user);
        Assert.assertThat(this.m.getSender(), Matchers.is(user));
    }

    /**
     * Test setting the recipient (user).
     */
    @Test
    public void testSetRecipient() {
        Assert.assertNotNull(this.m.getRecipient());
        final UserEntity user = new UserEntity();
        user.setUniqueId(UUID.randomUUID().toString());
        this.m.setRecipient(user);
        Assert.assertThat(this.m.getRecipient(), Matchers.is(user));
    }

    /**
     * Test setting the subject.
     */
    @Test
    public void testSetSubject() {
        Assert.assertNotNull(this.m.getSubject());
        final String subject = UUID.randomUUID().toString();
        this.m.setSubject(subject);
        Assert.assertThat(this.m.getSubject(), Matchers.is(subject));
    }

    /**
     * Test setting the text.
     */
    @Test
    public void testSetText() {
        Assert.assertNotNull(this.m.getText());
        final String text = UUID.randomUUID().toString();
        this.m.setText(text);
        Assert.assertThat(this.m.getText(), Matchers.is(text));
    }

    /**
     * Test setting the date of read.
     */
    @Test
    public void testSetDateOfRead() {
        final Date dateOfRead = RandomSupplier.DATE.get();
        this.m.setDateOfRead(dateOfRead);
        Assert.assertThat(this.m.getDateOfRead().orElseGet(RandomSupplier.DATE), Matchers.is(dateOfRead));
    }

    /**
     * Test setting the visible for sender.
     */
    @Test
    public void testSetVisibleForSender() {
        final boolean visibleForSender = true;
        this.m.setVisibleForSender(visibleForSender);
        Assert.assertTrue(this.m.isVisibleForSender());
    }

    /**
     * Test setting the visible for recipient.
     */
    @Test
    public void testSetVisibleForRecipient() {
        final boolean visibleForRecipient = true;
        this.m.setVisibleForRecipient(visibleForRecipient);
        Assert.assertTrue(this.m.isVisibleForRecipient());
    }

    /**
     * Test the toString method.
     */
    @Test
    public void testToString() {
        Assert.assertNotNull(this.m.toString());
    }
}

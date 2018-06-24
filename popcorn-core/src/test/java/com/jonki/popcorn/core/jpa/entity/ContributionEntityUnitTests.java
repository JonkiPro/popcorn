package com.jonki.popcorn.core.jpa.entity;

import com.google.common.collect.Sets;
import com.jonki.popcorn.common.dto.DataStatus;
import com.jonki.popcorn.common.dto.MovieField;
import com.jonki.popcorn.test.category.UnitTest;
import com.jonki.popcorn.test.supplier.RandomSupplier;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

/**
 * Test the ContributionEntity class.
 */
@Category(UnitTest.class)
public class ContributionEntityUnitTests extends EntityTestsBase {

    private static final MovieEntity MOVIE = new MovieEntity();
    private static final UserEntity USER = new UserEntity();
    private static final DataStatus STATUS = DataStatus.ACCEPTED;
    private static final MovieField FIELD = MovieField.SYNOPSIS;
    private static final Set<String> SOURCES = Sets.newHashSet("http://www.1.com", "http://www.2.com");

    private ContributionEntity c;

    /**
     * Setup the tests.
     */
    @Before
    public void setup() {
        this.c = new ContributionEntity();
        this.c.setMovie(MOVIE);
        this.c.setUser(USER);
        this.c.setStatus(STATUS);
        this.c.setField(FIELD);
        this.c.setSources(SOURCES);
    }

    /**
     * Test the default Constructor.
     */
    @Test
    public void testDefaultConstructor() {
        final ContributionEntity entity = new ContributionEntity();
        Assert.assertNull(entity.getMovie());
        Assert.assertNull(entity.getUser());
        Assert.assertTrue(entity.getIdsToAdd().isEmpty());
        Assert.assertTrue(entity.getIdsToUpdate().isEmpty());
        Assert.assertTrue(entity.getIdsToDelete().isEmpty());
        Assert.assertNull(entity.getStatus());
        Assert.assertNull(entity.getField());
        Assert.assertTrue(entity.getSources().isEmpty());
        Assert.assertFalse(entity.getUserComment().isPresent());
        Assert.assertNull(entity.getCreated());
        Assert.assertFalse(entity.getVerificationDate().isPresent());
        Assert.assertFalse(entity.getVerificationUser().isPresent());
        Assert.assertFalse(entity.getVerificationComment().isPresent());
    }

    /**
     * Make sure validation works on valid contribution.
     */
    @Test
    public void testValidate() {
        this.validate(this.c);
    }

    /**
     * Make sure validation works on with failure from super class.
     */
    @Test(expected = ConstraintViolationException.class)
    public void testValidateMovie() {
        this.c.setMovie(null);
        this.validate(this.c);
    }

    /**
     * Make sure validation works on with failure from super class.
     */
    @Test(expected = ConstraintViolationException.class)
    public void testValidateUser() {
        this.c.setUser(null);
        this.validate(this.c);
    }

    /**
     * Make sure validation works on with failure from super class.
     */
    @Test(expected = ConstraintViolationException.class)
    public void testValidateStatus() {
        this.c.setStatus(null);
        this.validate(this.c);
    }

    /**
     * Make sure validation works on with failure from super class.
     */
    @Test(expected = ConstraintViolationException.class)
    public void testValidateField() {
        this.c.setField(null);
        this.validate(this.c);
    }

    /**
     * Make sure validation works on with failure from super class.
     */
    @Test(expected = ConstraintViolationException.class)
    public void testValidateSources() {
        this.c.setSources(Sets.newHashSet());
        this.validate(this.c);
    }

    /**
     * Test validate with exception from super class.
     */
    @Test(expected = ConstraintViolationException.class)
    public void testValidateBadSuperClass() {
        this.validate(new ContributionEntity());
    }

    /**
     * Test to make sure @PrePersist annotation will do what we want before persistence.
     */
    @Test
    public void testOnCreateContributionEntity() {
        final ContributionEntity local = new ContributionEntity();
        Assert.assertNull(local.getStatus());
        local.onCreateContributionEntity();
        Assert.assertThat(local.getStatus(), Matchers.is(DataStatus.WAITING));
    }

    /**
     * Test setting the movie.
     */
    @Test
    public void testSetMovie() {
        Assert.assertNotNull(this.c.getMovie());
        final MovieEntity movie = new MovieEntity();
        this.c.setMovie(movie);
        Assert.assertThat(this.c.getMovie(), Matchers.is(movie));
    }

    /**
     * Test setting the user.
     */
    @Test
    public void testSetUser() {
        Assert.assertNotNull(this.c.getUser());
        final UserEntity user = new UserEntity();
        user.setUniqueId(UUID.randomUUID().toString());
        this.c.setUser(user);
        Assert.assertThat(this.c.getUser(), Matchers.is(user));
    }

    /**
     * Test setting the status.
     */
    @Test
    public void testSetStatus() {
        Assert.assertNotNull(this.c.getStatus());
        final DataStatus status = DataStatus.ACCEPTED;
        this.c.setStatus(status);
        Assert.assertThat(this.c.getStatus(), Matchers.is(status));
    }

    /**
     * Test setting the field.
     */
    @Test
    public void testSetField() {
        Assert.assertNotNull(this.c.getField());
        final MovieField field = MovieField.SYNOPSIS;
        this.c.setField(field);
        Assert.assertThat(this.c.getField(), Matchers.is(field));
    }

    /**
     * Test setting the user comment.
     */
    @Test
    public void testSetUserComment() {
        Assert.assertFalse(this.c.getUserComment().isPresent());
        final String userComment = UUID.randomUUID().toString();
        this.c.setUserComment(userComment);
        Assert.assertThat(this.c.getUserComment().orElseGet(RandomSupplier.STRING), Matchers.is(userComment));

        this.c.setUserComment(null);
        Assert.assertFalse(this.c.getUserComment().isPresent());
    }

    /**
     * Test setting the verification date.
     */
    @Test
    public void testSetVerificationDate() {
        Assert.assertFalse(this.c.getVerificationDate().isPresent());
        final Date verificationDate = new Date();
        this.c.setVerificationDate(verificationDate);
        Assert.assertThat(this.c.getVerificationDate().orElseGet(RandomSupplier.DATE), Matchers.is(verificationDate));

        this.c.setVerificationDate(null);
        Assert.assertFalse(this.c.getVerificationDate().isPresent());
    }

    /**
     * Test setting the verification user.
     */
    @Test
    public void testSetVerificationUser() {
        Assert.assertFalse(this.c.getVerificationUser().isPresent());
        final UserEntity verificationUser = new UserEntity();
        verificationUser.setUniqueId(UUID.randomUUID().toString());
        this.c.setVerificationUser(verificationUser);
        final UserEntity actualVerificationUser = this.c.getVerificationUser().get();
        Assert.assertThat(actualVerificationUser, Matchers.is(verificationUser));

        this.c.setVerificationUser(null);
        Assert.assertFalse(this.c.getVerificationUser().isPresent());
    }

    /**
     * Test setting the verification comment.
     */
    @Test
    public void testSetVerificationComment() {
        Assert.assertFalse(this.c.getVerificationComment().isPresent());
        final String verificationComment = UUID.randomUUID().toString();
        this.c.setVerificationComment(verificationComment);
        Assert.assertThat(
                this.c.getVerificationComment().orElseGet(RandomSupplier.STRING),
                Matchers.is(verificationComment)
        );

        this.c.setVerificationComment(null);
        Assert.assertFalse(this.c.getVerificationComment().isPresent());
    }

    /**
     * Test the toString method.
     */
    @Test
    public void testToString() {
        Assert.assertNotNull(this.c.toString());
    }
}

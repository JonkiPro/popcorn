package com.jonki.popcorn.core.jpa.entity.movie;

import com.jonki.popcorn.common.dto.DataStatus;
import com.jonki.popcorn.core.jpa.entity.EntityTestsBase;
import com.jonki.popcorn.core.jpa.entity.MovieEntity;
import com.jonki.popcorn.test.category.UnitTest;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import javax.validation.ConstraintViolationException;
import java.util.UUID;

/**
 * Test the MovieReviewEntity class.
 */
@Category(UnitTest.class)
public class MovieReviewEntityUnitTests extends EntityTestsBase {

    private static final MovieEntity MOVIE = new MovieEntity();
    private static final DataStatus STATUS = DataStatus.ACCEPTED;

    private MovieReviewEntity m;

    /**
     * Setup the tests.
     */
    @Before
    public void setup() {
        this.m = new MovieReviewEntity();
        this.m.setMovie(MOVIE);
        this.m.setStatus(STATUS);
    }

    /**
     * Test the default Constructor.
     */
    @Test
    public void testDefaultConstructor() {
        final MovieReviewEntity local = new MovieReviewEntity();
        Assert.assertNull(local.getMovie());
        Assert.assertNull(local.getStatus());
        Assert.assertFalse(local.isReportedForUpdate());
        Assert.assertFalse(local.isReportedForDelete());
        Assert.assertNull(local.getTitle());
        Assert.assertNull(local.getReview());
        Assert.assertFalse(local.isSpoiler());
    }

    /**
     * Test the constructor with all args.
     */
    @Test
    public void testConstructorAllArgs() {
        final String title = UUID.randomUUID().toString();
        final String review = UUID.randomUUID().toString();
        final boolean spoiler = true;
        final MovieReviewEntity local = new MovieReviewEntity(title, review, spoiler);
        Assert.assertNull(local.getMovie());
        Assert.assertNull(local.getStatus());
        Assert.assertFalse(local.isReportedForUpdate());
        Assert.assertFalse(local.isReportedForDelete());
        Assert.assertThat(local.getTitle(), Matchers.is(title));
        Assert.assertThat(local.getReview(), Matchers.is(review));
        Assert.assertTrue(local.isSpoiler());
    }

    /**
     * Make sure validation works on valid movies.
     */
    @Test
    public void testValidate() {
        this.validate(this.m);
    }

    /**
     * Test validate with exception from super class.
     */
    @Test(expected = ConstraintViolationException.class)
    public void testValidateBadSuperClass() {
        this.validate(new MovieReviewEntity());
    }

    /**
     * Test setting the title.
     */
    @Test
    public void testSetTitle() {
        final String title = UUID.randomUUID().toString();
        this.m.setTitle(title);
        Assert.assertThat(this.m.getTitle(), Matchers.is(title));
    }

    /**
     * Test setting the review.
     */
    @Test
    public void testSetReview() {
        final String review = UUID.randomUUID().toString();
        this.m.setReview(review);
        Assert.assertThat(this.m.getReview(), Matchers.is(review));
    }

    /**
     * Test setting the spoiler.
     */
    @Test
    public void testSetSpoiler() {
        final boolean spoiler = true;
        this.m.setSpoiler(spoiler);
        Assert.assertTrue(this.m.isSpoiler());
    }

    /**
     * Test the toString method.
     */
    @Test
    public void testToString() {
        Assert.assertNotNull(this.m.toString());
    }
}

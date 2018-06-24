package com.jonki.popcorn.core.jpa.entity.movie;

import com.jonki.popcorn.core.jpa.entity.EntityTestsBase;
import com.jonki.popcorn.core.jpa.entity.MovieEntity;
import com.jonki.popcorn.core.jpa.entity.UserEntity;
import com.jonki.popcorn.test.category.UnitTest;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import javax.validation.ConstraintViolationException;

/**
 * Test the MovieRateEntity class.
 */
@Category(UnitTest.class)
public class MovieRateEntityUnitTests extends EntityTestsBase {

    private static final MovieEntity MOVIE = new MovieEntity();
    private static final UserEntity USER = new UserEntity();
    private static final Integer RATE = 8;

    private MovieRateEntity m;

    /**
     * Setup the tests.
     */
    @Before
    public void setup() {
        this.m = new MovieRateEntity();
        this.m.setMovie(MOVIE);
        this.m.setUser(USER);
        this.m.setRate(RATE);
    }

    /**
     * Test the default Constructor.
     */
    @Test
    public void testDefaultConstructor() {
        final MovieRateEntity local = new MovieRateEntity();
        Assert.assertNull(local.getMovie());
        Assert.assertNull(local.getUser());
        Assert.assertNull(local.getRate());
    }

    /**
     * Make sure validation works on valid contribution.
     */
    @Test
    public void testValidate() {
        this.validate(this.m);
    }

    /**
     * Make sure validation works on with failure from super class.
     */
    @Test(expected = ConstraintViolationException.class)
    public void testValidateMovie() {
        this.m.setMovie(null);
        this.validate(this.m);
    }

    /**
     * Make sure validation works on with failure from super class.
     */
    @Test(expected = ConstraintViolationException.class)
    public void testValidateUser() {
        this.m.setUser(null);
        this.validate(this.m);
    }

    /**
     * Make sure validation works on with failure from super class.
     */
    @Test(expected = ConstraintViolationException.class)
    public void testValidateRate() {
        this.m.setRate(null);
        this.validate(this.m);
    }

    /**
     * Test validate with exception from super class.
     */
    @Test(expected = ConstraintViolationException.class)
    public void testValidateBadSuperClass() {
        this.validate(new MovieRateEntity());
    }

    /**
     * Test setting the movie.
     */
    @Test
    public void testSetMovie() {
        Assert.assertNotNull(this.m.getMovie());
        final MovieEntity movie = new MovieEntity();
        this.m.setMovie(movie);
        Assert.assertThat(this.m.getMovie(), Matchers.is(movie));
    }

    /**
     * Test setting the user.
     */
    @Test
    public void testSetUser() {
        Assert.assertNotNull(this.m.getUser());
        final UserEntity user = new UserEntity();
        this.m.setUser(user);
        Assert.assertThat(this.m.getUser(), Matchers.is(user));
    }

    /**
     * Test setting the rate.
     */
    @Test
    public void testSetRate() {
        Assert.assertNotNull(this.m.getRate());
        final Integer rate = 8;
        this.m.setRate(rate);
        Assert.assertThat(this.m.getRate(), Matchers.is(rate));
    }

    /**
     * Test the toString method.
     */
    @Test
    public void testToString() {
        Assert.assertNotNull(this.m.toString());
    }
}

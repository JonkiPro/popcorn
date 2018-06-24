package com.jonki.popcorn.core.jpa.entity.movie;

import com.jonki.popcorn.common.dto.DataStatus;
import com.jonki.popcorn.core.jpa.entity.EntityTestsBase;
import com.jonki.popcorn.core.jpa.entity.MovieEntity;
import com.jonki.popcorn.test.category.UnitTest;
import com.jonki.popcorn.test.supplier.RandomSupplier;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import javax.validation.ConstraintViolationException;

/**
 * Test the MovieSynopsisEntity class.
 */
@Category(UnitTest.class)
public class MovieSynopsisEntityUnitTests extends EntityTestsBase {

    private static final MovieEntity MOVIE = new MovieEntity();
    private static final DataStatus STATUS = DataStatus.ACCEPTED;

    private MovieSynopsisEntity m;

    /**
     * Setup the tests.
     */
    @Before
    public void setup() {
        this.m = new MovieSynopsisEntity();
        this.m.setMovie(MOVIE);
        this.m.setStatus(STATUS);
    }

    /**
     * Test the default Constructor.
     */
    @Test
    public void testDefaultConstructor() {
        final MovieSynopsisEntity local = new MovieSynopsisEntity();
        Assert.assertNull(local.getMovie());
        Assert.assertNull(local.getStatus());
        Assert.assertFalse(local.isReportedForUpdate());
        Assert.assertFalse(local.isReportedForDelete());
        Assert.assertNull(local.getSynopsis());
    }

    /**
     * Test the constructor with all args.
     */
    @Test
    public void testConstructorAllArgs() {
        final String synopsis = RandomSupplier.STRING.get();
        final MovieSynopsisEntity local = new MovieSynopsisEntity(synopsis);
        Assert.assertNull(local.getMovie());
        Assert.assertNull(local.getStatus());
        Assert.assertFalse(local.isReportedForUpdate());
        Assert.assertFalse(local.isReportedForDelete());
        Assert.assertThat(local.getSynopsis(), Matchers.is(synopsis));
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
        this.validate(new MovieSynopsisEntity());
    }

    /**
     * Test setting the synopsis.
     */
    @Test
    public void testSetSynopsis() {
        final String synopsis = RandomSupplier.STRING.get();
        this.m.setSynopsis(synopsis);
        Assert.assertThat(this.m.getSynopsis(), Matchers.is(synopsis));
    }

    /**
     * Test the toString method.
     */
    @Test
    public void testToString() {
        Assert.assertNotNull(this.m.toString());
    }
}

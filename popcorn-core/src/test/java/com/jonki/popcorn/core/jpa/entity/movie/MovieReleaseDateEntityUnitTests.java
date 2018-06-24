package com.jonki.popcorn.core.jpa.entity.movie;

import com.jonki.popcorn.common.dto.DataStatus;
import com.jonki.popcorn.common.dto.movie.type.CountryType;
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
import java.util.Date;

/**
 * Test the MovieReleaseDateEntity class.
 */
@Category(UnitTest.class)
public class MovieReleaseDateEntityUnitTests extends EntityTestsBase {

    private static final MovieEntity MOVIE = new MovieEntity();
    private static final DataStatus STATUS = DataStatus.ACCEPTED;

    private MovieReleaseDateEntity m;

    /**
     * Setup the tests.
     */
    @Before
    public void setup() {
        this.m = new MovieReleaseDateEntity();
        this.m.setMovie(MOVIE);
        this.m.setStatus(STATUS);
    }

    /**
     * Test the default Constructor.
     */
    @Test
    public void testDefaultConstructor() {
        final MovieReleaseDateEntity local = new MovieReleaseDateEntity();
        Assert.assertNull(local.getMovie());
        Assert.assertNull(local.getStatus());
        Assert.assertFalse(local.isReportedForUpdate());
        Assert.assertFalse(local.isReportedForDelete());
        Assert.assertNull(local.getDate());
        Assert.assertNull(local.getCountry());
    }

    /**
     * Test the constructor with all args.
     */
    @Test
    public void testConstructorAllArgs() {
        final Date date = RandomSupplier.DATE.get();
        final CountryType country = CountryType.USA;
        final MovieReleaseDateEntity local = new MovieReleaseDateEntity(date, country);
        Assert.assertNull(local.getMovie());
        Assert.assertNull(local.getStatus());
        Assert.assertFalse(local.isReportedForUpdate());
        Assert.assertFalse(local.isReportedForDelete());
        Assert.assertThat(local.getDate(), Matchers.is(date));
        Assert.assertThat(local.getCountry(), Matchers.is(country));
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
        this.validate(new MovieReleaseDateEntity());
    }

    /**
     * Test setting the date.
     */
    @Test
    public void testSetDate() {
        final Date date = RandomSupplier.DATE.get();
        this.m.setDate(date);
        Assert.assertThat(this.m.getDate(), Matchers.is(date));
    }

    /**
     * Test setting the country.
     */
    @Test
    public void testSetCountry() {
        final CountryType country = CountryType.USA;
        this.m.setCountry(country);
        Assert.assertThat(this.m.getCountry(), Matchers.is(country));
    }

    /**
     * Test the toString method.
     */
    @Test
    public void testToString() {
        Assert.assertNotNull(this.m.toString());
    }
}

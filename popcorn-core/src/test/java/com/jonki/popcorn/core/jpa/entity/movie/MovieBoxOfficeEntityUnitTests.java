package com.jonki.popcorn.core.jpa.entity.movie;

import com.jonki.popcorn.common.dto.DataStatus;
import com.jonki.popcorn.common.dto.movie.type.CountryType;
import com.jonki.popcorn.core.jpa.entity.EntityTestsBase;
import com.jonki.popcorn.core.jpa.entity.MovieEntity;
import com.jonki.popcorn.test.category.UnitTest;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;

/**
 * Test the MovieBoxOfficeEntity class.
 */
@Category(UnitTest.class)
public class MovieBoxOfficeEntityUnitTests extends EntityTestsBase {

    private static final MovieEntity MOVIE = new MovieEntity();
    private static final DataStatus STATUS = DataStatus.ACCEPTED;

    private MovieBoxOfficeEntity m;

    /**
     * Setup the tests.
     */
    @Before
    public void setup() {
        this.m = new MovieBoxOfficeEntity();
        this.m.setMovie(MOVIE);
        this.m.setStatus(STATUS);
    }

    /**
     * Test the default Constructor.
     */
    @Test
    public void testDefaultConstructor() {
        final MovieBoxOfficeEntity local = new MovieBoxOfficeEntity();
        Assert.assertNull(local.getMovie());
        Assert.assertNull(local.getStatus());
        Assert.assertFalse(local.isReportedForUpdate());
        Assert.assertFalse(local.isReportedForDelete());
        Assert.assertNull(local.getBoxOffice());
        Assert.assertNull(local.getCountry());
    }

    /**
     * Test the constructor with all args.
     */
    @Test
    public void testConstructorAllArgs() {
        final BigDecimal boxOffice = new BigDecimal("1000");
        final CountryType country = CountryType.USA;
        final MovieBoxOfficeEntity local = new MovieBoxOfficeEntity(boxOffice, country);
        Assert.assertNull(local.getMovie());
        Assert.assertNull(local.getStatus());
        Assert.assertFalse(local.isReportedForUpdate());
        Assert.assertFalse(local.isReportedForDelete());
        Assert.assertThat(local.getBoxOffice(), Matchers.is(boxOffice));
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
        this.validate(new MovieBoxOfficeEntity());
    }

    /**
     * Test setting the box office.
     */
    @Test
    public void testSetBoxOffice() {
        final BigDecimal boxOffice = new BigDecimal("1000");
        this.m.setBoxOffice(boxOffice);
        Assert.assertThat(this.m.getBoxOffice(), Matchers.is(boxOffice));
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

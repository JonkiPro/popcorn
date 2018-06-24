package com.jonki.popcorn.core.jpa.entity.movie;

import com.jonki.popcorn.common.dto.DataStatus;
import com.jonki.popcorn.common.dto.TitleAttribute;
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

/**
 * Test the MovieOtherTitleEntity class.
 */
@Category(UnitTest.class)
public class MovieOtherTitleEntityUnitTests extends EntityTestsBase {

    private static final MovieEntity MOVIE = new MovieEntity();
    private static final DataStatus STATUS = DataStatus.ACCEPTED;

    private MovieOtherTitleEntity m;

    /**
     * Setup the tests.
     */
    @Before
    public void setup() {
        this.m = new MovieOtherTitleEntity();
        this.m.setMovie(MOVIE);
        this.m.setStatus(STATUS);
    }

    /**
     * Test the default Constructor.
     */
    @Test
    public void testDefaultConstructor() {
        final MovieOtherTitleEntity local = new MovieOtherTitleEntity();
        Assert.assertNull(local.getMovie());
        Assert.assertNull(local.getStatus());
        Assert.assertFalse(local.isReportedForUpdate());
        Assert.assertFalse(local.isReportedForDelete());
        Assert.assertNull(local.getTitle());
        Assert.assertNull(local.getCountry());
        Assert.assertNull(local.getAttribute());
    }

    /**
     * Test the constructor with all args.
     */
    @Test
    public void testConstructorAllArgs() {
        final String title = RandomSupplier.STRING.get();
        final CountryType country = CountryType.USA;
        final TitleAttribute attribute = TitleAttribute.ORIGINAL_TITLE;
        final MovieOtherTitleEntity local = new MovieOtherTitleEntity(title, country, attribute);
        Assert.assertNull(local.getMovie());
        Assert.assertNull(local.getStatus());
        Assert.assertFalse(local.isReportedForUpdate());
        Assert.assertFalse(local.isReportedForDelete());
        Assert.assertThat(local.getTitle(), Matchers.is(title));
        Assert.assertThat(local.getCountry(), Matchers.is(country));
        Assert.assertThat(local.getAttribute(), Matchers.is(attribute));
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
        this.validate(new MovieOtherTitleEntity());
    }

    /**
     * Test setting the title.
     */
    @Test
    public void testSetTitle() {
        final String title = RandomSupplier.STRING.get();
        this.m.setTitle(title);
        Assert.assertThat(this.m.getTitle(), Matchers.is(title));
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
     * Test setting the attribute.
     */
    @Test
    public void testSetAttribute() {
        final TitleAttribute attribute = TitleAttribute.ORIGINAL_TITLE;
        this.m.setAttribute(attribute);
        Assert.assertThat(this.m.getAttribute(), Matchers.is(attribute));
    }

    /**
     * Test the toString method.
     */
    @Test
    public void testToString() {
        Assert.assertNotNull(this.m.toString());
    }
}

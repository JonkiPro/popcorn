package com.jonki.popcorn.core.jpa.entity.movie;

import com.jonki.popcorn.common.dto.DataStatus;
import com.jonki.popcorn.common.dto.movie.type.SiteType;
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
 * Test the MovieSiteEntity class.
 */
@Category(UnitTest.class)
public class MovieSiteEntityUnitTests extends EntityTestsBase {

    private static final MovieEntity MOVIE = new MovieEntity();
    private static final DataStatus STATUS = DataStatus.ACCEPTED;

    private MovieSiteEntity m;

    /**
     * Setup the tests.
     */
    @Before
    public void setup() {
        this.m = new MovieSiteEntity();
        this.m.setMovie(MOVIE);
        this.m.setStatus(STATUS);
    }

    /**
     * Test the default Constructor.
     */
    @Test
    public void testDefaultConstructor() {
        final MovieSiteEntity local = new MovieSiteEntity();
        Assert.assertNull(local.getMovie());
        Assert.assertNull(local.getStatus());
        Assert.assertFalse(local.isReportedForUpdate());
        Assert.assertFalse(local.isReportedForDelete());
        Assert.assertNull(local.getSite());
        Assert.assertNull(local.getOfficial());
    }

    /**
     * Test the constructor with all args.
     */
    @Test
    public void testConstructorAllArgs() {
        final String site = RandomSupplier.STRING.get();
        final SiteType official = SiteType.OFFICIAL;
        final MovieSiteEntity local = new MovieSiteEntity(site, official);
        Assert.assertNull(local.getMovie());
        Assert.assertNull(local.getStatus());
        Assert.assertFalse(local.isReportedForUpdate());
        Assert.assertFalse(local.isReportedForDelete());
        Assert.assertThat(local.getSite(), Matchers.is(site));
        Assert.assertThat(local.getOfficial(), Matchers.is(official));
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
        this.validate(new MovieSiteEntity());
    }

    /**
     * Test setting the site.
     */
    @Test
    public void testSetSite() {
        final String site = RandomSupplier.STRING.get();
        this.m.setSite(site);
        Assert.assertThat(this.m.getSite(), Matchers.is(site));
    }

    /**
     * Test setting the official.
     */
    @Test
    public void testSetOfficial() {
        final SiteType official = SiteType.OFFICIAL;
        this.m.setOfficial(official);
        Assert.assertThat(this.m.getOfficial(), Matchers.is(official));
    }

    /**
     * Test the toString method.
     */
    @Test
    public void testToString() {
        Assert.assertNotNull(this.m.toString());
    }
}

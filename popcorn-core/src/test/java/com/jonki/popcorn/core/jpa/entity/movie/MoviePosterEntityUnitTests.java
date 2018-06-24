package com.jonki.popcorn.core.jpa.entity.movie;

import com.jonki.popcorn.common.dto.DataStatus;
import com.jonki.popcorn.common.dto.StorageProvider;
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
 * Test the MoviePosterEntity class.
 */
@Category(UnitTest.class)
public class MoviePosterEntityUnitTests extends EntityTestsBase {

    private static final MovieEntity MOVIE = new MovieEntity();
    private static final DataStatus STATUS = DataStatus.ACCEPTED;

    private MoviePosterEntity m;

    /**
     * Setup the tests.
     */
    @Before
    public void setup() {
        this.m = new MoviePosterEntity();
        this.m.setMovie(MOVIE);
        this.m.setStatus(STATUS);
    }

    /**
     * Test the default Constructor.
     */
    @Test
    public void testDefaultConstructor() {
        final MoviePosterEntity local = new MoviePosterEntity();
        Assert.assertNull(local.getMovie());
        Assert.assertNull(local.getStatus());
        Assert.assertFalse(local.isReportedForUpdate());
        Assert.assertFalse(local.isReportedForDelete());
        Assert.assertNull(local.getIdInCloud());
        Assert.assertNull(local.getProvider());
    }

    /**
     * Test the constructor with all args.
     */
    @Test
    public void testConstructorAllArgs() {
        final String idInCloud = UUID.randomUUID().toString();
        final StorageProvider storageProvider = StorageProvider.GOOGLE;
        final MoviePosterEntity local = new MoviePosterEntity(idInCloud, storageProvider);
        Assert.assertNull(local.getMovie());
        Assert.assertNull(local.getStatus());
        Assert.assertFalse(local.isReportedForUpdate());
        Assert.assertFalse(local.isReportedForDelete());
        Assert.assertThat(local.getIdInCloud(), Matchers.is(idInCloud));
        Assert.assertThat(local.getProvider(), Matchers.is(storageProvider));
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
        this.validate(new MoviePosterEntity());
    }

    /**
     * Test setting the ID in cloud.
     */
    @Test
    public void testSetIdInCloud() {
        final String idInCloud = UUID.randomUUID().toString();
        this.m.setIdInCloud(idInCloud);
        Assert.assertThat(this.m.getIdInCloud(), Matchers.is(idInCloud));
    }

    /**
     * Test setting the provider.
     */
    @Test
    public void testSetStorageProvider() {
        final StorageProvider storageProvider = StorageProvider.GOOGLE;
        this.m.setProvider(storageProvider);
        Assert.assertThat(this.m.getProvider(), Matchers.is(storageProvider));
    }

    /**
     * Test the toString method.
     */
    @Test
    public void testToString() {
        Assert.assertNotNull(this.m.toString());
    }
}

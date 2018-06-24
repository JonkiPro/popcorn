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

/**
 * Test the MovieInfoEntity class.
 */
@Category(UnitTest.class)
public class MovieInfoEntityUnitTests extends EntityTestsBase {

    private static final MovieEntity MOVIE = new MovieEntity();
    private static final DataStatus STATUS = DataStatus.ACCEPTED;

    private MovieInfoEntity m;

    /**
     * Setup the tests.
     */
    @Before
    public void setup() {
        this.m = new MovieInfoEntity();
        this.m.setMovie(MOVIE);
        this.m.setStatus(STATUS);
    }

    /**
     * Test the default Constructor.
     */
    @Test
    public void testDefaultConstructor() {
        final MovieInfoEntity local = new MovieInfoEntity();
        Assert.assertNull(local.getMovie());
        Assert.assertNull(local.getStatus());
        Assert.assertFalse(local.isReportedForUpdate());
        Assert.assertFalse(local.isReportedForDelete());
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
    public void testValidateMovie() {
        this.m.setMovie(null);
        this.validate(this.m);
    }

    /**
     * Make sure validation works on with failure from super class.
     */
    @Test(expected = ConstraintViolationException.class)
    public void testValidateStatus() {
        this.m.setStatus(null);
        this.validate(this.m);
    }

    /**
     * Test validate with exception from super class.
     */
    @Test(expected = ConstraintViolationException.class)
    public void testValidateBadSuperClass() {
        this.validate(new MovieInfoEntity());
    }

    /**
     * Test to make sure @PrePersist annotation will do what we want before persistence.
     */
    @Test
    public void testOnCreateMovieInfoEntity() {
        final MovieInfoEntity local = new MovieInfoEntity();
        Assert.assertNull(local.getStatus());
        local.onCreateMovieInfoEntity();
        Assert.assertThat(local.getStatus(), Matchers.is(DataStatus.WAITING));
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
     * Test setting the status.
     */
    @Test
    public void testSetStatus() {
        Assert.assertNotNull(this.m.getStatus());
        final DataStatus status = DataStatus.ACCEPTED;
        this.m.setStatus(status);
        Assert.assertThat(this.m.getStatus(), Matchers.is(status));
    }

    /**
     * Test setting the reported for update.
     */
    @Test
    public void testSetReportedForUpdate() {
        final boolean reportedForUpdate = true;
        this.m.setReportedForUpdate(reportedForUpdate);
        Assert.assertTrue(this.m.isReportedForUpdate());
    }

    /**
     * Test setting the reported for delete.
     */
    @Test
    public void testSetReportedForDelete() {
        final boolean reportedForDelete = true;
        this.m.setReportedForDelete(reportedForDelete);
        Assert.assertTrue(this.m.isReportedForDelete());
    }

//    /**
//     * Test get discriminator value.
//     */
//    @Test
//    public void testGetDiscriminatorValue() {
//        final MovieSynopsisEntity synopsis = new MovieSynopsisEntity();
//        Assert.assertThat(synopsis.getDiscriminatorValue(), Matchers.is(MovieField.SYNOPSIS));
//    }

    /**
     * Test the toString method.
     */
    @Test
    public void testToString() {
        Assert.assertNotNull(this.m.toString());
    }
}

package com.jonki.popcorn.core.jpa.entity;

import com.jonki.popcorn.common.dto.DataStatus;
import com.jonki.popcorn.common.dto.movie.type.MovieType;
import com.jonki.popcorn.test.category.UnitTest;
import com.jonki.popcorn.test.supplier.RandomSupplier;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import javax.validation.ConstraintViolationException;
import java.util.UUID;

/**
 * Test the MovieEntity class.
 */
@Category(UnitTest.class)
public class MovieEntityUnitTests extends EntityTestsBase {

    private static final String TITLE = RandomSupplier.STRING.get();
    private static final MovieType TYPE = MovieType.CINEMA;
    private static final DataStatus STATUS = DataStatus.ACCEPTED;
    private static final Integer FAVORITE_COUNT = RandomSupplier.INT.get();

    private MovieEntity m;

    /**
     * Setup the tests.
     */
    @Before
    public void setup() {
        this.m = new MovieEntity();
        this.m.setTitle(TITLE);
        this.m.setType(TYPE);
        this.m.setStatus(STATUS);
        this.m.setFavoriteCount(FAVORITE_COUNT);
    }

    /**
     * Test the default Constructor.
     */
    @Test
    public void testDefaultConstructor() {
        final MovieEntity entity = new MovieEntity();
        Assert.assertNull(entity.getTitle());
        Assert.assertNull(entity.getType());
        Assert.assertTrue(entity.getOtherTitles().isEmpty());
        Assert.assertTrue(entity.getReleaseDates().isEmpty());
        Assert.assertTrue(entity.getOutlines().isEmpty());
        Assert.assertTrue(entity.getSummaries().isEmpty());
        Assert.assertTrue(entity.getSynopses().isEmpty());
        Assert.assertTrue(entity.getBoxOffices().isEmpty());
        Assert.assertTrue(entity.getSites().isEmpty());
        Assert.assertTrue(entity.getCountries().isEmpty());
        Assert.assertTrue(entity.getLanguages().isEmpty());
        Assert.assertTrue(entity.getGenres().isEmpty());
        Assert.assertTrue(entity.getReviews().isEmpty());
        Assert.assertTrue(entity.getPhotos().isEmpty());
        Assert.assertTrue(entity.getPosters().isEmpty());
        Assert.assertTrue(entity.getRatings().isEmpty());
        Assert.assertNull(entity.getBudget());
        Assert.assertNull(entity.getStatus());
        Assert.assertFalse(entity.getRating().isPresent());
        Assert.assertNull(entity.getFavoriteCount());
        Assert.assertTrue(entity.getContributions().isEmpty());
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
    public void testValidateTitle() {
        this.m.setTitle("");
        this.validate(this.m);
    }

    /**
     * Make sure validation works on with failure from super class.
     */
    @Test(expected = ConstraintViolationException.class)
    public void testValidateType() {
        this.m.setType(null);
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
     * Make sure validation works on with failure from super class.
     */
    @Test(expected = ConstraintViolationException.class)
    public void testValidateFavoriteCount() {
        this.m.setFavoriteCount(null);
        this.validate(this.m);
    }

    /**
     * Test validate with exception from super class.
     */
    @Test(expected = ConstraintViolationException.class)
    public void testValidateBadSuperClass() {
        this.validate(new MovieEntity());
    }

    /**
     * Test to make sure @PrePersist annotation will do what we want before persistence.
     */
    @Test
    public void testOnCreateMovieEntity() {
        final MovieEntity local = new MovieEntity();
        Assert.assertNull(local.getStatus());
        Assert.assertNull(local.getFavoriteCount());
        local.onCreateMovieEntity();
        Assert.assertThat(local.getStatus(), Matchers.is(DataStatus.WAITING));
        Assert.assertThat(local.getFavoriteCount(), Matchers.is(0));
    }

    /**
     * Test setting the title.
     */
    @Test
    public void testSetTitle() {
        Assert.assertNotNull(this.m.getTitle());
        final String title = UUID.randomUUID().toString();
        this.m.setTitle(title);
        Assert.assertThat(this.m.getTitle(), Matchers.is(title));
    }

    /**
     * Test setting the type.
     */
    @Test
    public void testSetType() {
        Assert.assertNotNull(this.m.getType());
        this.m.setType(MovieType.TV);
        Assert.assertEquals(this.m.getType(), MovieType.TV);
    }

    /**
     * Test setting the budget.
     */
    @Test
    public void testSetBudget() {
        final String budget = UUID.randomUUID().toString();
        this.m.setBudget(budget);
        Assert.assertThat(this.m.getBudget(), Matchers.is(budget));
    }

    /**
     * Test setting the status.
     */
    @Test
    public void testSetStatus() {
        Assert.assertNotNull(this.m.getStatus());
        this.m.setStatus(DataStatus.ACCEPTED);
        Assert.assertEquals(this.m.getStatus(), DataStatus.ACCEPTED);
    }

    /**
     * Test setting the rating.
     */
    @Test
    public void testSetRating() {
        Assert.assertFalse(this.m.getRating().isPresent());
        final Float rating = 8f;
        this.m.setRating(rating);
        Assert.assertThat(this.m.getRating().orElse((float) RandomSupplier.INT.get()), Matchers.is(rating));
    }

    /**
     * Test setting the favorite count.
     */
    @Test
    public void testSetFavoriteCount() {
        Assert.assertNotNull(this.m.getFavoriteCount());
        final Integer favoriteCount = 10;
        this.m.setFavoriteCount(favoriteCount);
        Assert.assertThat(this.m.getFavoriteCount(), Matchers.is(favoriteCount));
    }

    /**
     * Test the toString method.
     */
    @Test
    public void testToString() {
        Assert.assertNotNull(this.m.toString());
    }
}

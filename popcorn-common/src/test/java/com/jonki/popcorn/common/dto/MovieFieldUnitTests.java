package com.jonki.popcorn.common.dto;

import com.jonki.popcorn.test.category.UnitTest;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * Tests for the MovieField enum.
 */
@Category(UnitTest.class)
public class MovieFieldUnitTests {

    /**
     * Tests whether enumeration types contain the necessary permissions.
     */
    @Test
    public void testNecessaryPermissions() {
        Assert.assertThat(
                MovieField.TITLE.getNecessaryPermissions(),
                IsIterableContainingInAnyOrder.containsInAnyOrder(UserMoviePermission.ALL, UserMoviePermission.TITLE)
        );
        Assert.assertThat(
                MovieField.TYPE.getNecessaryPermissions(),
                IsIterableContainingInAnyOrder.containsInAnyOrder(UserMoviePermission.ALL, UserMoviePermission.TYPE)
        );
        Assert.assertThat(
                MovieField.OTHER_TITLE.getNecessaryPermissions(),
                IsIterableContainingInAnyOrder.containsInAnyOrder(UserMoviePermission.ALL, UserMoviePermission.OTHER_TITLE)
        );
        Assert.assertThat(
                MovieField.REVIEW.getNecessaryPermissions(),
                IsIterableContainingInAnyOrder.containsInAnyOrder(UserMoviePermission.ALL, UserMoviePermission.REVIEW)
        );
        Assert.assertThat(
                MovieField.BUDGET.getNecessaryPermissions(),
                IsIterableContainingInAnyOrder.containsInAnyOrder(UserMoviePermission.ALL, UserMoviePermission.BUDGET)
        );
        Assert.assertThat(
                MovieField.BOX_OFFICE.getNecessaryPermissions(),
                IsIterableContainingInAnyOrder.containsInAnyOrder(UserMoviePermission.ALL, UserMoviePermission.BOX_OFFICE)
        );
        Assert.assertThat(
                MovieField.SITE.getNecessaryPermissions(),
                IsIterableContainingInAnyOrder.containsInAnyOrder(UserMoviePermission.ALL, UserMoviePermission.SITE)
        );
        Assert.assertThat(
                MovieField.RELEASE_DATE.getNecessaryPermissions(),
                IsIterableContainingInAnyOrder.containsInAnyOrder(UserMoviePermission.ALL, UserMoviePermission.RELEASE_DATE)
        );
        Assert.assertThat(
                MovieField.OUTLINE.getNecessaryPermissions(),
                IsIterableContainingInAnyOrder.containsInAnyOrder(UserMoviePermission.ALL, UserMoviePermission.OUTLINE)
        );
        Assert.assertThat(
                MovieField.SUMMARY.getNecessaryPermissions(),
                IsIterableContainingInAnyOrder.containsInAnyOrder(UserMoviePermission.ALL, UserMoviePermission.SUMMARY)
        );
        Assert.assertThat(
                MovieField.SYNOPSIS.getNecessaryPermissions(),
                IsIterableContainingInAnyOrder.containsInAnyOrder(UserMoviePermission.ALL, UserMoviePermission.SYNOPSIS)
        );
        Assert.assertThat(
                MovieField.COUNTRY.getNecessaryPermissions(),
                IsIterableContainingInAnyOrder.containsInAnyOrder(UserMoviePermission.ALL, UserMoviePermission.COUNTRY)
        );
        Assert.assertThat(
                MovieField.GENRE.getNecessaryPermissions(),
                IsIterableContainingInAnyOrder.containsInAnyOrder(UserMoviePermission.ALL, UserMoviePermission.GENRE)
        );
        Assert.assertThat(
                MovieField.LANGUAGE.getNecessaryPermissions(),
                IsIterableContainingInAnyOrder.containsInAnyOrder(UserMoviePermission.ALL, UserMoviePermission.LANGUAGE)
        );
        Assert.assertThat(
                MovieField.PHOTO.getNecessaryPermissions(),
                IsIterableContainingInAnyOrder.containsInAnyOrder(UserMoviePermission.ALL, UserMoviePermission.PHOTO)
        );
        Assert.assertThat(
                MovieField.POSTER.getNecessaryPermissions(),
                IsIterableContainingInAnyOrder.containsInAnyOrder(UserMoviePermission.ALL, UserMoviePermission.POSTER)
        );
    }
}

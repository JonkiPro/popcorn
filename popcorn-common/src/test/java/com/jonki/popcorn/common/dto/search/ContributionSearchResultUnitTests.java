package com.jonki.popcorn.common.dto.search;

import com.jonki.popcorn.common.dto.MovieField;
import com.jonki.popcorn.test.category.UnitTest;
import com.jonki.popcorn.test.supplier.RandomSupplier;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Date;

/**
 * Tests for the ContributionSearchResult DTO.
 */
@Category(UnitTest.class)
public class ContributionSearchResultUnitTests {

    /**
     * Make sure constructor works.
     */
    @Test
    public void canConstruct() {
        final Long id = RandomSupplier.LONG.get();
        final MovieField field = MovieField.SYNOPSIS;
        final Date date = new Date();
        final ContributionSearchResult searchResult
                = new ContributionSearchResult(id, field, date);

        Assert.assertThat(searchResult.getId(), Matchers.is(id.toString()));
        Assert.assertThat(searchResult.getField(), Matchers.is(field));
        Assert.assertThat(searchResult.getDate(), Matchers.is(date));
    }
}

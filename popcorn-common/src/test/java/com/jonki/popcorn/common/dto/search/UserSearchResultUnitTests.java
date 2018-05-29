package com.jonki.popcorn.common.dto.search;

import com.jonki.popcorn.test.category.UnitTest;
import com.jonki.popcorn.test.supplier.RandomSupplier;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * Tests for the UserSearchResult DTO.
 */
@Category(UnitTest.class)
public class UserSearchResultUnitTests {

    /**
     * Make sure constructor works.
     */
    @Test
    public void canConstruct() {
        final String id = RandomSupplier.STRING.get();
        final String username = RandomSupplier.STRING.get();
        final String email = RandomSupplier.STRING.get();
        final UserSearchResult searchResult
                = new UserSearchResult(id, username, email);

        Assert.assertThat(searchResult.getId(), Matchers.is(id));
        Assert.assertThat(searchResult.getUsername(), Matchers.is(username));
        Assert.assertThat(searchResult.getEmail(), Matchers.is(email));
    }
}

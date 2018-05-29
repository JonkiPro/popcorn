package com.jonki.popcorn.common.dto;

import com.jonki.popcorn.test.category.UnitTest;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.UUID;

/**
 * Tests for the ShallowUser DTO.
 */
@Category(UnitTest.class)
public class ShallowUserUnitTests {

    private static final String USERNAME = UUID.randomUUID().toString();
    private static final String AVATAR_SRC = UUID.randomUUID().toString();

    /**
     * Test to make sure we can build an shallow user using the default builder constructor.
     */
    @Test
    public void canBuildShallowUser() {
        final ShallowUser shallowUser = new ShallowUser.Builder(
                USERNAME,
                AVATAR_SRC
        ).build();
        Assert.assertThat(shallowUser.getUsername(), Matchers.is(USERNAME));
        Assert.assertThat(shallowUser.getAvatarSrc(), Matchers.is(AVATAR_SRC));
    }

    /**
     * Test equals.
     */
    @Test
    public void canFindEquality() {
        final ShallowUser.Builder builder = new ShallowUser.Builder(
                USERNAME,
                AVATAR_SRC
        );
        final ShallowUser shallowUser1 = builder.build();
        final ShallowUser shallowUser2 = builder.build();

        Assert.assertTrue(shallowUser1.equals(shallowUser2));
        Assert.assertTrue(shallowUser2.equals(shallowUser1));
    }

    /**
     * Test hash code.
     */
    @Test
    public void canUseHashCode() {
        final ShallowUser.Builder builder = new ShallowUser.Builder(
                USERNAME,
                AVATAR_SRC
        );
        final ShallowUser shallowUser1 = builder.build();
        final ShallowUser shallowUser2 = builder.build();

        Assert.assertEquals(shallowUser1.hashCode(), shallowUser2.hashCode());
    }
}

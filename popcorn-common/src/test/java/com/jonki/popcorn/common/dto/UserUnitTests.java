package com.jonki.popcorn.common.dto;

import com.jonki.popcorn.test.category.UnitTest;
import com.jonki.popcorn.test.supplier.RandomSupplier;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Optional;
import java.util.UUID;

/**
 * Tests for the User DTO.
 */
@Category(UnitTest.class)
public class UserUnitTests {

    private static final String USERNAME = RandomSupplier.STRING.get();
    private static final String EMAIL = RandomSupplier.STRING.get();
    private static final Integer NUMBER_OF_FRIENDS = RandomSupplier.INT.get();

    /**
     * Test to make sure we can build an user using the default builder constructor.
     */
    @Test
    public void canBuildUser() {
        final User user = new User.Builder(
                USERNAME,
                EMAIL,
                NUMBER_OF_FRIENDS
        ).build();
        Assert.assertThat(user.getUsername(), Matchers.is(USERNAME));
        Assert.assertThat(user.getEmail(), Matchers.is(EMAIL));
        Assert.assertThat(user.getNumberOfFriends(), Matchers.is(NUMBER_OF_FRIENDS));
        Assert.assertFalse(Optional.ofNullable(user.getAvatarSrc()).isPresent());
        Assert.assertFalse(Optional.ofNullable(user.getId()).isPresent());
    }

    /**
     * Test to make sure we can build a user with all optional parameters.
     */
    @Test
    public void canBuildUserWithOptionals() {
        final User.Builder builder = new User.Builder(
                USERNAME,
                EMAIL,
                NUMBER_OF_FRIENDS
        );

        final String avatarSrc = RandomSupplier.STRING.get();
        builder.withAvatarSrc(avatarSrc);

        final String id = RandomSupplier.STRING.get();
        builder.withId(id);

        final User user = builder.build();
        Assert.assertThat(user.getUsername(), Matchers.is(USERNAME));
        Assert.assertThat(user.getEmail(), Matchers.is(EMAIL));
        Assert.assertThat(user.getNumberOfFriends(), Matchers.is(NUMBER_OF_FRIENDS));
        Assert.assertThat(user.getAvatarSrc(), Matchers.is(avatarSrc));
        Assert.assertThat(Optional.of(user.getId()).orElseThrow(IllegalArgumentException::new), Matchers.is(id));
    }

    /**
     * Test to make sure we can build an user with null collection parameters.
     */
    @Test
    public void canBuildUserNullOptionals() {
        final User.Builder builder = new User.Builder(
                USERNAME,
                EMAIL,
                NUMBER_OF_FRIENDS
        );
        builder.withAvatarSrc(null);
        builder.withId(null);

        final User user = builder.build();
        Assert.assertThat(user.getUsername(), Matchers.is(USERNAME));
        Assert.assertThat(user.getEmail(), Matchers.is(EMAIL));
        Assert.assertThat(user.getNumberOfFriends(), Matchers.is(NUMBER_OF_FRIENDS));
        Assert.assertFalse(Optional.ofNullable(user.getAvatarSrc()).isPresent());
        Assert.assertFalse(Optional.ofNullable(user.getId()).isPresent());
    }

    /**
     * Test equals.
     */
    @Test
    public void canFindEquality() {
        final User.Builder builder = new User.Builder(
                USERNAME,
                EMAIL,
                NUMBER_OF_FRIENDS
        );
        builder.withAvatarSrc(null);
        builder.withId(UUID.randomUUID().toString());
        final User user1 = builder.build();
        final User user2 = builder.build();
        builder.withId(UUID.randomUUID().toString());
        final User user3 = builder.build();

        Assert.assertTrue(user1.equals(user2));
        Assert.assertTrue(user2.equals(user1));
        Assert.assertFalse(user1.equals(user3));
    }

    /**
     * Test hash code.
     */
    @Test
    public void canUseHashCode() {
        final User.Builder builder = new User.Builder(
                USERNAME,
                EMAIL,
                NUMBER_OF_FRIENDS
        );
        builder.withAvatarSrc(null);
        builder.withId(UUID.randomUUID().toString());
        final User user1 = builder.build();
        final User user2 = builder.build();
        builder.withId(UUID.randomUUID().toString());
        final User user3 = builder.build();

        Assert.assertEquals(user1.hashCode(), user2.hashCode());
        Assert.assertNotEquals(user1.hashCode(), user3.hashCode());
    }
}

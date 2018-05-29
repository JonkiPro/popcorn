package com.jonki.popcorn.common.dto;

import com.google.common.collect.Sets;
import com.jonki.popcorn.common.dto.movie.Synopsis;
import com.jonki.popcorn.test.category.UnitTest;
import com.jonki.popcorn.test.supplier.RandomSupplier;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Tests for the Contribution DTO.
 */
@Category(UnitTest.class)
public class ContributionUnitTests {

    private static final Long MOVIE_ID = RandomSupplier.LONG.get();
    private static final String MOVIE_TITLE = RandomSupplier.STRING.get();
    private static final ShallowUser OWNER = new ShallowUser.Builder(RandomSupplier.STRING.get(), RandomSupplier.STRING.get()).build();
    private static final DataStatus STATUS = DataStatus.ACCEPTED;
    private static final MovieField FIELD = MovieField.SYNOPSIS;
    private static final Set<String> SOURCES = Sets.newHashSet(RandomSupplier.STRING.get(), RandomSupplier.STRING.get());
    private static final Date CREATION_DATE = RandomSupplier.DATE.get();

    /**
     * Test to make sure we can build an contribution using the default builder constructor.
     */
    @Test
    public void canBuildContribution() {
        final Contribution contribution = new Contribution.Builder(
                MOVIE_ID,
                MOVIE_TITLE,
                OWNER,
                STATUS,
                FIELD,
                SOURCES,
                CREATION_DATE
        ).build();
        Assert.assertTrue(contribution.getElementsToAdd().isEmpty());
        Assert.assertTrue(contribution.getElementsToUpdate().isEmpty());
        Assert.assertTrue(contribution.getElementsToDelete().isEmpty());
        Assert.assertTrue(contribution.getElementsUpdated().isEmpty());
        Assert.assertThat(contribution.getMovieId(), Matchers.is(MOVIE_ID));
        Assert.assertThat(contribution.getMovieTitle(), Matchers.is(MOVIE_TITLE));
        Assert.assertThat(contribution.getOwner(), Matchers.is(OWNER));
        Assert.assertThat(contribution.getStatus(), Matchers.is(DataStatus.ACCEPTED));
        Assert.assertThat(contribution.getField(), Matchers.is(MovieField.SYNOPSIS));
        Assert.assertThat(contribution.getSources(), Matchers.is(SOURCES));
        Assert.assertThat(contribution.getCreationDate(), Matchers.is(CREATION_DATE));
        Assert.assertFalse(Optional.ofNullable(contribution.getUserComment()).isPresent());
        Assert.assertFalse(Optional.ofNullable(contribution.getVerificationDate()).isPresent());
        Assert.assertFalse(Optional.ofNullable(contribution.getVerificationUser()).isPresent());
        Assert.assertFalse(Optional.ofNullable(contribution.getVerificationComment()).isPresent());
        Assert.assertFalse(Optional.ofNullable(contribution.getId()).isPresent());
    }

    /**
     * Test to make sure we can build a contribution with all optional parameters.
     */
    @Test
    public void canBuildContributionWithOptionals() {
        final Contribution.Builder<Synopsis> builder = new Contribution.Builder<>(
                MOVIE_ID,
                MOVIE_TITLE,
                OWNER,
                STATUS,
                FIELD,
                SOURCES,
                CREATION_DATE
        );

        final Map<Long, Synopsis> elements = new HashMap<>();
        elements.put(RandomSupplier.LONG.get(), new Synopsis.Builder(RandomSupplier.STRING.get()).build());
        builder.withElementsToAdd(elements);
        builder.withElementsToUpdate(elements);
        builder.withElementsToDelete(elements);
        builder.withElementsUpdated(elements);

        final String userComment = RandomSupplier.STRING.get();
        builder.withUserComment(userComment);

        final Date verificationDate = RandomSupplier.DATE.get();
        builder.withVerificationDate(verificationDate);

        builder.withVerificationUser(OWNER);

        final String verificationComment = RandomSupplier.STRING.get();
        builder.withVerificationComment(verificationComment);

        final String id = RandomSupplier.STRING.get();
        builder.withId(id);

        final Contribution contribution = builder.build();
        Assert.assertThat(contribution.getElementsToAdd(), Matchers.is(elements));
        Assert.assertThat(contribution.getElementsToUpdate(), Matchers.is(elements));
        Assert.assertThat(contribution.getElementsToDelete(), Matchers.is(elements));
        Assert.assertThat(contribution.getElementsUpdated(), Matchers.is(elements));
        Assert.assertThat(contribution.getMovieId(), Matchers.is(MOVIE_ID));
        Assert.assertThat(contribution.getMovieTitle(), Matchers.is(MOVIE_TITLE));
        Assert.assertThat(contribution.getOwner(), Matchers.is(OWNER));
        Assert.assertThat(contribution.getStatus(), Matchers.is(DataStatus.ACCEPTED));
        Assert.assertThat(contribution.getField(), Matchers.is(MovieField.SYNOPSIS));
        Assert.assertThat(contribution.getSources(), Matchers.is(SOURCES));
        Assert.assertThat(contribution.getCreationDate(), Matchers.is(CREATION_DATE));
        Assert.assertThat(contribution.getUserComment(), Matchers.is(userComment));
        Assert.assertThat(contribution.getVerificationDate(), Matchers.is(verificationDate));
        Assert.assertThat(contribution.getVerificationUser(), Matchers.is(OWNER));
        Assert.assertThat(contribution.getVerificationComment(), Matchers.is(verificationComment));
        Assert.assertThat(Optional.of(contribution.getId()).orElseThrow(IllegalArgumentException::new), Matchers.is(id));
    }

    /**
     * Test to make sure we can build an contribution with null collection parameters.
     */
    @Test
    public void canBuildContributionNullOptionals() {
        final Contribution.Builder<Synopsis> builder = new Contribution.Builder<>(
                MOVIE_ID,
                MOVIE_TITLE,
                OWNER,
                STATUS,
                FIELD,
                SOURCES,
                CREATION_DATE
        );
        builder.withElementsToAdd(null);
        builder.withElementsToUpdate(null);
        builder.withElementsToDelete(null);
        builder.withElementsUpdated(null);
        builder.withUserComment(null);
        builder.withVerificationDate(null);
        builder.withVerificationUser(null);
        builder.withVerificationComment(null);
        builder.withId(null);

        final Contribution contribution = builder.build();
        Assert.assertTrue(contribution.getElementsToAdd().isEmpty());
        Assert.assertTrue(contribution.getElementsToUpdate().isEmpty());
        Assert.assertTrue(contribution.getElementsToDelete().isEmpty());
        Assert.assertTrue(contribution.getElementsUpdated().isEmpty());
        Assert.assertThat(contribution.getMovieId(), Matchers.is(MOVIE_ID));
        Assert.assertThat(contribution.getMovieTitle(), Matchers.is(MOVIE_TITLE));
        Assert.assertThat(contribution.getOwner(), Matchers.is(OWNER));
        Assert.assertThat(contribution.getStatus(), Matchers.is(DataStatus.ACCEPTED));
        Assert.assertThat(contribution.getField(), Matchers.is(MovieField.SYNOPSIS));
        Assert.assertThat(contribution.getSources(), Matchers.is(SOURCES));
        Assert.assertThat(contribution.getCreationDate(), Matchers.is(CREATION_DATE));
        Assert.assertFalse(Optional.ofNullable(contribution.getUserComment()).isPresent());
        Assert.assertFalse(Optional.ofNullable(contribution.getVerificationDate()).isPresent());
        Assert.assertFalse(Optional.ofNullable(contribution.getVerificationUser()).isPresent());
        Assert.assertFalse(Optional.ofNullable(contribution.getVerificationComment()).isPresent());
        Assert.assertFalse(Optional.ofNullable(contribution.getId()).isPresent());
    }

    /**
     * Test equals.
     */
    @Test
    public void canFindEquality() {
        final Contribution.Builder<Synopsis> builder = new Contribution.Builder<>(
                MOVIE_ID,
                MOVIE_TITLE,
                OWNER,
                STATUS,
                FIELD,
                SOURCES,
                CREATION_DATE
        );
        builder.withElementsToAdd(null);
        builder.withElementsToUpdate(null);
        builder.withElementsToDelete(null);
        builder.withElementsUpdated(null);
        builder.withUserComment(null);
        builder.withVerificationDate(null);
        builder.withVerificationUser(null);
        builder.withVerificationComment(null);
        builder.withId(UUID.randomUUID().toString());
        final Contribution contribution1 = builder.build();
        final Contribution contribution2 = builder.build();
        builder.withId(UUID.randomUUID().toString());
        final Contribution contribution3 = builder.build();

        Assert.assertTrue(contribution1.equals(contribution2));
        Assert.assertTrue(contribution2.equals(contribution1));
        Assert.assertFalse(contribution1.equals(contribution3));
    }

    /**
     * Test hash code.
     */
    @Test
    public void canUseHashCode() {
        final Contribution.Builder<Synopsis> builder = new Contribution.Builder<>(
                MOVIE_ID,
                MOVIE_TITLE,
                OWNER,
                STATUS,
                FIELD,
                SOURCES,
                CREATION_DATE
        );
        builder.withElementsToAdd(null);
        builder.withElementsToUpdate(null);
        builder.withElementsToDelete(null);
        builder.withElementsUpdated(null);
        builder.withUserComment(null);
        builder.withVerificationDate(null);
        builder.withVerificationUser(null);
        builder.withVerificationComment(null);
        builder.withId(UUID.randomUUID().toString());
        final Contribution contribution1 = builder.build();
        final Contribution contribution2 = builder.build();
        builder.withId(UUID.randomUUID().toString());
        final Contribution contribution3 = builder.build();

        Assert.assertEquals(contribution1.hashCode(), contribution2.hashCode());
        Assert.assertNotEquals(contribution1.hashCode(), contribution3.hashCode());
    }
}

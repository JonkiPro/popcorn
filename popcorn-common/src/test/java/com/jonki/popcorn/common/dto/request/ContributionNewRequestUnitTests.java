package com.jonki.popcorn.common.dto.request;

import com.google.common.collect.Sets;
import com.jonki.popcorn.common.dto.movie.Synopsis;
import com.jonki.popcorn.test.category.UnitTest;
import com.jonki.popcorn.test.supplier.RandomSupplier;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Tests for the ContributionNewRequest DTO.
 */
@Category(UnitTest.class)
public class ContributionNewRequestUnitTests {

    private static final Set<String> SOURCES = Sets.newHashSet(RandomSupplier.STRING.get(), RandomSupplier.STRING.get());

    /**
     * Test to make sure we can build an contribution new request using the default builder constructor.
     */
    @Test
    public void canBuildContributionNewRequest() {
        final ContributionNewRequest contributionNewRequest = new ContributionNewRequest.Builder(
                SOURCES
        ).build();
        Assert.assertTrue(contributionNewRequest.getElementsToAdd().isEmpty());
        Assert.assertTrue(contributionNewRequest.getElementsToUpdate().isEmpty());
        Assert.assertTrue(contributionNewRequest.getIdsToDelete().isEmpty());
        Assert.assertThat(contributionNewRequest.getSources(), Matchers.is(SOURCES));
        Assert.assertFalse(Optional.ofNullable(contributionNewRequest.getComment()).isPresent());
    }

    /**
     * Test to make sure we can build a contribution new request with all optional parameters.
     */
    @Test
    public void canBuildContributionNewRequestWithOptionals() {
        final ContributionNewRequest.Builder<Synopsis> builder = new ContributionNewRequest.Builder<>(
                SOURCES
        );

        final List<Synopsis> elementToAdd = new ArrayList<>();
        elementToAdd.add(new Synopsis.Builder(RandomSupplier.STRING.get()).build());
        builder.withElementsToAdd(elementToAdd);

        final Map<Long, Synopsis> elementsToUpdate = new HashMap<>();
        elementsToUpdate.put(RandomSupplier.LONG.get(), new Synopsis.Builder(RandomSupplier.STRING.get()).build());
        builder.withElementsToUpdate(elementsToUpdate);

        final Set<Long> idsToDelete = new HashSet<>();
        idsToDelete.add(RandomSupplier.LONG.get());
        builder.withIdsToDelete(idsToDelete);

        final String comment = RandomSupplier.STRING.get();
        builder.withComment(comment);

        final ContributionNewRequest contributionNewRequest = builder.build();
        Assert.assertThat(contributionNewRequest.getElementsToAdd(), Matchers.is(elementToAdd));
        Assert.assertThat(contributionNewRequest.getElementsToUpdate(), Matchers.is(elementsToUpdate));
        Assert.assertThat(contributionNewRequest.getIdsToDelete(), Matchers.is(idsToDelete));
        Assert.assertThat(contributionNewRequest.getSources(), Matchers.is(SOURCES));
        Assert.assertThat(contributionNewRequest.getComment(), Matchers.is(comment));
    }

    /**
     * Test to make sure we can build an contribution new request with null collection parameters.
     */
    @Test
    public void canBuildContributionNewRequestNullOptionals() {
        final ContributionNewRequest.Builder<Synopsis> builder = new ContributionNewRequest.Builder<>(
                SOURCES
        );
        builder.withElementsToAdd(null);
        builder.withElementsToUpdate(null);
        builder.withIdsToDelete(null);
        builder.withComment(null);

        final ContributionNewRequest contributionNewRequest = builder.build();
        Assert.assertTrue(contributionNewRequest.getElementsToAdd().isEmpty());
        Assert.assertTrue(contributionNewRequest.getElementsToUpdate().isEmpty());
        Assert.assertTrue(contributionNewRequest.getIdsToDelete().isEmpty());
        Assert.assertThat(contributionNewRequest.getSources(), Matchers.is(SOURCES));
        Assert.assertFalse(Optional.ofNullable(contributionNewRequest.getComment()).isPresent());
    }

    /**
     * Test equals.
     */
    @Test
    public void canFindEquality() {
        final ContributionNewRequest.Builder<Synopsis> builder = new ContributionNewRequest.Builder<>(
                SOURCES
        );
        builder.withElementsToAdd(null);
        builder.withElementsToUpdate(null);
        builder.withIdsToDelete(null);
        builder.withComment(null);
        final ContributionNewRequest contributionNewRequest1 = builder.build();
        final ContributionNewRequest contributionNewRequest2 = builder.build();

        Assert.assertTrue(contributionNewRequest1.equals(contributionNewRequest2));
        Assert.assertTrue(contributionNewRequest2.equals(contributionNewRequest1));
    }

    /**
     * Test hash code.
     */
    @Test
    public void canUseHashCode() {
        final ContributionNewRequest.Builder<Synopsis> builder = new ContributionNewRequest.Builder<>(
                SOURCES
        );
        builder.withElementsToAdd(null);
        builder.withElementsToUpdate(null);
        builder.withIdsToDelete(null);
        builder.withComment(null);
        final ContributionNewRequest contributionNewRequest1 = builder.build();
        final ContributionNewRequest contributionNewRequest2 = builder.build();

        Assert.assertEquals(contributionNewRequest1.hashCode(), contributionNewRequest2.hashCode());
    }
}

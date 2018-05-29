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
 * Tests for the ContributionUpdateRequest DTO.
 */
@Category(UnitTest.class)
public class ContributionUpdateRequestUnitTests {

    private static final Set<String> SOURCES = Sets.newHashSet(RandomSupplier.STRING.get(), RandomSupplier.STRING.get());

    /**
     * Test to make sure we can build an contribution update request using the default builder constructor.
     */
    @Test
    public void canBuildContributionUpdateRequest() {
        final ContributionUpdateRequest contributionUpdateRequest = new ContributionUpdateRequest.Builder(
                SOURCES
        ).build();
        Assert.assertTrue(contributionUpdateRequest.getElementsToAdd().isEmpty());
        Assert.assertTrue(contributionUpdateRequest.getNewElementsToAdd().isEmpty());
        Assert.assertTrue(contributionUpdateRequest.getElementsToUpdate().isEmpty());
        Assert.assertTrue(contributionUpdateRequest.getIdsToDelete().isEmpty());
        Assert.assertThat(contributionUpdateRequest.getSources(), Matchers.is(SOURCES));
        Assert.assertFalse(Optional.ofNullable(contributionUpdateRequest.getComment()).isPresent());
    }

    /**
     * Test to make sure we can build a contribution update request with all optional parameters.
     */
    @Test
    public void canBuildContributionUpdateRequestWithOptionals() {
        final ContributionUpdateRequest.Builder<Synopsis> builder = new ContributionUpdateRequest.Builder<>(
                SOURCES
        );

        final Map<Long, Synopsis> elementToAdd = new HashMap<>();
        elementToAdd.put(RandomSupplier.LONG.get(), new Synopsis.Builder(RandomSupplier.STRING.get()).build());
        builder.withElementsToAdd(elementToAdd);

        final List<Synopsis> newElementToAdd = new ArrayList<>();
        newElementToAdd.add(new Synopsis.Builder(RandomSupplier.STRING.get()).build());
        builder.withNewElementsToAdd(newElementToAdd);

        final Map<Long, Synopsis> elementsToUpdate = new HashMap<>();
        elementsToUpdate.put(RandomSupplier.LONG.get(), new Synopsis.Builder(RandomSupplier.STRING.get()).build());
        builder.withElementsToUpdate(elementsToUpdate);

        final Set<Long> idsToDelete = new HashSet<>();
        idsToDelete.add(RandomSupplier.LONG.get());
        builder.withIdsToDelete(idsToDelete);

        final String comment = RandomSupplier.STRING.get();
        builder.withComment(comment);

        final ContributionUpdateRequest contributionUpdateRequest = builder.build();
        Assert.assertThat(contributionUpdateRequest.getElementsToAdd(), Matchers.is(elementToAdd));
        Assert.assertThat(contributionUpdateRequest.getNewElementsToAdd(), Matchers.is(newElementToAdd));
        Assert.assertThat(contributionUpdateRequest.getElementsToUpdate(), Matchers.is(elementsToUpdate));
        Assert.assertThat(contributionUpdateRequest.getIdsToDelete(), Matchers.is(idsToDelete));
        Assert.assertThat(contributionUpdateRequest.getSources(), Matchers.is(SOURCES));
        Assert.assertThat(contributionUpdateRequest.getComment(), Matchers.is(comment));
    }

    /**
     * Test to make sure we can build an contribution update request with null collection parameters.
     */
    @Test
    public void canBuildContributionUpdateRequestNullOptionals() {
        final ContributionUpdateRequest.Builder<Synopsis> builder = new ContributionUpdateRequest.Builder<>(
                SOURCES
        );
        builder.withElementsToAdd(null);
        builder.withNewElementsToAdd(null);
        builder.withElementsToUpdate(null);
        builder.withIdsToDelete(null);
        builder.withComment(null);

        final ContributionUpdateRequest contributionUpdateRequest = builder.build();
        Assert.assertTrue(contributionUpdateRequest.getElementsToAdd().isEmpty());
        Assert.assertTrue(contributionUpdateRequest.getNewElementsToAdd().isEmpty());
        Assert.assertTrue(contributionUpdateRequest.getElementsToUpdate().isEmpty());
        Assert.assertTrue(contributionUpdateRequest.getIdsToDelete().isEmpty());
        Assert.assertThat(contributionUpdateRequest.getSources(), Matchers.is(SOURCES));
        Assert.assertFalse(Optional.ofNullable(contributionUpdateRequest.getComment()).isPresent());
    }

    /**
     * Test equals.
     */
    @Test
    public void canFindEquality() {
        final ContributionUpdateRequest.Builder<Synopsis> builder = new ContributionUpdateRequest.Builder<>(
                SOURCES
        );
        builder.withElementsToAdd(null);
        builder.withNewElementsToAdd(null);
        builder.withElementsToUpdate(null);
        builder.withIdsToDelete(null);
        builder.withComment(null);
        final ContributionUpdateRequest contributionUpdateRequest1 = builder.build();
        final ContributionUpdateRequest contributionUpdateRequest2 = builder.build();

        Assert.assertTrue(contributionUpdateRequest1.equals(contributionUpdateRequest2));
        Assert.assertTrue(contributionUpdateRequest2.equals(contributionUpdateRequest1));
    }

    /**
     * Test hash code.
     */
    @Test
    public void canUseHashCode() {
        final ContributionUpdateRequest.Builder<Synopsis> builder = new ContributionUpdateRequest.Builder<>(
                SOURCES
        );
        builder.withElementsToAdd(null);
        builder.withNewElementsToAdd(null);
        builder.withElementsToUpdate(null);
        builder.withIdsToDelete(null);
        builder.withComment(null);
        final ContributionUpdateRequest contributionUpdateRequest1 = builder.build();
        final ContributionUpdateRequest contributionUpdateRequest2 = builder.build();

        Assert.assertEquals(contributionUpdateRequest1.hashCode(), contributionUpdateRequest2.hashCode());
    }
}

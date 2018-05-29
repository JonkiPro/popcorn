package com.jonki.popcorn.common.dto.movie;

import com.jonki.popcorn.common.dto.movie.type.SiteType;
import com.jonki.popcorn.test.category.UnitTest;
import com.jonki.popcorn.test.supplier.RandomSupplier;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * Tests for the Site DTO.
 */
@Category(UnitTest.class)
public class SiteUnitTests {

    private static final String SITE = RandomSupplier.STRING.get();
    private static final SiteType SITE_TYPE = SiteType.OFFICIAL;

    /**
     * Test to make sure we can build an site using the default builder constructor.
     */
    @Test
    public void canBuildSite() {
        final Site site = new Site.Builder(
                SITE,
                SITE_TYPE
        ).build();
        Assert.assertThat(site.getSite(), Matchers.is(SITE));
        Assert.assertThat(site.getOfficial(), Matchers.is(SITE_TYPE));
    }

    /**
     * Test equals.
     */
    @Test
    public void canFindEquality() {
        final Site.Builder builder = new Site.Builder(
                SITE,
                SITE_TYPE
        );
        final Site site1 = builder.build();
        final Site site2 = builder.build();

        Assert.assertTrue(site1.equals(site2));
        Assert.assertTrue(site2.equals(site1));
    }

    /**
     * Test hash code.
     */
    @Test
    public void canUseHashCode() {
        final Site.Builder builder = new Site.Builder(
                SITE,
                SITE_TYPE
        );
        final Site site1 = builder.build();
        final Site site2 = builder.build();

        Assert.assertEquals(site1.hashCode(), site2.hashCode());
    }
}

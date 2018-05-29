package com.jonki.popcorn.common.dto.movie;

import com.jonki.popcorn.common.dto.TitleAttribute;
import com.jonki.popcorn.common.dto.movie.type.CountryType;
import com.jonki.popcorn.test.category.UnitTest;
import com.jonki.popcorn.test.supplier.RandomSupplier;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Optional;

/**
 * Tests for the OtherTitle DTO.
 */
@Category(UnitTest.class)
public class OtherTitleUnitTests {

    private static final String TITLE = RandomSupplier.STRING.get();
    private static final CountryType COUNTRY_TYPE = CountryType.USA;

    /**
     * Test to make sure we can build an other title using the default builder constructor.
     */
    @Test
    public void canBuildOtherTitle() {
        final OtherTitle otherTitle = new OtherTitle.Builder(
                TITLE,
                COUNTRY_TYPE
        ).build();
        Assert.assertThat(otherTitle.getTitle(), Matchers.is(TITLE));
        Assert.assertThat(otherTitle.getCountry(), Matchers.is(COUNTRY_TYPE));
        Assert.assertFalse(Optional.ofNullable(otherTitle.getAttribute()).isPresent());
    }

    /**
     * Test to make sure we can build a other title with all optional parameters.
     */
    @Test
    public void canBuildOtherTitleWithOptionals() {
        final OtherTitle.Builder builder = new OtherTitle.Builder(
                TITLE,
                COUNTRY_TYPE
        );

        final TitleAttribute titleAttribute = TitleAttribute.ORIGINAL_TITLE;
        builder.withAttribute(titleAttribute);

        final OtherTitle otherTitle = builder.build();
        Assert.assertThat(otherTitle.getTitle(), Matchers.is(TITLE));
        Assert.assertThat(otherTitle.getCountry(), Matchers.is(COUNTRY_TYPE));
        Assert.assertThat(otherTitle.getAttribute(), Matchers.is(titleAttribute));
    }

    /**
     * Test to make sure we can build an other title with null collection parameters.
     */
    @Test
    public void canBuildOtherTitleNullOptionals() {
        final OtherTitle.Builder builder = new OtherTitle.Builder(
                TITLE,
                COUNTRY_TYPE
        );
        builder.withAttribute(null);

        final OtherTitle otherTitle = builder.build();
        Assert.assertThat(otherTitle.getTitle(), Matchers.is(TITLE));
        Assert.assertThat(otherTitle.getCountry(), Matchers.is(COUNTRY_TYPE));
        Assert.assertFalse(Optional.ofNullable(otherTitle.getAttribute()).isPresent());
    }

    /**
     * Test equals.
     */
    @Test
    public void canFindEquality() {
        final OtherTitle.Builder builder = new OtherTitle.Builder(
                TITLE,
                COUNTRY_TYPE
        );
        builder.withAttribute(null);
        final OtherTitle otherTitle1 = builder.build();
        final OtherTitle otherTitle2 = builder.build();

        Assert.assertTrue(otherTitle1.equals(otherTitle2));
        Assert.assertTrue(otherTitle2.equals(otherTitle1));
    }

    /**
     * Test hash code.
     */
    @Test
    public void canUseHashCode() {
        final OtherTitle.Builder builder = new OtherTitle.Builder(
                TITLE,
                COUNTRY_TYPE
        );
        builder.withAttribute(null);
        final OtherTitle otherTitle1 = builder.build();
        final OtherTitle otherTitle2 = builder.build();

        Assert.assertEquals(otherTitle1.hashCode(), otherTitle2.hashCode());
    }
}

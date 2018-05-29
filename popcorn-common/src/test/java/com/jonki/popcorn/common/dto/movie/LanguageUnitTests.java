package com.jonki.popcorn.common.dto.movie;

import com.jonki.popcorn.common.dto.movie.type.LanguageType;
import com.jonki.popcorn.test.category.UnitTest;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/**
 * Tests for the Language DTO.
 */
@Category(UnitTest.class)
public class LanguageUnitTests {

    private static final LanguageType LANGUAGE_TYPE = LanguageType.ENGLISH;

    /**
     * Test to make sure we can build an language using the default builder constructor.
     */
    @Test
    public void canBuildLanguage() {
        final Language language = new Language.Builder(
                LANGUAGE_TYPE
        ).build();
        Assert.assertThat(language.getLanguage(), Matchers.is(LANGUAGE_TYPE));
    }

    /**
     * Test equals.
     */
    @Test
    public void canFindEquality() {
        final Language.Builder builder = new Language.Builder(
                LANGUAGE_TYPE
        );
        final Language language1 = builder.build();
        final Language language2 = builder.build();

        Assert.assertTrue(language1.equals(language2));
        Assert.assertTrue(language2.equals(language1));
    }

    /**
     * Test hash code.
     */
    @Test
    public void canUseHashCode() {
        final Language.Builder builder = new Language.Builder(
                LANGUAGE_TYPE
        );
        final Language language1 = builder.build();
        final Language language2 = builder.build();

        Assert.assertEquals(language1.hashCode(), language2.hashCode());
    }
}

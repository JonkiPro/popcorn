package com.jonki.popcorn.core.util;

import com.jonki.popcorn.test.category.UnitTest;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Locale;

/**
 * Unit tests for LocaleUtils.
 */
@Category(UnitTest.class)
public class LocaleUtilsUnitTests {

    /**
     * Get a location by language and country.
     */
    @Test
    public void testGetLocaleForLanguageAndCountry() {
        final Locale locale = LocaleUtils.getLocale("en-US");
        Assert.assertThat(locale.getLanguage(), Matchers.is("en"));
        Assert.assertThat(locale.getCountry(), Matchers.is("US"));
    }

    /**
     * Get a location by language.
     */
    @Test
    public void testGetLocaleForLanguage() {
        final Locale locale = LocaleUtils.getLocale("pl");
        Assert.assertThat(locale.getLanguage(), Matchers.is("pl"));
        Assert.assertThat(locale.getCountry(), Matchers.is("PL"));
    }
}

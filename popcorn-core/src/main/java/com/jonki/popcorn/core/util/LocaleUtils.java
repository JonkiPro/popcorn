package com.jonki.popcorn.core.util;

import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * Utility methods for locations.
 * @see <a href="http://www.oracle.com/technetwork/java/javase/java8locales-2095355.html">Locations supported by JAVA 8</a>
 */
public final class LocaleUtils {

    private static final Map<String, String> map = new TreeMap<>();

    // Map initialization with locations supported by JAVA 8.
    static {
        final Locale[] locales = Locale.getAvailableLocales();
        for (final Locale locale : locales) {
            if ((locale.getDisplayCountry() != null) && (!"".equals(locale.getDisplayCountry()))) {
                map.put(locale.getLanguage(), locale.getCountry());
            }
        }
    }

    /**
     * Get a location for the code.
     *
     * @param code The Accept-Language request HTTP header
     * @return Location {@link Locale} obtained from the header
     */
    public static Locale getLocale(String code) {
        //If the code has the form ```language-COUNTRY```. The country code is already provided.
        //input: en-US  output: new Locale("en", "US")
        if(code.indexOf("-") == 2) {
            return new Locale(code.substring(0, 2), code.substring(3, 5));
        }
        //If the code has the form ```language```. You need to get the country code from the map.
        //input: pl  output: new Locale("pl", "PL")
        code = code.substring(0, 2);
        Locale locale = Locale.forLanguageTag(code);
        if ("".equals(locale.getCountry())) {
            locale = new Locale(code, map.get(code));
        }
        return locale;
    }
}

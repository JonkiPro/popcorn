package com.jonki.popcorn.core.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Class for encryption.
 */
public final class EncryptUtils {

    /**
     * Encryption strength.
     */
    private static final int STRENGTH = 12;

    /**
     * This method converts text on code of force 12.
     *
     * @param text Text to encode
     * @return Returns encoded text by force 12
     */
    public static String encrypt(final String text) {
        return new BCryptPasswordEncoder(STRENGTH).encode(text);
    }

    /**
     * This method compares texts codes.
     *
     * @param text Text to compare code with text2
     * @param text2 Text to compare code with text
     * @return Returns TRUE if texts are the same
     */
    public static boolean matches(final String text, final String text2) {
        return new BCryptPasswordEncoder(STRENGTH).matches(text, text2);
    }
}

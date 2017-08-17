package com.service.app.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncryptUtils {

    /**
     * Encryption strength.
     */
    private static final int STRENGTH = 12;

    /**
     * This method converts text on code of force 12.
     * @param text Text to encode.
     * @return Returns encoded text by force 12.
     */
    public static String encrypt(String text) {
        return new BCryptPasswordEncoder(STRENGTH).encode(text);
    }

    /**
     * This method compares password codes.
     * @param password1 Password to compare code with password2.
     * @param password2 Password to compare code with password1.
     * @return Returns TRUE if passwords are the same.
     */
    public static boolean matches(String password1, String password2) {
        return new BCryptPasswordEncoder(STRENGTH).matches(password1, password2);
    }
}

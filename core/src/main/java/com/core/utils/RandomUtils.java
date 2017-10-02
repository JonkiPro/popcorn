package com.core.utils;

/**
 * Class for returning random texts.
 */
public class RandomUtils {

    private static final char[] CHARACTERS = ("abcdefghijklmnopqrstuvwxyz" +
                                              "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                                              "0123456789").toCharArray();

    /**
     * This method generates a password.
     *
     * @return Returns the generated password.
     */
    public static String randomPassword() {
        final java.util.Random random = new java.util.Random();

        final byte randomLength = (byte) (random.nextInt(14) + 6);
        byte randomElementNumber;
        String password = "";

        for (int i = 0; i < randomLength; ++i) {
            randomElementNumber = (byte) random.nextInt(CHARACTERS.length);

            password = new StringBuilder(password).append(CHARACTERS[randomElementNumber]).toString();
        }

        return password;
    }

    /**
     * This method generates a token.
     *
     * @return Returns the generated token.
     */
    public static String randomToken() {
        final java.util.Random random = new java.util.Random();

        final byte randomLength = (byte) (random.nextInt(28) + 12);
        byte randomElementNumber;
        String token = "";

        for (int i = 0; i < randomLength; ++i) {
            randomElementNumber = (byte) random.nextInt(CHARACTERS.length);

            token = new StringBuilder(token).append(CHARACTERS[randomElementNumber]).toString();
        }

        return token;
    }
}

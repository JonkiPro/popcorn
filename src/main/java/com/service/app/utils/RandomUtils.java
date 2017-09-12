package com.service.app.utils;

public class RandomUtils {

    /**
     * This method generates a password.
     * @return Returns the generated password.
     */
    public static String randomPassword() {
        char[] alphabet = ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789").toCharArray();

        java.util.Random random = new java.util.Random();

        byte randomLength = (byte) (random.nextInt(14) + 6);
        byte randomElementNumber;
        String password = "";

        for (int i = 0; i < randomLength; ++i) {
            randomElementNumber = (byte) random.nextInt(alphabet.length);

            password = new StringBuilder(password).append(alphabet[randomElementNumber]).toString();
        }

        return password;
    }

    /**
     * This method generates a token.
     * @return Returns the generated token.
     */
    public static String randomToken() {
        char[] alphabet = ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789").toCharArray();

        java.util.Random random = new java.util.Random();

        byte randomLength = (byte) (random.nextInt(28) + 12);
        byte randomElementNumber;
        String token = "";

        for (int i = 0; i < randomLength; ++i) {
            randomElementNumber = (byte) random.nextInt(alphabet.length);

            token = new StringBuilder(token).append(alphabet[randomElementNumber]).toString();
        }

        return token;
    }
}

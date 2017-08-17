package com.service.app.utils;

public class RandomUtils {

    public static String randomPassword() {
        char[] alphabet = ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789").toCharArray();

        java.util.Random random = new java.util.Random();

        byte randomLength = (byte) (random.nextInt(14) + 6);
        byte randomElementNumber;
        String newPassword = "";

        for (int i = 0; i < randomLength; ++i) {
            randomElementNumber = (byte) random.nextInt(alphabet.length);

            newPassword = new StringBuilder(newPassword).append(alphabet[randomElementNumber]).toString();
        }

        return newPassword;
    }
}

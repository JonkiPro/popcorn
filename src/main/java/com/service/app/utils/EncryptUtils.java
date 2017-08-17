package com.service.app.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class EncryptUtils {

    public static String encrypt(String text) {
        return new BCryptPasswordEncoder(12).encode(text);
    }

    public static boolean matches(String password1, String password2) {
        return new BCryptPasswordEncoder(12).matches(password1, password2);
    }
}

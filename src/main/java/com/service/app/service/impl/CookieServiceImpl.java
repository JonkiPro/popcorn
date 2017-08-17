package com.service.app.service.impl;

import com.service.app.service.CookieService;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class CookieServiceImpl implements CookieService {

    @Override
    public void addCookie(HttpServletResponse response,
                          String name,
                          String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(3600);
        response.addCookie(cookie);
    }

    @Override
    public void removeCookie(HttpServletRequest request,
                             HttpServletResponse response,
                             String name) {
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equalsIgnoreCase(name)) {
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
    }

    @Override
    public boolean isCookie(HttpServletRequest request,
                            String name) {
        try {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equalsIgnoreCase(name))
                    return true;
            }
        } catch (NullPointerException e) {
            return false;
        }

        return false;
    }

    @Override
    public String getValueCookie(HttpServletRequest request,
                                 String name) {
        try {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equalsIgnoreCase(name))
                    return cookie.getValue();
            }
        } catch (NullPointerException e) {
            return "";
        }

        return "";
    }
}

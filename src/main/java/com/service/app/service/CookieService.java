package com.service.app.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CookieService {

    void addCookie(HttpServletResponse response,
                   String name,
                   String value);

    void removeCookie(HttpServletRequest request,
                      HttpServletResponse response,
                      String name);

    boolean isCookie(HttpServletRequest request,
                     String name);

    String getValueCookie(HttpServletRequest request,
                          String name);
}

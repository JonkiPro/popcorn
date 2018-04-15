package com.web.web.interceptor;

import com.core.util.LocaleUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * Interceptor for setting the default locale by header.
 */
public class LocaleInterceptor extends HandlerInterceptorAdapter {

    /**
     * Set the default locale before the request.
     */
    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) throws Exception {
        final String userLocale = request.getHeader("Accept-Language");
        Locale.setDefault(LocaleUtils.getLocale(userLocale));
        return true;
    }
}

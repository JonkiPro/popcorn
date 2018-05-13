package com.jonki.popcorn.web.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interceptor for calculating the request time.
 */
public class RequestProcessingTimeInterceptor extends HandlerInterceptorAdapter {

    private final Logger logger = LoggerFactory.getLogger(RequestProcessingTimeInterceptor.class);

    /**
     * This method is called immediately before the request.
     */
    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) throws Exception {
        final long startTime = System.currentTimeMillis();

        logger.info("Request URL::" + request.getRequestURI() + ":: Start Time=" + System.currentTimeMillis());
        request.setAttribute("startTime", startTime);

        return true;
    }

    /**
     * This method is called immediately after the request is processed by HandlerAdapter.
     */
    @Override
    public void postHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler,
            final ModelAndView modelAndView
    ) throws Exception {
        System.out.println("Request URL::" + request.getRequestURI() + " Sent to Handler :: Current Time=" + System.currentTimeMillis());
    }

    /**
     * This method is called immediately after a successful request.
     */
    @Override
    public void afterCompletion(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler,
            final Exception ex
    ) throws Exception {
        final long startTime = (Long) request.getAttribute("startTime");

        logger.info("Request URL::" + request.getRequestURI() + ":: End Time=" + System.currentTimeMillis());
        logger.info("Request URL::" + request.getRequestURI() + ":: Time Taken=" + (System.currentTimeMillis() - startTime));
    }
}

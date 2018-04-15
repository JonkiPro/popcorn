package com.web.web.config;

import com.web.web.interceptor.LocaleInterceptor;
import com.web.web.interceptor.RequestProcessingTimeInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;

/**
 * Configuration for Spring MVC.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * Add an interceptor.
     *
     * @param registry Register of interceptors
     */
    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new RequestProcessingTimeInterceptor());
        registry.addInterceptor(new LocaleInterceptor()).addPathPatterns("/api/v1.0/movies/{id}");
    }

    /**
     * Character encoding filter that forces content-type in response to be UTF-8.
     *
     * @return The encoding filter
     */
    @Bean
    public CharacterEncodingFilter characterEncodingFilter() {
        final CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding(StandardCharsets.UTF_8.name());
        // This effectively obliterates any upstream default and/or encoding detectors
        // As a result, everything is served as UTF-8
        characterEncodingFilter.setForceEncoding(true);
        return characterEncodingFilter;
    }
}

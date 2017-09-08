package com.service.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("security")
public class SecurityProperties {

    // Pattern
    private String pattern;

    // Form Login
    private String loginPage;
    private String failureUrl;
    private String usernameParameter;
    private String passwordParameter;

    // Logout
    private boolean invalidateHttpSession;
    private boolean clearAuthentication;
    private String logoutRequestMatcher;
    private String logoutSuccessUrl;

    // Session Management
    private String invalidSessionUrl;
    private int maximumSessions;
    private String expiredUrl;

    // Remember Me
    private String rememberMeParameter;
    private String rememberMeCookieName;
    private int tokenValiditySeconds;

    // Authentication Manager
    private String usersByUsernameQuery;
    private String authoritiesByUsernameQuery;

    // Password Encoder
    private int strengthPasswordEncoder;
}

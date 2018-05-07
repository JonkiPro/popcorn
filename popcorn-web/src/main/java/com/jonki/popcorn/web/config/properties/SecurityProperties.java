package com.jonki.popcorn.web.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Properties for Spring Security configuration.
 */
@ConfigurationProperties(prefix = "security")
@Getter
@Setter
@Validated
public class SecurityProperties {

    // Pattern
    /**
     * Pattern
     */
    private String pattern;

    // Form Login
    /**
     * The login page
     */
    private String loginPage;
    /**
     * Username parameter
     */
    private String usernameParameter;
    /**
     * Password parameter
     */
    private String passwordParameter;

    // Logout
    /**
     * Invalidate HTTP session
     */
    private boolean invalidateHttpSession;
    /**
     * Clear authentication
     */
    private boolean clearAuthentication;
    /**
     * Logout address
     */
    private String logoutRequestMatcher;
    /**
     * Address of successful logout
     */
    private String logoutSuccessUrl;

    // Session Management
    /**
     * Invalid session URL
     */
    private String invalidSessionUrl;
    /**
     * Maximum number of sessions
     */
    private int maximumSessions;
    /**
     * Expired URL
     */
    private String expiredUrl;

    // Remember Me
    /**
     * Remember-me parameter
     */
    private String rememberMeParameter;
    /**
     * Remember-me cookie name
     */
    private String rememberMeCookieName;
    /**
     * Token validity seconds
     */
    private int tokenValiditySeconds;

    // Password Encoder
    /**
     * The strength of the password encoder
     */
    private int strengthPasswordEncoder;
}

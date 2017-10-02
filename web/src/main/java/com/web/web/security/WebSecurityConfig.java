package com.web.web.security;

import com.web.web.config.WebDatasourceConfig;
import com.web.web.security.handler.RestAuthenticationSuccessHandler;
import com.web.web.security.handler.RestAuthenticationFailureHandler;
import com.web.web.security.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

/**
 * Configuration for Spring Security.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Import({WebDatasourceConfig.class})
@ComponentScan(basePackages = "com.web.web.security")
@EnableConfigurationProperties(SecurityProperties.class)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private RestAuthenticationSuccessHandler restAuthenticationSuccessHandler;
    @Autowired
    private RestAuthenticationFailureHandler restAuthenticationFailureHandler;

    /**
     * It allows configuring web based security for specific http requests.
     */
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(securityProperties.getPattern()).permitAll()
                .and()
                    .formLogin()
                        .loginPage(securityProperties.getLoginPage())
                        .successHandler(restAuthenticationSuccessHandler)
                        .failureHandler(restAuthenticationFailureHandler)
                        .usernameParameter(securityProperties.getUsernameParameter())
                        .passwordParameter(securityProperties.getPasswordParameter())
                .and()
                    .logout()
                        .invalidateHttpSession(securityProperties.isInvalidateHttpSession())
                        .clearAuthentication(securityProperties.isClearAuthentication())
                        .logoutRequestMatcher(new AntPathRequestMatcher(securityProperties.getLogoutRequestMatcher()))
                        .logoutSuccessUrl(securityProperties.getLogoutSuccessUrl())
                .and()
                    .sessionManagement()
                        .invalidSessionUrl(securityProperties.getInvalidSessionUrl())
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(securityProperties.getMaximumSessions())
                        .expiredUrl(securityProperties.getExpiredUrl()).and()
                .and()
                    .rememberMe()
                        .rememberMeParameter(securityProperties.getRememberMeParameter())
                        .rememberMeCookieName(securityProperties.getRememberMeCookieName())
                        .tokenValiditySeconds(securityProperties.getTokenValiditySeconds())
                        .tokenRepository(persistentTokenRepository())
                .and()
                    .csrf().disable();
    }

    /**
     * Allows for easily adding UserDetailsService.
     */
    @Autowired
    public void configureGlobal(final AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    /**
     * Used to store the persistent login tokens for a user.
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        final JdbcTokenRepositoryImpl impl = new JdbcTokenRepositoryImpl();
        impl.setDataSource(dataSource);
        return impl;
    }

    /**
     * Service interface for encoding passwords.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(securityProperties.getStrengthPasswordEncoder());
    }

    /**
     * Implemented a listener - HttpSessionEventPublisher.
     */
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}

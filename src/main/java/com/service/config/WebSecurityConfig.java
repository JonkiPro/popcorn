package com.service.config;

import com.service.app.security.CustomAuthenticationSuccessHandler;
import com.service.config.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@Import({WebDatasourceConfig.class})
@ComponentScan(basePackages = "com.service.app.security")
@EnableConfigurationProperties(SecurityProperties.class)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(securityProperties.getPattern()).permitAll()
                .and()
                    .formLogin()
                        .loginPage(securityProperties.getLoginPage())
                        .successHandler(customAuthenticationSuccessHandler)
                        .failureUrl(securityProperties.getFailureUrl())
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

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .passwordEncoder(passwordEncoder())
                .dataSource(dataSource)
                .usersByUsernameQuery(securityProperties.getUsersByUsernameQuery())
                .authoritiesByUsernameQuery(securityProperties.getAuthoritiesByUsernameQuery());
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl impl = new JdbcTokenRepositoryImpl();
        impl.setDataSource(dataSource);
        return impl;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(securityProperties.getStrengthPasswordEncoder());
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
}
